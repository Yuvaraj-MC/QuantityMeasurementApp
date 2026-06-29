package org.example;

/**
 * Standalone length unit enum. Prati unit ki feet (base unit) relative
 * conversion factor undi, and base unit ki/nundi convert chese
 * responsibility kuda EE enum ke untundi (Single Responsibility).
 */
public enum LengthUnit {
    FEET(1.0),            // 1 foot = 1 foot (base)
    INCH(1.0 / 12),       // 1 inch = 1/12 foot
    YARDS(3.0),           // 1 yard = 3 feet
    CENTIMETERS(1.0 / 30.48); // 1 cm = 1/30.48 foot (1 foot = 30.48 cm exact)

    private final double conversionFactor; // 1 unit = conversionFactor feet

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Ee unit lo unna value ni base unit (feet) loki convert chestundi.
     * Example: INCH.convertToBaseUnit(12.0) = 1.0 feet
     */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Base unit (feet) value ni ee unit loki convert chestundi.
     * Example: INCH.convertFromBaseUnit(1.0) = 12.0 inches
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}