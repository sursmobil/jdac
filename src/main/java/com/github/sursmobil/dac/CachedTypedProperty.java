package com.github.sursmobil.dac;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class CachedTypedProperty<T> implements TypedProperty<T> {

    private final T value;

    public CachedTypedProperty(T value) {
        this.value = value;
    }


    @Override
    public T get() {
        return value;
    }

    @Override
    public T getAllowNull() {
        return value;
    }

    @Override
    public TypedProperty<T> withDefault(T defaultValue) {
        return this;
    }

    @Override
    public TypedProperty<T> cached() {
        return this;
    }

    @Override
    public Optional<T> getOptional() {
        return Optional.of(value);
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
