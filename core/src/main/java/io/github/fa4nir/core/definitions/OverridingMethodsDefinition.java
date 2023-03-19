package io.github.fa4nir.core.definitions;

import io.github.fa4nir.core.annotations.DelegateResultTo;
import io.github.fa4nir.core.annotations.FallBackMethod;
import io.github.fa4nir.core.annotations.NotifyTo;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;

public interface OverridingMethodsDefinition {

    ExecutableElement getSourceMethod();

    Element getTarget();

    NotifyTo getAnnotationNotifyTo();

    FallBackMethod getAnnotationFallBackMethod();

    List<? extends Element> getTargetMethods();

    List<? extends VariableElement> getSourceParameters();

    Map<String, ? extends VariableElement> getGroupOfSourceParameters();

    String getTargetFieldName();

    ExecutableElement getTargetMethod();

    String getResultName();

    DelegateResultTo[] getDelegateResultToAnnotations();

    ExecutableElement getFallBackMethod();

    TypeMirror getTargetMethodReturnType();

    List<? extends VariableElement> getTargetParameters();

    List<ExecutableElement> getPredicateMethods();

    String getNotifyToTarget();

    String getFallBackMethodName();

    boolean isPredicateMethodsSize();

    TypeMirror sourceReturnType();

    boolean hasSourceReturnType();

    String sourceMethodResultName();

    boolean hasAnnotationReturnStatement();

}
