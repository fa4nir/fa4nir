package io.github.fa4nir.core.factories.fields;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

public class TransmitterTargetFieldsFactory implements FieldsFactory {
    @Override
    public FieldSpec newField(TypeName targetTypeName, String targetAsFieldName) {
        return FieldSpec.builder(targetTypeName, targetAsFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addModifiers()
                .build();
    }
}
