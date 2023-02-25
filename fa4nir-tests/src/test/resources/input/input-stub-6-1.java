package com.github.interceptors;

import com.github.core.annotations.*;
import com.google.common.util.concurrent.FutureCallback;

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
