package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import io.github.fa4nir.core.annotations.DelegateResultTo;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.FetchParam;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.builders.DelegateMethodsDefinitionBuilder;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OverridingMethodsFactory implements InterceptMethodFactory {

    private final FallBackMethodFactory fallBackMethodFactory;

    public OverridingMethodsFactory(FallBackMethodFactory fallBackMethodFactory) {
        this.fallBackMethodFactory = fallBackMethodFactory;
    }

    @Override
    public MethodSpec newMethodSpec(ExecutableElement sourceMethod, Element target) {
        NotifyTo annotationNotifyTo = sourceMethod.getAnnotation(NotifyTo.class);
        FallBackMethod annotationFallBackMethod = sourceMethod.getAnnotation(FallBackMethod.class);
        if (Objects.nonNull(annotationNotifyTo)) {
            MethodSpec.Builder builder = MethodSpec.overriding(sourceMethod);
            List<? extends VariableElement> sourceParameters = sourceMethod.getParameters();
            Map<String, ? extends VariableElement> groupOfSourceParameters = sourceParameters.stream()
                    .collect(Collectors.toMap(k -> k.getSimpleName().toString(), Function.identity()));
            String targetFieldName = Introspector.decapitalize(target.getSimpleName().toString());
            String notifyToTarget = annotationNotifyTo.name();
            String fallBackMethodName = Objects.nonNull(annotationFallBackMethod) ? annotationFallBackMethod.name() : "";
            ExecutableElement targetMethod = findMethod(target, notifyToTarget);
            String resultName = createResultName(sourceParameters, targetMethod);
            DelegateResultTo[] delegateResultToAnnotations = targetMethod.getAnnotationsByType(DelegateResultTo.class);
            ExecutableElement fallBackMethod = findMethod(target, fallBackMethodName);
            TypeMirror targetMethodReturnType = targetMethod.getReturnType();
            List<? extends VariableElement> targetParameters = Objects.requireNonNull(
                    targetMethod, String.format("Cannot find method %s", targetMethod.getSimpleName())
            ).getParameters();
            builder.beginControlFlow("try");
            String parametersAsString = parametersAsString(sourceParameters, groupOfSourceParameters, targetParameters);
            if (Objects.nonNull(delegateResultToAnnotations) && delegateResultToAnnotations.length > 0) {
                builder.addStatement("$T $N = this.$N.$N($N)", ParameterizedTypeName.get(targetMethodReturnType), resultName,
                        targetFieldName, targetMethod.getSimpleName().toString(), parametersAsString);
                List<CodeBlock> callsToDelegateMethods = DelegateMethodsDefinitionBuilder.newBuilder()
                        .setResultName(resultName)
                        .setTargetEnclosedElements(target.getEnclosedElements())
                        .setTargetFieldName(targetFieldName)
                        .setSourceParameters(sourceParameters)
                        .setGroupOfSourceParameters(groupOfSourceParameters)
                        .setDelegateResultToAnnotations(delegateResultToAnnotations)
                        .build();
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

    private String parametersAsString(List<? extends VariableElement> targetParameters,
                                      Map<String, ? extends VariableElement> groupOfSourceParameters,
                                      List<? extends VariableElement> parameters) {
        return parameters.stream()
                .map(parameter -> parameter.getAnnotation(FetchParam.class))
                .filter(Objects::nonNull)
                .map(annotation -> getVariableElement(targetParameters, groupOfSourceParameters, annotation))
                .map(VariableElement::getSimpleName)
                .collect(Collectors.joining(","));
    }

}
