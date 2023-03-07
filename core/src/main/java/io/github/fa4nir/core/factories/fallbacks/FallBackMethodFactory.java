package io.github.fa4nir.core.factories.fallbacks;

import com.squareup.javapoet.CodeBlock;
import io.github.fa4nir.core.factories.MethodParametersExtractor;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import java.util.List;

public interface FallBackMethodFactory extends MethodParametersExtractor {

    CodeBlock newFallBackCodeBlock(Element fallBackMethod, String currentClassFieldName, List<? extends VariableElement> sourceParameters);

}
