package io.github.fa4nir.core.definitions;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.core.utils.TypeSpecConstructorsUtils;

import javax.lang.model.element.Element;

public class DefaultTransmitterDefinition implements TransmitterDefinition {

    private final Transmitter transmitter;

    private final Element target;

    private final String beanName;

    private final TypeName targetTypeName;

    private final String targetAsFieldName;

    public DefaultTransmitterDefinition(Transmitter transmitter, Element target, String beanName,
                                        TypeName targetTypeName, String targetAsFieldName) {
        this.transmitter = transmitter;
        this.target = target;
        this.beanName = beanName;
        this.targetTypeName = targetTypeName;
        this.targetAsFieldName = targetAsFieldName;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public TypeName getTargetTypeName() {
        return this.targetTypeName;
    }

    @Override
    public String getTargetAsFieldName() {
        return this.targetAsFieldName;
    }

    @Override
    public Element getTarget() {
        return this.target;
    }

    @Override
    public MethodSpec getConstructor() {
        return TypeSpecConstructorsUtils.constructor(this.targetTypeName, this.targetAsFieldName).build();
    }

    @Override
    public boolean isSupper() {
        return this.transmitter.isSupper();
    }

}
