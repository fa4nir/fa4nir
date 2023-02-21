package com.github.core.factories.methods;

import com.github.core.annotations.*;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.*;
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
                    .getCurrentTypeElementMethods().stream()
                    .filter(e -> e.getSimpleName().toString().equals(target.toCurrentMethod()))
                    .findFirst().orElse(null));
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
                builder.addStatement("$T result = this.$N.$N($N)",
                        parameterizedReturnType, currentClassFieldName,
                        target.toCurrentMethod(), parametersAsString
                );
                Arrays.stream(delegateToMethodAnnotations)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                        .forEach(delegateToMethod -> {
                            String methodName = delegateToMethod.methodName();
                            List<String> delegateParametersLst = methodMetaInfo.getCurrentTypeElementMethods().stream()
                                    .map(delegator -> ((ExecutableElement) delegator))
                                    .filter(delegator -> delegator.getSimpleName().toString().equals(methodName))
                                    .map(delegator -> delegator.getParameters().stream().map(parameter -> {
                                        ActualResult actualResult = parameter.getAnnotation(ActualResult.class);
                                        GetParameter getParameter = parameter.getAnnotation(GetParameter.class);
                                        if (Objects.nonNull(actualResult)) {
                                            return "result";
                                        } else if (Objects.nonNull(getParameter)) {
                                            return targetParameters.get(getParameter.num()).getSimpleName().toString();
                                        }
                                        return "";
                                    }).collect(Collectors.joining(",")))
                                    .collect(Collectors.toList());
                            delegateParametersLst.forEach(delegatorParametersAsString -> {
                                builder.addStatement("this.$N.$N($N)", currentClassFieldName, methodName, delegatorParametersAsString);
                            });
                        });
            } else {
                builder.addStatement("this.$N.$N($N)", currentClassFieldName, target.toCurrentMethod(), parametersAsString);
            }
            CodeBlock fallBackMethodAsString = CodeBlock.builder().build();
            if (Objects.nonNull(fallBackMethod)) {
                ExecutableElement fallBackExecutableMethod = ((ExecutableElement) fallBackMethod);
                List<? extends VariableElement> fallBackParameters = fallBackExecutableMethod.getParameters();
                String fallBackVariableElementsAsString = fallBackParameters.stream()
                        .map(fallBackParameter -> fallBackParameter.getAnnotation(GetParameter.class))
                        .filter(Objects::nonNull)
                        .map(annotation -> targetParameters.get(annotation.num()))
                        .map(VariableElement::getSimpleName)
                        .collect(Collectors.joining(","));
                if (StringUtils.isNoneBlank(fallBackVariableElementsAsString)) {
                    fallBackMethodAsString = CodeBlock.builder()
                            .addStatement("this.$N.$N(e, $N)", currentClassFieldName, fallBackMethod.getSimpleName(), fallBackVariableElementsAsString)
                            .build();
                } else {
                    fallBackMethodAsString = CodeBlock.builder()
                            .addStatement("this.$N.$N(e)", currentClassFieldName, fallBackMethod.getSimpleName())
                            .build();
                }
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
