package com.github.core.utils;

import java.util.Objects;

public class ListenerTypeAndFallBackAnnotationData {

    private String listenerType;

    private String fallBackClassHandler;

    public ListenerTypeAndFallBackAnnotationData() {
    }

    public ListenerTypeAndFallBackAnnotationData(String listenerType, String fallBackClassHandler) {
        this.listenerType = listenerType;
        this.fallBackClassHandler = fallBackClassHandler;
    }

    public String getListenerType() {
        return listenerType;
    }

    public String getFallBackClassHandler() {
        return fallBackClassHandler;
    }

    public void setListenerType(String listenerType) {
        this.listenerType = listenerType;
    }

    public void setFallBackClassHandler(String fallBackClassHandler) {
        this.fallBackClassHandler = fallBackClassHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListenerTypeAndFallBackAnnotationData)) return false;
        ListenerTypeAndFallBackAnnotationData that = (ListenerTypeAndFallBackAnnotationData) o;
        return Objects.equals(listenerType, that.listenerType) && Objects.equals(fallBackClassHandler, that.fallBackClassHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listenerType, fallBackClassHandler);
    }

    @Override
    public String toString() {
        return "ClassAnnotationPair{" +
                "listenerType='" + listenerType + '\'' +
                ", fallBackClassHandler='" + fallBackClassHandler + '\'' +
                '}';
    }
}
