package com.github.core.utils;

import com.github.core.annotations.InterceptMapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

public class OverridingMethodMetaInfo {

    private final ExecutableElement method;

    private final InterceptMapper[] methods;

    private final Map<String, ? extends Element> currentTypeElementMethods;

    private final Element fallBackMethod;

    private final String currentClassFieldName;

    public static Builder builder() {
        return new Builder();
    }

    private OverridingMethodMetaInfo(Builder b) {
        this.method = b.method;
        this.methods = b.methods;
        this.currentTypeElementMethods = b.currentTypeElementMethods;
        this.fallBackMethod = b.fallBackMethod;
        this.currentClassFieldName = b.currentClassFieldName;
    }

    public static class Builder {
        private ExecutableElement method;

        private InterceptMapper[] methods;

        private Map<String, ? extends Element> currentTypeElementMethods;

        private Element fallBackMethod;

        private String currentClassFieldName;

        public Builder method(ExecutableElement method) {
            this.method = method;
            return this;
        }

        public Builder methods(InterceptMapper[] methods) {
            this.methods = methods;
            return this;
        }

        public Builder currentTypeElementMethods(Map<String, ? extends Element> currentTypeElementMethods) {
            this.currentTypeElementMethods = currentTypeElementMethods;
            return this;
        }

        public Builder fallBackMethod(Element fallBackMethod) {
            this.fallBackMethod = fallBackMethod;
            return this;
        }

        public Builder currentClassFieldName(String currentClassFieldName) {
            this.currentClassFieldName = currentClassFieldName;
            return this;
        }

        public OverridingMethodMetaInfo build() {
            return new OverridingMethodMetaInfo(this);
        }

    }

    public ExecutableElement getMethod() {
        return method;
    }

    public InterceptMapper[] getMethods() {
        return methods;
    }

    public Map<String, ? extends Element> getCurrentTypeElementMethods() {
        return currentTypeElementMethods;
    }

    public Element getFallBackMethod() {
        return fallBackMethod;
    }

    public String getCurrentClassFieldName() {
        return currentClassFieldName;
    }
}
