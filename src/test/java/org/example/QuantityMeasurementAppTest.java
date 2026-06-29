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

    // ---------- Same-unit addition ----------
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityMeasurementApp.QuantityLength result = q(1.0, FEET).add(q(2.0, FEET));
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityMeasurementApp.QuantityLength result = q(6.0, INCH).add(q(6.0, INCH));
        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(INCH, result.getUnit());
    }

    // ---------- Cross-unit addition ----------
    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        // 1 ft + 12 inch = 2 ft (result feet, first operand)
        QuantityMeasurementApp.QuantityLength result = q(1.0, FEET).add(q(12.0, INCH));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        // 12 inch + 1 ft = 24 inch (result inches, first operand)
        QuantityMeasurementApp.QuantityLength result = q(12.0, INCH).add(q(1.0, FEET));
        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals(INCH, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        // 1 yard + 3 ft = 2 yards
        QuantityMeasurementApp.QuantityLength result = q(1.0, YARDS).add(q(3.0, FEET));
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(YARDS, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        // 2.54 cm + 1 inch = ~5.08 cm
        QuantityMeasurementApp.QuantityLength result = q(2.54, CM).add(q(1.0, INCH));
        assertEquals(5.08, result.getValue(), 1e-3);
        assertEquals(CM, result.getUnit());
    }

    // ---------- Commutativity (same physical length) ----------
    @Test
    void testAddition_Commutativity() {
        // add(A,B) and add(B,A) same length (equals true), display unit veru avvochu
        QuantityMeasurementApp.QuantityLength ab = q(1.0, FEET).add(q(12.0, INCH));
        QuantityMeasurementApp.QuantityLength ba = q(12.0, INCH).add(q(1.0, FEET));
        assertEquals(ab, ba); // equals() base-unit lo compare chestundi
    }

    // ---------- Identity (zero) ----------
    @Test
    void testAddition_WithZero() {
        QuantityMeasurementApp.QuantityLength result = q(5.0, FEET).add(q(0.0, INCH));
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    // ---------- Negative ----------
    @Test
    void testAddition_NegativeValues() {
        QuantityMeasurementApp.QuantityLength result = q(5.0, FEET).add(q(-2.0, FEET));
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    // ---------- Null operand ----------
    @Test
    void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> q(1.0, FEET).add(null));
    }

    // ---------- Large values ----------
    @Test
    void testAddition_LargeValues() {
        QuantityMeasurementApp.QuantityLength result = q(1e6, FEET).add(q(1e6, FEET));
        assertEquals(2e6, result.getValue(), EPSILON);
    }

    // ---------- Small values ----------
    @Test
    void testAddition_SmallValues() {
        QuantityMeasurementApp.QuantityLength result = q(0.001, FEET).add(q(0.002, FEET));
        assertEquals(0.003, result.getValue(), EPSILON);
    }

    // ---------- Static overloads ----------
    @Test
    void testAddition_StaticOverload_Objects() {
        QuantityMeasurementApp.QuantityLength result =
                QuantityMeasurementApp.add(q(1.0, FEET), q(2.0, FEET));
        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_StaticOverload_RawValues() {
        // 1 ft + 12 inch, target = INCH => 24 inch
        QuantityMeasurementApp.QuantityLength result =
                QuantityMeasurementApp.add(1.0, FEET, 12.0, INCH, INCH);
        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals(INCH, result.getUnit());
    }

    // ---------- Immutability check ----------
    @Test
    void testAddition_OriginalsUnchanged() {
        QuantityMeasurementApp.QuantityLength a = q(1.0, FEET);
        QuantityMeasurementApp.QuantityLength b = q(2.0, FEET);
        a.add(b);
        assertEquals(1.0, a.getValue(), EPSILON);
        assertEquals(2.0, b.getValue(), EPSILON);
    }
}