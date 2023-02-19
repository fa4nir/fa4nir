package com.github.processor.utils;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.io.IOException;

public class JavaFileWriterUtils {

    public static void write(ProcessingEnvironment processingEnv, String packageName, TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
