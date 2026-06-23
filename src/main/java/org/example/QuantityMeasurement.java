package org.example;

import java.util.Objects;

class QuantityMeasurementApp {

    // ---------- LengthUnit Enum (base unit = FEET) ----------
    enum LengthUnit {
        FEET(1.0),                  // 1 foot = 1 foot (base)
        INCH(1.0 / 12),             // 1 inch = 1/12 foot
        YARDS(3.0),                 // 1 yard = 3 feet
        CENTIMETERS(0.393701 / 12); // 1 cm = 0.393701 inch = 0.393701/12 feet

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }


    static class QuantityLength {
        private static final double EPSILON = 1e-6;

        private final double value;
        private final LengthUnit unit;


        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toBaseUnit() {
            return value * unit.getConversionFactor();
        }


        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double convertedValue = toBaseUnit() / targetUnit.getConversionFactor();
            return new QuantityLength(convertedValue, targetUnit);
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityLength other = (QuantityLength) obj;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.round(toBaseUnit() / EPSILON));
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }


    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return value * (source.getConversionFactor() / target.getConversionFactor());
    }

    // ---------- Overloaded demonstration methods ----------

    // Method 1: raw value + from/to units
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = convert(value, from, to);
        System.out.println("Input: convert(" + value + ", " + from + ", " + to + ") → Output: " + result);
    }

    // Method 2: existing QuantityLength object + target unit (OVERLOAD - same name, different params)
    public static void demonstrateLengthConversion(QuantityLength quantity, LengthUnit to) {
        QuantityLength converted = quantity.convertTo(to);
        System.out.println("Input: convert(" + quantity + ", " + to + ") → Output: " + converted.getValue());
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCH);

        // Overloaded version - QuantityLength object tho
        QuantityLength lengthInYards = new QuantityLength(2.0, LengthUnit.YARDS);
        demonstrateLengthConversion(lengthInYards, LengthUnit.INCH);
    }
}