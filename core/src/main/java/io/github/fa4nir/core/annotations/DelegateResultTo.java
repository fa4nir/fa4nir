package io.github.fa4nir.core.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(value = DelegateResultTos.class)
public @interface DelegateResultTo {
    String method();
}
