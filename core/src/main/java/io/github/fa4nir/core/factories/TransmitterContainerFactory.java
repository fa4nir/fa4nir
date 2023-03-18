package io.github.fa4nir.core.factories;

import io.github.fa4nir.core.factories.fallbacks.ExceptionFallBackMethodFactory;
import io.github.fa4nir.core.factories.fallbacks.FallBackMethodFactory;
import io.github.fa4nir.core.factories.fields.TransmitterTargetFieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;
import io.github.fa4nir.core.factories.methods.OverridingMethodsFactory;
import io.github.fa4nir.core.factories.types.AnnotationTransferFactory;
import io.github.fa4nir.core.factories.types.TransmitterAbstractClassFactory;
import io.github.fa4nir.core.factories.types.TransmitterInterfaceFactory;
import io.github.fa4nir.core.wrappers.*;

public class TransmitterContainerFactory {

    public static AnnotationTransferFactory transmitterInterfaceFactory() {
        return new TransmitterInterfaceFactory(overridingMethodFactory(), new TransmitterTargetFieldsFactory());
    }

    public static AnnotationTransferFactory transmitterAbstractClassFactory() {
        return new TransmitterAbstractClassFactory(overridingMethodFactory(), new TransmitterTargetFieldsFactory());
    }

    public static InterceptMethodFactory overridingMethodFactory() {
        return new OverridingMethodsFactory();
    }

    public static FallBackMethodFactory exceptionFallBackMethodFactory() {
        return new ExceptionFallBackMethodFactory();
    }

    public static OverrideMethodWrapper tryCatchMethodBaseWrapper() {
        return new TryCatchMethodBaseWrapper(exceptionFallBackMethodFactory(), new BodyMethodBaseWrapper());
    }

    public static OverrideMethodWrapper predicateWrapper() {
        return new PredicateMethodWrapper(new TryCatchMethodBaseWrapper(exceptionFallBackMethodFactory(), new BodyMethodBaseWrapper()));
    }

    public static OverrideMethodWrapper returnStatement(OverrideMethodWrapper wrapper) {
        return new ResultReturnWrapper(wrapper);
    }

}
