package com.github.interceptors;

//import com.github.interceptors.impl.FutureCallbackImpl;

import com.github.interceptors.impl.CustomListenerCase2Impl;

import java.util.ArrayList;

public class GenerateClassMain {

    public static void main(String[] args) {
        CustomListenerCase2Impl c = new CustomListenerCase2Impl(new CustomListenerClass2());
        c.onSuccess("param1", 2030, new ArrayList<>());
    }

}
