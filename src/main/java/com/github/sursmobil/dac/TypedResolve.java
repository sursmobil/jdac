package com.github.sursmobil.dac;

import java.util.function.BooleanSupplier;

public interface TypedResolve<T> {
    TypedProperty<T> from(TypedGetter<T> getter);

    TypedResolve<T> when(BooleanSupplier predicate);

    <Z> TransientResolve<T, Z> fromOther(TypedGetter<Z> otherGetter);
}
