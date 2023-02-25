package com.github.core.factories;

import com.github.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import com.github.core.factories.fallbacks.FallBackMethodFactory;
import com.github.core.factories.methods.InterceptMethodFactory;
import com.github.core.factories.methods.OverridingMethodsFactory;
import com.github.core.factories.types.AnnotationTransferFactory;
import com.github.core.factories.types.TransmitterFactory;

public class TransmitterContainerFactory {

    public static AnnotationTransferFactory transmitterFactory() {
        return new TransmitterFactory(overridingMethodFactory());
    }

    public static InterceptMethodFactory overridingMethodFactory() {
        return new OverridingMethodsFactory(exceptionFallBackMethodFactory());
    }

    public static FallBackMethodFactory exceptionFallBackMethodFactory() {
        return new ExceptionFallBackMethodFactory();
    }

}
