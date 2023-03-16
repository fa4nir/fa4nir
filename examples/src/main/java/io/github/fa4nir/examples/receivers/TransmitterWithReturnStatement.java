package io.github.fa4nir.examples.receivers;

import io.github.fa4nir.core.annotations.*;
import io.github.fa4nir.examples.Person;

import java.util.logging.Level;
import java.util.logging.Logger;

@Receiver(name = "TransmitterWithReturnStatement")
public class TransmitterWithReturnStatement {

    private static final Logger log = Logger.getLogger(ParametersWithNameReceivers.class.getName());

    @DelegateResultTo(method = "firstAcceptor")
    @DelegateResultTo(method = "nextAcceptor")
    public Integer listener(@FetchParam(name = "personName") String name) {
        return 0;
    }

    public void firstAcceptor(@FetchResult Integer person,
                              @FetchParam(name = "personName") String name) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, name});
    }

    @ReturnStatement
    public Person nextAcceptor(@FetchResult Integer person,
                               @FetchParam(num = 1) Double weight) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, weight});
        return new Person();
    }

    public void fallback(@ErrorSignal Exception e, @FetchParam(name = "personName") String name) {
        log.log(Level.WARNING, "Message {0}, {1}", new Object[]{e.getMessage(), name});
    }


}
