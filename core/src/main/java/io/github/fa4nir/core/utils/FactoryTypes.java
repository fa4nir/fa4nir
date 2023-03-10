package io.github.fa4nir.core.utils;

import javax.lang.model.element.ElementKind;

public enum FactoryTypes {
    classDefinition, interfaceDefinition, none;

    public static FactoryTypes getKey(ElementKind kind) {
        if (kind.isClass()) {
            return classDefinition;
        } else if (kind.isInterface()) {
            return interfaceDefinition;
        } else {
            return none;
        }
    }

}
