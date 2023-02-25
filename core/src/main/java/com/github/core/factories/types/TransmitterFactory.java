package com.github.core.factories.types;

import com.github.core.annotations.Receiver;
import com.github.core.annotations.Transmitter;
import com.github.core.factories.methods.InterceptMethodFactory;
import com.github.core.utils.TypeSpecConstructorsUtils;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TransmitterFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor;

    public TransmitterFactory(InterceptMethodFactory overridingInterceptor) {
        this.overridingInterceptor = overridingInterceptor;
    }

    @Override
    public TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv, Set<? extends Element> receivers) {
        TypeMirror typeMirror = element.asType();
        TypeName sourceClassType = ClassName.get(typeMirror);
        Transmitter transmitter = element.getAnnotation(Transmitter.class);
        String beanName = transmitter.beanName();
        String receiverName = transmitter.receiverName();
        boolean isSupper = transmitter.isSupper();
        Element target = receivers.stream()
                .filter(e -> e.getAnnotation(Receiver.class).name().equals(receiverName))
                .findFirst().orElse(null);
        TypeName targetTypeName = ClassName.get(target.asType());
        String targetAsFieldName = Introspector.decapitalize(target.getSimpleName().toString());
        TypeSpec.Builder builder = TypeSpec.classBuilder(String.format("%sImpl", beanName));
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(sourceClassType.toString());
        List<? extends TypeMirror> interfaces = source.getInterfaces();
        if (Objects.isNull(interfaces) || interfaces.size() != 1) {
            throw new IllegalArgumentException("Cannot find main interface");
        }
        Map.Entry<TypeMirror, List<MethodSpec>> overrideMethods = overrideMethods(isSupper, interfaces, source, target);
        FieldSpec currentClassField = FieldSpec.builder(targetTypeName, targetAsFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addModifiers()
                .build();
        builder.addField(currentClassField);
        MethodSpec.Builder constructor = TypeSpecConstructorsUtils.constructor(targetTypeName, targetAsFieldName);
        return builder
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
