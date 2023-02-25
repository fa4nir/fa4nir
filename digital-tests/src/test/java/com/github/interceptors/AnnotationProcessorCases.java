package com.github.interceptors;

import com.github.processor.TransmitterProcessor;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.interceptors.utils.TestFileUtils.loadJavaFileAsString;

class AnnotationProcessorCases {

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_0() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stab-0.java";
        String outputFileName = "output/output-stab-0.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZeroImpl", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_1() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stab-1.java";
        String outputFileName = "output/output-stab-1.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZeroImpl", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_2() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stab-2.java";
        String outputFileName = "output/output-stab-2.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZeroImpl", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_3() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stab-3.java";
        String outputFileName = "output/output-stab-3.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZeroImpl", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

}
