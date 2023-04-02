package io.github.fa4nir.examples.receivers;

import io.github.fa4nir.core.annotations.*;
import io.github.fa4nir.examples.listeners.CustomListenerWithMultipleReturnStatement;
import io.github.fa4nir.examples.payloads.Person;

import java.util.logging.Level;
import java.util.logging.Logger;

@Receiver(name = "ReceiverWithMultipleReturnStatement")
public class ReceiverWithMultipleReturnStatement {

    private static final Logger log = Logger.getLogger(ParametersWithNameReceivers.class.getName());

//    @PayloadPredicate
//    public boolean isPayloadNotNull(@FetchParam(name = "personName") String payload) {
//        return Objects.nonNull(payload);
//    }

    @DelegateResultTo(method = "firstAcceptor")
    @DelegateResultTo(method = "nextAcceptor")
    public Integer listener(@FetchParam(name = "personName") String name) {
        return 0;
    }

    @ReturnStatement
    public Person firstAcceptor(@FetchResult Integer person,
                                @FetchParam(name = "personName") String name) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, name});
        return new Person();
    }

    public void nextAcceptor(@FetchResult Integer person,
                             @FetchParam(num = 1) Double weight) {
        log.log(Level.INFO, "Message {0}, {1}", new Object[]{person, weight});
    }

    public void fallback(@ErrorSignal Exception e, @FetchParam(name = "personName") String name) {
        log.log(Level.WARNING, "Message {0}, {1}", new Object[]{e.getMessage(), name});
    }

    @DelegateResultTo(method = "anotherDelegator")
//    @ReturnStatement
    public String onNotify(@FetchParam(num = 0) long id, @FetchParam(num = 1) String name, @FetchParam(num = 2) int age) {
        return "Hello World";
    }

    public void fallbackOnNotify(@ErrorSignal Exception e, @FetchParam(name = "name") String name) {
        log.log(Level.WARNING, "Message {0}, {1}", new Object[]{e.getMessage(), name});
    }

    @ReturnStatement
    public String anotherDelegator(@FetchResult String payload) {
        return "Supper " + payload;
    }

    @Transmitter(
            beanName = "TransmitterMultipleReturnStatement",
            receiverName = "ReceiverWithMultipleReturnStatement",
            isSupper = false
    )
    public interface ParametersTransmitterReturnStatement extends CustomListenerWithMultipleReturnStatement {

        @Override
        @NotifyTo(name = "listener")
        @FallBackMethod(name = "fallback")
        Person onSuccess(String personName, Double personEight);

        @Override
        @NotifyTo(name = "onNotify")
        @FallBackMethod(name = "fallbackOnNotify")
        String notifyToIt(long id, String name, int age);

    }

}
