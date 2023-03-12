package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.FetchParam;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinitionBuilder;
import io.github.fa4nir.core.wrappers.OverrideMethodWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OverridingMethodsFactory implements InterceptMethodFactory {

    private final OverrideMethodWrapper tryCatchMethodBaseWrapper;

    private final OverrideMethodWrapper predicateWrapper;

    public OverridingMethodsFactory(OverrideMethodWrapper tryCatchMethodBaseWrapper, OverrideMethodWrapper predicateWrapper) {
        this.tryCatchMethodBaseWrapper = tryCatchMethodBaseWrapper;
        this.predicateWrapper = predicateWrapper;
    }

    @Override
    public MethodSpec newMethodSpec(ExecutableElement sourceMethod, Element target) {
        NotifyTo annotationNotifyTo = sourceMethod.getAnnotation(NotifyTo.class);
        FallBackMethod annotationFallBackMethod = sourceMethod.getAnnotation(FallBackMethod.class);
        if (Objects.nonNull(annotationNotifyTo)) {
            MethodSpec.Builder builder = MethodSpec.overriding(sourceMethod);
            OverridingMethodsDefinition definition = OverridingMethodsDefinitionBuilder.newBuilder()
                    .sourceMethod(sourceMethod).target(target).annotationNotifyTo(annotationNotifyTo)
                    .annotationFallBackMethod(annotationFallBackMethod).notifyToTarget().targetMethods().sourceParameters()
                    .groupOfSourceParameters().targetFieldName().targetMethod()
                    .resultName().delegateResultToAnnotations().fallBackMethodName()
                    .fallBackMethod().targetMethodReturnType()
                    .targetParameters().predicateMethods()
                    .build();
            String parametersAsString = parametersAsString(definition.getSourceParameters(),
                    definition.getGroupOfSourceParameters(),
                    definition.getTargetParameters());
            if (definition.isPredicateMethodsSize()) {
                return this.predicateWrapper.wrap(parametersAsString, definition, builder)
                        .build();
            }
            return this.tryCatchMethodBaseWrapper.wrap(parametersAsString, definition, builder).build();
        }
        return MethodSpec.overriding(sourceMethod).build();
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
