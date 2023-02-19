package com.github.processor;

import com.github.core.annotations.FunctionInterceptor;
import com.github.core.factories.types.AnnotationTransferFactory;
import com.github.core.factories.types.FunctionInterceptorFactory;
import com.github.processor.utils.JavaFileWriterUtils;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Objects;
import java.util.Set;

@SupportedAnnotationTypes(value = "com.github.core.annotations.FunctionInterceptor")
@SupportedSourceVersion(value = SourceVersion.RELEASE_11)
public class FunctionInterceptorProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    public AnnotationTransferFactory functionInterceptorFactory;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        this.functionInterceptorFactory = new FunctionInterceptorFactory();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(FunctionInterceptor.class);
            for (Element element : elements) {
                PackageElement packageElement = this.processingEnv.getElementUtils().getPackageOf(element);
                TypeSpec typeSpec = this.functionInterceptorFactory.newTypeSpec(element);
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

}
