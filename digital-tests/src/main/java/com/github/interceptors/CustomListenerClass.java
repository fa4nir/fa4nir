package com.github.interceptors;

import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.GetParameter;
import com.github.core.annotations.InterceptMapper;
import com.google.common.util.concurrent.FutureCallback;

@FunctionInterceptor(
        listenerType = FutureCallback.class,
        methods = {
                @InterceptMapper(listenerMethodName = "onSuccess", toCurrentMethod = "customListener")
        },
        fallBackClassHandler = CustomFallBackClass.class
)
public class CustomListenerClass {

     public void customListener(@GetParameter(num = 0) Object payload) {

     }

}
