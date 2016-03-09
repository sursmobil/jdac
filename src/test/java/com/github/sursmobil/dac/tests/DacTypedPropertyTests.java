package com.github.sursmobil.dac.tests;

import com.github.sursmobil.dac.Dac;
import com.github.sursmobil.dac.DacException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DacTypedPropertyTests extends Dac {
    public DacTypedPropertyTests() {
        super("DAC Tests");
    }

    @Test
    public void basicGetValueTest() {
        int actual = typedProperty("test", Integer.class)
                .from(() -> Optional.of(1))
                .get();
        assertEquals(1, actual);
    }

    @Test
    public void getFirstDefinedValue() {
        Boolean actual = typedProperty("test", Boolean.class)
                .from(Optional::empty)
                .from(() -> Optional.of(true))
                .from(() -> Optional.of(false))
                .get();
        assertEquals(true, actual);
    }

    @Test(expected = DacException.class)
    public void expectErrorWhenValueIsMissing() {
        typedProperty("test", String.class)
                .from(Optional::empty)
                .get();
    }

    @Test
    public void allowToSpecifyDefaultValue() {
        int actual = typedProperty("test", Integer.class)
                .withDefault(123)
                .from(Optional::empty)
                .get();
        assertEquals(123, actual);

        actual = typedProperty("test", Integer.class)
                .withDefault(543)
                .from(() -> Optional.of(11))
                .get();
        assertEquals(11, actual);
    }

    @Test
    public void useMiddleForm() {
        int actual = typedProperty("test", Integer.class)
                .fromOther(() -> Optional.of("1"))
                    .get(Integer::parseInt)
                .get();
        assertEquals(1, actual);

        actual = typedProperty("test", Integer.class)
                .fromOther(() -> Optional.of("12"))
                    .transform(s -> s.substring(1))
                    .get(Integer::parseInt)
                .get();
        assertEquals(2, actual);
    }

    @Test
    public void allowResultCachingByName() {
        final int[] calls = {0};
        final Date now = new Date();
        Date value = typedProperty("test1", Date.class)
                .from(() -> { calls[0]++; return Optional.of(now); })
                .cached()
                .get();
        assertEquals(now, value);

        value = typedProperty("test1", Date.class)
                .from(() -> { calls[0]++; return Optional.of(new Date()); })
                .cached()
                .get();
        assertEquals(now, value);
        assertEquals(1, calls[0]);
    }

    @Test
    public void conditionTest() {
        int actual = typedProperty("test", Integer.class)
                .when(() -> true).from(() -> Optional.of(1))
                .from(() -> Optional.of(2))
                .get();
        assertEquals(1, actual);

        actual = typedProperty("test", Integer.class)
                .when(() -> false).from(() -> Optional.of(3))
                .from(() -> Optional.of(4))
                .get();
        assertEquals(4, actual);
    }
}
