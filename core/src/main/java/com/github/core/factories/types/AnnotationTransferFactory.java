package com.github.core.factories.types;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import java.util.Set;

public interface AnnotationTransferFactory {

    TypeSpec newTypeSpec(Element element);

}
