package io.github.fa4nir.processor.wrappers;

import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.definitions.TransmitterDefinition;
import io.github.fa4nir.processor.utils.JavaFileWriterUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.Objects;

public class JavaFileWriterTransmitter implements TransmitterSource {

    private final ProcessingEnvironment processingEnv;

    public JavaFileWriterTransmitter(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public void writerJavaFile(TransmitterDefinition definition, Element element, TypeSpec.Builder typeSpec) {
        if (Objects.nonNull(typeSpec)) {
            PackageElement packageElement = this.processingEnv.getElementUtils().getPackageOf(element);
            String packageName = String.format("%s.impl", packageElement.getQualifiedName());
            JavaFileWriterUtils.write(this.processingEnv, packageName, typeSpec.build());
        }
    }

}
