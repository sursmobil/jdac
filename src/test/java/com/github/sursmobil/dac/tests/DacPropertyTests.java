package com.github.sursmobil.dac.tests;

import com.github.sursmobil.dac.Dac;
import com.github.sursmobil.dac.DacException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DacPropertyTests extends Dac {

    public DacPropertyTests() {
        super("DAC Tests");
    }

    @Test
    public void basicGetValueTest() {
        String actual = property("test")
                .from(() -> Optional.of("defined"))
                .get();
        assertEquals("defined", actual);
    }

    @Test
    public void getFirstDefinedValue() {
        String actual = property("test")
                .from(Optional::empty)
                .from(() -> Optional.of("first"))
                .from(() -> Optional.of("second"))
                .get();
        assertEquals("first", actual);
    }

    @Test(expected = DacException.class)
    public void expectErrorWhenValueIsMissing() {
        property("test")
                .from(Optional::empty)
                .get();
    }

    @Test
    public void allowToSpecifyDefaultValue() {
        String actual = property("test")
                .from(Optional::empty)
                .withDefault("default")
                .get();
        assertEquals("default", actual);

        actual = property("test")
                .from(() -> Optional.of("defined"))
                .withDefault("default")
                .get();
        assertEquals("defined", actual);
    }

    @Test
    public void transformStringValue() {
        int actual = property("test")
                .from(() -> Optional.of("1"))
                .transform(Integer::parseInt);
        assertEquals(1, actual);
    }

    @Test
    public void allowResultCachingByName() {
        final int[] calls = {0};
        String value = property("test1")
                .from(() -> { calls[0]++; return Optional.of("defined"); })
                .cached()
                .get();
        assertEquals("defined", value);

        value = property("test1")
                .from(() -> { calls[0]++; return Optional.of("defined2"); })
                .cached()
                .get();
        assertEquals("defined", value);
        assertEquals(1, calls[0]);
    }

    @Test
    public void conditionTest() {
        String actual = property("test")
                .when(() -> true).from(() -> Optional.of("true"))
                .from(() -> Optional.of("other"))
                .get();
        assertEquals("true", actual);

        actual = property("test")
                .when(() -> false).from(() -> Optional.of("false"))
                .from(() -> Optional.of("other"))
                .get();
        assertEquals("other", actual);
    }
}
