package io.github.fa4nir.core.factories.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.definitions.TransmitterDefinition;
import io.github.fa4nir.core.factories.fields.FieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransmitterAbstractClassFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor;

    private final FieldsFactory transmitterTargetFieldsFactory;

    public TransmitterAbstractClassFactory(InterceptMethodFactory overridingInterceptor, FieldsFactory transmitterTargetFieldsFactory) {
        this.overridingInterceptor = overridingInterceptor;
        this.transmitterTargetFieldsFactory = transmitterTargetFieldsFactory;
    }

    @Override
    public TypeSpec.Builder newTypeSpec(Element element, ProcessingEnvironment processingEnv, TransmitterDefinition definition) {
        TypeMirror typeMirror = element.asType();
        TypeName sourceClassType = ClassName.get(typeMirror);
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(sourceClassType.toString());
        TypeMirror sourceSupperClass = source.getSuperclass();
        TypeElement sourceSupperElement = processingEnv.getElementUtils().
                getTypeElement(sourceSupperClass.toString());
        Map.Entry<TypeMirror, List<MethodSpec>> overrideMethods =
                overrideMethods(definition.isSupper(), sourceSupperElement, source, definition.getTarget());
        return TypeSpec.classBuilder(definition.getBeanName())
                .addField(this.transmitterTargetFieldsFactory.newField(
                        definition.getTargetTypeName(), definition.getTargetAsFieldName())
                ).superclass(overrideMethods.getKey())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(definition.getConstructor())
                .addMethods(overrideMethods.getValue());
    }

    private Map.Entry<TypeMirror, List<MethodSpec>>
    overrideMethods(boolean isSupper, TypeElement sourceSupperElement, TypeElement source, Element target) {
        TypeMirror supperInterface;
        List<MethodSpec> methods;
        if (isSupper) {
            supperInterface = sourceSupperElement.asType();
            Map<String, ExecutableElement> overrideFromBase = sourceSupperElement.getEnclosedElements().stream()
                    .map(method -> ((ExecutableElement) method))
                    .filter(method -> method.getModifiers().contains(Modifier.ABSTRACT))
                    .collect(Collectors.toMap(Object::toString, Function.identity()));
            methods = source.getEnclosedElements().stream()
                    .map(method -> ((ExecutableElement) method))
                    .filter(method -> method.getModifiers().contains(Modifier.ABSTRACT))
                    .peek(method -> overrideFromBase.remove(method.toString()))
                    .map(method -> this.overridingInterceptor.newMethodSpec(method, target))
                    .collect(Collectors.toList());
            List<MethodSpec> overrideFromBaseMethods = overrideFromBase.values().stream()
                    .map(method -> this.overridingInterceptor.newMethodSpec(method, target))
                    .collect(Collectors.toList());
            methods.addAll(overrideFromBaseMethods);
        } else {
            supperInterface = source.asType();
            methods = source.getEnclosedElements().stream()
                    .map(method -> ((ExecutableElement) method))
                    .filter(method -> method.getModifiers().contains(Modifier.ABSTRACT))
                    .map(method -> this.overridingInterceptor.newMethodSpec(method, target))
                    .collect(Collectors.toList());
        }
        return Map.entry(supperInterface, methods);
    }

}
