package io.github.fa4nir.core.utils;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

public class ValidationUtils {

    public static List<? extends TypeMirror> validInterface(List<? extends TypeMirror> interfaces) {
        if (Objects.isNull(interfaces) || interfaces.size() != 1) {
            throw new IllegalArgumentException("Cannot find main interface");
        }
        return interfaces;
    }

}
