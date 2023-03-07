package com.github.interceptors.impl;

import com.github.interceptors.CaseCustomListenerSpec;
import com.github.interceptors.TransmitterTemplate;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;

public class TransmitterTemplateBeanCaseZero implements TransmitterTemplate {
    private final CaseCustomListenerSpec caseCustomListenerSpec;

    public TransmitterTemplateBeanCaseZero(CaseCustomListenerSpec caseCustomListenerSpec) {
        this.caseCustomListenerSpec = caseCustomListenerSpec;
    }

    @Override
    public void onSuccess(String myCustomName) {
        try {
            String result = this.caseCustomListenerSpec.customListener(myCustomName);
            this.caseCustomListenerSpec.delegatorAcceptor(result);
            this.caseCustomListenerSpec.delegatorAcceptor(result,myCustomName);
            this.caseCustomListenerSpec.supperDelegatorAcceptor(myCustomName,result);
        } catch (Exception e) {
            this.caseCustomListenerSpec.fallBackForCustomListener(e);
        }
    }
}
