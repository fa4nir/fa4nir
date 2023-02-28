package io.github.fa4nir.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation mark interface or abstract class as template
 * of our future listener. And contains necessary methods to generate
 * class with implementation of listener.
 *
 * @author Andrii
 * @version 1.0.0
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transmitter {

    /**
     * This field should contain bean name of out implementation.
     *
     * @return future generated class name
     */
    String beanName();

    /**
     * This field should contain receiver id.
     *
     * @return class receiver id
     */
    String receiverName();

    /**
     * If true - base implementation of listener.
     * If false - current implementation.
     *
     * @return true/false
     */
    boolean isSupper() default true;

}
