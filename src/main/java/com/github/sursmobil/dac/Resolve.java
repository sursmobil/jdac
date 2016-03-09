package com.github.sursmobil.dac;

import java.util.function.BooleanSupplier;

/**
 * Created by sjanota on 08/03/16.
 */
public interface Resolve {
    Property from(Getter getter);

    Resolve when(BooleanSupplier predicate);
}
