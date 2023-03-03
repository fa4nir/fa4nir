package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.factories.MethodParametersExtractor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public interface InterceptMethodFactory extends MethodParametersExtractor {

    MethodSpec newMethodSpec(ExecutableElement sourceMethod, Element target);

}
