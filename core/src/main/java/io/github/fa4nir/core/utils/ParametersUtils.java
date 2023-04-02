package io.github.fa4nir.core.utils;

import io.github.fa4nir.core.annotations.FetchParam;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParametersUtils {

   public static String parametersAsString(List<? extends VariableElement> targetParameters,
                                           Map<String, ? extends VariableElement> groupOfSourceParameters,
                                           List<? extends VariableElement> parameters) {
      return parameters.stream()
              .map(parameter -> parameter.getAnnotation(FetchParam.class))
              .filter(Objects::nonNull)
              .map(annotation -> getVariableElement(targetParameters, groupOfSourceParameters, annotation))
              .map(VariableElement::getSimpleName)
              .collect(Collectors.joining(","));
   }

   public static VariableElement getVariableElement(List<? extends VariableElement> targetParameters,
                                                    Map<String, ? extends VariableElement> groupOfSourceParameters,
                                                    FetchParam annotation) {
      String name = annotation.name();
      int num = annotation.num();
      if (num != -1) {
         return targetParameters.get(num);
      } else if (StringUtils.isNoneBlank(name)) {
         return Objects.requireNonNull(groupOfSourceParameters.get(name),
                 "Can't you recheck parameters name because not found parameter with this name: " + name);
      }
      throw new IllegalArgumentException("Cannot find parameter in annotation " + annotation.getClass());
   }

}
