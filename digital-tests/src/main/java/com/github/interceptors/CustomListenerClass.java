package com.github.interceptors;

import com.github.core.annotations.*;
import com.google.common.util.concurrent.FutureCallback;

@FunctionInterceptor(
        listenerType = FutureCallback.class,
        methods = {
                @InterceptMapper(
                        listenerMethodName = "onSuccess",
                        toCurrentMethod = "customListener"
                )
        }
)
public class CustomListenerClass {

    @DelegateResultTo(
            methods = @DelegateToMethod(
                    methodName = "delegatorAcceptor"
            )
    )
    public String customListener(@GetParameter(num = 0) Object payload) {
        System.out.println(payload);
        return (String) payload;
    }

    @FallBackMethod
    public void fallBackForCustomListener(Exception e) {
        System.out.println(e.getMessage());
    }

    public void delegatorAcceptor(String payload) {
        System.out.println("Delegate to " + payload);
    }

}
