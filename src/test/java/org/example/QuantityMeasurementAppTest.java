package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private QuantityMeasurementApp.QuantityLength q(double v, QuantityMeasurementApp.LengthUnit u) {
        return new QuantityMeasurementApp.QuantityLength(v, u);
    }

    private final QuantityMeasurementApp.LengthUnit FEET = QuantityMeasurementApp.LengthUnit.FEET;
    private final QuantityMeasurementApp.LengthUnit INCH = QuantityMeasurementApp.LengthUnit.INCH;
    private final QuantityMeasurementApp.LengthUnit YARDS = QuantityMeasurementApp.LengthUnit.YARDS;
    private final QuantityMeasurementApp.LengthUnit CM = QuantityMeasurementApp.LengthUnit.CENTIMETERS;

    // ---------- Yard-to-Yard ----------
    @Test
    void testEquality_YardToYard_SameValue() {
        assertEquals(q(1.0, YARDS), q(1.0, YARDS));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        assertNotEquals(q(1.0, YARDS), q(2.0, YARDS));
    }

    // ---------- Yard cross-unit ----------
    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertEquals(q(1.0, YARDS), q(3.0, FEET));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        assertEquals(q(3.0, FEET), q(1.0, YARDS)); // symmetry
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertEquals(q(1.0, YARDS), q(36.0, INCH));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        assertEquals(q(36.0, INCH), q(1.0, YARDS)); // symmetry
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        assertNotEquals(q(1.0, YARDS), q(2.0, FEET));
    }

    // ---------- Centimeters ----------
    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        assertEquals(q(1.0, CM), q(0.393701, INCH));
    }

    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        assertNotEquals(q(1.0, CM), q(1.0, FEET));
    }

    @Test
    void testEquality_CmToCm_SameValue() {
        assertEquals(q(2.0, CM), q(2.0, CM));
    }

    @Test
    void testEquality_CmToCm_DifferentValue() {
        assertNotEquals(q(2.0, CM), q(3.0, CM));
    }

    // ---------- Transitive property ----------
    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        // 1 yard == 3 feet, 3 feet == 36 inch  =>  1 yard == 36 inch
        QuantityMeasurementApp.QuantityLength a = q(1.0, YARDS);
        QuantityMeasurementApp.QuantityLength b = q(3.0, FEET);
        QuantityMeasurementApp.QuantityLength c = q(36.0, INCH);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c); // transitive
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        // 2 yard == 6 feet == 72 inch
        assertEquals(q(2.0, YARDS), q(6.0, FEET));
        assertEquals(q(6.0, FEET), q(72.0, INCH));
        assertEquals(q(2.0, YARDS), q(72.0, INCH));
    }

    // ---------- Null unit ----------
    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> q(1.0, null));
    }

    // ---------- Reflexive ----------
    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.QuantityLength yard = q(1.0, YARDS);
        assertEquals(yard, yard);
    }

    // ---------- Null comparison ----------
    @Test
    void testEquality_NullComparison() {
        assertNotEquals(q(1.0, YARDS), null);
    }

    // ---------- Type safety ----------
    @Test
    void testEquality_NonQuantityType() {
        assertNotEquals(q(1.0, YARDS), "1.0 yards");
    }
}