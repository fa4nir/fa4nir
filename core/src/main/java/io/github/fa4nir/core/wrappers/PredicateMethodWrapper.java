package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

import static io.github.fa4nir.core.utils.ParametersUtils.parametersAsString;

public class PredicateMethodWrapper implements OverrideMethodWrapper {

    private final OverrideMethodWrapper wrapper;

    public PredicateMethodWrapper(OverrideMethodWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
        if (definition.getPredicateMethods().size() > 0) {
            ExecutableElement predicate = definition.getPredicateMethods().get(0);
            String predicateSimpleName = predicate.getSimpleName().toString();
            List<? extends VariableElement> predicateParameters = predicate.getParameters();
            String predicateParametersAsString = parametersAsString(definition.getSourceParameters(), definition.getGroupOfSourceParameters(), predicateParameters);
            builder.beginControlFlow("if(this.$N.$N($N))", definition.getTargetFieldName(), predicateSimpleName, predicateParametersAsString);
            return this.wrapper.wrap(parametersAsString, definition, builder)
                    .endControlFlow();
        }
        return this.wrapper.wrap(parametersAsString, definition, builder);
    }

}
