package com.github.sursmobil.dac;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

class UnresolvedTypedProperty<T> implements TypedProperty<T> {

    private final String name;
    private final Dac config;

    private T defaultValue;
    private boolean cached;

    public UnresolvedTypedProperty(String name, Dac config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public TypedProperty<T> withDefault(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public TypedProperty<T> cached() {
        this.cached = true;
        return this;
    }

    @Override
    public Optional<T> getOptional() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public T get() {
        Optional<T> r = getOptional();
        if (!r.isPresent()) {
            throw new DacException("Property '" + name + "' in config '" + config.name + "' not defined.");
        } else {
            return r.get();
        }
    }

    @Override
    public T getAllowNull() {
        return defaultValue;
    }

    @Override
    public TypedProperty<T> from(TypedGetter<T> getter) {
        return new Resolve().from(getter);
    }

    @Override
    public TypedResolve<T> when(BooleanSupplier predicate) {
        return new Resolve().when(predicate);
    }

    @Override
    public <Z> TransientResolve<T, Z> fromOther(TypedGetter<Z> otherGetter) {
        return new Resolve().fromOther(otherGetter);
    }

    private class Resolve implements TypedResolve<T> {
        @Override
        public TypedProperty<T> from(TypedGetter<T> getter) {
            Optional<T> result = getter.get();
            if(result.isPresent()) {
                return resolve(result.get());
            } else {
                return UnresolvedTypedProperty.this;
            }
        }

        @Override
        public TypedResolve<T> when(BooleanSupplier predicate) {
            if(predicate.getAsBoolean()) {
                return this;
            } else {
                return new NoopResolve();
            }
        }

        @Override
        public <Z> TransientResolve<T, Z> fromOther(TypedGetter<Z> otherGetter) {
            Optional<Z> result = otherGetter.get();
            if(result.isPresent()) {
                return new Transient<>(result.get());
            } else {
                return new NoopTransientResolve<>(UnresolvedTypedProperty.this);
            }
        }

        private TypedProperty<T> resolve(T value) {
            if(cached) {
                return config.cacheTypedResult(name, value);
            } else {
                return new ResolvedTypedProperty<>(name, value, config);
            }
        }

        private class Transient<Z> implements TransientResolve<T, Z> {
            private final Z middleForm;

            private Transient(Z middleForm) {
                this.middleForm = middleForm;
            }

            @Override
            public TypedProperty<T> get(Function<Z, T> parser) {
                T parsed = parser.apply(middleForm);
                return resolve(parsed);
            }

            @Override
            public <O2> TransientResolve<T, O2> transform(Function<Z, O2> parser) {
                O2 another = parser.apply(middleForm);
                return new Transient<>(another);
            }
        }
    }


    private class NoopResolve implements TypedResolve<T> {

        @Override
        public TypedProperty<T> from(TypedGetter<T> getter) {
            return UnresolvedTypedProperty.this;
        }

        @Override
        public TypedResolve<T> when(BooleanSupplier predicate) {
            return this;
        }

        @Override
        public <Z> TransientResolve<T, Z> fromOther(TypedGetter<Z> otherGetter) {
            return new NoopTransientResolve<>(UnresolvedTypedProperty.this);
        }
    }

    private class Value {
        final T value;

        private Value(T value) {
            this.value = value;
        }
    }
}
