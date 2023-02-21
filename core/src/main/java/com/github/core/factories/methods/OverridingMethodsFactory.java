package com.github.core.factories.methods;

import com.github.core.annotations.DelegateResultTo;
import com.github.core.annotations.DelegateToMethod;
import com.github.core.annotations.GetParameter;
import com.github.core.annotations.InterceptMapper;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OverridingMethodsFactory implements InterceptMethodFactory {
    @Override
    public MethodSpec newMethodSpec(OverridingMethodMetaInfo methodMetaInfo) {
        ExecutableElement method = methodMetaInfo.getMethod();
        Element fallBackMethod = methodMetaInfo.getFallBackMethod();
        String currentClassFieldName = methodMetaInfo.getCurrentClassFieldName();
        InterceptMapper target = Arrays.stream(methodMetaInfo.getMethods())
                .filter(interceptor -> method.getSimpleName().toString().equals(interceptor.listenerMethodName()))
                .findFirst().orElse(null);
        if (Objects.nonNull(target)) {
            MethodSpec.Builder builder = MethodSpec.overriding(method);
            builder.beginControlFlow("try");
            ExecutableElement elementMethod = ((ExecutableElement) methodMetaInfo
                    .getCurrentTypeElementMethods()
                    .get(target.toCurrentMethod()));
            List<? extends VariableElement> targetParameters = method.getParameters();
            List<? extends VariableElement> parameters = elementMethod.getParameters();
            String parametersAsString = parameters.stream()
                    .map(parameter -> parameter.getAnnotation(GetParameter.class))
                    .filter(Objects::nonNull)
                    .map(GetParameter::num)
                    .map(targetParameters::get)
                    .map(VariableElement::getSimpleName)
                    .collect(Collectors.joining(","));

            DelegateResultTo delegateResultToAnnotations = elementMethod.getAnnotation(DelegateResultTo.class);
            if (Objects.nonNull(delegateResultToAnnotations)) {
                TypeMirror returnType = elementMethod.getReturnType();
                TypeName parameterizedReturnType = ParameterizedTypeName.get(returnType);
                DelegateToMethod[] delegateToMethodAnnotations = delegateResultToAnnotations.methods();
                builder.addStatement("$T result = this.$N.$N($N)", parameterizedReturnType, currentClassFieldName, target.toCurrentMethod(), parametersAsString);
                Arrays.stream(delegateToMethodAnnotations)
                        .forEach(delegateToMethod -> {
                           builder.addStatement("this.$N.$N(result)", currentClassFieldName, delegateToMethod.methodName());
                        });
            } else {
                builder.addStatement("this.$N.$N($N)", currentClassFieldName, target.toCurrentMethod(), parametersAsString);
            }

            CodeBlock fallBackMethodAsString = CodeBlock.builder().build();
            if (Objects.nonNull(fallBackMethod)) {
                fallBackMethodAsString = CodeBlock.builder()
                        .addStatement("this.$N.$N(e)", currentClassFieldName, fallBackMethod.getSimpleName())
                        .build();
            }
            return builder
                    .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                    .addCode(fallBackMethodAsString)
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.overriding(method).build();
    }
}
