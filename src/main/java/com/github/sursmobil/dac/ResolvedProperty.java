package com.github.sursmobil.dac;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Created by sjanota on 08/03/16.
 */
class ResolvedProperty implements Property {
    private final String name;
    private final String value;
    private final Dac config;

    public ResolvedProperty(String name, String value, Dac config) {
        this.name = name;
        this.value = value;
        this.config = config;
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
    public Property from(Getter getter) {
        return this;
    }

    @Override
    public Resolve when(BooleanSupplier predicate) {
        return this;
    }

    @Override
    public Property cached() {
        return config.cacheResult(name, value);
    }

}
