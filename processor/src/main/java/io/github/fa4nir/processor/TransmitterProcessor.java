package io.github.fa4nir.processor;

import io.github.fa4nir.core.annotations.Receiver;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.core.definitions.TransmitterDefinition;
import io.github.fa4nir.core.factories.TransmitterContainerFactory;
import io.github.fa4nir.core.factories.types.AnnotationTransferFactory;
import io.github.fa4nir.core.utils.FactoryTypes;
import io.github.fa4nir.processor.wrappers.JavaFileWriterTransmitter;
import io.github.fa4nir.processor.wrappers.SpringStereotype;
import io.github.fa4nir.processor.wrappers.TransmitterSource;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SupportedAnnotationTypes(value = "io.github.fa4nir.core.annotations.Transmitter")
@SupportedSourceVersion(value = SourceVersion.RELEASE_11)
public class TransmitterProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    public Map<FactoryTypes, AnnotationTransferFactory> functionInterceptorFactories;

    private TransmitterSource transmitterSource;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        this.functionInterceptorFactories = new HashMap<>();
        this.functionInterceptorFactories.put(FactoryTypes.interfaceDefinition,
                TransmitterContainerFactory.transmitterInterfaceFactory());
        this.functionInterceptorFactories.put(FactoryTypes.classDefinition,
                TransmitterContainerFactory.transmitterAbstractClassFactory());
        this.transmitterSource = new SpringStereotype(new JavaFileWriterTransmitter(
                this.processingEnv
        ));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            try {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Transmitter.class);
                Set<? extends Element> receivers = roundEnv.getElementsAnnotatedWith(Receiver.class);
                for (Element element : elements) {
                    AnnotationTransferFactory factory = this.functionInterceptorFactories.get(
                            FactoryTypes.getKey(element.getKind()));
                    if (Objects.nonNull(factory)) {
                        TransmitterDefinition definition = TransmitterDefinition.newDefinition(element, receivers)
                                .transmitter().target().beanName()
                                .targetTypeName().targetAsFieldName().build();
                        this.transmitterSource.writerJavaFile(definition, element,
                                factory.newTypeSpec(element, this.processingEnv, definition));
                    }
                }
            } catch (Throwable e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                throw e;
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

}
