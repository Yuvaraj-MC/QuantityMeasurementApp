package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private final QuantityMeasurementApp.LengthUnit FEET = QuantityMeasurementApp.LengthUnit.FEET;
    private final QuantityMeasurementApp.LengthUnit INCH = QuantityMeasurementApp.LengthUnit.INCH;
    private final QuantityMeasurementApp.LengthUnit YARDS = QuantityMeasurementApp.LengthUnit.YARDS;
    private final QuantityMeasurementApp.LengthUnit CM = QuantityMeasurementApp.LengthUnit.CENTIMETERS;

    // ---------- Basic conversions ----------
    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0, QuantityMeasurementApp.convert(1.0, FEET, INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0, QuantityMeasurementApp.convert(24.0, INCH, FEET), EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0, QuantityMeasurementApp.convert(1.0, YARDS, INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(2.0, QuantityMeasurementApp.convert(72.0, INCH, YARDS), EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(1.0, QuantityMeasurementApp.convert(2.54, CM, INCH), EPSILON);
    }

    @Test
    void testConversion_FeetToYards() {
        assertEquals(2.0, QuantityMeasurementApp.convert(6.0, FEET, YARDS), EPSILON);
    }

    // ---------- Round-trip ----------
    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double original = 5.0;
        double toInches = QuantityMeasurementApp.convert(original, FEET, INCH);
        double backToFeet = QuantityMeasurementApp.convert(toInches, INCH, FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    // ---------- Same unit ----------
    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, QuantityMeasurementApp.convert(5.0, FEET, FEET), EPSILON);
    }

    // ---------- Zero ----------
    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, QuantityMeasurementApp.convert(0.0, FEET, INCH), EPSILON);
    }

    // ---------- Negative ----------
    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0, QuantityMeasurementApp.convert(-1.0, FEET, INCH), EPSILON);
    }

    // ---------- Large value ----------
    @Test
    void testConversion_LargeValue() {
        assertEquals(12000.0, QuantityMeasurementApp.convert(1000.0, FEET, INCH), EPSILON);
    }

    // ---------- Invalid unit ----------
    @Test
    void testConversion_NullSourceUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(1.0, null, INCH));
    }

    @Test
    void testConversion_NullTargetUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(1.0, FEET, null));
    }

    // ---------- NaN / Infinite ----------
    @Test
    void testConversion_NaN_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(Double.NaN, FEET, INCH));
    }

    @Test
    void testConversion_Infinite_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(Double.POSITIVE_INFINITY, FEET, INCH));
    }

    // ---------- convertTo instance method ----------
    @Test
    void testConvertTo_ReturnsNewInstance() {
        QuantityMeasurementApp.QuantityLength yard =
                new QuantityMeasurementApp.QuantityLength(1.0, YARDS);
        QuantityMeasurementApp.QuantityLength inInches = yard.convertTo(INCH);
        assertEquals(36.0, inInches.getValue(), EPSILON);
        assertEquals(INCH, inInches.getUnit());
        // original object change avvaledu (immutability)
        assertEquals(1.0, yard.getValue(), EPSILON);
        assertEquals(YARDS, yard.getUnit());
    }

    // ---------- Backward compatibility (UC1-UC4 equality still works) ----------
    @Test
    void testEquality_YardToFeet_StillWorks() {
        assertEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, YARDS),
                new QuantityMeasurementApp.QuantityLength(3.0, FEET));
    }
}