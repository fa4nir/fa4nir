# Fa4nir lib

## Fa4nir description

Project oriented on minimize efforts when we work with legacy listeners (not only). The main idea is define interface
which annotated as Transmitter and extends legacy interface and you can define fallback and method which you want to
notify in Receiver class. Receiver class is your default POJO class with contains method which we want to notify and
fallback method  
and methods for delegate result. And after this declaration on compile steps fa4nir will generate code which you can use
to listening your notifications.

## Build

```
    gradle clean build
```

## Tests

```
    gradle :fa4nir-test:test
```

## Dependencies

```
    implementation 'io.github.fa4nir:fa4nir:1.0.2'
    annotationProcessor 'io.github.fa4nir:fa4nir:1.0.2'
```

# Examples

To define Transmitter, write interface and define `beanName` to named generated class and define `receiverName` to find
necessary receiver. Also you should override methods which need to use in receiver class and not necessary define as
default. Method which we want to use should have annotations ``` @NotifyTo and @FallBackMethod ```
if necessary.

```java

@Transmitter(beanName = "CustomListenerImpl", receiverName = "custom-listener-receiver")
public interface TransmitterTemplate extends CustomListener {
    @Override
    @NotifyTo(name = "receiverMethod")
    @FallBackMethod(name = "fallBackForReceiverMethod")
    void onSuccess(String parameter0,
                   Integer parameter1,
                   List<String> parameters2);

    @Override
    default void onFailure(Throwable t) {
    }

    @Override
    default void onSuccess(String r) {
    }

    @Override
    default void onSuccess1(String parameter0, Double parameters2) {
    }
}
```

The next step is provide receiver. On this stem You should annotate your class as `@Receiver` and define unique name for
it, define base method with name which we write in previous step `@NotifyTo(name = "receiverMethod")` and define
parameters use annotation `@FetchParam(num = 0)` where num is index in base method parameters also you can use name to
find parameters by name. and if you use annotation `@FallBackMethod(name = "fallBackForReceiverMethod")` create method
fallback. You can delegate return statement to another methods to do
this `@DelegateResultTo(method = "delegatorAcceptor")` where put method name which should received result and can
receive parameters of base method too. Result should be mapped as `@FetchResult`.

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {

    private static final Logger log = Logger.getLogger(CustomListenerClass2.class.getName());

    @DelegateResultTo(method = "delegatorAcceptor")
    public Person receiverMethod(@FetchParam(num = 0) String name, @FetchParam(name = "parameters2") List<String> payload) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return new Person(1L, "Mit9i", "mit9i@gmail.com");
    }

    public void delegatorAcceptor(@FetchResult Person person,
                                  @FetchParam(num = 0) String name,
                                  @FetchParam(num = 1) Integer payload) {
        log.log(Level.INFO, "Message {0}, {1}, {2}", new Object[]{person, name, payload});
    }

    public void fallBackForReceiverMethod(@ErrorSignal Exception e, @FetchParam(num = 1) Integer number) {
        log.log(Level.WARNING, "Message: {0}, {1}", new Object[]{e.getMessage(), number});
    }

}

```

Generated code:

```java
package io.github.fa4nir.examples.impl;

import io.github.fa4nir.examples.receivers.CustomReceiverForCustomListenerCase2;
import io.github.fa4nir.examples.payloads.Person;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.List;

public class CustomListenerImpl implements CustomListener {
    private final ReceiverTemplate receiverTemplate;

    public CustomListenerCase2(ReceiverTemplate receiverTemplate) {
        this.receiverTemplate = receiverTemplate;
    }

    @Override
    public void onSuccess(String parameter0, Integer parameter1, List<String> parameters2) {
        try {
            Person result = this.receiverTemplate.receiverMethod(parameter0, parameters2);
            this.receiverTemplate.delegatorAcceptor(result, parameter0, parameter1);
        } catch (Exception e) {
            this.receiverTemplate.fallBackForReceiverMethod(e, parameter1);
        }
    }

    // Start block_1
    @Override
    void onFailure(Throwable t) {
    }

    @Override
    void onSuccess(String r) {
    }

    @Override
    void onSuccess1(String parameter0, Double parameters2) {
    }
    // End block_1

}
```

But if you can see `block_1` is useless code, so remove it. To do this
`@Transmitter(beanName = "CustomListenerImpl", receiverName = "custom-listener-receiver", isSupper=false)`
use this `isSupper` and set false to it.

Generated code:

`Tip: because default methods`

```java
package io.github.fa4nir.examples.impl;

import io.github.fa4nir.examples.receivers.CustomReceiverForCustomListenerCase2;
import io.github.fa4nir.examples.payloads.Person;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.List;

public class CustomListenerImpl implements TransmitterTemplate {
    private final ReceiverTemplate receiverTemplate;

    public CustomListenerCase2(ReceiverTemplate receiverTemplate) {
        this.receiverTemplate = receiverTemplate;
    }

    @Override
    public void onSuccess(String parameter0, Integer parameter1, List<String> parameters2) {
        try {
            Person result = this.receiverTemplate.receiverMethod(parameter0, parameters2);
            this.receiverTemplate.delegatorAcceptor(result, parameter0, parameter1);
        } catch (Exception e) {
            this.receiverTemplate.fallBackForReceiverMethod(e, parameter1);
        }
    }
}
```

The next is method fallback:

If you do not need parameters, define like this:

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {
    public void fallBackForReceiverMethod(Exception e) {
    }
    //...
}
```

If you need parameters, define like this: where annotation `@ErrorSignal` is required.

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {
    public void fallBackForReceiverMethod(@ErrorSignal Exception e, @FetchParam(name = "parameters1") Integer number) {
    }
    //...
}
```

`@DelegateResultTo` can be used for any delegated methods:

Example:

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {

    private static final Logger log = Logger.getLogger(CustomListenerClass2.class.getName());

    @DelegateResultTo(method = "delegatorAcceptor0")
    @DelegateResultTo(method = "delegatorAcceptor1")
    @DelegateResultTo(method = "delegatorAcceptor2")
    //... any
    public Person receiverMethod(@FetchParam(num = 0) String name, @FetchParam(name = "parameters2") List<String> payload) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return new Person(1L, "Mit9i", "mit9i@gmail.com");
    }

    //...

}
```

Predicate (mark method as `@PayloadPredicate`, and required is method should return boolean)

```java

@Receiver(name = "ReceiverBase")
public class ReceiverWithNameReceivers {

    private static final Logger log = Logger.getLogger(ParametersWithNameReceivers.class.getName());

    @PayloadPredicate
    public boolean isPayloadNotNull(@FetchParam(name = "personName") String payload) {
        return Objects.nonNull(payload);
    }
    //...
}
```

Example:

```java
    public class TransmitterWithNameInParameters implements ParametersWithNameTransmitter {
    @Override
    public void onSuccess1(String personName, Double personEight) {
        // should be like this 
        if (this.parametersWithNameReceivers.isPayloadNotNull(personName)) {
            try {
                Person result = this.parametersWithNameReceivers.listenerWithParametersName(personName);
                this.parametersWithNameReceivers.receivePersonWithName(result, personName);
                this.parametersWithNameReceivers.receivePersonWithNameAndWeight(result, personEight);
            } catch (Exception e) {
                this.parametersWithNameReceivers.fallbackListenerWithParametersName(e, personName);
            }
        }
    }
}
```

`@ReturnStatement` this annotation mark method which should return result to listener:

Example_1:

```java

@Transmitter(beanName = "CustomListenerImpl", receiverName = "custom-listener-receiver")
public interface TransmitterTemplate extends CustomListener {
    @Override
    @NotifyTo(name = "receiverMethod")
    @FallBackMethod(name = "fallBackForReceiverMethod")
    Person onSuccess(String parameter0,
                     Integer parameter1,
                     List<String> parameters2);

    @Override
    default void onFailure(Throwable t) {
    }

    @Override
    default void onSuccess(String r) {
    }

    @Override
    default void onSuccess1(String parameter0, Double parameters2) {
    }
}
```

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {

    private static final Logger log = Logger.getLogger(CustomListenerClass2.class.getName());

    @ReturnStatement
    @DelegateResultTo(method = "delegatorAcceptor0")
    @DelegateResultTo(method = "delegatorAcceptor1")
    @DelegateResultTo(method = "delegatorAcceptor2")
    //... any
    public Person receiverMethod(@FetchParam(num = 0) String name, @FetchParam(name = "parameters2") List<String> payload) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return new Person(1L, "Mit9i", "mit9i@gmail.com");
    }

    //...

}
```

Generated code:

```java

public class TransmitterWithNameInParameters implements ParametersWithNameTransmitter {
    @Override
    public Person onSuccess1(String personName, List<String> payload) {
        Person result;
        if (this.parametersWithNameReceivers.isPayloadNotNull(personName)) {
            try {
                result = this.parametersWithNameReceivers.receiverMethod(personName);
                this.parametersWithNameReceivers.receivePersonWithName(result, personName);
                this.parametersWithNameReceivers.receivePersonWithNameAndWeight(result, payload);
            } catch (Exception e) {
                this.parametersWithNameReceivers.fallbackListenerWithParametersName(e, personName);
            }
        }
        return result;
    }
}

```

Example_2:

```java

@Transmitter(beanName = "CustomListenerImpl", receiverName = "custom-listener-receiver")
public interface TransmitterTemplate extends CustomListener {
    @Override
    @NotifyTo(name = "receiverMethod")
    @FallBackMethod(name = "fallBackForReceiverMethod")
    Person onSuccess(String parameter0,
                     Integer parameter1,
                     List<String> parameters2);

    @Override
    default void onFailure(Throwable t) {
    }

    @Override
    default void onSuccess(String r) {
    }

    @Override
    default void onSuccess1(String parameter0, Double parameters2) {
    }
}
```

```java

@Receiver(name = "custom-listener-receiver")
public class ReceiverTemplate {

    private static final Logger log = Logger.getLogger(CustomListenerClass2.class.getName());

    @DelegateResultTo(method = "delegateMethod")
    public int receiverMethod(@FetchParam(num = 0) String name, @FetchParam(name = "parameters2") List<String> payload) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return 10;
    }

    public Person delegateMethod(@FetchResult int index) {
        log.log(Level.INFO, "Enter: {0}, {1}", new Object[]{name, payload});
        return new Person(index, "Mit9i", "mit9i@gmail.com");
    }

    //...

}
```

Generated code:

```java

public class TransmitterWithNameInParameters implements ParametersWithNameTransmitter {
    @Override
    public Person onSuccess1(String personName, List<String> payload) {
        Person result;
        if (this.parametersWithNameReceivers.isPayloadNotNull(personName)) {
            try {
                int secondResult = this.receiverTemplate.receiverMethod(personName);
                result = this.receiverTemplate.delegateMethod(secondResult);
            } catch (Exception e) {
                this.parametersWithNameReceivers.fallbackListenerWithParametersName(e, personName);
            }
        }
        return result;
    }
}

```

