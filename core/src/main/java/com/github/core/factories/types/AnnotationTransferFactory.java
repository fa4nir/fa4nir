package com.github.core.factories.types;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;

public interface AnnotationTransferFactory {

    TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv, Set<? extends Element> receivers);

}
