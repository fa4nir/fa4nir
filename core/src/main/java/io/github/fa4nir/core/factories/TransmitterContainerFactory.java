package io.github.fa4nir.core.factories;

import io.github.fa4nir.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;
import io.github.fa4nir.core.factories.fields.TransmitterTargetFieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;
import io.github.fa4nir.core.factories.methods.OverridingMethodsFactory;
import io.github.fa4nir.core.factories.types.AnnotationTransferFactory;
import io.github.fa4nir.core.factories.types.TransmitterAbstractClassFactory;
import io.github.fa4nir.core.factories.types.TransmitterInterfaceFactory;

public class TransmitterContainerFactory {

    public static AnnotationTransferFactory transmitterInterfaceFactory() {
        return new TransmitterInterfaceFactory(overridingMethodFactory(), new TransmitterTargetFieldsFactory());
    }

    public static AnnotationTransferFactory transmitterAbstractClassFactory() {
        return new TransmitterAbstractClassFactory(overridingMethodFactory(), new TransmitterTargetFieldsFactory());
    }

    public static InterceptMethodFactory overridingMethodFactory() {
        return new OverridingMethodsFactory(exceptionFallBackMethodFactory());
    }

    public static FallBackMethodFactory exceptionFallBackMethodFactory() {
        return new ExceptionFallBackMethodFactory();
    }

}
