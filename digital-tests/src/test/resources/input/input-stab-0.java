package com.github.interceptors;

import com.github.core.annotations.*;
import com.google.common.util.concurrent.FutureCallback;

@Receiver(name = "ReceiverCustomListenerId")
public class CaseCustomListenerSpecOne {

    @DelegateResultTo(method = "delegatorAcceptor")
    public String customListener(@FetchParam(num = 0) String payload) {
        System.out.println(payload);
        return "Suppa pupa dupa";
    }

    public void delegatorAcceptor(@FetchResult String payload) {
        System.out.println("Delegate to " + payload);
    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {
        System.out.println(e.getMessage());
    }

    @Transmitter(
            beanName = "TransmitterTemplateBeanCaseZero",
            receiverName = "ReceiverCustomListenerId"
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
