package com.github.sursmobil.dac;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class CachedProperty implements Property {

    private final String value;

    public CachedProperty(String value) {
        this.value = value;
    }

    @Override
    public <P> P transform(Function<String, P> transform) {
        return transform.apply(value);
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public Property withDefault(String defaultValue) {
        return this;
    }

    @Override
    public Property cached() {
        return this;
    }

    @Override
    public Property from(Getter getter) {
        return this;
    }

    @Override
    public Resolve when(BooleanSupplier predicate) {
        return this;
    }
}
