package com.github.sursmobil.dac;

import java.util.Optional;

public interface TypedProperty<T> extends TypedResolve<T> {
    TypedProperty<T> withDefault(T defaultValue);

    TypedProperty<T> cached();

    Optional<T> getOptional();

    T getAllowNull();

    T get();
}
