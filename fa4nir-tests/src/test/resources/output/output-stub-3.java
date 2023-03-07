package com.github.interceptors.impl;

import com.github.interceptors.CaseCustomListenerSpec;
import com.google.common.util.concurrent.FutureCallback;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.Throwable;

public class TransmitterTemplateBeanCaseZero implements FutureCallback<String> {
    private final CaseCustomListenerSpec caseCustomListenerSpec;

    public TransmitterTemplateBeanCaseZeroImpl(CaseCustomListenerSpec caseCustomListenerSpec) {
        this.caseCustomListenerSpec = caseCustomListenerSpec;
    }

    @Override
    public void onSuccess(String result) {
        try {
            String resultFromCustomListener = this.caseCustomListenerSpec.customListener(result);
            this.caseCustomListenerSpec.delegatorAcceptor(resultFromCustomListener);
            this.caseCustomListenerSpec.delegatorAcceptor(resultFromCustomListener, result);
        } catch (Exception e) {
            this.caseCustomListenerSpec.fallBackForCustomListener(e);
        }
    }

    @Override
    public void onFailure(Throwable t) {
    }
}
