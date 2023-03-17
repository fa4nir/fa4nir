package com.github.interceptors;

import com.google.common.util.concurrent.FutureCallback;
import io.github.fa4nir.core.annotations.*;

import java.util.Objects;

@Receiver(name = "ReceiverCustomListenerId")
public class CaseCustomListenerSpec {

    @PayloadPredicate
    public boolean isPayloadNotNull(@FetchParam(name = "result") String payload) {
        return Objects.nonNull(payload);
    }

    @DelegateResultTo(method = "delegatorAcceptor")
    public String customListener(@FetchParam(num = 0) String payload) {
        return "test-string";
    }

    @ReturnStatement
    public String delegatorAcceptor(@FetchResult String payload) {
        return "Hello test";
    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {

    }

    @Transmitter(
            beanName = "TransmitterTemplateBeanCaseZero",
            receiverName = "ReceiverCustomListenerId",
            isSupper = false
    )
    public interface TransmitterTemplate extends SupperCallback {
        @Override
        @NotifyTo(name = "customListener")
        @FallBackMethod(name = "fallBackForCustomListener")
        String onSuccess(String result);

        @Override
        default void onFailure(Throwable t) {
        }
    }

    public interface SupperCallback {
        String onSuccess(String result);
        void onFailure(Throwable t);
    }

}
