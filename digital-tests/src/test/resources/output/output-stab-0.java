package com.github.interceptors.impl;

import com.github.interceptors.CaseCustomListenerSpecOne;
import com.google.common.util.concurrent.FutureCallback;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.Throwable;

public class TransmitterTemplateBeanCaseZeroImpl implements FutureCallback<String> {
    private final CaseCustomListenerSpecOne caseCustomListenerSpecOne;

    public TransmitterTemplateBeanCaseZeroImpl(CaseCustomListenerSpecOne caseCustomListenerSpecOne) {
        this.caseCustomListenerSpecOne = caseCustomListenerSpecOne;
    }

    @Override
    public void onSuccess(String result) {
        try {
            String resultFromCustomListener = this.caseCustomListenerSpecOne.customListener(result);
            this.caseCustomListenerSpecOne.delegatorAcceptor(resultFromCustomListener);
        } catch (Exception e) {
            this.caseCustomListenerSpecOne.fallBackForCustomListener(e);
        }
    }

    @Override
    public void onFailure(Throwable t) {
    }
}
