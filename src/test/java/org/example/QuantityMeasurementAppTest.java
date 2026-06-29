package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private QuantityLength q(double v, LengthUnit u) {
        return new QuantityLength(v, u);
    }

    // ---------- LengthUnit enum constants + factors ----------
    @Test
    void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12, LengthUnit.INCH.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0 / 30.48, LengthUnit.CENTIMETERS.getConversionFactor(), EPSILON);
    }

    // ---------- convertToBaseUnit (this unit -> feet) ----------
    @Test
    void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(5.0, LengthUnit.FEET.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCH.convertToBaseUnit(12.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), EPSILON);
    }

    // ---------- convertFromBaseUnit (feet -> this unit) ----------
    @Test
    void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, LengthUnit.FEET.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCH.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), EPSILON);
    }

    // ---------- Refactored QuantityLength ----------
    @Test
    void testQuantityLengthRefactored_Equality() {
        assertEquals(q(1.0, LengthUnit.FEET), q(12.0, LengthUnit.INCH));
    }

    @Test
    void testQuantityLengthRefactored_ConvertTo() {
        QuantityLength r = q(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH);
        assertEquals(12.0, r.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, r.getUnit());
    }

    @Test
    void testQuantityLengthRefactored_Add() {
        QuantityLength r = q(1.0, LengthUnit.FEET).add(q(12.0, LengthUnit.INCH), LengthUnit.FEET);
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, r.getUnit());
    }

    @Test
    void testQuantityLengthRefactored_AddWithTargetUnit() {
        QuantityLength r = q(1.0, LengthUnit.FEET).add(q(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        assertEquals(0.6667, r.getValue(), 1e-3);
        assertEquals(LengthUnit.YARDS, r.getUnit());
    }

    @Test
    void testQuantityLengthRefactored_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> q(1.0, null));
    }

    @Test
    void testQuantityLengthRefactored_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> q(Double.NaN, LengthUnit.FEET));
    }

    // ---------- Backward compatibility (UC1-UC7 representatives) ----------
    @Test
    void testBackwardCompatibility_UC1Equality() {
        assertEquals(q(1.0, LengthUnit.FEET), q(1.0, LengthUnit.FEET));
        assertNotEquals(q(1.0, LengthUnit.FEET), q(2.0, LengthUnit.FEET));
    }

    @Test
    void testBackwardCompatibility_UC5Conversion() {
        assertEquals(36.0, QuantityMeasurementApp.convert(1.0, LengthUnit.YARDS, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testBackwardCompatibility_UC6Addition() {
        QuantityLength r = q(1.0, LengthUnit.FEET).add(q(12.0, LengthUnit.INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, r.getUnit());
    }

    @Test
    void testBackwardCompatibility_UC7AdditionWithTarget() {
        QuantityLength r = QuantityMeasurementApp.add(
                q(36.0, LengthUnit.INCH), q(1.0, LengthUnit.YARDS), LengthUnit.FEET);
        assertEquals(6.0, r.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, r.getUnit());
    }

    // ---------- Round trip ----------
    @Test
    void testRoundTripConversion_RefactoredDesign() {
        double original = 5.0;
        double toCm = QuantityMeasurementApp.convert(original, LengthUnit.FEET, LengthUnit.CENTIMETERS);
        double backToFeet = QuantityMeasurementApp.convert(toCm, LengthUnit.CENTIMETERS, LengthUnit.FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    // ---------- Commutativity still holds ----------
    @Test
    void testAddition_Commutativity_StillWorks() {
        QuantityLength ab = q(1.0, LengthUnit.FEET).add(q(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength ba = q(12.0, LengthUnit.INCH).add(q(1.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertEquals(ab, ba);
    }
}