package com.github.interceptors;

import com.google.common.util.concurrent.FutureCallback;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.annotations.Transmitter;

@Transmitter(
        beanName = "TransmitterTemplateBeanCaseZero",
        receiverName = "ReceiverCustomListenerId",
        isSupper = false
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
