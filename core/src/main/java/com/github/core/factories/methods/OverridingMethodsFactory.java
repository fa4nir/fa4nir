package com.github.core.factories.methods;

import com.github.core.annotations.*;
import com.github.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import com.github.core.factories.fallbacks.FallBackMethodFactory;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.stream.Collectors;

public class OverridingMethodsFactory implements InterceptMethodFactory {

    private final FallBackMethodFactory fallBackMethodFactory = new ExceptionFallBackMethodFactory();

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
            String parametersAsString = parametersAsString(targetParameters, parameters);
            DelegateResultTo delegateResultToAnnotations = elementMethod.getAnnotation(DelegateResultTo.class);
            if (Objects.nonNull(delegateResultToAnnotations)) {
                TypeMirror returnType = elementMethod.getReturnType();
                TypeName parameterizedReturnType = ParameterizedTypeName.get(returnType);
                DelegateToMethod[] delegateToMethodAnnotations = delegateResultToAnnotations.methods();
                builder.addStatement("$T result = this.$N.$N($N)",
                        parameterizedReturnType, currentClassFieldName,
                        target.toCurrentMethod(), parametersAsString
                );
                List<CodeBlock> delegateMethods = generateCallToDelegateMethods(
                        methodMetaInfo, currentClassFieldName,
                        targetParameters, delegateToMethodAnnotations
                );
                delegateMethods.forEach(builder::addStatement);
            } else {
                builder.addStatement("this.$N.$N($N)", currentClassFieldName, target.toCurrentMethod(), parametersAsString);
            }
            CodeBlock fallBackMethodAsString = this.fallBackMethodFactory
                    .newFallBackCodeBlock(fallBackMethod, currentClassFieldName, targetParameters);
            return builder
                    .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                    .addCode(fallBackMethodAsString)
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.overriding(method).build();
    }

    private List<CodeBlock> generateCallToDelegateMethods(OverridingMethodMetaInfo methodMetaInfo, String currentClassFieldName,
                                                          List<? extends VariableElement> targetParameters, DelegateToMethod[] delegateToMethodAnnotations) {
        return Arrays.stream(delegateToMethodAnnotations)
                .collect(Collectors.toCollection(LinkedHashSet::new)).stream()
                .flatMap(delegateToMethod -> {
                    String methodName = delegateToMethod.methodName();
                    return collectDelegateParameters(methodMetaInfo, targetParameters, methodName).stream()
                            .map(delegatorParametersAsString -> CodeBlock.of("this.$N.$N($N)", currentClassFieldName, methodName, delegatorParametersAsString));
                }).collect(Collectors.toList());
    }

    private String parametersAsString(List<? extends VariableElement> targetParameters, List<? extends VariableElement> parameters) {
        return parameters.stream()
                .map(parameter -> parameter.getAnnotation(GetParameter.class))
                .filter(Objects::nonNull)
                .map(GetParameter::num)
                .map(targetParameters::get)
                .map(VariableElement::getSimpleName)
                .collect(Collectors.joining(","));
    }

    private List<String> collectDelegateParameters(OverridingMethodMetaInfo methodMetaInfo, List<? extends VariableElement> targetParameters, String methodName) {
        return methodMetaInfo.getCurrentTypeElementMethods().stream()
                .map(delegator -> ((ExecutableElement) delegator))
                .filter(delegator -> delegator.getSimpleName().toString().equals(methodName))
                .map(delegator -> delegator.getParameters().stream().map(parameter -> retrieveLink(targetParameters, parameter))
                        .collect(Collectors.joining(","))
                ).collect(Collectors.toList());
    }

    private String retrieveLink(List<? extends VariableElement> targetParameters, VariableElement parameter) {
        ActualResult actualResult = parameter.getAnnotation(ActualResult.class);
        GetParameter getParameter = parameter.getAnnotation(GetParameter.class);
        if (Objects.nonNull(actualResult)) {
            return "result";
        } else if (Objects.nonNull(getParameter)) {
            return targetParameters.get(getParameter.num()).getSimpleName().toString();
        }
        return "";
    }

}
