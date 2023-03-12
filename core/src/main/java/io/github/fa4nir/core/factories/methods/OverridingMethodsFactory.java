package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.FetchParam;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.definitions.DelegateMethodsDefinitionBuilder;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinitionBuilder;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            OverridingMethodsDefinition definition = OverridingMethodsDefinitionBuilder.newBuilder()
                    .sourceMethod(sourceMethod).target(target).annotationNotifyTo(annotationNotifyTo)
                    .annotationFallBackMethod(annotationFallBackMethod).notifyToTarget().targetMethods().sourceParameters()
                    .groupOfSourceParameters().targetFieldName().targetMethod()
                    .resultName().delegateResultToAnnotations().fallBackMethod().targetMethodReturnType()
                    .targetParameters().predicateMethods().fallBackMethodName()
                    .build();
            if (definition.getPredicateMethods().size() > 0) {
                ExecutableElement predicate = definition.getPredicateMethods().get(0);
                String predicateSimpleName = predicate.getSimpleName().toString();
                List<? extends VariableElement> predicateParameters = predicate.getParameters();
                String strPredicateParameters = parametersAsString(definition.getSourceParameters(), definition.getGroupOfSourceParameters(), predicateParameters);
                builder.beginControlFlow("if(this.$N.$N($N))", definition.getTargetFieldName(), predicateSimpleName, strPredicateParameters);
                return decorateMethodSpec(definition, builder)
                        .endControlFlow()
                        .build();
            }
            return decorateMethodSpec(definition, builder).build();
        }
        return MethodSpec.overriding(sourceMethod).build();
    }

    private MethodSpec.Builder decorateMethodSpec(OverridingMethodsDefinition definition,
                                                  MethodSpec.Builder builder) {
        builder.beginControlFlow("try");
        String parametersAsString = parametersAsString(definition.getSourceParameters(), definition.getGroupOfSourceParameters(), definition.getTargetParameters());
        if (Objects.nonNull(definition.getDelegateResultToAnnotations()) && definition.getDelegateResultToAnnotations().length > 0) {
            builder.addStatement("$T $N = this.$N.$N($N)", ParameterizedTypeName.get(definition.getTargetMethodReturnType()), definition.getResultName(),
                    definition.getTargetFieldName(), definition.getTargetMethod().getSimpleName().toString(), parametersAsString);
            List<CodeBlock> callsToDelegateMethods = DelegateMethodsDefinitionBuilder.newBuilder()
                    .setResultName(definition.getResultName())
                    .setTargetEnclosedElements(definition.getTargetMethods())
                    .setTargetFieldName(definition.getTargetFieldName())
                    .setSourceParameters(definition.getSourceParameters())
                    .setGroupOfSourceParameters(definition.getGroupOfSourceParameters())
                    .setDelegateResultToAnnotations(definition.getDelegateResultToAnnotations())
                    .build();
            callsToDelegateMethods.forEach(builder::addStatement);
        } else {
            builder.addStatement("this.$N.$N($N)",
                    definition.getTargetFieldName(), definition.getTargetMethod().getSimpleName().toString(), parametersAsString);
        }
        CodeBlock fallBackMethodAsString = this.fallBackMethodFactory
                .newFallBackCodeBlock(definition.getFallBackMethod(), definition.getTargetFieldName(), definition.getSourceParameters());
        return builder
                .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                .addCode(fallBackMethodAsString)
                .endControlFlow();
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
