package com.github.core.factories.fallbacks;

import com.github.core.annotations.ErrorSignal;
import com.github.core.annotations.FetchParam;
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
    public CodeBlock newFallBackCodeBlock(Element fallBackMethod, String currentClassFieldName, List<? extends VariableElement> sourceParameters) {
        CodeBlock fallBackMethodCodeBlock = CodeBlock.builder().build();
        if (Objects.nonNull(fallBackMethod)) {
            ExecutableElement fallBackExecutableMethod = ((ExecutableElement) fallBackMethod);
            List<? extends VariableElement> fallBackParameters = fallBackExecutableMethod.getParameters();
            String fallBackVariableElementsAsString = fallBackParameters.stream()
                    .map(fallBackParameter -> getParameterName(sourceParameters, fallBackParameter))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(","));
            if (StringUtils.isNoneBlank(fallBackVariableElementsAsString)) {
                fallBackMethodCodeBlock = CodeBlock.builder()
                        .addStatement("this.$N.$N($N)", currentClassFieldName,
                                fallBackMethod.getSimpleName(), fallBackVariableElementsAsString)
                        .build();
            } else {
                fallBackMethodCodeBlock = CodeBlock.builder()
                        .addStatement("this.$N.$N(e)", currentClassFieldName, fallBackMethod.getSimpleName())
                        .build();
            }
        }
        return fallBackMethodCodeBlock;
    }

    private String getParameterName(List<? extends VariableElement> targetParameters, VariableElement fallBackParameter) {
        ErrorSignal errorSignal = fallBackParameter.getAnnotation(ErrorSignal.class);
        FetchParam fetchParam = fallBackParameter.getAnnotation(FetchParam.class);
        if (Objects.nonNull(errorSignal)) {
            return "e";
        } else if (Objects.nonNull(fetchParam)) {
            VariableElement targetParameterName = targetParameters.get(fetchParam.num());
            return targetParameterName.getSimpleName().toString();
        } else {
            return null;
        }
    }
}
