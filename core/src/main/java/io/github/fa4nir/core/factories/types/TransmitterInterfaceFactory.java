package io.github.fa4nir.core.factories.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
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
import java.util.stream.Collectors;

public class TransmitterInterfaceFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor;

    private final FieldsFactory transmitterTargetFieldsFactory;

    public TransmitterInterfaceFactory(InterceptMethodFactory overridingInterceptor, FieldsFactory transmitterTargetFieldsFactory) {
        this.overridingInterceptor = overridingInterceptor;
        this.transmitterTargetFieldsFactory = transmitterTargetFieldsFactory;
    }

    @Override
    public TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv, TransmitterDefinition definition) {
        TypeMirror typeMirror = element.asType();
        TypeName sourceClassType = ClassName.get(typeMirror);
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(sourceClassType.toString());
        List<? extends TypeMirror> interfaces = ValidationUtils.validInterface(source.getInterfaces());
        Map.Entry<TypeMirror, List<MethodSpec>> overrideMethods = overrideMethods(definition.isSupper(), interfaces, source, definition.getTarget());
        return TypeSpec.classBuilder(definition.getBeanName())
                .addField(this.transmitterTargetFieldsFactory.newField(definition.getTargetTypeName(), definition.getTargetAsFieldName()))
                .addSuperinterface(overrideMethods.getKey())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(definition.getConstructor())
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
