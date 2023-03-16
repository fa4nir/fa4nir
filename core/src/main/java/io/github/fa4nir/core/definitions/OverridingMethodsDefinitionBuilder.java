package io.github.fa4nir.core.definitions;

import io.github.fa4nir.core.annotations.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface OverridingMethodsDefinitionBuilder {

    static OverridingMethodsDefinitionBuilder newBuilder() {
        return new DefaultOverridingMethodsDefinition();
    }

    OverridingMethodsDefinitionBuilder sourceMethod(ExecutableElement sourceMethod);

    OverridingMethodsDefinitionBuilder target(Element target);

    OverridingMethodsDefinitionBuilder annotationNotifyTo(NotifyTo annotationNotifyTo);

    OverridingMethodsDefinitionBuilder annotationFallBackMethod(FallBackMethod annotationFallBackMethod);

    OverridingMethodsDefinitionBuilder targetMethods();

    OverridingMethodsDefinitionBuilder sourceParameters();

    OverridingMethodsDefinitionBuilder groupOfSourceParameters();

    OverridingMethodsDefinitionBuilder targetFieldName();

    OverridingMethodsDefinitionBuilder targetMethod();

    OverridingMethodsDefinitionBuilder resultName();

    OverridingMethodsDefinitionBuilder delegateResultToAnnotations();

    OverridingMethodsDefinitionBuilder fallBackMethod();

    OverridingMethodsDefinitionBuilder targetMethodReturnType();

    OverridingMethodsDefinitionBuilder notifyToTarget();

    OverridingMethodsDefinitionBuilder targetParameters();

    OverridingMethodsDefinitionBuilder predicateMethods();

    OverridingMethodsDefinitionBuilder fallBackMethodName();

    OverridingMethodsDefinitionBuilder sourceReturnType();

    OverridingMethodsDefinitionBuilder sourceMethodResultName();

    OverridingMethodsDefinition build();

    class DefaultOverridingMethodsDefinition implements OverridingMethodsDefinitionBuilder {

        private ExecutableElement sourceMethod;

        private Element target;

        private NotifyTo annotationNotifyTo;

        private FallBackMethod annotationFallBackMethod;

        private List<? extends Element> targetMethods;

        private List<? extends VariableElement> sourceParameters;

        private Map<String, ? extends VariableElement> groupOfSourceParameters;

        private String targetFieldName;

        private ExecutableElement targetMethod;

        private String resultName;

        private DelegateResultTo[] delegateResultToAnnotations;

        private ExecutableElement fallBackMethod;

        private TypeMirror targetMethodReturnType;

        private List<? extends VariableElement> targetParameters;

        private List<ExecutableElement> predicateMethods;

        private String notifyToTarget;

        private String fallBackMethodName;

        private TypeMirror sourceMethodReturnType;

        private String sourceMethodResultName;

        private boolean isTargetMethodHasReturnStatement;

        DefaultOverridingMethodsDefinition() {
        }

        private String createResultName(List<? extends VariableElement> sourceParameters, ExecutableElement targetMethod) {
            String resultName = "result";
            boolean isResultMatch = sourceParameters.stream().anyMatch(param -> param.getSimpleName().toString().equals("result"));
            if (isResultMatch) {
                resultName = String.format("%sFrom%s", resultName, StringUtils.capitalize(targetMethod.getSimpleName().toString()));
            }
            return resultName;
        }

        private ExecutableElement findMethod(Element target, String name) {
            return target.getEnclosedElements().stream()
                    .filter(method -> method instanceof ExecutableElement)
                    .map(method -> (ExecutableElement) method)
                    .filter(method -> method.getSimpleName().toString().equals(name))
                    .findFirst().orElse(null);
        }

        @Override
        public OverridingMethodsDefinitionBuilder sourceMethod(ExecutableElement sourceMethod) {
            this.sourceMethod = sourceMethod;
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder target(Element target) {
            this.target = target;
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder annotationNotifyTo(NotifyTo annotationNotifyTo) {
            this.annotationNotifyTo = annotationNotifyTo;
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder annotationFallBackMethod(FallBackMethod annotationFallBackMethod) {
            this.annotationFallBackMethod = annotationFallBackMethod;
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder targetMethods() {
            this.targetMethods = this.target.getEnclosedElements();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder sourceParameters() {
            this.sourceParameters = this.sourceMethod.getParameters();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder groupOfSourceParameters() {
            this.groupOfSourceParameters = this.sourceParameters.stream()
                    .collect(Collectors.toMap(k -> k.getSimpleName().toString(), Function.identity()));
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder targetFieldName() {
            this.targetFieldName = Introspector.decapitalize(this.target.getSimpleName().toString());
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder targetMethod() {
            this.targetMethod = findMethod(this.target, this.notifyToTarget);
            this.isTargetMethodHasReturnStatement = Objects.nonNull(this.targetMethod.getAnnotation(ReturnStatement.class));
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder resultName() {
            this.resultName = createResultName(this.sourceParameters, this.targetMethod);
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder delegateResultToAnnotations() {
            this.delegateResultToAnnotations = this.targetMethod.getAnnotationsByType(DelegateResultTo.class);
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder fallBackMethod() {
            this.fallBackMethod = findMethod(this.target, this.fallBackMethodName);
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder targetMethodReturnType() {
            this.targetMethodReturnType = this.targetMethod.getReturnType();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder targetParameters() {
            this.targetParameters = Objects.requireNonNull(
                    this.targetMethod, String.format("Cannot find method %s", this.targetMethod.getSimpleName())
            ).getParameters();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder predicateMethods() {
            this.predicateMethods = this.targetMethods.stream()
                    .filter(method -> method instanceof ExecutableElement)
                    .filter(method -> Objects.nonNull(method.getAnnotation(PayloadPredicate.class)))
                    .map(method -> ((ExecutableElement) method))
                    .collect(Collectors.toList());
            if (this.predicateMethods.size() > 1) {
                throw new IllegalArgumentException("Only one predicate method.");
            } else if (this.predicateMethods.size() == 1) {
                TypeMirror type = this.predicateMethods.get(0).getReturnType();
                if (!TypeKind.BOOLEAN.equals(type.getKind())) {
                    throw new IllegalArgumentException("Method predicate should return boolean");
                }
            }
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder notifyToTarget() {
            this.notifyToTarget = this.annotationNotifyTo.name();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder fallBackMethodName() {
            this.fallBackMethodName = Objects.nonNull(this.annotationFallBackMethod) ? this.annotationFallBackMethod.name() : "";
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder sourceReturnType() {
            this.sourceMethodReturnType = this.sourceMethod.getReturnType();
            return this;
        }

        @Override
        public OverridingMethodsDefinitionBuilder sourceMethodResultName() {
            this.sourceMethodResultName = createSourceResult(this.sourceParameters, this.sourceMethod);
            if (!this.isTargetMethodHasReturnStatement) {
                this.resultName = "second" + StringUtils.capitalize(this.resultName);
            }
            return this;
        }

        private String createSourceResult(List<? extends VariableElement> sourceParameters, ExecutableElement targetMethod) {
            String resultName = "result";
            boolean isResultMatch = sourceParameters.stream().anyMatch(param -> param.getSimpleName().toString().equals("result"));
            if (isResultMatch) {
                resultName = String.format("%sOf%s", resultName, StringUtils.capitalize(targetMethod.getSimpleName().toString()));
            }
            return resultName;
        }

        @Override
        public OverridingMethodsDefinition build() {
            return new OverridingMethodsDefinition() {
                @Override
                public ExecutableElement getSourceMethod() {
                    return sourceMethod;
                }

                @Override
                public Element getTarget() {
                    return target;
                }

                @Override
                public NotifyTo getAnnotationNotifyTo() {
                    return annotationNotifyTo;
                }

                @Override
                public FallBackMethod getAnnotationFallBackMethod() {
                    return annotationFallBackMethod;
                }

                @Override
                public List<? extends Element> getTargetMethods() {
                    return targetMethods;
                }

                @Override
                public List<? extends VariableElement> getSourceParameters() {
                    return sourceParameters;
                }

                @Override
                public Map<String, ? extends VariableElement> getGroupOfSourceParameters() {
                    return groupOfSourceParameters;
                }

                @Override
                public String getTargetFieldName() {
                    return targetFieldName;
                }

                @Override
                public ExecutableElement getTargetMethod() {
                    return targetMethod;
                }

                @Override
                public String getResultName() {
                    return resultName;
                }

                @Override
                public DelegateResultTo[] getDelegateResultToAnnotations() {
                    return delegateResultToAnnotations;
                }

                @Override
                public ExecutableElement getFallBackMethod() {
                    return fallBackMethod;
                }

                @Override
                public TypeMirror getTargetMethodReturnType() {
                    return targetMethodReturnType;
                }

                @Override
                public List<? extends VariableElement> getTargetParameters() {
                    return targetParameters;
                }

                @Override
                public List<ExecutableElement> getPredicateMethods() {
                    return predicateMethods;
                }

                @Override
                public String getNotifyToTarget() {
                    return notifyToTarget;
                }

                @Override
                public String getFallBackMethodName() {
                    return fallBackMethodName;
                }

                @Override
                public boolean isPredicateMethodsSize() {
                    return predicateMethods.size() == 1;
                }

                @Override
                public TypeMirror sourceReturnType() {
                    return sourceMethodReturnType;
                }

                @Override
                public boolean hasSourceReturnType() {
                    return !TypeKind.VOID.equals(sourceMethodReturnType.getKind());
                }

                @Override
                public String sourceMethodResultName() {
                    return sourceMethodResultName;
                }

                @Override
                public boolean hasAnnotationReturnStatement() {
                    return Objects.nonNull(targetMethod.getAnnotation(ReturnStatement.class));
                }

            };
        }
    }

}
