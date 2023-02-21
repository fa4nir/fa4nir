package com.github.interceptors;

import com.github.core.annotations.*;
import com.google.common.util.concurrent.FutureCallback;

import java.util.List;

@FunctionInterceptor(
        listenerType = CustomListenerType.class,
        methods = {
                @InterceptMapper(
                        listenerMethodName = "onSuccess",
                        toCurrentMethod = "customListener"
                )
        }
)
public class CustomListenerClass2 {

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
    public Person customListener(@GetParameter(num = 2) List<String> payload) {
        System.out.println(payload);
        return new Person();
    }

    public void delegatorAcceptor(@ActualResult Person payload,
                                  @GetParameter(num = 1) Integer number,
                                  @GetParameter(num = 2) List<String> param0) {
        System.out.println("Delegate to " + payload + " Num " + number + " parameter " + param0);
    }

    public void delegatorAcceptor(@ActualResult Person payload) {
        System.out.println("Delegate to " + payload);
    }

    public void supperDelegatorAcceptor(@GetParameter(num = 2) List<String> param0,
                                        @GetParameter(num = 0) String payload) {
        System.out.println(param0 + " Delegate to " + payload);
    }

    @FallBackMethod
    public void fallBackForCustomListener(Exception e) {
        System.out.println(e.getMessage());
    }

}
