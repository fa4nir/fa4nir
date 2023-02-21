package com.github.interceptors;

import com.github.interceptors.impl.FutureCallbackImpl;

public class Main {

    public static void main(String[] args) {
        FutureCallbackImpl<String> t = new FutureCallbackImpl<String>(new CustomListenerClass());
        t.onSuccess("Hurma murma");
    }

}
