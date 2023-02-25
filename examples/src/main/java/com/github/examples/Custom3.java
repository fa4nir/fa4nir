package com.github.examples;

public interface Custom3 extends CustomListenerType {

    void onSuccess(String r);

    void onFailure(Throwable t);

}
