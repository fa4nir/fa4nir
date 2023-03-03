package io.github.fa4nir.core.factories.fallbacks;

import com.squareup.javapoet.CodeBlock;
import io.github.fa4nir.core.annotations.ErrorSignal;
import io.github.fa4nir.core.annotations.FetchParam;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExceptionFallBackMethodFactory implements FallBackMethodFactory {

    @Override
    public CodeBlock newFallBackCodeBlock(Element fallBackMethod, String currentClassFieldName,
                                          List<? extends VariableElement> sourceParameters) {
        CodeBlock fallBackMethodCodeBlock = CodeBlock.builder().build();
        if (Objects.nonNull(fallBackMethod)) {
            Map<String, ? extends VariableElement> groupOfSourceParameters = sourceParameters.stream()
                    .collect(Collectors.toMap(k -> k.getSimpleName().toString(), Function.identity()));
            ExecutableElement fallBackExecutableMethod = ((ExecutableElement) fallBackMethod);
            List<? extends VariableElement> fallBackParameters = fallBackExecutableMethod.getParameters();
            String fallBackVariableElementsAsString = fallBackParameters.stream()
                    .map(fallBackParameter -> getParameterName(sourceParameters, groupOfSourceParameters, fallBackParameter))
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

    private String getParameterName(List<? extends VariableElement> targetParameters,
                                    Map<String, ? extends VariableElement> groupOfSourceParameters,
                                    VariableElement fallBackParameter) {
        ErrorSignal errorSignal = fallBackParameter.getAnnotation(ErrorSignal.class);
        FetchParam fetchParam = fallBackParameter.getAnnotation(FetchParam.class);
        if (Objects.nonNull(errorSignal)) {
            return "e";
        } else if (Objects.nonNull(fetchParam)) {
            return getVariableElement(targetParameters, groupOfSourceParameters, fetchParam)
                    .getSimpleName().toString();
        } else {
            return null;
        }
    }

}
