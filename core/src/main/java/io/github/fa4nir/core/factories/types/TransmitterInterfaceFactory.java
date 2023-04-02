package io.github.fa4nir.core.factories.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.github.fa4nir.core.annotations.NotifyTo;
import io.github.fa4nir.core.annotations.PayloadPredicate;
import io.github.fa4nir.core.annotations.ReturnStatement;
import io.github.fa4nir.core.annotations.UniqueMarker;
import io.github.fa4nir.core.definitions.TransmitterDefinition;
import io.github.fa4nir.core.factories.fields.FieldsFactory;
import io.github.fa4nir.core.factories.methods.InterceptMethodFactory;
import io.github.fa4nir.core.utils.ValidationUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransmitterInterfaceFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor;

    private final FieldsFactory transmitterTargetFieldsFactory;

    public TransmitterInterfaceFactory(InterceptMethodFactory overridingInterceptor, FieldsFactory transmitterTargetFieldsFactory) {
        this.overridingInterceptor = overridingInterceptor;
        this.transmitterTargetFieldsFactory = transmitterTargetFieldsFactory;
    }

    @Override
    public TypeSpec.Builder newTypeSpec(Element element, ProcessingEnvironment processingEnv, TransmitterDefinition definition) {
        TypeMirror typeMirror = element.asType();
        TypeName sourceClassType = ClassName.get(typeMirror);
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(sourceClassType.toString());
        validation(source, definition.getTarget());
        List<? extends TypeMirror> interfaces = ValidationUtils.validInterface(source.getInterfaces());
        Map.Entry<TypeMirror, List<MethodSpec>> overrideMethods = overrideMethods(
                definition.isSupper(), interfaces, source, definition.getTarget());
        return TypeSpec.classBuilder(definition.getBeanName())
                .addField(this.transmitterTargetFieldsFactory.newField(definition.getTargetTypeName(), definition.getTargetAsFieldName()))
                .addSuperinterface(overrideMethods.getKey())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(definition.getConstructor())
                .addMethods(overrideMethods.getValue());
    }

    private void validation(TypeElement source, Element target) {
        List<? extends Element> sourceMethods = source.getEnclosedElements();
        List<? extends Element> targetMethods = target.getEnclosedElements();

        List<NotifyTo> notifyToAnnotations = sourceMethods.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(method -> ((ExecutableElement) method))
                .map(method -> method.getAnnotation(NotifyTo.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<UniqueMarker> uniqueMarkersAnnotations = sourceMethods.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(method -> ((ExecutableElement) method))
                .map(method -> method.getAnnotation(UniqueMarker.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<PayloadPredicate> payloadPredicateAnnotations = targetMethods.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(method -> ((ExecutableElement) method))
                .map(method -> method.getAnnotation(PayloadPredicate.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ReturnStatement> returnStatementAnnotations = targetMethods.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(method -> ((ExecutableElement) method))
                .map(method -> method.getAnnotation(ReturnStatement.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (notifyToAnnotations.size() > 1) {
            if (uniqueMarkersAnnotations.size() != payloadPredicateAnnotations.size()) {
                throw new IllegalArgumentException(String.format("Transmitter: %s, Receiver: %s, Miss mapping. 2 annotation NotifyTo and 2 annotation PayloadPredicate," +
                        " but UniqueMarker annotation less then PayloadPredicate", source.getSimpleName().toString(), target.getSimpleName().toString()));
            } else {
                Map<String, UniqueMarker> groupByUniqueMarker = uniqueMarkersAnnotations.stream().collect(
                        Collectors.toMap(UniqueMarker::marker, Function.identity())
                );
                Map<String, PayloadPredicate> groupByPayloadPredicate = payloadPredicateAnnotations.stream().collect(
                        Collectors.toMap(PayloadPredicate::marker, Function.identity())
                );
                groupByUniqueMarker.forEach((name, uniqueMarker) -> {
                    PayloadPredicate payloadPredicate = groupByPayloadPredicate.get(name);
                    if (Objects.isNull(payloadPredicate)) {
                        throw new IllegalArgumentException("Miss match naming between UniqueMarker and PayloadPredicate");
                    }
                });
            }
        }
        if (returnStatementAnnotations.size() > notifyToAnnotations.size()) {
            throw new IllegalArgumentException(String.format("Target: %s, Something wrong with ReturnStatement!", target.getSimpleName().toString()));
        }
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
