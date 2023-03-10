package io.github.fa4nir.core.factories.types;

import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.definitions.TransmitterDefinition;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public interface AnnotationTransferFactory {

    TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv, TransmitterDefinition definition);

}
