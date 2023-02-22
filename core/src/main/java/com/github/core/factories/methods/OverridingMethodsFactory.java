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
        ExecutableElement target = methodMetaInfo.getMethod();
        Map<String, ? extends Element> allFallBackMethod = methodMetaInfo.getFallBackMethods();
        Element fallBackMethod = allFallBackMethod.get(target.getSimpleName().toString());
        String currentClassFieldName = methodMetaInfo.getSourceClassFieldName();
        InterceptMapper source = Arrays.stream(methodMetaInfo.getMethods())
                .filter(interceptor -> target.getSimpleName().toString().equals(interceptor.listenerMethodName()))
                .findFirst().orElse(null);
        if (Objects.nonNull(source)) {
            String requiredErrorMessage = String.format("Can't find method %s", source.toCurrentMethod());
            MethodSpec.Builder builder = MethodSpec.overriding(target);
            builder.beginControlFlow("try");
            ExecutableElement elementMethod = ((ExecutableElement) methodMetaInfo
                    .getSourceTypeElementMethods().stream()
                    .filter(e -> e.getSimpleName().toString().equals(source.toCurrentMethod()))
                    .findFirst().orElse(null));
            List<? extends VariableElement> targetParameters = target.getParameters();
            List<? extends VariableElement> parameters = Objects.requireNonNull(elementMethod, requiredErrorMessage)
                    .getParameters();
            String parametersAsString = parametersAsString(targetParameters, parameters);
            DelegateResultTo delegateResultToAnnotations = elementMethod.getAnnotation(DelegateResultTo.class);
            if (Objects.nonNull(delegateResultToAnnotations)) {
                TypeMirror returnType = elementMethod.getReturnType();
                TypeName parameterizedReturnType = ParameterizedTypeName.get(returnType);
                DelegateToMethod[] delegateToMethodAnnotations = delegateResultToAnnotations.methods();
                builder.addStatement("$T result = this.$N.$N($N)",
                        parameterizedReturnType, currentClassFieldName,
                        source.toCurrentMethod(), parametersAsString
                );
                List<CodeBlock> delegateMethods = generateCallToDelegateMethods(
                        methodMetaInfo, currentClassFieldName,
                        targetParameters, delegateToMethodAnnotations
                );
                delegateMethods.forEach(builder::addStatement);
            } else {
                builder.addStatement("this.$N.$N($N)", currentClassFieldName, source.toCurrentMethod(), parametersAsString);
            }
            CodeBlock fallBackMethodAsString = this.fallBackMethodFactory
                    .newFallBackCodeBlock(fallBackMethod, currentClassFieldName, targetParameters);
            return builder
                    .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                    .addCode(fallBackMethodAsString)
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.overriding(target).build();
    }

    private List<CodeBlock> generateCallToDelegateMethods(OverridingMethodMetaInfo methodMetaInfo, String currentClassFieldName,
                                                          List<? extends VariableElement> targetParameters,
                                                          DelegateToMethod[] delegateToMethodAnnotations) {
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
        return methodMetaInfo.getSourceTypeElementMethods().stream()
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
