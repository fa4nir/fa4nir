package com.github.interceptors;

import com.google.common.util.concurrent.FutureCallback;
import io.github.fa4nir.core.annotations.*;

@Receiver(name = "ReceiverCustomListenerId")
public class CaseCustomListenerSpec {

    @DelegateResultTo(method = "delegatorAcceptor")
    @DelegateResultTo(method = "supperDelegatorAcceptor")
    public String customListener(@FetchParam(num = 0) String payload) {
        return "test-string";
    }

    public void delegatorAcceptor(@FetchResult String payload) {

    }

    public void delegatorAcceptor(@FetchResult String payload,
                                  @FetchParam(num = 0) Object param0) {

    }

    public void supperDelegatorAcceptor(@FetchParam(num = 0) Object param0,
                                        @FetchResult String payload) {

    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {

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
