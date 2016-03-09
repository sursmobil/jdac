package com.github.sursmobil.dac;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sjanota on 08/03/16.
 */
public abstract class Dac {
    final String name;
    private final Map<String, TypedProperty<?>> cache = new HashMap<>();

    protected Dac(String name) {
        this.name = name;
    }

    public static TypedGetter<String> env(String string) {
        return () -> Optional.ofNullable(System.getenv(string));
    }

    @SuppressWarnings("unchecked")
    protected final <T> TypedProperty<T> typedProperty(String name, Class<T> type) {
        if(cache.containsKey(name)) {
            return (TypedProperty<T>) cache.get(name);
        } else {
            return new UnresolvedTypedProperty<>(name, this);
        }
    }

    final <T> TypedProperty<T> cacheTypedResult(String property, T value) {
        CachedTypedProperty<T> cached = new CachedTypedProperty<>(value);
        cache.put(property, cached);
        return cached;
    }
}
