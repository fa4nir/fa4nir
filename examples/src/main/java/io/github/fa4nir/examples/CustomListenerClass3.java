package io.github.fa4nir.examples;

import io.github.fa4nir.core.annotations.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Receiver(name = "custom-listener-case-3")
public class CustomListenerClass3 {

    private static final Logger log = Logger.getLogger(CustomListenerClass3.class.getName());

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

}
