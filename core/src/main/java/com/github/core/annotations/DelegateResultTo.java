package com.github.core.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(value = DelegateResultTos.class)
public @interface DelegateResultTo {
    String method();
}
