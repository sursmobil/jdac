package com.github.sursmobil.dac;

import java.util.function.Function;

/**
 * Created by sjanota on 08/03/16.
 */
public interface Property extends Resolve {
    <P> P transform(Function<String, P> transform);

    String get();

    Property withDefault(String defaultValue);

    Property cached();
}
