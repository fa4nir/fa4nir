package com.github.interceptors;

import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.InterceptMapper;

@FunctionInterceptor(
        listenerType = CustomListenerType.class,
        methods = {
                @InterceptMapper(listenerMethodName = "lst", toCurrentMethod = "21d")
        },
        fallBackClassHandler = CustomFallBackClass.class
)
public class CustomListenerClass {
}
