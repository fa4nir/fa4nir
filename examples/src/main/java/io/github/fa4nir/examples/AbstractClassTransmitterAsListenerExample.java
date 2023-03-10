package io.github.fa4nir.examples;

import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.annotations.Transmitter;

import java.util.List;

@Transmitter(beanName = "AbstractClassTransmitterAsListenerFirstExample", receiverName = "custom-listener-case-3")
public abstract class AbstractClassTransmitterAsListenerExample extends AbstractClassAsListener {

    @NotifyTo(name = "customListener")
    @FallBackMethod(name = "fallBackForCustomListener")
    public abstract void onSuccess(String parameter0, Integer parameter1, List<String> parameters2);

}
