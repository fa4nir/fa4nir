package io.github.fa4nir.processor.wrappers;

import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.definitions.TransmitterDefinition;

import javax.lang.model.element.Element;

public interface TransmitterSource {

    void writerJavaFile(TransmitterDefinition definition, Element element, TypeSpec.Builder typeSpec);

}
