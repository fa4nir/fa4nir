package io.github.fa4nir.core.factories.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.annotations.Receiver;
import io.github.fa4nir.core.annotations.Transmitter;
import io.github.fa4nir.core.exceptions.ValidationExceptionsDeclarations;
import io.github.fa4nir.core.factories.fields.FieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;
import io.github.fa4nir.core.utils.TypeSpecConstructorsUtils;
import io.github.fa4nir.core.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TransmitterFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor;

    private final FieldsFactory transmitterTargetFieldsFactory;

    public TransmitterFactory(InterceptMethodFactory overridingInterceptor, FieldsFactory transmitterTargetFieldsFactory) {
        this.overridingInterceptor = overridingInterceptor;
        this.transmitterTargetFieldsFactory = transmitterTargetFieldsFactory;
    }

    @Override
    public TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv, Set<? extends Element> receivers) {
        TypeMirror typeMirror = element.asType();
        TypeName sourceClassType = ClassName.get(typeMirror);
        Transmitter transmitter = element.getAnnotation(Transmitter.class);
        String beanName = Optional.of(transmitter.beanName())
                .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::beanNameIsBlank);
        String receiverName = Optional.of(transmitter.receiverName())
                .filter(StringUtils::isNoneBlank).orElseThrow(ValidationExceptionsDeclarations::receiverNameIsBlank);
        boolean isSupper = transmitter.isSupper();
        Element target = receivers.stream()
                .filter(e -> e.getAnnotation(Receiver.class).name().equals(receiverName))
                .findFirst().orElseThrow(() -> ValidationExceptionsDeclarations.targetClassNotFound(transmitter));
        TypeName targetTypeName = ClassName.get(target.asType());
        String targetAsFieldName = Introspector.decapitalize(target.getSimpleName().toString());
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(sourceClassType.toString());
        List<? extends TypeMirror> interfaces = ValidationUtils.validInterface(source.getInterfaces());
        Map.Entry<TypeMirror, List<MethodSpec>> overrideMethods = overrideMethods(isSupper, interfaces, source, target);
        MethodSpec.Builder constructor = TypeSpecConstructorsUtils.constructor(targetTypeName, targetAsFieldName);
        return TypeSpec.classBuilder(beanName)
                .addField(this.transmitterTargetFieldsFactory.newField(targetTypeName, targetAsFieldName))
                .addSuperinterface(overrideMethods.getKey())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor.build())
                .addMethods(overrideMethods.getValue())
                .build();
    }

    private Map.Entry<TypeMirror, List<MethodSpec>>
    overrideMethods(boolean isSupper, List<? extends TypeMirror> interfaces, TypeElement source, Element target) {
        TypeMirror supperInterface;
        List<MethodSpec> methods;
        if (isSupper) {
            supperInterface = interfaces.get(0);
            methods = source.getEnclosedElements().stream()
                    .map(method -> ((ExecutableElement) method))
                    .map(method -> this.overridingInterceptor.newMethodSpec(method, target))
                    .collect(Collectors.toList());
        } else {
            supperInterface = source.asType();
            methods = source.getEnclosedElements().stream()
                    .map(method -> ((ExecutableElement) method))
                    .filter(method -> !method.isDefault())
                    .map(method -> this.overridingInterceptor.newMethodSpec(method, target))
                    .collect(Collectors.toList());
        }
        return Map.entry(supperInterface, methods);
    }

}
