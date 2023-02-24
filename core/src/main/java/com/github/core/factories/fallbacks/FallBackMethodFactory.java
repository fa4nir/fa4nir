package com.github.core.factories.fallbacks;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import java.util.List;

public interface FallBackMethodFactory {

    CodeBlock newFallBackCodeBlock(Element fallBackMethod, String currentClassFieldName, List<? extends VariableElement> sourceParameters);

}
