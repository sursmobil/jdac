package com.github.sursmobil.dac;

import java.util.function.Function;

public interface TransientResolve<T, O1> {
    TypedProperty<T> get(Function<O1, T> parser);

    <O2> TransientResolve<T, O2> transform(Function<O1, O2> parser);
}
