package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;

public interface OverrideMethodWrapper {

    MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder);

}
