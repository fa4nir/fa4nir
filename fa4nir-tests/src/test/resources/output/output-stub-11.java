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
    public String onSuccess(String result) {
        String resultOfOnSuccess = null;
        if(this.caseCustomListenerSpec.isPayloadNotNull(result)) {
            try {
                String secondResultFromCustomListener = this.caseCustomListenerSpec.customListener(result);
                resultOfOnSuccess = this.caseCustomListenerSpec.delegatorAcceptor(secondResultFromCustomListener);
            } catch (Exception e) {
                this.caseCustomListenerSpec.fallBackForCustomListener(e);
            }
        }
        return resultOfOnSuccess;
    }
}