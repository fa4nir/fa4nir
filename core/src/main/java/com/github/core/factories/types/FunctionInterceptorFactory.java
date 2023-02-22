package com.github.core.factories.types;

import com.github.core.annotations.FallBackMethod;
import com.github.core.annotations.FunctionInterceptor;
import com.github.core.annotations.InterceptMapper;
import com.github.core.factories.methods.InterceptMethodFactory;
import com.github.core.factories.methods.OverridingMethodsFactory;
import com.github.core.utils.OverridingMethodMetaInfo;
import com.github.core.utils.TypeSpecConstructorsUtils;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionInterceptorFactory implements AnnotationTransferFactory {

    private final InterceptMethodFactory overridingInterceptor = new OverridingMethodsFactory();

    @Override
    public TypeSpec newTypeSpec(Element element, ProcessingEnvironment processingEnv) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        TypeMirror typeMirror = element.asType();
        String currentClassForField = element.getSimpleName().toString();
        TypeName currentClassType = ClassName.get(typeMirror);
        FunctionInterceptor baseAnnotation = element.getAnnotation(FunctionInterceptor.class);
        TypeMirror listenerTypeMirror = findTypeInAnnotation("listenerType", annotationMirrors);
        String beanName = baseAnnotation.beanName();
        InterceptMapper[] methods = baseAnnotation.methods();
        ClassName listenerType = ClassName.bestGuess(listenerTypeMirror.toString());
        String name = StringUtils.isNoneBlank(beanName) ? beanName :
                String.format("%sImpl", listenerType.simpleName());
        TypeSpec.Builder builder = TypeSpec.classBuilder(name);
        String sourceClassFieldName = Introspector.decapitalize(currentClassForField);
        TypeElement source = processingEnv.getElementUtils().
                getTypeElement(currentClassType.toString());
        Map<String, ? extends Element> fallBackMethods = getFallBackMethods(source);
        TypeElement listenerTypeElement = processingEnv.getElementUtils()
                .getTypeElement(listenerType.toString());
        List<? extends TypeParameterElement> listerGenericType = listenerTypeElement.getTypeParameters();
        if (listerGenericType.size() != 0) {
            TypeMirror type = listerGenericType.get(0).asType();
            builder.addTypeVariable(TypeVariableName.get(type.toString()));
        }
        List<? extends Element> listenerTypeElementMethods = listenerTypeElement.getEnclosedElements();
        List<MethodSpec> listenerMethodsInfo = listenerTypeElementMethods.stream()
                .map(method -> ((ExecutableElement) method))
                .map(method -> this.overridingInterceptor.newMethodSpec(
                        OverridingMethodMetaInfo.builder()
                                .method(method)
                                .methods(methods)
                                .currentTypeElementMethods(source.getEnclosedElements())
                                .fallBackMethods(fallBackMethods)
                                .sourceClassFieldName(sourceClassFieldName)
                                .build()
                )).collect(Collectors.toList());
        FieldSpec currentClassField = FieldSpec.builder(currentClassType, sourceClassFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addModifiers()
                .build();
        builder.addField(currentClassField);
        MethodSpec.Builder constructor = TypeSpecConstructorsUtils.constructor(currentClassType, sourceClassFieldName);
        return builder
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(listenerTypeElement.asType())
                .addMethod(constructor.build())
                .addMethods(listenerMethodsInfo)
                .build();
    }

    private Map<String, ? extends Element> getFallBackMethods(TypeElement currentTypeElement) {
        return currentTypeElement.getEnclosedElements().stream()
                .filter(method -> Objects.nonNull(method.getAnnotation(FallBackMethod.class)))
                .collect(Collectors.toMap(this::fetchInsideOfMethod, Function.identity()));
    }

    private String fetchInsideOfMethod(Element key) {
        String insideOfMethod = key.getAnnotation(FallBackMethod.class).insideOfMethod();
        if (StringUtils.isNoneBlank(insideOfMethod)) {
            return insideOfMethod;
        }
        throw new RuntimeException("FallBackMethod annotation should contains listener method name.");
    }

    private TypeMirror findTypeInAnnotation(String name, List<? extends AnnotationMirror> annotationMirrors) {
        FunctionInterceptorVisitor visitor = new FunctionInterceptorVisitor(name);
        annotationMirrors.forEach(annotationMirror ->
                annotationMirror.getElementValues().forEach((key, value) -> value.accept(visitor, key)));
        return visitor.getResult();
    }


    public static class FunctionInterceptorVisitor extends SimpleAnnotationValueVisitor7<Void, ExecutableElement> {

        private final String methodName;

        private TypeMirror result;

        public FunctionInterceptorVisitor(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public Void visitType(TypeMirror t, ExecutableElement executableElement) {
            if (executableElement.getSimpleName().toString().equals(methodName)) {
                this.result = t;
            }
            return super.visitType(t, executableElement);
        }

        public TypeMirror getResult() {
            return result;
        }
    }

}
