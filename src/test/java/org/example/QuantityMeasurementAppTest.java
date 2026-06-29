package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private QuantityWeight w(double v, WeightUnit u) {
        return new QuantityWeight(v, u);
    }

    private final WeightUnit KG = WeightUnit.KILOGRAM;
    private final WeightUnit G = WeightUnit.GRAM;
    private final WeightUnit LB = WeightUnit.POUND;

    // ---------- Same-unit equality ----------
    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        assertEquals(w(1.0, KG), w(1.0, KG));
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {
        assertNotEquals(w(1.0, KG), w(2.0, KG));
    }

    @Test
    void testEquality_GramToGram_SameValue() {
        assertEquals(w(500.0, G), w(500.0, G));
    }

    @Test
    void testEquality_PoundToPound_SameValue() {
        assertEquals(w(2.0, LB), w(2.0, LB));
    }

    // ---------- Cross-unit equality ----------
    @Test
    void testEquality_KilogramToGram_EquivalentValue() {
        assertEquals(w(1.0, KG), w(1000.0, G));
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue() {
        assertEquals(w(1000.0, G), w(1.0, KG)); // symmetry
    }

    @Test
    void testEquality_KilogramToPound_EquivalentValue() {
        assertEquals(w(1.0, KG), w(2.20462, LB)); // 1 kg ~ 2.20462 lb
    }

    @Test
    void testEquality_GramToPound_EquivalentValue() {
        assertEquals(w(453.592, G), w(1.0, LB)); // 453.592 g ~ 1 lb
    }

    // ---------- Transitive ----------
    @Test
    void testEquality_TransitiveProperty() {
        QuantityWeight a = w(1.0, KG);
        QuantityWeight b = w(1000.0, G);
        QuantityWeight c = w(2.20462, LB);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    // ---------- Weight vs Length incompatibility ----------
    @Test
    void testEquality_WeightVsLength_Incompatible() {
        QuantityWeight kg = w(1.0, KG);
        QuantityLength ft = new QuantityLength(1.0, LengthUnit.FEET);
        assertNotEquals(kg, ft); // different categories
    }

    // ---------- Null / reference ----------
    @Test
    void testEquality_NullComparison() {
        assertNotEquals(w(1.0, KG), null);
    }

    @Test
    void testEquality_SameReference() {
        QuantityWeight kg = w(1.0, KG);
        assertEquals(kg, kg);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> w(1.0, null));
    }

    // ---------- Edge cases ----------
    @Test
    void testEquality_ZeroValue() {
        assertEquals(w(0.0, KG), w(0.0, G));
    }

    @Test
    void testEquality_NegativeWeight() {
        assertEquals(w(-1.0, KG), w(-1000.0, G));
    }

    @Test
    void testEquality_LargeWeightValue() {
        assertEquals(w(1_000_000.0, G), w(1000.0, KG));
    }

    @Test
    void testEquality_SmallWeightValue() {
        assertEquals(w(0.001, KG), w(1.0, G));
    }

    // ---------- Conversions ----------
    @Test
    void testConversion_KilogramToGram() {
        QuantityWeight r = w(1.0, KG).convertTo(G);
        assertEquals(1000.0, r.getValue(), EPSILON);
        assertEquals(G, r.getUnit());
    }

    @Test
    void testConversion_PoundToKilogram() {
        QuantityWeight r = w(2.20462, LB).convertTo(KG);
        assertEquals(1.0, r.getValue(), 1e-4);
    }

    @Test
    void testConversion_KilogramToPound() {
        QuantityWeight r = w(1.0, KG).convertTo(LB);
        assertEquals(2.20462, r.getValue(), 1e-4);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, w(5.0, KG).convertTo(KG).getValue(), EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, w(0.0, KG).convertTo(G).getValue(), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0, w(-1.0, KG).convertTo(G).getValue(), EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        double back = w(1.5, KG).convertTo(G).convertTo(KG).getValue();
        assertEquals(1.5, back, EPSILON);
    }

    // ---------- Addition (implicit target) ----------
    @Test
    void testAddition_SameUnit_KilogramPlusKilogram() {
        QuantityWeight r = w(1.0, KG).add(w(2.0, KG));
        assertEquals(3.0, r.getValue(), EPSILON);
        assertEquals(KG, r.getUnit());
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram() {
        QuantityWeight r = w(1.0, KG).add(w(1000.0, G));
        assertEquals(2.0, r.getValue(), EPSILON);
        assertEquals(KG, r.getUnit());
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram() {
        QuantityWeight r = w(2.20462, LB).add(w(1.0, KG));
        assertEquals(4.40924, r.getValue(), 1e-3);
        assertEquals(LB, r.getUnit());
    }

    // ---------- Addition (explicit target) ----------
    @Test
    void testAddition_ExplicitTargetUnit_Gram() {
        QuantityWeight r = w(1.0, KG).add(w(1000.0, G), G);
        assertEquals(2000.0, r.getValue(), EPSILON);
        assertEquals(G, r.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Kilogram() {
        // 2 kg + 4 lb = ~3.814 kg
        QuantityWeight r = w(2.0, KG).add(w(4.0, LB), KG);
        assertEquals(3.814368, r.getValue(), 1e-4);
        assertEquals(KG, r.getUnit());
    }

    // ---------- Commutativity ----------
    @Test
    void testAddition_Commutativity() {
        QuantityWeight ab = w(1.0, KG).add(w(1000.0, G), G);
        QuantityWeight ba = w(1000.0, G).add(w(1.0, KG), G);
        assertEquals(ab, ba);
    }

    // ---------- Identity / negative / large ----------
    @Test
    void testAddition_WithZero() {
        QuantityWeight r = w(5.0, KG).add(w(0.0, G));
        assertEquals(5.0, r.getValue(), EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityWeight r = w(5.0, KG).add(w(-2000.0, G));
        assertEquals(3.0, r.getValue(), EPSILON);
    }

    @Test
    void testAddition_LargeValues() {
        QuantityWeight r = w(1e6, KG).add(w(1e6, KG));
        assertEquals(2e6, r.getValue(), EPSILON);
    }

    // ---------- WeightUnit enum methods ----------
    @Test
    void testWeightUnit_ConvertToBaseUnit() {
        assertEquals(1.0, WeightUnit.GRAM.convertToBaseUnit(1000.0), EPSILON);
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testWeightUnit_ConvertFromBaseUnit() {
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), EPSILON);
    }

    // ---------- HashCode consistency (equal objects -> same hash) ----------
    @Test
    void testHashCode_EqualObjects_SameHash() {
        assertEquals(w(1.0, KG).hashCode(), w(1000.0, G).hashCode());
    }

    // ---------- Backward compatibility (length still works) ----------
    @Test
    void testBackwardCompatibility_LengthStillWorks() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH));
    }
}