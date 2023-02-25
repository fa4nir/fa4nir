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

class TransmitterProcessorCases {

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_0() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-0.java";
        String outputFileName = "output/output-stub-0.java";
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
        String inputFileName = "input/input-stub-1.java";
        String outputFileName = "output/output-stub-1.java";
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
        String inputFileName = "input/input-stub-2.java";
        String outputFileName = "output/output-stub-2.java";
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
        String inputFileName = "input/input-stub-3.java";
        String outputFileName = "output/output-stub-3.java";
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
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_4() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-4.java";
        String outputFileName = "output/output-stub-4.java";
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
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_5() throws URISyntaxException, IOException {
        String inputInterfaceFileName = "input/input-stub-5-1.java";
        String inputFileName = "input/input-stub-5-2.java";
        String outputFileName = "output/output-stub-4.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject inputInterface = JavaFileObjects.forSourceString(
                "com.github.interceptors.TransmitterTemplate", loadJavaFileAsString(inputInterfaceFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZeroImpl", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(inputInterface, input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

}
