package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public interface InterceptMethodFactory {

    MethodSpec newMethodSpec(ExecutableElement sourceMethod, Element target);

}
