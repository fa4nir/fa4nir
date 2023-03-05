package io.github.fa4nir.core.factories.fields;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

public interface FieldsFactory {

    FieldSpec newField(TypeName targetTypeName, String targetAsFieldName);

}
