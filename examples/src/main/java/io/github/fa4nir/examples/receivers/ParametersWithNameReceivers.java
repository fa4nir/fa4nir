package io.github.fa4nir.examples.receivers;

import io.github.fa4nir.core.annotations.*;
import io.github.fa4nir.examples.Person;

import java.util.logging.Level;
import java.util.logging.Logger;

@Receiver(name = "TransmitterWithNameInParameters")
public class ParametersWithNameReceivers {

    private static final Logger log = Logger.getLogger(ParametersWithNameReceivers.class.getName());

    @DelegateResultTo(method = "receivePersonWithName")
    @DelegateResultTo(method = "receivePersonWithNameAndWeight")
    public Person listenerWithParametersName(@FetchParam(name = "personName") String name) {
        return new Person(1L, name, "email.com");
    }

    public void receivePersonWithName(@FetchResult Person person,
                                      @FetchParam(name = "personName") String name) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, name});
    }

    public void receivePersonWithNameAndWeight(@FetchResult Person person,
                                               @FetchParam(num = 1) Double weight) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, weight});
    }

    public void fallbackListenerWithParametersName(@ErrorSignal Exception e, @FetchParam(name = "personName") String name) {
        log.log(Level.WARNING, "Message {0}, {1}", new Object[]{e.getMessage(), name});
    }

}
