package com.github.interceptors.impl;

import com.github.interceptors.CaseCustomListenerSpec;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;

public class TransmitterTemplateBeanCaseZero implements CaseCustomListenerSpec.TransmitterTemplate {
    private final CaseCustomListenerSpec caseCustomListenerSpec;

    public TransmitterTemplateBeanCaseZeroImpl(CaseCustomListenerSpec caseCustomListenerSpec) {
        this.caseCustomListenerSpec = caseCustomListenerSpec;
    }

    @Override
    public void onSuccess(String result) {
        try {
            String resultFromCustomListener = this.caseCustomListenerSpec.customListener(result);
            this.caseCustomListenerSpec.delegatorAcceptor(resultFromCustomListener);
            this.caseCustomListenerSpec.delegatorAcceptor(resultFromCustomListener,result);
            this.caseCustomListenerSpec.supperDelegatorAcceptor(result,resultFromCustomListener);
        } catch (Exception e) {
            this.caseCustomListenerSpec.fallBackForCustomListener(e);
        }
    }
}
