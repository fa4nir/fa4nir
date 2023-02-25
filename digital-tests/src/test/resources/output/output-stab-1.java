package com.github.interceptors.impl;

import com.github.interceptors.CaseCustomListenerSpecOne;
import com.google.common.util.concurrent.FutureCallback;

public class TransmitterTemplateBeanCaseZeroImpl implements FutureCallback<String> {
    private final CaseCustomListenerSpecOne caseCustomListenerSpecOne;

    public TransmitterTemplateBeanCaseZeroImpl(CaseCustomListenerSpecOne caseCustomListenerSpecOne) {
        this.caseCustomListenerSpecOne = caseCustomListenerSpecOne;
    }

    @Override
    public void onSuccess(String result) {
        try {
            this.caseCustomListenerSpecOne.customListener(result);
        } catch (Exception e) {
            this.caseCustomListenerSpecOne.fallBackForCustomListener(e);
        }
    }

    @Override
    public void onFailure(Throwable t) {
    }
}
