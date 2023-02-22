package com.github.interceptors;

import com.github.core.annotations.*;

import java.util.List;

@FunctionInterceptor(
        beanName = "MySupperPuperImpl",
        listenerType = CustomListenerType.class,
        methods = {
                @InterceptMapper(
                        listenerMethodName = "onSuccess",
                        toCurrentMethod = "customListener"
                ),
                @InterceptMapper(
                        listenerMethodName = "onSuccess1",
                        toCurrentMethod = "customListener1"
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
    public Person customListener(@GetParameter(num = 0) String name, @GetParameter(num = 2) List<String> payload) {
        System.out.println(payload);
        return new Person();
    }

    public void customListener1(@GetParameter(num = 0) String name, @GetParameter(num = 1) Double payload) {
        System.out.println(payload);
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

    @FallBackMethod(insideOfMethod = "onSuccess")
    public void fallBackForCustomListener(Exception e, @GetParameter(num = 2) List<String> payload) {
        System.out.println(e.getMessage());
    }

    @FallBackMethod(insideOfMethod = "onSuccess1")
    public void fallBackForCustomListener1(Exception e, @GetParameter(num = 1) double payload) {
        System.out.println(e.getMessage());
    }

}
