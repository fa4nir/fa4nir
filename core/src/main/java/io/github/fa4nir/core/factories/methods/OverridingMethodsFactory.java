package io.github.fa4nir.core.factories.methods;

import com.squareup.javapoet.MethodSpec;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinition;
import io.github.fa4nir.core.definitions.OverridingMethodsDefinitionBuilder;
import io.github.fa4nir.core.wrappers.OverrideMethodWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Objects;

import static io.github.fa4nir.core.utils.ParametersUtils.parametersAsString;

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
            OverridingMethodsDefinition definition = ofDefinitions(
                    sourceMethod, target, annotationNotifyTo, annotationFallBackMethod
            );
            String parametersAsString = parametersAsString(
                    definition.getSourceParameters(),
                    definition.getGroupOfSourceParameters(),
                    definition.getTargetParameters()
            );
            return definition.isPredicateMethodsSize() ?
                    this.predicateWrapper.wrap(parametersAsString, definition, builder)
                            .build() :
                    this.tryCatchMethodBaseWrapper.wrap(parametersAsString, definition, builder)
                            .build();
        }
        return MethodSpec.overriding(sourceMethod).build();
    }

    private OverridingMethodsDefinition ofDefinitions(ExecutableElement sourceMethod, Element target,
                                                      NotifyTo annotationNotifyTo, FallBackMethod annotationFallBackMethod) {
        return OverridingMethodsDefinitionBuilder.newBuilder()
                .sourceMethod(sourceMethod).target(target).annotationNotifyTo(annotationNotifyTo)
                .annotationFallBackMethod(annotationFallBackMethod).notifyToTarget()
                .targetMethods().sourceParameters()
                .groupOfSourceParameters().targetFieldName().targetMethod()
                .resultName().delegateResultToAnnotations().fallBackMethodName()
                .fallBackMethod().targetMethodReturnType()
                .targetParameters().predicateMethods()
                .build();
    }

}
