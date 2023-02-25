package com.github.processor;

import com.github.core.annotations.Receiver;
import com.github.core.annotations.Transmitter;
import com.github.core.factories.types.AnnotationTransferFactory;
import com.github.core.factories.types.TransmitterFactory;
import com.github.processor.utils.JavaFileWriterUtils;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Objects;
import java.util.Set;

@SupportedAnnotationTypes(value = "com.github.core.annotations.Transmitter")
@SupportedSourceVersion(value = SourceVersion.RELEASE_11)
public class TransmitterProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    public AnnotationTransferFactory functionInterceptorFactory;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        this.functionInterceptorFactory = new TransmitterFactory();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Transmitter.class);
            Set<? extends Element> receivers = roundEnv.getElementsAnnotatedWith(Receiver.class);
            for (Element element : elements) {
                PackageElement packageElement = this.processingEnv.getElementUtils().getPackageOf(element);
                TypeSpec typeSpec = this.functionInterceptorFactory.newTypeSpec(element, this.processingEnv, receivers);
                if (Objects.nonNull(typeSpec)) {
                    JavaFileWriterUtils.write(this.processingEnv,
                            String.format("%s.impl", packageElement.getQualifiedName()),
                            typeSpec
                    );
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }
}
