package io.github.fa4nir.core.definitions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.github.fa4nir.core.annotations.Receiver;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.core.exceptions.ValidationExceptionsDeclarations;
import io.github.fa4nir.core.utils.TypeSpecConstructorsUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import java.beans.Introspector;
import java.util.Optional;
import java.util.Set;

public class DefaultTransmitterDefinition implements TransmitterDefinition {

    private final Transmitter transmitter;

    private final Element target;

    private final String beanName;

    private final TypeName targetTypeName;

    private final String targetAsFieldName;

    public DefaultTransmitterDefinition(Element element, Set<? extends Element> receivers) {
        this.transmitter = element.getAnnotation(Transmitter.class);
        String receiverName = Optional.of(this.transmitter.receiverName())
                .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::receiverNameIsBlank);
        this.target = receivers.stream()
                .filter(e -> e.getAnnotation(Receiver.class).name().equals(receiverName))
                .findFirst().orElseThrow(() -> ValidationExceptionsDeclarations.targetClassNotFound(this.transmitter));
        this.beanName = Optional.of(this.transmitter.beanName())
                .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::beanNameIsBlank);
        this.targetTypeName = ClassName.get(this.target.asType());
        this.targetAsFieldName = Introspector.decapitalize(this.target.getSimpleName().toString());
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
