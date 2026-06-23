package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // ---------- Same-unit equality ----------
    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET),
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH),
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH));
    }

    // ---------- Cross-unit equality (main UC3 feature) ----------
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        assertEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET),
                new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        // symmetry test: b.equals(a) kuda true ravali
        assertEquals(
                new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH),
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET));
    }

    // ---------- Different value (not equal) ----------
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertNotEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET),
                new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertNotEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH),
                new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.INCH));
    }

    // ---------- Null unit handling ----------
    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () ->
                new QuantityMeasurementApp.QuantityLength(1.0, null));
    }

    // ---------- Reflexive (same reference) ----------
    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.QuantityLength q =
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(q, q);
    }

    // ---------- Null comparison ----------
    @Test
    void testEquality_NullComparison() {
        assertNotEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET),
                null);
    }

    // ---------- Type safety (different type) ----------
    @Test
    void testEquality_NonQuantityType() {
        assertNotEquals(
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET),
                "1.0 feet");
    }
}