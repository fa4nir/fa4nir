package io.github.fa4nir.core.definitions;

import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public interface OverridingMethodsDefinitionBuilder {

    static OverridingMethodsDefinitionBuilder newBuilder() {
        return new DefaultOverridingMethodsDefinition();
    }

    OverridingMethodsDefinitionBuilder sourceMethod(ExecutableElement sourceMethod);

    OverridingMethodsDefinitionBuilder target(Element target);

    OverridingMethodsDefinitionBuilder annotationNotifyTo(NotifyTo annotationNotifyTo);

    OverridingMethodsDefinitionBuilder annotationFallBackMethod(FallBackMethod annotationFallBackMethod);

    OverridingMethodsDefinitionBuilder targetMethods();

    OverridingMethodsDefinitionBuilder sourceParameters();

    OverridingMethodsDefinitionBuilder groupOfSourceParameters();

    OverridingMethodsDefinitionBuilder targetFieldName();

    OverridingMethodsDefinitionBuilder targetMethod();

    OverridingMethodsDefinitionBuilder resultName();

    OverridingMethodsDefinitionBuilder delegateResultToAnnotations();

    OverridingMethodsDefinitionBuilder fallBackMethod();

    OverridingMethodsDefinitionBuilder targetMethodReturnType();

    OverridingMethodsDefinitionBuilder notifyToTarget();

    OverridingMethodsDefinitionBuilder targetParameters();

    OverridingMethodsDefinitionBuilder predicateMethods();

    OverridingMethodsDefinitionBuilder fallBackMethodName();

    OverridingMethodsDefinition build();

}
