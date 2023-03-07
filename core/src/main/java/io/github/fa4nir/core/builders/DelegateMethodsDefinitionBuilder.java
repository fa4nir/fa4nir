package io.github.fa4nir.core.builders;

import com.squareup.javapoet.CodeBlock;
import io.github.fa4nir.core.annotations.DelegateResultTo;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;

public interface DelegateMethodsDefinitionBuilder {

    static DelegateMethodsDefinitionBuilder newBuilder() {
        return new DelegateMethodsDefinition();
    }

    DelegateMethodsDefinitionBuilder setResultName(String resultName);

    DelegateMethodsDefinitionBuilder setTargetEnclosedElements(List<? extends Element> targetEnclosedElements);

    DelegateMethodsDefinitionBuilder setTargetFieldName(String targetFieldName);

    DelegateMethodsDefinitionBuilder setSourceParameters(List<? extends VariableElement> sourceParameters);

    DelegateMethodsDefinitionBuilder setGroupOfSourceParameters(Map<String, ? extends VariableElement> groupOfSourceParameters);

    DelegateMethodsDefinitionBuilder setDelegateResultToAnnotations(DelegateResultTo[] delegateResultToAnnotations);

    List<CodeBlock> build();

}
