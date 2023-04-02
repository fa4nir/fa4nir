package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.annotations.UniqueMarker;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.fa4nir.core.utils.ParametersUtils.parametersAsString;

public class PredicateMethodWrapper implements OverrideMethodWrapper {

    private final OverrideMethodWrapper wrapper;

    public PredicateMethodWrapper(OverrideMethodWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
        if (definition.getPredicateMethods().size() > 0) {
            UniqueMarker marker = Objects.requireNonNull(
                    definition.getSourceMethod().getAnnotation(UniqueMarker.class),
                    "Transmitter has any predicate methods, so mark source methods as UniqueMarker with unique name.");
            Map<String, ExecutableElement> groupByMarker = definition.getPredicateMethods();
            ExecutableElement predicate = Objects.requireNonNull(groupByMarker.get(marker.marker()),
                    "If you use two predicate you should mark methods UniqueMarker and set unique name.");
            return predicateMethodControllFlow(parametersAsString, definition, builder, predicate);
        } else if (definition.getListOfPredicateMethods().size() > 0) {
            if (definition.getListOfPredicateMethods().size() > 1) {
                throw new IllegalArgumentException("You should use UniqueMarker annotation");
            }
            ExecutableElement predicate = definition.getListOfPredicateMethods().get(0);
            return predicateMethodControllFlow(parametersAsString, definition, builder, predicate);
        }
        return this.wrapper.wrap(parametersAsString, definition, builder);
    }

    private MethodSpec.Builder predicateMethodControllFlow(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder, ExecutableElement predicate) {
        String predicateSimpleName = predicate.getSimpleName().toString();
        List<? extends VariableElement> predicateParameters = predicate.getParameters();
        String predicateParametersAsString = parametersAsString(definition.getSourceParameters(), definition.getGroupOfSourceParameters(), predicateParameters);
        if (StringUtils.isBlank(predicateParametersAsString)) {
            throw new IllegalArgumentException("Predicate mast contains any of parameters.");
        }
        builder.beginControlFlow("if(this.$N.$N($N))", definition.getTargetFieldName(), predicateSimpleName, predicateParametersAsString);
        return this.wrapper.wrap(parametersAsString, definition, builder)
                .endControlFlow();
    }

}
