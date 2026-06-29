package org.example;

/**
 * Standalone weight unit enum. Base unit = KILOGRAM.
 * Conversion responsibility EE enum ke (UC8 pattern - SRP).
 */
public enum WeightUnit {
    KILOGRAM(1.0),       // base unit
    GRAM(0.001),         // 1 g = 0.001 kg
    POUND(0.453592);     // 1 lb = 0.453592 kg

    private final double conversionFactor; // 1 unit = conversionFactor kg

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    /** Ee unit value ni base unit (kg) loki convert chestundi. */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /** Base unit (kg) value ni ee unit loki convert chestundi. */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}