package io.github.fa4nir;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import io.github.fa4nir.processor.TransmitterProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static io.github.fa4nir.utils.TestFileUtils.loadJavaFileAsString;

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
        String outputFileName = "output/output-stub-5.java";
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

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_6() throws URISyntaxException, IOException {
        String inputInterfaceFileName = "input/input-stub-6-1.java";
        String inputFileName = "input/input-stub-6-2.java";
        String outputFileName = "output/output-stub-6.java";
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

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_7() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-7.java";
        String outputFileName = "output/output-stub-7.java";
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
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_8() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-8.java";
        String outputFileName = "output/output-stub-8.error.txt";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        String output = loadJavaFileAsString(outputFileName);
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .failsToCompile()
                .withErrorContaining(output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_9() throws URISyntaxException, IOException {
        String inputInterfaceFileName = "input/input-stub-9-1.java";
        String inputFileName = "input/input-stub-9-2.java";
        String outputFileName = "output/output-stub-9.java";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        JavaFileObject inputInterface = JavaFileObjects.forSourceString(
                "com.github.interceptors.TransmitterTemplate", loadJavaFileAsString(inputInterfaceFileName)
        );
        JavaFileObject output = JavaFileObjects.forSourceString(
                "com.github.interceptors.impl.TransmitterTemplateBeanCaseZero", loadJavaFileAsString(outputFileName)
        );
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(inputInterface, input))
                .processedWith(new TransmitterProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_10() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-10.java";
        String outputFileName = "output/output-stub-10.java";
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
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_10_1() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-10_1.java";
        String outputFileName = "output/output-stub-10_1.error.txt";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        String output = loadJavaFileAsString(outputFileName);
        Assertions.assertThrows(RuntimeException.class, () -> Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .failsToCompile(), output);

    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_10_2() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-10_2.java";
        String outputFileName = "output/output-stub-10_2.error.txt";
        JavaFileObject input = JavaFileObjects.forSourceString(
                "com.github.interceptors.CaseCustomListenerSpec", loadJavaFileAsString(inputFileName)
        );
        String output = loadJavaFileAsString(outputFileName);
        Assertions.assertThrows(RuntimeException.class, () -> Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(List.of(input))
                .processedWith(new TransmitterProcessor())
                .failsToCompile(), output);
    }

    @Test
    void givenInputAnnotationClass_whenRunProcessor_thenReceivedOutputFile_11() throws URISyntaxException, IOException {
        String inputFileName = "input/input-stub-11.java";
        String outputFileName = "output/output-stub-11.java";
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
