package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private final LengthUnit FEET = LengthUnit.FEET;
    private final LengthUnit INCH = LengthUnit.INCH;
    private final LengthUnit YARDS = LengthUnit.YARDS;
    private final LengthUnit CM = LengthUnit.CENTIMETERS;
    private final WeightUnit KG = WeightUnit.KILOGRAM;
    private final WeightUnit G = WeightUnit.GRAM;
    private final WeightUnit LB = WeightUnit.POUND;

    @Test
    void testIMeasurable_LengthUnitImplementation() {
        assertTrue(FEET instanceof IMeasurable);
        assertEquals(1.0, FEET.getConversionFactor(), EPSILON);
        assertEquals("FEET", FEET.getUnitName());
    }

    @Test
    void testIMeasurable_WeightUnitImplementation() {
        assertTrue(KG instanceof IMeasurable);
        assertEquals(1.0, KG.getConversionFactor(), EPSILON);
        assertEquals("KILOGRAM", KG.getUnitName());
    }

    @Test
    void testIMeasurable_ConsistentBehavior() {
        assertEquals(1.0, INCH.convertToBaseUnit(12.0), EPSILON);
        assertEquals(1.0, G.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testGenericQuantity_LengthEquality() {
        assertEquals(new Quantity<>(1.0, FEET), new Quantity<>(12.0, INCH));
    }

    @Test
    void testGenericQuantity_LengthEquality_Different() {
        assertNotEquals(new Quantity<>(1.0, FEET), new Quantity<>(2.0, FEET));
    }

    @Test
    void testGenericQuantity_WeightEquality() {
        assertEquals(new Quantity<>(1.0, KG), new Quantity<>(1000.0, G));
    }

    @Test
    void testGenericQuantity_WeightEquality_KgToPound() {
        assertEquals(new Quantity<>(1.0, KG), new Quantity<>(2.20462, LB));
    }

    @Test
    void testGenericQuantity_LengthConversion() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).convertTo(INCH);
        assertEquals(12.0, r.getValue(), EPSILON);
        assertEquals(INCH, r.getUnit());
    }

    @Test
    void testGenericQuantity_WeightConversion() {
        Quantity<WeightUnit> r = new Quantity<>(1.0, KG).convertTo(G);
        assertEquals(1000.0, r.getValue(), EPSILON);
        assertEquals(G, r.getUnit());
    }

    @Test
    void testGenericQuantity_LengthAddition() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH), FEET);
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    @Test
    void testGenericQuantity_WeightAddition() {
        Quantity<WeightUnit> r = new Quantity<>(1.0, KG).add(new Quantity<>(1000.0, G), KG);
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(KG, r.getUnit());
    }

    @Test
    void testGenericQuantity_AddImplicitTarget() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(FEET, r.getUnit());
    }

    @Test
    void testCrossCategoryPrevention_LengthVsWeight() {
        Quantity<LengthUnit> ft = new Quantity<>(1.0, FEET);
        Quantity<WeightUnit> kg = new Quantity<>(1.0, KG);
        assertNotEquals(ft, kg);
    }

    @Test
    void testConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, (LengthUnit) null));
    }

    @Test
    void testConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, FEET));
    }

    @Test
    void testConstructorValidation_Infinite() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.POSITIVE_INFINITY, KG));
    }

    @Test
    void testEquals_Reflexive() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, FEET);
        assertEquals(q, q);
    }

    @Test
    void testEquals_Symmetric() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, INCH);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void testEquals_Transitive() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0, FEET);
        Quantity<LengthUnit> c = new Quantity<>(36.0, INCH);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void testEquals_NullComparison() {
        assertNotEquals(new Quantity<>(1.0, FEET), null);
    }

    @Test
    void testConversion_RoundTrip() {
        double back = new Quantity<>(1.5, KG).convertTo(G).convertTo(KG).getValue();
        assertEquals(1.5, back, EPSILON);
    }

    @Test
    void testConversion_CentimeterToFeet() {
        assertEquals(1.0, CM.convertToBaseUnit(30.48), EPSILON);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<LengthUnit> ab = new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCH), YARDS);
        Quantity<LengthUnit> ba = new Quantity<>(12.0, INCH).add(new Quantity<>(1.0, FEET), YARDS);
        assertEquals(ab, ba);
    }

    @Test
    void testHashCode_EqualObjects_SameHash() {
        assertEquals(new Quantity<>(1.0, KG).hashCode(), new Quantity<>(1000.0, G).hashCode());
    }

    @Test
    void testHashCode_CrossCategory_DifferentHash() {
        Quantity<LengthUnit> ft = new Quantity<>(1.0, FEET);
        Quantity<WeightUnit> kg = new Quantity<>(1.0, KG);
        assertNotEquals(ft.hashCode(), kg.hashCode());
    }

    @Test
    void testScalability_NewUnitEnumIntegration() {
        Quantity<TestVolumeUnit> a = new Quantity<>(1.0, TestVolumeUnit.LITRE);
        Quantity<TestVolumeUnit> b = new Quantity<>(1000.0, TestVolumeUnit.MILLILITRE);
        assertEquals(a, b);
        assertEquals(1000.0, a.convertTo(TestVolumeUnit.MILLILITRE).getValue(), EPSILON);
    }

    @Test
    void testImmutability_OriginalsUnchanged() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, FEET);
        a.add(b);
        assertEquals(1.0, a.getValue(), EPSILON);
        assertEquals(2.0, b.getValue(), EPSILON);
    }

    enum TestVolumeUnit implements IMeasurable {
        LITRE(1.0),
        MILLILITRE(0.001);

        private final double conversionFactor;

        TestVolumeUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }

        @Override
        public double convertToBaseUnit(double value) {
            return value * conversionFactor;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue / conversionFactor;
        }

        @Override
        public String getUnitName() {
            return name();
        }
    }
}