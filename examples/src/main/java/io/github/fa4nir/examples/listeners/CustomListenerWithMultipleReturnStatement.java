package io.github.fa4nir.examples.listeners;

import io.github.fa4nir.examples.payloads.Person;

public interface CustomListenerWithMultipleReturnStatement {

    Person onSuccess(String parameter0, Double parameters2);

    String notifyToIt(long id, String name, int age);

}
