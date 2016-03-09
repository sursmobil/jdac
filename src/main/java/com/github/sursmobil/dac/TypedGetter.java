package com.github.sursmobil.dac;

import java.util.Optional;
import java.util.function.Supplier;

public interface TypedGetter<T> extends Supplier<Optional<T>> {
}
