package io.github.fa4nir.core.factories;

import io.github.fa4nir.core.annotations.FetchParam;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;

public interface MethodParametersExtractor {

    default VariableElement getVariableElement(List<? extends VariableElement> targetParameters,
                                               Map<String, ? extends VariableElement> groupOfSourceParameters,
                                               FetchParam annotation) {
        String name = annotation.name();
        int num = annotation.num();
        if (num != -1) {
            return targetParameters.get(num);
        } else if (StringUtils.isNoneBlank(name)) {
            return groupOfSourceParameters.get(name);
        }
        throw new IllegalArgumentException("Cannot find parameter in annotation " + annotation.getClass());
    }

}
