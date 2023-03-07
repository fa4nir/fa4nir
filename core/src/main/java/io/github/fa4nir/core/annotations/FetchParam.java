package io.github.fa4nir.core.annotations;

import java.lang.annotation.*;

/**
 * This annotation indicate parameter which should be retrieved
 * from base method of listener. By two ways. First way is put
 * number of parameter second way is retrieved parameter by name.
 *
 * @author Andrii
 * @version 1.0.0
 */
@Documented
@Target(value = {
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface FetchParam {

    /**
     * This field should contain number of base method parameter.
     *
     * @return should return positive int
     */
    int num() default -1;

    /**
     * This field should contain name of method parameter.
     *
     * @return should return name of base method parameter
     */
    String name() default "";
}
