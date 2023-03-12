package io.github.fa4nir.core.wrappers;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.annotations.FetchParam;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.factories.MethodParametersExtractor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PredicateMethodWrapper implements OverrideMethodWrapper, MethodParametersExtractor {

   private final OverrideMethodWrapper wrapper;

   public PredicateMethodWrapper(OverrideMethodWrapper wrapper) {
      this.wrapper = wrapper;
   }

   @Override
   public MethodSpec.Builder wrap(String parametersAsString, OverridingMethodsDefinition definition, MethodSpec.Builder builder) {
      if (definition.getPredicateMethods().size() > 0) {
         ExecutableElement predicate = definition.getPredicateMethods().get(0);
         String predicateSimpleName = predicate.getSimpleName().toString();
         List<? extends VariableElement> predicateParameters = predicate.getParameters();
         String strPredicateParameters = parametersAsString(definition.getSourceParameters(), definition.getGroupOfSourceParameters(), predicateParameters);
         builder.beginControlFlow("if(this.$N.$N($N))", definition.getTargetFieldName(), predicateSimpleName, strPredicateParameters);
         return this.wrapper.wrap(parametersAsString, definition, builder)
                 .endControlFlow();
      }
      return this.wrapper.wrap(parametersAsString, definition, builder);
   }

   private String parametersAsString(List<? extends VariableElement> targetParameters,
                                     Map<String, ? extends VariableElement> groupOfSourceParameters,
                                     List<? extends VariableElement> parameters) {
      return parameters.stream()
              .map(parameter -> parameter.getAnnotation(FetchParam.class))
              .filter(Objects::nonNull)
              .map(annotation -> getVariableElement(targetParameters, groupOfSourceParameters, annotation))
              .map(VariableElement::getSimpleName)
              .collect(Collectors.joining(","));
   }

}
