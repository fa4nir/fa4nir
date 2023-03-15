package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;

public class TryCatchMethodBaseWrapper implements OverrideMethodWrapper {

    private final FallBackMethodFactory fallBackMethodFactory;

    private final OverrideMethodWrapper wrapper;

    public TryCatchMethodBaseWrapper(FallBackMethodFactory fallBackMethodFactory, OverrideMethodWrapper wrapper) {
        this.fallBackMethodFactory = fallBackMethodFactory;
        this.wrapper = wrapper;
    }

    @Override
    public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
        builder.beginControlFlow("try");
        CodeBlock fallBackMethodAsString = this.fallBackMethodFactory
                .newFallBackCodeBlock(definition.getFallBackMethod(), definition.getTargetFieldName(), definition.getSourceParameters());
        return this.wrapper.wrap(parametersAsString, definition, builder)
                .nextControlFlow("catch($T e)", ClassName.get(Exception.class))
                .addCode(fallBackMethodAsString)
                .endControlFlow();
    }

}
