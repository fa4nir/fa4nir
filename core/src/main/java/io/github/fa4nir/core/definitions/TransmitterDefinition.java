package io.github.fa4nir.core.definitions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.github.fa4nir.core.annotations.Receiver;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.core.exceptions.ValidationExceptionsDeclarations;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import java.beans.Introspector;
import java.util.Optional;
import java.util.Set;

public interface TransmitterDefinition {

    static TransmitterDefinition.Builder newDefinition(Element element, Set<? extends Element> receivers) {
        return TransmitterDefinition.Builder.newBuilder(element, receivers);
    }

    String getBeanName();

    TypeName getTargetTypeName();

    String getTargetAsFieldName();

    Element getTarget();

    MethodSpec getConstructor();

    boolean isSupper();

    class Builder {

        private final Element element;
        private final Set<? extends Element> receivers;
        private Transmitter transmitter;
        private Element target;
        private String beanName;
        private TypeName targetTypeName;
        private String targetAsFieldName;

        private Builder(Element element, Set<? extends Element> receivers) {
            this.element = element;
            this.receivers = receivers;
        }

        public static Builder newBuilder(Element element, Set<? extends Element> receivers) {
            return new Builder(element, receivers);
        }

        public Builder transmitter() {
            this.transmitter = this.element.getAnnotation(Transmitter.class);
            return this;
        }

        public Builder target() {
            String receiverName = Optional.of(this.transmitter.receiverName())
                    .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::receiverNameIsBlank);
            this.target = this.receivers.stream()
                    .filter(e -> e.getAnnotation(Receiver.class).name().equals(receiverName))
                    .findFirst().orElseThrow(() -> ValidationExceptionsDeclarations.targetClassNotFound(this.transmitter));
            return this;
        }

        public Builder beanName() {
            this.beanName = Optional.of(this.transmitter.beanName())
                    .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::beanNameIsBlank);
            return this;
        }

        public Builder targetTypeName() {
            this.targetTypeName = ClassName.get(this.target.asType());
            return this;
        }

        public Builder targetAsFieldName() {
            this.targetAsFieldName = Introspector.decapitalize(this.target.getSimpleName().toString());
            return this;
        }

        public TransmitterDefinition build() {
            return new DefaultTransmitterDefinition(this.transmitter, this.target, this.beanName,
                    this.targetTypeName, this.targetAsFieldName);
        }

    }

}
