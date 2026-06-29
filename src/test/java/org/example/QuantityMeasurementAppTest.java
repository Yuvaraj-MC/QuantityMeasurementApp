package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private final LengthUnit FEET = LengthUnit.FEET;
    private final LengthUnit INCH = LengthUnit.INCH;
    private final WeightUnit KG = WeightUnit.KILOGRAM;
    private final WeightUnit G = WeightUnit.GRAM;
    private final VolumeUnit L = VolumeUnit.LITRE;
    private final VolumeUnit ML = VolumeUnit.MILLILITRE;

    // ---------- Behavior preserved (UC12) ----------
    @Test
    void testAdd_UC12_BehaviorPreserved() {
        assertEquals(2.0, new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH)).getValue(), EPSILON);
        assertEquals(15000.0,
                new Quantity<>(10.0, KG).add(new Quantity<>(5000.0, G), G).getValue(), EPSILON);
    }

    @Test
    void testSubtract_UC12_BehaviorPreserved() {
        assertEquals(9.5, new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCH)).getValue(), EPSILON);
        assertEquals(3000.0,
                new Quantity<>(5.0, L).subtract(new Quantity<>(2.0, L), ML).getValue(), EPSILON);
    }

    @Test
    void testDivide_UC12_BehaviorPreserved() {
        assertEquals(5.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET)), EPSILON);
        assertEquals(1.0, new Quantity<>(24.0, INCH).divide(new Quantity<>(2.0, FEET)), EPSILON);
    }

    // ---------- Validation consistency across operations ----------
    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, FEET).add(null));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, FEET).subtract(null));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, FEET).divide(null));
    }

    @Test
    void testValidation_NullOperand_SameMessage() {
        String addMsg = assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).add(null)).getMessage();
        String subMsg = assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract(null)).getMessage();
        String divMsg = assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).divide(null)).getMessage();
        assertEquals(addMsg, subMsg);
        assertEquals(subMsg, divMsg);
    }

    @Test
    void testValidation_NullTargetUnit_AddSubtractReject() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).add(new Quantity<>(5.0, FEET), null));
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, FEET), null));
    }

    // ---------- Division by zero ----------
    @Test
    void testDivision_ByZero_Throws() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, FEET).divide(new Quantity<>(0.0, FEET)));
    }

    // ---------- Non-commutativity preserved ----------
    @Test
    void testSubtraction_NonCommutative() {
        double ab = new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, FEET)).getValue();
        double ba = new Quantity<>(5.0, FEET).subtract(new Quantity<>(10.0, FEET)).getValue();
        assertEquals(5.0, ab, EPSILON);
        assertEquals(-5.0, ba, EPSILON);
    }

    @Test
    void testDivision_NonCommutative() {
        assertEquals(2.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(5.0, FEET)), EPSILON);
        assertEquals(0.5, new Quantity<>(5.0, FEET).divide(new Quantity<>(10.0, FEET)), EPSILON);
    }

    // ---------- Implicit / explicit target unit ----------
    @Test
    void testImplicitTargetUnit_AddSubtract() {
        assertEquals(FEET, new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH)).getUnit());
        assertEquals(FEET, new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCH)).getUnit());
    }

    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides() {
        assertEquals(INCH, new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH), INCH).getUnit());
        assertEquals(INCH, new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCH), INCH).getUnit());
    }

    // ---------- Rounding ----------
    @Test
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        // result rounded to 2 decimals
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).add(new Quantity<>(1.0, INCH));
        // 1 ft + 1 inch = 1.0833... ft -> rounded 1.08
        assertEquals(1.08, r.getValue(), EPSILON);
    }

    // ---------- Immutability ----------
    @Test
    void testImmutability_AfterAdd() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, FEET);
        a.add(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    @Test
    void testImmutability_AfterSubtract() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, FEET);
        a.subtract(b);
        assertEquals(10.0, a.getValue(), EPSILON);
    }

    @Test
    void testImmutability_AfterDivide() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    // ---------- Cross-category prevention (all operations) ----------
    @Test
    void testCrossCategory_Subtract_Throws() {
        Quantity rawA = new Quantity<>(10.0, FEET);
        Quantity rawB = new Quantity<>(5.0, KG);
        assertThrows(IllegalArgumentException.class, () -> rawA.subtract(rawB));
    }

    // ---------- All categories ----------
    @Test
    void testAllOperations_AcrossAllCategories() {
        assertEquals(3.0, new Quantity<>(5.0, FEET).subtract(new Quantity<>(2.0, FEET)).getValue(), EPSILON);
        assertEquals(3.0, new Quantity<>(5.0, KG).subtract(new Quantity<>(2.0, KG)).getValue(), EPSILON);
        assertEquals(3.0, new Quantity<>(5.0, L).subtract(new Quantity<>(2.0, L)).getValue(), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(5.0, FEET)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, KG).divide(new Quantity<>(5.0, KG)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, L).divide(new Quantity<>(5.0, L)), EPSILON);
    }

    // ---------- Chained operations ----------
    @Test
    void testArithmetic_Chain_Operations() {
        // (10 - 2) ft = 8 ft, then / 2 ft = 4.0
        double r = new Quantity<>(10.0, FEET)
                .subtract(new Quantity<>(2.0, FEET))
                .divide(new Quantity<>(2.0, FEET));
        assertEquals(4.0, r, EPSILON);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, FEET);
        assertEquals(10.0, a.add(b).subtract(b).getValue(), EPSILON);
    }

    // ---------- Equality / conversion still work ----------
    @Test
    void testBackwardCompatibility_EqualityAndConversion() {
        assertEquals(new Quantity<>(1.0, FEET), new Quantity<>(12.0, INCH));
        assertEquals(1000.0, new Quantity<>(1.0, L).convertTo(ML).getValue(), EPSILON);
    }
}