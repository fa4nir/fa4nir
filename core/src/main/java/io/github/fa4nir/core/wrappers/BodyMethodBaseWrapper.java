package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import io.github.fa4nir.core.definitions.DelegateMethodsDefinitionBuilder;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;

import java.util.List;
import java.util.Objects;

public class BodyMethodBaseWrapper implements OverrideMethodWrapper {

    @Override
    public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
        if (Objects.nonNull(definition.getDelegateResultToAnnotations()) && definition.getDelegateResultToAnnotations().length > 0) {
            builder.addStatement("$T $N = this.$N.$N($N)", ParameterizedTypeName.get(definition.getTargetMethodReturnType()), definition.getResultName(),
                    definition.getTargetFieldName(), definition.getTargetMethod().getSimpleName().toString(), parametersAsString);
            List<CodeBlock> callsToDelegateMethods = ofDelegateDefinitions(definition);
            callsToDelegateMethods.forEach(builder::addStatement);
        } else {
            builder.addStatement("this.$N.$N($N)",
                    definition.getTargetFieldName(), definition.getTargetMethod().getSimpleName().toString(), parametersAsString);
        }
        return builder;
    }

    private List<CodeBlock> ofDelegateDefinitions(OverridingMethodsDefinition definition) {
        return DelegateMethodsDefinitionBuilder.newBuilder()
                .setResultName(definition.getResultName())
                .setTargetEnclosedElements(definition.getTargetMethods())
                .setTargetFieldName(definition.getTargetFieldName())
                .setSourceParameters(definition.getSourceParameters())
                .setGroupOfSourceParameters(definition.getGroupOfSourceParameters())
                .setDelegateResultToAnnotations(definition.getDelegateResultToAnnotations())
                .build();
    }

}
