package io.github.fa4nir.core.exceptions;

import io.github.fa4nir.core.annotations.Transmitter;

public class ValidationExceptionsDeclarations {

    public static IllegalArgumentException targetClassNotFound(Transmitter transmitter) {
        return new IllegalArgumentException(String.format("Cannot find receiver by class name in %s", transmitter.beanName()));
    }

    public static IllegalArgumentException beanNameIsBlank() {
        return new IllegalArgumentException("Transmitter bean name is null or empty");
    }

    public static IllegalArgumentException receiverNameIsBlank() {
        return new IllegalArgumentException("Transmitter receiver name is null or empty");
    }

}
