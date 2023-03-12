package io.github.fa4nir.core.factories.methods;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InterceptMethodsContainer {

    private static InterceptMethodsContainer instance = null;

    private final Map<String, InterceptMethodFactory> interceptMethod;

    private InterceptMethodsContainer() {
        this.interceptMethod = new ConcurrentHashMap<>();
    }

    public static InterceptMethodsContainer getInstance() {
        if (Objects.isNull(instance)) {
            instance = new InterceptMethodsContainer();
        }
        return instance;
    }

    public void put(String key, InterceptMethodFactory value) {
        this.interceptMethod.put(key, value);
    }

    public InterceptMethodFactory get(String key) {
        return this.interceptMethod.get(key);
    }

}
