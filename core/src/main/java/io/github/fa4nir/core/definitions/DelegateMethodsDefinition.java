package io.github.fa4nir.core.definitions;

import com.squareup.javapoet.CodeBlock;
import io.github.fa4nir.core.annotations.DelegateResultTo;
import io.github.fa4nir.core.annotations.FetchParam;
import io.github.fa4nir.core.annotations.FetchResult;
import io.github.fa4nir.core.annotations.ReturnStatement;
import io.github.fa4nir.core.utils.ParametersUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.*;
import java.util.stream.Collectors;

public class DelegateMethodsDefinition implements DelegateMethodsDefinitionBuilder {

    private String resultName;

    private List<? extends Element> targetEnclosedElements;

    private String targetFieldName;

    private List<? extends VariableElement> sourceParameters;

    private Map<String, ? extends VariableElement> groupOfSourceParameters;

    private DelegateResultTo[] delegateResultToAnnotations;

    private String sourceResultName;

    DelegateMethodsDefinition() {
    }

    @Override
    public DelegateMethodsDefinitionBuilder setResultName(String resultName) {
        this.resultName = resultName;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setTargetEnclosedElements(List<? extends Element> targetEnclosedElements) {
        this.targetEnclosedElements = targetEnclosedElements;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setSourceParameters(List<? extends VariableElement> sourceParameters) {
        this.sourceParameters = sourceParameters;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setGroupOfSourceParameters(Map<String, ? extends VariableElement> groupOfSourceParameters) {
        this.groupOfSourceParameters = groupOfSourceParameters;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setDelegateResultToAnnotations(DelegateResultTo[] delegateResultToAnnotations) {
        this.delegateResultToAnnotations = delegateResultToAnnotations;
        return this;
    }

    @Override
    public DelegateMethodsDefinitionBuilder setSourceResultName(String sourceResultName) {
        this.sourceResultName = sourceResultName;
        return this;
    }

    @Override
    public List<CodeBlock> build() {
        return generateCallToDelegateMethods();
    }

    private List<CodeBlock> generateCallToDelegateMethods() {
        return Arrays.stream(this.delegateResultToAnnotations)
                .collect(Collectors.toCollection(LinkedHashSet::new)).stream()
                .flatMap(delegateToMethod -> collectDelegateParameters(delegateToMethod.method()).stream())
                .collect(Collectors.toList());
    }

    private List<CodeBlock> collectDelegateParameters(String methodName) {
        return this.targetEnclosedElements.stream()
                .filter(method -> method instanceof ExecutableElement)
                .map(delegator -> ((ExecutableElement) delegator))
                .filter(delegator -> delegator.getSimpleName().toString().equals(methodName))
                .map(this::codeBlock).collect(Collectors.toList());
    }

    private CodeBlock codeBlock(ExecutableElement delegator) {
        String parametersAsString = parametersAsString(delegator);
        ReturnStatement annotation = delegator.getAnnotation(ReturnStatement.class);
        if (Objects.nonNull(annotation)) {
            return CodeBlock.of("$N = this.$N.$N($N)",
                    this.sourceResultName,
                    this.targetFieldName,
                    delegator.getSimpleName().toString(),
                    parametersAsString
            );
        } else {
            return CodeBlock.of("this.$N.$N($N)",
                    this.targetFieldName,
                    delegator.getSimpleName().toString(),
                    parametersAsString
            );
        }
    }

    private String parametersAsString(ExecutableElement delegator) {
        return delegator.getParameters().stream().map(parameter ->
                        retrieveLink(this.resultName, this.sourceParameters, this.groupOfSourceParameters, parameter))
                .collect(Collectors.joining(","));
    }

    private String retrieveLink(String resultName,
                                List<? extends VariableElement> targetParameters,
                                Map<String, ? extends VariableElement> groupOfSourceParameters,
                                VariableElement parameter) {
        FetchResult actualResult = parameter.getAnnotation(FetchResult.class);
        FetchParam fetchParam = parameter.getAnnotation(FetchParam.class);
        if (Objects.nonNull(actualResult)) {
            return resultName;
        } else if (Objects.nonNull(fetchParam)) {
            return ParametersUtils.getVariableElement(
                    targetParameters,
                    groupOfSourceParameters,
                    fetchParam
            ).getSimpleName().toString();
        }
        return "";
    }

}
