package com.github.sursmobil.dac;

import java.util.function.Function;

class NoopTransientResolve<T, Z> implements TransientResolve<T, Z> {
    private final TypedProperty<T> parent;

    NoopTransientResolve(TypedProperty<T> parent) {
        this.parent = parent;
    }

    @Override
    public TypedProperty<T> get(Function<Z, T> parser) {
        return parent;
    }

    @Override
    public <O2> TransientResolve<T, O2> transform(Function<Z, O2> parser) {
        return new NoopTransientResolve<>(parent);
    }
}
