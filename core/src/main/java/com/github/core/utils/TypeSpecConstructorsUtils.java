package com.github.core.utils;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

public class TypeSpecConstructorsUtils {

    public static MethodSpec.Builder constructor(TypeName mainField, String mainFieldName) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mainField, mainFieldName)
                .addStatement(CodeBlock.of("this.$N = $N", mainFieldName, mainFieldName));
    }

}
