package io.github.fa4nir.examples.listeners;

public interface CustomListenerWithExtendsAnotherListener extends CustomListenerType {

    void onSuccess(String r);

    void onFailure(Throwable t);

}
