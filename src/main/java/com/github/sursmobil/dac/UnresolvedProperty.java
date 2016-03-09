package com.github.sursmobil.dac;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Created by sjanota on 08/03/16.
 */
class UnresolvedProperty implements Property {
    private final String name;
    private final Dac config;

    private String defaultValue;
    private boolean cached;

    UnresolvedProperty(String name, Dac config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public <P> P transform(Function<String, P> transform) {
        return transform.apply(get());
    }

    @Override
    public String get() {
        return failOrDefault();
    }

    @Override
    public Property withDefault(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public Property cached() {
        this.cached = true;
        return this;
    }

    @Override
    public Property from(Getter getter) {
        return new Resolve().from(getter);
    }

    @Override
    public com.github.sursmobil.dac.Resolve when(BooleanSupplier predicate) {
        return new Resolve().when(predicate);
    }

    private String failOrDefault() {
        if (defaultValue == null) {
            throw new DacException("Property '" + name + "' in config '" + config.name + "' not defined.");
        } else {
            return defaultValue;
        }
    }

    private class Resolve implements com.github.sursmobil.dac.Resolve {
        @Override
        public Property from(Getter getter) {
            Optional<String> result = getter.get();
            if(result.isPresent()) {
                return resolve(result.get());
            } else {
                return UnresolvedProperty.this;
            }
        }

        @Override
        public com.github.sursmobil.dac.Resolve when(BooleanSupplier predicate) {
            if(predicate.getAsBoolean()) {
                return this;
            } else {
                return new NoopResolve();
            }
        }

        private Property resolve(String value) {
            if(cached) {
                return config.cacheResult(name, value);
            } else {
                return new ResolvedProperty(name, value, config);
            }
        }
    }

    private class NoopResolve implements com.github.sursmobil.dac.Resolve {

        @Override
        public Property from(Getter getter) {
            return UnresolvedProperty.this;
        }

        @Override
        public com.github.sursmobil.dac.Resolve when(BooleanSupplier predicate) {
            return this;
        }
    }
}
