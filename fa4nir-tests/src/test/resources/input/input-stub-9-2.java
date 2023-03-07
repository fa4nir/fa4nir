package com.github.interceptors;

import io.github.fa4nir.core.annotations.*;

@Receiver(name = "ReceiverCustomListenerId")
public class CaseCustomListenerSpec {

    @DelegateResultTo(method = "delegatorAcceptor")
    @DelegateResultTo(method = "supperDelegatorAcceptor")
    public String customListener(@FetchParam(name = "myCustomName") String payload) {
        return "test-string";
    }

    public void delegatorAcceptor(@FetchResult String payload) {

    }

    public void delegatorAcceptor(@FetchResult String payload,
                                  @FetchParam(name = "myCustomName") Object param0) {

    }

    public void supperDelegatorAcceptor(@FetchParam(name = "myCustomName") Object param0,
                                        @FetchResult String payload) {

    }

    public void fallBackForCustomListener(@ErrorSignal Exception e) {

    }

}
