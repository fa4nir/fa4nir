package io.github.fa4nir.examples.transmitters;

import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.examples.CustomListenerType;

import java.util.List;

@Transmitter(
        beanName = "TransmitterWithNameInParameters",
        receiverName = "TransmitterWithNameInParameters",
        isSupper = false
)
public interface ParametersWithNameTransmitter extends CustomListenerType {

    @Override
    @NotifyTo(name = "listenerWithParametersName")
    @FallBackMethod(name = "fallbackListenerWithParametersName")
    void onSuccess1(String personName, Double personEight);

    @Override
    default void onSuccess(String parameter0, Integer parameter1, List<String> parameters2) {
    }
}
