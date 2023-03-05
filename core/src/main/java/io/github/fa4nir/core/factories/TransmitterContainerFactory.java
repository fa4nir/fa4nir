package io.github.fa4nir.core.factories;

import io.github.fa4nir.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;
import io.github.fa4nir.core.factories.fields.TransmitterTargetFieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;
import io.github.fa4nir.core.factories.methods.OverridingMethodsFactory;
import io.github.fa4nir.core.factories.types.AnnotationTransferFactory;
import io.github.fa4nir.core.factories.types.TransmitterFactory;

public class TransmitterContainerFactory {

    public static AnnotationTransferFactory transmitterFactory() {
        return new TransmitterFactory(overridingMethodFactory(), new TransmitterTargetFieldsFactory());
    }

    public static InterceptMethodFactory overridingMethodFactory() {
        return new OverridingMethodsFactory(exceptionFallBackMethodFactory());
    }

    public static FallBackMethodFactory exceptionFallBackMethodFactory() {
        return new ExceptionFallBackMethodFactory();
    }

}
