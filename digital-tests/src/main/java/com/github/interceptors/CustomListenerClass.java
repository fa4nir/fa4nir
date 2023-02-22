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
            methods = {
                    @DelegateToMethod(
                            methodName = "delegatorAcceptor"
                    ),
                    @DelegateToMethod(
                            methodName = "delegatorAcceptor"
                    ),
                    @DelegateToMethod(
                            methodName = "supperDelegatorAcceptor"
                    )
            }
    )
    public String customListener(@GetParameter(num = 0) Object payload) {
        System.out.println(payload);
        return "Suppa pupa dupa";
    }

    public void delegatorAcceptor(@ActualResult String payload,
                                  @GetParameter(num = 0) Object param0) {
        System.out.println("Delegate to " + payload + " parameter " + param0);
    }

    public void delegatorAcceptor(@ActualResult String payload) {
        System.out.println("Delegate to " + payload);
    }

    public void supperDelegatorAcceptor(@GetParameter(num = 0) Object param0,
                                        @ActualResult String payload) {
        System.out.println(param0 + " Delegate to " + payload);
    }

    @FallBackMethod(insideOfMethod = "onSuccess")
    public void fallBackForCustomListener(Exception e) {
        System.out.println(e.getMessage());
    }

}
