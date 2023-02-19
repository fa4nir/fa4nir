package com.github.core.factories.types;

import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.InterceptMapper;
import com.github.core.utils.ListenerTypeAndFallBackAnnotationData;
import com.github.core.utils.TypeSpecConstructorsUtils;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;

public class FunctionInterceptorFactory implements AnnotationTransferFactory {

    @Override
    public TypeSpec newTypeSpec(Element element) {
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
                .addSuperinterface(listenerType)
                .addMethod(constructor.build())
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
