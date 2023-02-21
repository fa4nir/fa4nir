package com.github.interceptors;

public class CustomFallBackClass {

    public void fallBackForCustomListener(Exception e) {
        System.out.println(e.getMessage());
    }

}
