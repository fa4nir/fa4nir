package com.github.core.factories.methods;

import com.github.core.utils.OverridingMethodMetaInfo;
import com.squareup.javapoet.MethodSpec;

public interface InterceptMethodFactory {

    MethodSpec newMethodSpec(OverridingMethodMetaInfo methodMetaInfo);

}
