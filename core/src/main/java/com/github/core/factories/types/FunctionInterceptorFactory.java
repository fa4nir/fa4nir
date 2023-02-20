package com.github.core.factories.types;

import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.GetParameter;
import com.github.core.annotations.InterceptMapper;
import com.github.core.utils.ListenerTypeAndFallBackAnnotationData;
import com.github.core.utils.TypeSpecConstructorsUtils;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionInterceptorFactory implements AnnotationTransferFactory {

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
        ClassName fallBackClassHandler;
        String currentClassFieldName = Introspector.decapitalize(currentClassForField);
        TypeElement currentTypeElement = processingEnv.getElementUtils().
                getTypeElement(currentClassType.toString());
        Map<String, ? extends Element> currentTypeElementMethods = currentTypeElement.getEnclosedElements()
                .stream().collect(Collectors.toMap(k -> k.getSimpleName().toString(), Function.identity()));
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
                .map(method -> {
                    InterceptMapper target = Arrays.stream(methods)
                            .filter(interceptor -> method.getSimpleName().toString().equals(interceptor.listenerMethodName()))
                            .findFirst().orElse(null);
                    if (Objects.nonNull(target)) {
                        // TODO: 20.02.23 refactoring on parameters
                        ExecutableElement elementMethod = ((ExecutableElement) currentTypeElementMethods.get(target.toCurrentMethod()));
                        VariableElement parameter = elementMethod.getParameters().get(0);
                        GetParameter parameterAnnotation = parameter.getAnnotation(GetParameter.class);
                        int numberOfParameter = parameterAnnotation.num();
                        VariableElement parameterToMethod = method.getParameters().get(numberOfParameter);
                        String parameters = parameterToMethod.getSimpleName().toString();
                        return MethodSpec.overriding(method)
                                .beginControlFlow("try")
                                .addStatement("this.$N.$N($N)", currentClassFieldName, target.toCurrentMethod(), parameters)
                                .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                                .endControlFlow()
                                .build();
                    }
                    return MethodSpec.overriding(method).build();
                }).collect(Collectors.toList());
        FieldSpec currentClassField = FieldSpec.builder(currentClassType, currentClassFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addModifiers()
                .build();
        builder.addField(currentClassField);
        MethodSpec.Builder constructor = TypeSpecConstructorsUtils.constructor(currentClassType, currentClassFieldName);
        if (StringUtils.isNoneBlank(classAnnotationPair.getFallBackClassHandler())) {
            fallBackClassHandler = ClassName.bestGuess(classAnnotationPair.getFallBackClassHandler());
            String fallBackFieldName = Introspector.decapitalize(fallBackClassHandler.simpleName());
            FieldSpec fallBackField = FieldSpec
                    .builder(fallBackClassHandler, fallBackFieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .addModifiers()
                    .build();
            builder.addField(fallBackField);
            constructor
                    .addParameter(fallBackClassHandler, fallBackFieldName)
                    .addStatement(CodeBlock.of("this.$N = $N", fallBackFieldName, fallBackFieldName));
        }
        return builder
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
                if ("fallBackClassHandler".equals(keySimpleName.toString())) {
                    result.setFallBackClassHandler(value.getValue().toString());
                }
            });
        });
        return result;
    }

}
