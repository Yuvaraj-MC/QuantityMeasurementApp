package org.example;

import java.util.Objects;

class QuantityMeasurementApp {

    // ---------- Step 1: LengthUnit Enum ----------
    enum LengthUnit {
        FEET(1.0),       // 1 foot = 1 foot (base)
        INCH(1.0 / 12);  // 1 inch = 1/12 foot

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // ---------- Step 2: Generic QuantityLength class ----------
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // value ni base unit (feet) loki convert chestundi
        private double toBaseUnit() {
            return value * unit.getConversionFactor();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;                              // reflexive
            if (obj == null || getClass() != obj.getClass()) return false; // null + type safety
            QuantityLength other = (QuantityLength) obj;
            // Rendu values ni base unit loki convert chesi compare cheyyi
            return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(toBaseUnit());
        }
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        QuantityLength oneFeet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCH);
        System.out.println("Input: Quantity(1.0, \"feet\") and Quantity(12.0, \"inches\")");
        System.out.println("Output: Equal (" + oneFeet.equals(twelveInches) + ")");

        QuantityLength oneInch1 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength oneInch2 = new QuantityLength(1.0, LengthUnit.INCH);
        System.out.println("Input: Quantity(1.0, \"inch\") and Quantity(1.0, \"inch\")");
        System.out.println("Output: Equal (" + oneInch1.equals(oneInch2) + ")");
    }
}