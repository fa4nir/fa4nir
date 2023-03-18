package io.github.fa4nir.examples.transmitters;

import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.examples.listeners.CustomListenerWithPersonAsReturnStatement;

@Transmitter(
        beanName = "TransmitterReturnStatement",
        receiverName = "TransmitterWithReturnStatement",
        isSupper = false
)
public interface ParametersTransmitterReturnStatement extends CustomListenerWithPersonAsReturnStatement {

    @Override
    @NotifyTo(name = "listener")
    @FallBackMethod(name = "fallback")
    Integer onSuccess(String personName, Double personEight);

}
