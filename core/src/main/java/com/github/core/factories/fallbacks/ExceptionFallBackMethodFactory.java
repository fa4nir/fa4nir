package com.github.core.factories.fallbacks;

import com.github.core.annotations.GetParameter;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExceptionFallBackMethodFactory implements FallBackMethodFactory {

    @Override
    public CodeBlock newFallBackCodeBlock(Element fallBackMethod, String currentClassFieldName, List<? extends VariableElement> targetParameters) {
        CodeBlock fallBackMethodAsString = CodeBlock.builder().build();
        if (Objects.nonNull(fallBackMethod)) {
            ExecutableElement fallBackExecutableMethod = ((ExecutableElement) fallBackMethod);
            List<? extends VariableElement> fallBackParameters = fallBackExecutableMethod.getParameters();
            String fallBackVariableElementsAsString = fallBackParameters.stream()
                    .map(fallBackParameter -> fallBackParameter.getAnnotation(GetParameter.class))
                    .filter(Objects::nonNull)
                    .map(annotation -> targetParameters.get(annotation.num()))
                    .map(VariableElement::getSimpleName)
                    .collect(Collectors.joining(","));
            if (StringUtils.isNoneBlank(fallBackVariableElementsAsString)) {
                fallBackMethodAsString = CodeBlock.builder()
                        .addStatement("this.$N.$N(e, $N)", currentClassFieldName, fallBackMethod.getSimpleName(), fallBackVariableElementsAsString)
                        .build();
            } else {
                fallBackMethodAsString = CodeBlock.builder()
                        .addStatement("this.$N.$N(e)", currentClassFieldName, fallBackMethod.getSimpleName())
                        .build();
            }
        }
        return fallBackMethodAsString;
    }
}
