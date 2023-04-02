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

    @ReturnStatement
    @DelegateResultTo(method = "delegatorAcceptor")
    public String customListener(@FetchParam(num = 0) String payload) {
        return "test-string";
    }

    public void delegatorAcceptor(@FetchResult String payload) {
    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {
    }

    @DelegateResultTo(method = "delegatorStatusListener")
    public String statusListener(@FetchParam(name = "t") String t) {
        return "100";
    }

    @ReturnStatement
    public int delegatorStatusListener(@FetchResult String payload) {
        return 100;
    }

    public void fallBackForStatusListener(@ErrorSignal Exception e) {
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
        @NotifyTo(name = "statusListener")
        @FallBackMethod(name = "fallBackForStatusListener")
        int onStatus(String t);
    }

    public interface SupperCallback {
        String onSuccess(String result);
        int onStatus(String t);
    }

}
