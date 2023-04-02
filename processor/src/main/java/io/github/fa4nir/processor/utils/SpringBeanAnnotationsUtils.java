package io.github.fa4nir.processor.utils;

import com.squareup.javapoet.ClassName;

public class SpringBeanAnnotationsUtils {

    public static ClassName component() {
        return ClassName.get("org.springframework.stereotype", "Component");
    }

}
