package com.github.core.factories.methods;

import com.github.core.annotations.*;
import com.github.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import com.github.core.factories.fallbacks.FallBackMethodFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OverridingMethodsFactory implements InterceptMethodFactory {

    private final FallBackMethodFactory fallBackMethodFactory = new ExceptionFallBackMethodFactory();

    @Override
    public MethodSpec newMethodSpec(ExecutableElement sourceMethod, Element target) {
        NotifyTo annotationNotifyTo = sourceMethod.getAnnotation(NotifyTo.class);
        FallBackMethod annotationFallBackMethod = sourceMethod.getAnnotation(FallBackMethod.class);
        if (Objects.nonNull(annotationNotifyTo)) {
            List<? extends VariableElement> sourceParameters = sourceMethod.getParameters();
            String targetFieldName = Introspector.decapitalize(target.getSimpleName().toString());
            String notifyToTarget = annotationNotifyTo.name();
            String fallBackMethodName = Objects.nonNull(annotationFallBackMethod) ? annotationFallBackMethod.name() : "";
            ExecutableElement targetMethod = findMethod(target, notifyToTarget);
            String resultName = createResultName(sourceParameters, targetMethod);
            DelegateResultTo[] delegateResultToAnnotations = targetMethod.getAnnotationsByType(DelegateResultTo.class);
            ExecutableElement fallBackMethod = findMethod(target, fallBackMethodName);
            TypeMirror targetMethodReturnType = targetMethod.getReturnType();
            List<? extends VariableElement> targetParameters = Objects.requireNonNull(targetMethod, "Cannot find method")
                    .getParameters();
            MethodSpec.Builder builder = MethodSpec.overriding(sourceMethod);
            builder.beginControlFlow("try");
            String parametersAsString = parametersAsString(sourceParameters, targetParameters);
            if (Objects.nonNull(delegateResultToAnnotations) && delegateResultToAnnotations.length > 0) {
                builder.addStatement("$T $N = this.$N.$N($N)", ParameterizedTypeName.get(targetMethodReturnType), resultName,
                        targetFieldName, targetMethod.getSimpleName().toString(), parametersAsString);
                List<CodeBlock> callsToDelegateMethods = generateCallToDelegateMethods(resultName, target.getEnclosedElements(), targetFieldName,
                        sourceParameters, delegateResultToAnnotations);
                callsToDelegateMethods.forEach(builder::addStatement);
            } else {
                builder.addStatement("this.$N.$N($N)",
                        targetFieldName, targetMethod.getSimpleName().toString(), parametersAsString);
            }
            CodeBlock fallBackMethodAsString = this.fallBackMethodFactory
                    .newFallBackCodeBlock(fallBackMethod, targetFieldName, sourceParameters);
            return builder
                    .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                    .addCode(fallBackMethodAsString)
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.overriding(sourceMethod).build();
    }

    private String createResultName(List<? extends VariableElement> sourceParameters, ExecutableElement targetMethod) {
        String resultName = "result";
        boolean isResultMatch = sourceParameters.stream().anyMatch(param -> param.getSimpleName().toString().equals("result"));
        if (isResultMatch) {
            resultName = String.format("%sFrom%s", resultName, StringUtils.capitalize(targetMethod.getSimpleName().toString()));
        }
        return resultName;
    }

    private ExecutableElement findMethod(Element target, String name) {
        return target.getEnclosedElements().stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(method -> (ExecutableElement) method)
                .filter(method -> method.getSimpleName().toString().equals(name))
                .findFirst().orElse(null);
    }

    private List<CodeBlock> generateCallToDelegateMethods(String resultName, List<? extends Element> targetElements, String currentClassFieldName,
                                                          List<? extends VariableElement> targetParameters,
                                                          DelegateResultTo[] delegateToMethodAnnotations) {
        return Arrays.stream(delegateToMethodAnnotations)
                .collect(Collectors.toCollection(LinkedHashSet::new)).stream()
                .flatMap(delegateToMethod -> {
                    String methodName = delegateToMethod.method();
                    return collectDelegateParameters(resultName, targetElements, targetParameters, methodName).stream()
                            .map(delegatorParametersAsString -> CodeBlock.of("this.$N.$N($N)", currentClassFieldName, methodName, delegatorParametersAsString));
                }).collect(Collectors.toList());
    }

    private List<String> collectDelegateParameters(String resultName, List<? extends Element> targetElements, List<? extends VariableElement> targetParameters, String methodName) {
        return targetElements.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(delegator -> ((ExecutableElement) delegator))
                .filter(delegator -> delegator.getSimpleName().toString().equals(methodName))
                .map(delegator -> delegator.getParameters().stream().map(parameter -> retrieveLink(resultName, targetParameters, parameter))
                        .collect(Collectors.joining(","))
                ).collect(Collectors.toList());
    }

    private String parametersAsString(List<? extends VariableElement> targetParameters, List<? extends VariableElement> parameters) {
        return parameters.stream()
                .map(parameter -> parameter.getAnnotation(FetchParam.class))
                .filter(Objects::nonNull)
                .map(FetchParam::num)
                .map(targetParameters::get)
                .map(VariableElement::getSimpleName)
                .collect(Collectors.joining(","));
    }

    private String retrieveLink(String resultName, List<? extends VariableElement> targetParameters, VariableElement parameter) {
        FetchResult actualResult = parameter.getAnnotation(FetchResult.class);
        FetchParam fetchParam = parameter.getAnnotation(FetchParam.class);
        if (Objects.nonNull(actualResult)) {
            return resultName;
        } else if (Objects.nonNull(fetchParam)) {
            return targetParameters.get(fetchParam.num()).getSimpleName().toString();
        }
        return "";
    }

}
