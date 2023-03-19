package io.github.fa4nir.processor.wrappers;

import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.annotations.SpringBean;
import io.github.fa4nir.core.definitions.TransmitterDefinition;
import io.github.fa4nir.processor.utils.SpringBeanAnnotationsUtils;

import javax.lang.model.element.Element;
import java.util.Objects;

public class SpringStereotype implements TransmitterSource {

    private final TransmitterSource transmitterSource;

    public SpringStereotype(TransmitterSource transmitterSource) {
        this.transmitterSource = transmitterSource;
    }

    @Override
    public void writerJavaFile(TransmitterDefinition definition, Element element, TypeSpec.Builder typeSpec) {
        if (Objects.nonNull(typeSpec)) {
            if (Objects.nonNull(element.getAnnotation(SpringBean.class))) {
                typeSpec.addAnnotation(SpringBeanAnnotationsUtils.component());
            }
        }
        this.transmitterSource.writerJavaFile(definition, element, typeSpec);
    }
}
