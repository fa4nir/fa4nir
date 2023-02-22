package com.github.core.utils;

import com.github.core.annotations.InterceptMapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;

public class OverridingMethodMetaInfo {

    private final ExecutableElement method;

    private final InterceptMapper[] methods;

    private final List<? extends Element> sourceTypeElementMethods;

    private final Map<String, ? extends Element> fallBackMethods;

    private final String sourceClassFieldName;

    public static Builder builder() {
        return new Builder();
    }

    private OverridingMethodMetaInfo(Builder b) {
        this.method = b.method;
        this.methods = b.methods;
        this.sourceTypeElementMethods = b.sourceTypeElementMethods;
        this.fallBackMethods = b.fallBackMethods;
        this.sourceClassFieldName = b.sourceClassFieldName;
    }

    public List<? extends Element> getSourceTypeElementMethods() {
        return sourceTypeElementMethods;
    }

    public ExecutableElement getMethod() {
        return method;
    }

    public InterceptMapper[] getMethods() {
        return methods;
    }

    public Map<String, ? extends Element> getFallBackMethods() {
        return fallBackMethods;
    }

    public String getSourceClassFieldName() {
        return sourceClassFieldName;
    }

    public static class Builder {
        private ExecutableElement method;

        private InterceptMapper[] methods;

        private List<? extends Element> sourceTypeElementMethods;

        private Map<String, ? extends Element> fallBackMethods;

        private String sourceClassFieldName;

        public Builder method(ExecutableElement method) {
            this.method = method;
            return this;
        }

        public Builder methods(InterceptMapper[] methods) {
            this.methods = methods;
            return this;
        }

        public Builder currentTypeElementMethods(List<? extends Element> sourceTypeElementMethods) {
            this.sourceTypeElementMethods = sourceTypeElementMethods;
            return this;
        }

        public Builder fallBackMethods(Map<String, ? extends Element> fallBackMethods) {
            this.fallBackMethods = fallBackMethods;
            return this;
        }

        public Builder sourceClassFieldName(String sourceClassFieldName) {
            this.sourceClassFieldName = sourceClassFieldName;
            return this;
        }

        public OverridingMethodMetaInfo build() {
            return new OverridingMethodMetaInfo(this);
        }

    }
}
