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

    // ---------- Subtraction: same unit ----------
    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> r = new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, FEET));
        assertEquals(5.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        Quantity<VolumeUnit> r = new Quantity<>(10.0, L).subtract(new Quantity<>(3.0, L));
        assertEquals(7.0, r.getValue(), EPSILON);
    }

    // ---------- Subtraction: cross unit ----------
    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        // 10 ft - 6 inch = 10 - 0.5 = 9.5 ft
        Quantity<LengthUnit> r = new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCH));
        assertEquals(9.5, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        // 120 inch - 5 ft = 120 - 60 = 60 inch
        Quantity<LengthUnit> r = new Quantity<>(120.0, INCH).subtract(new Quantity<>(5.0, FEET));
        assertEquals(60.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    // ---------- Subtraction: explicit target ----------
    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        // 10 ft - 6 inch = 9.5 ft = 114 inch
        Quantity<LengthUnit> r = new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCH), INCH);
        assertEquals(114.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        // 5 L - 2 L = 3 L = 3000 mL
        Quantity<VolumeUnit> r = new Quantity<>(5.0, L).subtract(new Quantity<>(2.0, L), ML);
        assertEquals(3000.0, r.getValue(), EPSILON);
        assertEquals(ML, r.getUnit());
    }

    // ---------- Subtraction: negative / zero / identity ----------
    @Test
    void testSubtraction_ResultingInNegative() {
        Quantity<LengthUnit> r = new Quantity<>(5.0, FEET).subtract(new Quantity<>(10.0, FEET));
        assertEquals(-5.0, r.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ResultingInZero() {
        // 10 ft - 120 inch = 10 - 10 = 0
        Quantity<LengthUnit> r = new Quantity<>(10.0, FEET).subtract(new Quantity<>(120.0, INCH));
        assertEquals(0.0, r.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        Quantity<LengthUnit> r = new Quantity<>(5.0, FEET).subtract(new Quantity<>(0.0, INCH));
        assertEquals(5.0, r.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        // 5 - (-2) = 7
        Quantity<LengthUnit> r = new Quantity<>(5.0, FEET).subtract(new Quantity<>(-2.0, FEET));
        assertEquals(7.0, r.getValue(), EPSILON);
    }

    // ---------- Subtraction: non-commutative ----------
    @Test
    void testSubtraction_NonCommutative() {
        double ab = new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, FEET)).getValue();
        double ba = new Quantity<>(5.0, FEET).subtract(new Quantity<>(10.0, FEET)).getValue();
        assertEquals(5.0, ab, EPSILON);
        assertEquals(-5.0, ba, EPSILON);
        assertNotEquals(ab, ba);
    }

    // ---------- Subtraction: large / small ----------
    @Test
    void testSubtraction_WithLargeValues() {
        Quantity<WeightUnit> r = new Quantity<>(1e6, KG).subtract(new Quantity<>(5e5, KG));
        assertEquals(5e5, r.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_WithSmallValues() {
        Quantity<LengthUnit> r = new Quantity<>(0.001, FEET).subtract(new Quantity<>(0.0005, FEET));
        assertEquals(0.0005, r.getValue(), 1e-9);
    }

    // ---------- Subtraction: validation ----------
    @Test
    void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, FEET), null));
    }

    // ---------- Subtraction: chaining ----------
    @Test
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> r = new Quantity<>(10.0, FEET)
                .subtract(new Quantity<>(2.0, FEET))
                .subtract(new Quantity<>(1.0, FEET));
        assertEquals(7.0, r.getValue(), EPSILON);
    }

    // ---------- Subtraction: immutability ----------
    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, FEET);
        a.subtract(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    // ---------- Division: same unit ----------
    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET)), EPSILON);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(2.0, new Quantity<>(10.0, L).divide(new Quantity<>(5.0, L)), EPSILON);
    }

    // ---------- Division: cross unit ----------
    @Test
    void testDivision_CrossUnit_InchesDividedByFeet() {
        // 24 inch = 2 ft, divided by 2 ft = 1.0
        assertEquals(1.0, new Quantity<>(24.0, INCH).divide(new Quantity<>(2.0, FEET)), EPSILON);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        // 2 kg / 2000 g (= 2 kg) = 1.0
        assertEquals(1.0, new Quantity<>(2.0, KG).divide(new Quantity<>(2000.0, G)), EPSILON);
    }

    // ---------- Division: ratios ----------
    @Test
    void testDivision_RatioGreaterThanOne() {
        assertEquals(5.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET)), EPSILON);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        assertEquals(0.5, new Quantity<>(5.0, FEET).divide(new Quantity<>(10.0, FEET)), EPSILON);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(10.0, FEET)), EPSILON);
    }

    // ---------- Division: non-commutative ----------
    @Test
    void testDivision_NonCommutative() {
        double ab = new Quantity<>(10.0, FEET).divide(new Quantity<>(5.0, FEET));
        double ba = new Quantity<>(5.0, FEET).divide(new Quantity<>(10.0, FEET));
        assertEquals(2.0, ab, EPSILON);
        assertEquals(0.5, ba, EPSILON);
    }

    // ---------- Division: by zero ----------
    @Test
    void testDivision_ByZero() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, FEET).divide(new Quantity<>(0.0, FEET)));
    }

    // ---------- Division: large / small ----------
    @Test
    void testDivision_WithLargeRatio() {
        assertEquals(1e6, new Quantity<>(1e6, KG).divide(new Quantity<>(1.0, KG)), EPSILON);
    }

    @Test
    void testDivision_WithSmallRatio() {
        assertEquals(1e-6, new Quantity<>(1.0, KG).divide(new Quantity<>(1e6, KG)), 1e-12);
    }

    // ---------- Division: validation ----------
    @Test
    void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, FEET).divide(null));
    }

    // ---------- Division: immutability ----------
    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue(), EPSILON);
        assertEquals(5.0, b.getValue(), EPSILON);
    }

    // ---------- Integration ----------
    @Test
    void testSubtractionAddition_Inverse() {
        // A + B - B ~ A
        Quantity<LengthUnit> a = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(3.0, FEET);
        Quantity<LengthUnit> result = a.add(b).subtract(b);
        assertEquals(10.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtractionAndDivision_Integration() {
        // (10 ft - 4 ft) / 2 ft = 6/2 = 3.0
        double r = new Quantity<>(10.0, FEET).subtract(new Quantity<>(4.0, FEET))
                .divide(new Quantity<>(2.0, FEET));
        assertEquals(3.0, r, EPSILON);
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        assertEquals(3.0, new Quantity<>(5.0, FEET).subtract(new Quantity<>(2.0, FEET)).getValue(), EPSILON);
        assertEquals(3.0, new Quantity<>(5.0, KG).subtract(new Quantity<>(2.0, KG)).getValue(), EPSILON);
        assertEquals(3.0, new Quantity<>(5.0, L).subtract(new Quantity<>(2.0, L)).getValue(), EPSILON);
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertEquals(2.0, new Quantity<>(10.0, FEET).divide(new Quantity<>(5.0, FEET)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, KG).divide(new Quantity<>(5.0, KG)), EPSILON);
        assertEquals(2.0, new Quantity<>(10.0, L).divide(new Quantity<>(5.0, L)), EPSILON);
    }

    // ---------- Backward compatibility ----------
    @Test
    void testBackwardCompatibility_AdditionStillWorks() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
    }
}