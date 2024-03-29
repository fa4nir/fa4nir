package io.github.fa4nir.examples.receivers;

import io.github.fa4nir.core.annotations.*;
import io.github.fa4nir.examples.listeners.CustomListenerWithExtendsAnotherListener;
import io.github.fa4nir.examples.payloads.Person;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Receiver(name = "custom-listener-case-2")
public class CustomReceiverForCustomListenerCase2 {

    private static final Logger log = Logger.getLogger(CustomReceiverForCustomListenerCase2.class.getName());

    @PayloadPredicate(marker = "first-method-inside-listener")
    public boolean isPayloadNotNull(@FetchParam(name = "parameter0") String payload) {
        return Objects.nonNull(payload);
    }

    @DelegateResultTo(method = "delegatorAcceptor")
    public Person customListener(@FetchParam(num = 0) String name, @FetchParam(num = 2) List<String> payload) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return new Person(1L, "Mit9i", "mit9i@gmail.com");
    }

    public void delegatorAcceptor(@FetchResult Person person,
                                  @FetchParam(num = 0) String name,
                                  @FetchParam(num = 1) Integer payload) {
        log.log(Level.INFO, "Message {0}, {1}, {2}", new Object[]{person, name, payload});
    }

    public void fallBackForCustomListener(@ErrorSignal Exception e, @FetchParam(num = 1) Integer number) {
        log.log(Level.WARNING, "Message: {0}, {1}", new Object[]{e.getMessage(), number});
    }

    @PayloadPredicate(marker = "second-method-inside-listener")
    public boolean isPayloadNull(@FetchParam(name = "name") String payload) {
        return Objects.isNull(payload);
    }

    public void customListener1(@FetchParam(name = "name") String name) {
        log.log(Level.INFO, "Message {0}, {1}, {2}", new Object[]{name});
    }

    public void fallBackForCustomListener1(@ErrorSignal Exception e, @FetchParam(name = "name") String name) {
        log.log(Level.WARNING, "Message: {0}, {1}", new Object[]{e.getMessage(), name});
    }

    @Transmitter(beanName = "CustomListenerCase2", receiverName = "custom-listener-case-2")
    public interface TransmitterCase2Template extends CustomListenerWithExtendsAnotherListener {
        @Override
        @NotifyTo(name = "customListener")
        @FallBackMethod(name = "fallBackForCustomListener")
        @UniqueMarker(marker = "first-method-inside-listener")
        void onSuccess(String parameter0,
                       Integer parameter1,
                       List<String> parameters2);

        @Override
        default void onFailure(Throwable t) {
        }

        @Override
        @NotifyTo(name = "customListener1")
        @FallBackMethod(name = "fallBackForCustomListener1")
        @UniqueMarker(marker = "second-method-inside-listener")
        default void onSuccess(String name) {
        }

        @Override
        default void onSuccess1(String parameter0, Double parameters2) {
        }
    }

}
