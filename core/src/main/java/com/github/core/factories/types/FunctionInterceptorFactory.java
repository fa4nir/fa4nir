package com.github.core.factories.types;

import com.github.core.annotations.FallBackMethod;
import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.InterceptMapper;
import com.github.core.factories.methods.InterceptMethodFactory;
import com.github.core.factories.methods.OverridingMethodsFactory;
import com.github.core.utils.ListenerTypeAndFallBackAnnotationData;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.github.core.utils.TypeSpecConstructorsUtils;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionInterceptorFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor = new OverridingMethodsFactory();

    @Override
    public TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        TypeMirror typeMirror = element.asType();
        String currentClassForField = element.getSimpleName().toString();
        TypeName currentClassType = ClassName.get(typeMirror);
        FunctionInterceptor baseAnnotation = element.getAnnotation(FunctionInterceptor.class);
        ListenerTypeAndFallBackAnnotationData classAnnotationPair = classAnnotationPair(annotationMirrors);
        String beanName = baseAnnotation.beanName();
        InterceptMapper[] methods = baseAnnotation.methods();
        ClassName listenerType = ClassName.bestGuess(classAnnotationPair.getListenerType());
        TypeSpec.Builder builder = TypeSpec.classBuilder(listenerType.simpleName() + "Impl");
        String currentClassFieldName = Introspector.decapitalize(currentClassForField);
        TypeElement currentTypeElement = processingEnv.getElementUtils().
                getTypeElement(currentClassType.toString());
        Map<String, ? extends Element> currentTypeElementMethods = currentTypeElement.getEnclosedElements()
                .stream().collect(Collectors.toMap(k -> k.getSimpleName().toString(), Function.identity()));
        Element fallBackMethod = currentTypeElement.getEnclosedElements().stream()
                .filter(method -> Objects.nonNull(method.getAnnotation(FallBackMethod.class)))
                .findFirst().orElse(null);
        TypeElement listenerTypeElement = processingEnv.getElementUtils().
                getTypeElement(listenerType.toString());
        List<? extends TypeParameterElement> listerGenericType = listenerTypeElement.getTypeParameters();
        if (listerGenericType.size() != 0) {
            TypeMirror type = listerGenericType.get(0).asType();
            builder.addTypeVariable(TypeVariableName.get(type.toString()));
        }
        List<? extends Element> listenerTypeElementMethods = listenerTypeElement.getEnclosedElements();
        List<MethodSpec> listenerMethodsInfo = listenerTypeElementMethods.stream()
                .map(method -> ((ExecutableElement) method))
                .map(method -> this.overridingInterceptor.newMethodSpec(
                        OverridingMethodMetaInfo.builder()
                                .method(method)
                                .methods(methods)
                                .currentTypeElementMethods(currentTypeElementMethods)
                                .fallBackMethod(fallBackMethod)
                                .currentClassFieldName(currentClassFieldName)
                                .build()
                )).collect(Collectors.toList());
        FieldSpec currentClassField = FieldSpec.builder(currentClassType, currentClassFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addModifiers()
                .build();
        builder.addField(currentClassField);
        MethodSpec.Builder constructor = TypeSpecConstructorsUtils.constructor(currentClassType, currentClassFieldName);
        return builder
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(listenerTypeElement.asType())
                .addMethod(constructor.build())
                .addMethods(listenerMethodsInfo)
                .build();
    }

    private ListenerTypeAndFallBackAnnotationData classAnnotationPair(List<? extends AnnotationMirror> annotationMirrors) {
        ListenerTypeAndFallBackAnnotationData result = new ListenerTypeAndFallBackAnnotationData();
        annotationMirrors.forEach(annotationMirror -> {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                    annotationMirror.getElementValues();
            elementValues.forEach((key, value) -> {
                Name keySimpleName = key.getSimpleName();
                if ("listenerType".equals(keySimpleName.toString())) {
                    result.setListenerType(value.getValue().toString());
                }
            });
        });
        return result;
    }

}
