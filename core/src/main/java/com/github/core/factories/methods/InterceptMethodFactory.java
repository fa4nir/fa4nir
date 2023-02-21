package com.github.core.factories.methods;

import com.github.core.annotations.InterceptMapper;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

public interface InterceptMethodFactory {

    MethodSpec newMethodSpec(OverridingMethodMetaInfo methodMetaInfo);

}
