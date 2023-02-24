package com.github.interceptors;

import com.github.core.annotations.*;
import com.google.common.util.concurrent.FutureCallback;

@Receiver(name = "customListenerClass")
public class CustomListenerClass {

    @DelegateResultTo(method = "delegatorAcceptor")
    @DelegateResultTo(method = "supperDelegatorAcceptor")
    public String customListener(@FetchParam(num = 0) String payload) {
        System.out.println(payload);
        return "Suppa pupa dupa";
    }

    public void delegatorAcceptor(@FetchResult String payload,
                                  @FetchParam(num = 0) Object param0) {
        System.out.println("Delegate to " + payload + " parameter " + param0);
    }

    public void delegatorAcceptor(@FetchResult String payload) {
        System.out.println("Delegate to " + payload);
    }

    public void supperDelegatorAcceptor(@FetchParam(num = 0) Object param0,
                                        @FetchResult String payload) {
        System.out.println(param0 + " Delegate to " + payload);
    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {
        System.out.println(e.getMessage());
    }

    @Transmitter(
            beanName = "MyCustomBean",
            receiverName = "customListenerClass"
    )
    public interface TransmitterTemplate extends FutureCallback<String> {
        @Override
        @NotifyTo(name = "customListener")
        @FallBackMethod(name = "fallBackForCustomListener")
        void onSuccess(String result);

        @Override
        default void onFailure(Throwable t) {
        }
    }

}
