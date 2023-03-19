package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.utils.PrimitiveUtils;

import javax.lang.model.type.TypeMirror;

public class ResultReturnWrapper implements OverrideMethodWrapper {

    private final OverrideMethodWrapper wrapper;

    public ResultReturnWrapper(OverrideMethodWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
        validateReturnStatement(definition);
        TypeMirror sourceReturnType = definition.sourceReturnType();
        String defaultValue = PrimitiveUtils.getDefault(TypeName.get(sourceReturnType));
        builder.addStatement("$T $N = $N",
                ParameterizedTypeName.get(sourceReturnType),
                definition.sourceMethodResultName(),
                defaultValue
        );
        return this.wrapper.wrap(parametersAsString, definition, builder)
                .addStatement("return $N", definition.sourceMethodResultName());
    }

    private void validateReturnStatement(OverridingMethodsDefinition definition) {
        if (!definition.hasSourceReturnType()) {
            throw new IllegalArgumentException("Source Method has not return statement.");
        }
    }

}
