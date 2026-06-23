package org.example;

import java.util.Objects;

class QuantityMeasurementApp {

    // ---------- LengthUnit Enum (base unit = FEET) ----------
    enum LengthUnit {
        FEET(1.0),               // 1 foot = 1 foot (base)
        INCH(1.0 / 12),          // 1 inch = 1/12 foot
        YARDS(3.0),              // 1 yard = 3 feet
        CENTIMETERS(0.393701 / 12); // 1 cm = 0.393701 inch = 0.393701/12 feet

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // ---------- Generic QuantityLength class ----------
    static class QuantityLength {

        private static final double EPSILON = 1e-9;

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toBaseUnit() {
            return value * unit.getConversionFactor();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;                              // reflexive
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityLength other = (QuantityLength) obj;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.round(toBaseUnit() / EPSILON));
        }
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        printResult("Quantity(1.0, YARDS) and Quantity(3.0, FEET)",
                new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));

        printResult("Quantity(1.0, YARDS) and Quantity(36.0, INCHES)",
                new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(36.0, LengthUnit.INCH));

        printResult("Quantity(2.0, YARDS) and Quantity(2.0, YARDS)",
                new QuantityLength(2.0, LengthUnit.YARDS),
                new QuantityLength(2.0, LengthUnit.YARDS));

        printResult("Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS)",
                new QuantityLength(2.0, LengthUnit.CENTIMETERS),
                new QuantityLength(2.0, LengthUnit.CENTIMETERS));

        printResult("Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCHES)",
                new QuantityLength(1.0, LengthUnit.CENTIMETERS),
                new QuantityLength(0.393701, LengthUnit.INCH));
    }

    private static void printResult(String label, QuantityLength a, QuantityLength b) {
        System.out.println("Input: " + label);
        System.out.println("Output: Equal (" + a.equals(b) + ")");
        System.out.println();
    }
}