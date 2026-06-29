package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private final QuantityMeasurementApp.LengthUnit FEET = QuantityMeasurementApp.LengthUnit.FEET;
    private final QuantityMeasurementApp.LengthUnit INCH = QuantityMeasurementApp.LengthUnit.INCH;
    private final QuantityMeasurementApp.LengthUnit YARDS = QuantityMeasurementApp.LengthUnit.YARDS;
    private final QuantityMeasurementApp.LengthUnit CM = QuantityMeasurementApp.LengthUnit.CENTIMETERS;

    private QuantityMeasurementApp.QuantityLength q(double v, QuantityMeasurementApp.LengthUnit u) {
        return new QuantityMeasurementApp.QuantityLength(v, u);
    }

    // ---------- Explicit target = feet ----------
    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        QuantityMeasurementApp.QuantityLength r = q(1.0, FEET).add(q(12.0, INCH), FEET);
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    // ---------- Explicit target = inches ----------
    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        QuantityMeasurementApp.QuantityLength r = q(1.0, FEET).add(q(12.0, INCH), INCH);
        assertEquals(24.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    // ---------- Explicit target = yards (different from both operands) ----------
    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        QuantityMeasurementApp.QuantityLength r = q(1.0, FEET).add(q(12.0, INCH), YARDS);
        assertEquals(0.6667, r.getValue(), 1e-3);
        assertEquals(YARDS, r.getUnit());
    }

    // ---------- Explicit target = centimeters ----------
    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        // 1 inch + 1 inch = 2 inch = ~5.08 cm
        QuantityMeasurementApp.QuantityLength r = q(1.0, INCH).add(q(1.0, INCH), CM);
        assertEquals(5.08, r.getValue(), 1e-2);
        assertEquals(CM, r.getUnit());
    }

    // ---------- Target same as first operand ----------
    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        QuantityMeasurementApp.QuantityLength r = q(2.0, YARDS).add(q(3.0, FEET), YARDS);
        assertEquals(3.0, r.getValue(), EPSILON);
        assertEquals(YARDS, r.getUnit());
    }

    // ---------- Target same as second operand ----------
    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        QuantityMeasurementApp.QuantityLength r = q(2.0, YARDS).add(q(3.0, FEET), FEET);
        assertEquals(9.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    // ---------- Commutativity ----------
    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        QuantityMeasurementApp.QuantityLength ab = q(1.0, FEET).add(q(12.0, INCH), YARDS);
        QuantityMeasurementApp.QuantityLength ba = q(12.0, INCH).add(q(1.0, FEET), YARDS);
        assertEquals(ab.getValue(), ba.getValue(), EPSILON);
        assertEquals(ab, ba);
    }

    // ---------- Zero with explicit target ----------
    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        // 5 ft + 0 inch = 5 ft = ~1.667 yards
        QuantityMeasurementApp.QuantityLength r = q(5.0, FEET).add(q(0.0, INCH), YARDS);
        assertEquals(1.6667, r.getValue(), 1e-3);
        assertEquals(YARDS, r.getUnit());
    }

    // ---------- Negative with explicit target ----------
    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        // 5 ft + (-2 ft) = 3 ft = 36 inch
        QuantityMeasurementApp.QuantityLength r = q(5.0, FEET).add(q(-2.0, FEET), INCH);
        assertEquals(36.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    // ---------- Null target unit ----------
    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> q(1.0, FEET).add(q(12.0, INCH), null));
    }

    // ---------- Large to small scale ----------
    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        // 1000 ft + 500 ft = 1500 ft = 18000 inch
        QuantityMeasurementApp.QuantityLength r = q(1000.0, FEET).add(q(500.0, FEET), INCH);
        assertEquals(18000.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    // ---------- Small to large scale ----------
    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        // 12 inch + 12 inch = 24 inch = ~0.667 yards
        QuantityMeasurementApp.QuantityLength r = q(12.0, INCH).add(q(12.0, INCH), YARDS);
        assertEquals(0.6667, r.getValue(), 1e-3);
        assertEquals(YARDS, r.getUnit());
    }

    // ---------- Mathematical correctness across target units ----------
    @Test
    void testAddition_ExplicitTargetUnit_SameSumDifferentUnits() {
        // same addition, different target units => same physical length
        QuantityMeasurementApp.QuantityLength inFeet = q(1.0, FEET).add(q(12.0, INCH), FEET);
        QuantityMeasurementApp.QuantityLength inInch = q(1.0, FEET).add(q(12.0, INCH), INCH);
        assertEquals(inFeet, inInch); // 2 ft == 24 inch (base unit lo same)
    }

    // ---------- Backward compatibility: UC6 add (no target) still works ----------
    @Test
    void testAddition_UC6_DefaultToFirstOperand_StillWorks() {
        QuantityMeasurementApp.QuantityLength r = q(1.0, FEET).add(q(12.0, INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    // ---------- Immutability ----------
    @Test
    void testAddition_OriginalsUnchanged() {
        QuantityMeasurementApp.QuantityLength a = q(1.0, FEET);
        QuantityMeasurementApp.QuantityLength b = q(12.0, INCH);
        a.add(b, YARDS);
        assertEquals(1.0, a.getValue(), EPSILON);
        assertEquals(12.0, b.getValue(), EPSILON);
    }
}