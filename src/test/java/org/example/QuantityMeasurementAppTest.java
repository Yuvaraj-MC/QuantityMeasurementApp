package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private final TemperatureUnit C = TemperatureUnit.CELSIUS;
    private final TemperatureUnit F = TemperatureUnit.FAHRENHEIT;
    private final TemperatureUnit K = TemperatureUnit.KELVIN;
    private final LengthUnit FEET = LengthUnit.FEET;
    private final WeightUnit KG = WeightUnit.KILOGRAM;
    private final VolumeUnit L = VolumeUnit.LITRE;

    // ---------- Same-unit equality ----------
    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        assertEquals(new Quantity<>(0.0, C), new Quantity<>(0.0, C));
    }

    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        assertEquals(new Quantity<>(32.0, F), new Quantity<>(32.0, F));
    }

    @Test
    void testTemperatureEquality_KelvinToKelvin_SameValue() {
        assertEquals(new Quantity<>(300.0, K), new Quantity<>(300.0, K));
    }

    @Test
    void testTemperatureDifferentValuesInequality() {
        assertNotEquals(new Quantity<>(50.0, C), new Quantity<>(100.0, C));
    }

    // ---------- Cross-unit equality ----------
    @Test
    void testTemperatureEquality_0Celsius_32Fahrenheit() {
        assertEquals(new Quantity<>(0.0, C), new Quantity<>(32.0, F));
    }

    @Test
    void testTemperatureEquality_100Celsius_212Fahrenheit() {
        assertEquals(new Quantity<>(100.0, C), new Quantity<>(212.0, F));
    }

    @Test
    void testTemperatureEquality_0Celsius_273Kelvin() {
        assertEquals(new Quantity<>(0.0, C), new Quantity<>(273.15, K));
    }

    @Test
    void testTemperatureEquality_100Celsius_373Kelvin() {
        assertEquals(new Quantity<>(100.0, C), new Quantity<>(373.15, K));
    }

    @Test
    void testTemperatureEquality_Negative40_Equal() {
        // -40 C = -40 F (intersection point)
        assertEquals(new Quantity<>(-40.0, C), new Quantity<>(-40.0, F));
    }

    @Test
    void testTemperatureEquality_Symmetric() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, C);
        Quantity<TemperatureUnit> b = new Quantity<>(32.0, F);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void testTemperatureEquality_Reflexive() {
        Quantity<TemperatureUnit> q = new Quantity<>(25.0, C);
        assertEquals(q, q);
    }

    @Test
    void testTemperatureEquality_Transitive() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, C);
        Quantity<TemperatureUnit> b = new Quantity<>(32.0, F);
        Quantity<TemperatureUnit> c = new Quantity<>(273.15, K);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    // ---------- Conversion ----------
    @Test
    void testTemperatureConversion_CelsiusToFahrenheit() {
        assertEquals(212.0, new Quantity<>(100.0, C).convertTo(F).getValue(), EPSILON);
        assertEquals(122.0, new Quantity<>(50.0, C).convertTo(F).getValue(), EPSILON);
        assertEquals(-4.0, new Quantity<>(-20.0, C).convertTo(F).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_FahrenheitToCelsius() {
        assertEquals(0.0, new Quantity<>(32.0, F).convertTo(C).getValue(), EPSILON);
        assertEquals(100.0, new Quantity<>(212.0, F).convertTo(C).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_CelsiusToKelvin() {
        assertEquals(273.15, new Quantity<>(0.0, C).convertTo(K).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_KelvinToCelsius() {
        assertEquals(0.0, new Quantity<>(273.15, K).convertTo(C).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_RoundTrip() {
        double back = new Quantity<>(37.0, C).convertTo(F).convertTo(C).getValue();
        assertEquals(37.0, back, EPSILON);
    }

    @Test
    void testTemperatureConversion_SameUnit() {
        assertEquals(25.0, new Quantity<>(25.0, C).convertTo(C).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_AbsoluteZero() {
        // -273.15 C = 0 K
        assertEquals(0.0, new Quantity<>(-273.15, C).convertTo(K).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_EqualPoint() {
        // -40 C = -40 F
        assertEquals(-40.0, new Quantity<>(-40.0, C).convertTo(F).getValue(), EPSILON);
    }

    @Test
    void testTemperatureConversion_LargeValue() {
        // 1000 C = 1832 F
        assertEquals(1832.0, new Quantity<>(1000.0, C).convertTo(F).getValue(), EPSILON);
    }

    // ---------- Unsupported operations ----------
    @Test
    void testTemperatureUnsupportedOperation_Add() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, C).add(new Quantity<>(50.0, C)));
    }

    @Test
    void testTemperatureUnsupportedOperation_Subtract() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, C).subtract(new Quantity<>(50.0, C)));
    }

    @Test
    void testTemperatureUnsupportedOperation_Divide() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, C).divide(new Quantity<>(50.0, C)));
    }

    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, C).add(new Quantity<>(50.0, C)));
        assertTrue(ex.getMessage().contains("Temperature"));
        assertTrue(ex.getMessage().contains("ADD"));
    }

    // ---------- Operation support methods ----------
    @Test
    void testOperationSupport_TemperatureFalse() {
        assertFalse(C.supportsArithmetic());
        assertFalse(F.supportsArithmetic());
    }

    @Test
    void testOperationSupport_OtherCategoriesTrue() {
        assertTrue(FEET.supportsArithmetic());
        assertTrue(KG.supportsArithmetic());
        assertTrue(L.supportsArithmetic());
    }

    @Test
    void testValidateOperationSupport_TemperatureThrows() {
        assertThrows(UnsupportedOperationException.class,
                () -> C.validateOperationSupport("addition"));
    }

    @Test
    void testValidateOperationSupport_LengthDoesNotThrow() {
        assertDoesNotThrow(() -> FEET.validateOperationSupport("addition"));
    }

    // ---------- Cross-category prevention ----------
    @Test
    void testTemperatureVsLength_Incompatible() {
        assertNotEquals(new Quantity<>(100.0, C), new Quantity<>(100.0, FEET));
    }

    @Test
    void testTemperatureVsWeight_Incompatible() {
        assertNotEquals(new Quantity<>(50.0, C), new Quantity<>(50.0, KG));
    }

    @Test
    void testTemperatureVsVolume_Incompatible() {
        assertNotEquals(new Quantity<>(25.0, C), new Quantity<>(25.0, L));
    }

    // ---------- Validation ----------
    @Test
    void testTemperatureNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(100.0, (TemperatureUnit) null));
    }

    @Test
    void testTemperatureNullComparison() {
        assertNotEquals(new Quantity<>(100.0, C), null);
    }

    @Test
    void testTemperature_ImplementsIMeasurable() {
        assertTrue(C instanceof IMeasurable);
    }

    @Test
    void testTemperature_NonLinearConversion() {
        // linear aithe 0 C -> 0 F avvali. Kani offset valla 32 F.
        assertEquals(32.0, new Quantity<>(0.0, C).convertTo(F).getValue(), EPSILON);
        assertNotEquals(0.0, new Quantity<>(0.0, C).convertTo(F).getValue());
    }

    // ---------- Backward compatibility (other categories still do arithmetic) ----------
    @Test
    void testBackwardCompatibility_LengthArithmeticStillWorks() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, LengthUnit.INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
    }

    @Test
    void testBackwardCompatibility_WeightDivisionStillWorks() {
        assertEquals(2.0, new Quantity<>(10.0, KG).divide(new Quantity<>(5.0, KG)), EPSILON);
    }
}