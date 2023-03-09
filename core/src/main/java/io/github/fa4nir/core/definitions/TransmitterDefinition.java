package io.github.fa4nir.core.definitions;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import java.util.Set;

public interface TransmitterDefinition {

    static TransmitterDefinition newDefinition(Element element, Set<? extends Element> receivers) {
        return new DefaultTransmitterDefinition(element, receivers);
    }

    String getBeanName();

    TypeName getTargetTypeName();

    String getTargetAsFieldName();

    Element getTarget();

    MethodSpec getConstructor();

    boolean isSupper();

}
