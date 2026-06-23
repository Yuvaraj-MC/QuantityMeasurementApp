package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // ---------- Feet tests (UC1) ----------
    @Test
    void testFeetEquality_SameValue() {
        assertEquals(new QuantityMeasurementApp.Feet(1.0),
                new QuantityMeasurementApp.Feet(1.0));
    }

    @Test
    void testFeetEquality_DifferentValue() {
        assertNotEquals(new QuantityMeasurementApp.Feet(1.0),
                new QuantityMeasurementApp.Feet(2.0));
    }

    @Test
    void testFeetEquality_NullComparison() {
        assertNotEquals(new QuantityMeasurementApp.Feet(1.0), null);
    }

    @Test
    void testFeetEquality_NonNumericInput() {
        assertNotEquals(new QuantityMeasurementApp.Feet(1.0), "1.0");
    }

    @Test
    void testFeetEquality_SameReference() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        assertEquals(f1, f1);
    }

    // ---------- Inches tests (UC2) ----------
    @Test
    void testInchesEquality_SameValue() {
        assertEquals(new QuantityMeasurementApp.Inches(1.0),
                new QuantityMeasurementApp.Inches(1.0));
    }

    @Test
    void testInchesEquality_DifferentValue() {
        assertNotEquals(new QuantityMeasurementApp.Inches(1.0),
                new QuantityMeasurementApp.Inches(2.0));
    }

    @Test
    void testInchesEquality_NullComparison() {
        assertNotEquals(new QuantityMeasurementApp.Inches(1.0), null);
    }

    @Test
    void testInchesEquality_NonNumericInput() {
        assertNotEquals(new QuantityMeasurementApp.Inches(1.0), "1.0");
    }

    @Test
    void testInchesEquality_SameReference() {
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        assertEquals(i1, i1);
    }
}