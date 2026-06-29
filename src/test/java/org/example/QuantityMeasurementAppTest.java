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
    private final VolumeUnit GAL = VolumeUnit.GALLON;

    @Test
    void testGenericQuantity_LengthEquality() {
        assertEquals(new Quantity<>(1.0, FEET), new Quantity<>(12.0, INCH));
    }

    @Test
    void testGenericQuantity_WeightEquality() {
        assertEquals(new Quantity<>(1.0, KG), new Quantity<>(1000.0, G));
    }

    @Test
    void testVolumeUnit_LitreConstant() {
        assertEquals(1.0, L.getConversionFactor(), EPSILON);
        assertEquals("LITRE", L.getUnitName());
    }

    @Test
    void testVolumeUnit_MillilitreConstant() {
        assertEquals(0.001, ML.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnit_GallonConstant() {
        assertEquals(3.78541, GAL.getConversionFactor(), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, ML.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, GAL.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, ML.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, GAL.convertFromBaseUnit(3.78541), 1e-4);
    }

    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertEquals(new Quantity<>(1.0, L), new Quantity<>(1.0, L));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, L), new Quantity<>(2.0, L));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, L), new Quantity<>(1000.0, ML));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1000.0, ML), new Quantity<>(1.0, L));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, L), new Quantity<>(0.264172, GAL));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, GAL), new Quantity<>(3.78541, L));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, L), new Quantity<>(1.0, FEET));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, L), new Quantity<>(1.0, KG));
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(new Quantity<>(1.0, L), null);
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, L);
        assertEquals(q, q);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, (VolumeUnit) null));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, L);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, ML);
        Quantity<VolumeUnit> c = new Quantity<>(0.264172, GAL);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void testEquality_ZeroValue() {
        assertEquals(new Quantity<>(0.0, L), new Quantity<>(0.0, ML));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertEquals(new Quantity<>(-1.0, L), new Quantity<>(-1000.0, ML));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertEquals(new Quantity<>(1_000_000.0, ML), new Quantity<>(1000.0, L));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertEquals(new Quantity<>(0.001, L), new Quantity<>(1.0, ML));
    }

    @Test
    void testConversion_LitreToMillilitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1.0, L).convertTo(ML);
        assertEquals(1000.0, r.getValue(), EPSILON);
        assertEquals(ML, r.getUnit());
    }

    @Test
    void testConversion_MillilitreToLitre() {
        assertEquals(1.0, new Quantity<>(1000.0, ML).convertTo(L).getValue(), EPSILON);
    }

    @Test
    void testConversion_GallonToLitre() {
        assertEquals(3.78541, new Quantity<>(1.0, GAL).convertTo(L).getValue(), 1e-4);
    }

    @Test
    void testConversion_LitreToGallon() {
        assertEquals(1.0, new Quantity<>(3.78541, L).convertTo(GAL).getValue(), 1e-4);
    }

    @Test
    void testConversion_MillilitreToGallon() {
        assertEquals(0.264172, new Quantity<>(1000.0, ML).convertTo(GAL).getValue(), 1e-4);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, new Quantity<>(5.0, L).convertTo(L).getValue(), EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, new Quantity<>(0.0, L).convertTo(ML).getValue(), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0, new Quantity<>(-1.0, L).convertTo(ML).getValue(), EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        double back = new Quantity<>(1.5, L).convertTo(ML).convertTo(L).getValue();
        assertEquals(1.5, back, EPSILON);
    }

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1.0, L).add(new Quantity<>(2.0, L));
        assertEquals(3.0, r.getValue(), EPSILON);
        assertEquals(L, r.getUnit());
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        Quantity<VolumeUnit> r = new Quantity<>(500.0, ML).add(new Quantity<>(500.0, ML));
        assertEquals(1000.0, r.getValue(), EPSILON);
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1.0, L).add(new Quantity<>(1000.0, ML));
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(L, r.getUnit());
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1000.0, ML).add(new Quantity<>(1.0, L));
        assertEquals(2000.0, r.getValue(), EPSILON);
        assertEquals(ML, r.getUnit());
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1.0, GAL).add(new Quantity<>(3.78541, L));
        assertEquals(2.0, r.getValue(), 1e-4);
        assertEquals(GAL, r.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> r = new Quantity<>(1.0, L).add(new Quantity<>(1000.0, ML), ML);
        assertEquals(2000.0, r.getValue(), EPSILON);
        assertEquals(ML, r.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        Quantity<VolumeUnit> r = new Quantity<>(3.78541, L).add(new Quantity<>(3.78541, L), GAL);
        assertEquals(2.0, r.getValue(), 1e-4);
        assertEquals(GAL, r.getUnit());
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> ab = new Quantity<>(1.0, L).add(new Quantity<>(1000.0, ML), ML);
        Quantity<VolumeUnit> ba = new Quantity<>(1000.0, ML).add(new Quantity<>(1.0, L), ML);
        assertEquals(ab, ba);
    }

    @Test
    void testAddition_WithZero() {
        Quantity<VolumeUnit> r = new Quantity<>(5.0, L).add(new Quantity<>(0.0, ML));
        assertEquals(5.0, r.getValue(), EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity<VolumeUnit> r = new Quantity<>(5.0, L).add(new Quantity<>(-2000.0, ML));
        assertEquals(3.0, r.getValue(), EPSILON);
    }

    @Test
    void testAddition_LargeValues() {
        Quantity<VolumeUnit> r = new Quantity<>(1e6, L).add(new Quantity<>(1e6, L));
        assertEquals(2e6, r.getValue(), EPSILON);
    }

    @Test
    void testHashCode_EqualObjects_SameHash() {
        assertEquals(new Quantity<>(1.0, L).hashCode(), new Quantity<>(1000.0, ML).hashCode());
    }

    @Test
    void testScalability_VolumeIntegration() {
        Quantity<VolumeUnit> v = new Quantity<>(1.0, GAL);
        assertEquals(3.78541, v.convertTo(L).getValue(), 1e-4);
    }

    @Test
    void testBackwardCompatibility_LengthAndWeightStillWork() {
        assertEquals(new Quantity<>(1.0, FEET), new Quantity<>(12.0, INCH));
        assertEquals(new Quantity<>(1.0, KG), new Quantity<>(1000.0, G));
    }
}