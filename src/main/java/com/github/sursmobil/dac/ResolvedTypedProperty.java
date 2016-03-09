package com.github.sursmobil.dac;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ResolvedTypedProperty<T> implements TypedProperty<T> {
    private final String name;
    private final T value;
    private final Dac config;

    public ResolvedTypedProperty(String name, T value, Dac config) {
        this.name = name;
        this.value = value;
        this.config = config;
    }

    @Override
    public TypedProperty<T> withDefault(T defaultValue) {
        return this;
    }

    @Override
    public TypedProperty<T> cached() {
        return config.cacheTypedResult(name, value);
    }

    @Override
    public Optional<T> getOptional() {
        return Optional.of(value);
    }

    @Override
    public T getAllowNull() {
        return value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public TypedProperty<T> from(TypedGetter<T> getter) {
        return this;
    }

    @Override
    public TypedResolve<T> when(BooleanSupplier predicate) {
        return this;
    }

    @Override
    public <Z> TransientResolve<T, Z> fromOther(TypedGetter<Z> otherGetter) {
        return new NoopTransientResolve<>(this);
    }

}
