package com.github.core.annotations;

import com.github.core.utils.DEFAULT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionInterceptor {

    String beanName() default "";

    Class<?> listenerType();

    InterceptMapper[] methods();

    Class<?> fallBackClassHandler() default DEFAULT.class;

}
