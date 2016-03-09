package com.github.sursmobil.dac;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sjanota on 08/03/16.
 */
public abstract class Dac {
    final String name;
    private final Map<String, Property> cache = new HashMap<>();
    private final Map<String, TypedProperty<?>> typedCache = new HashMap<>();

    protected Dac(String name) {
        this.name = name;
    }

    public static TypedGetter<String> env(String string) {
        return () -> Optional.ofNullable(System.getenv(string));
    }

    protected final Property property(String name) {
        if(cache.containsKey(name)) {
            return cache.get(name);
        } else {
            return new UnresolvedProperty(name, this);
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T> TypedProperty<T> typedProperty(String name, Class<T> type) {
        if(typedCache.containsKey(name)) {
            return (TypedProperty<T>) typedCache.get(name);
        } else {
            return new UnresolvedTypedProperty<>(name, this);
        }
    }

    final Property cacheResult(String property, String value) {
        Property cached = new CachedProperty(value);
        cache.put(property, cached);
        return cached;
    }

    final <T> TypedProperty<T> cacheTypedResult(String property, T value) {
        CachedTypedProperty<T> cached = new CachedTypedProperty<>(value);
        typedCache.put(property, cached);
        return cached;
    }
}
