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

    /**
     * Immutable length measurement with a value and unit.
     * Supports equality (cross-unit), conversion, and addition.
     */
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


        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Cannot add a null quantity");
            }
            double sumInBase = this.toBaseUnit() + other.toBaseUnit();
            double resultValue = sumInBase / this.unit.getConversionFactor();
            return new QuantityLength(resultValue, this.unit);
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
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // ---------- Static convert API (UC5) ----------
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return value * (source.getConversionFactor() / target.getConversionFactor());
    }


    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return a.add(b);
    }

    // Overload 2: raw values + units, result targetUnit lo
    public static QuantityLength add(double v1, LengthUnit u1, double v2, LengthUnit u2, LengthUnit targetUnit) {
        QuantityLength q1 = new QuantityLength(v1, u1);
        QuantityLength q2 = new QuantityLength(v2, u2);
        return q1.add(q2).convertTo(targetUnit);
    }

    // ---------- Demonstration ----------
    private static void demonstrateAddition(QuantityLength a, QuantityLength b) {
        QuantityLength result = add(a, b);
        System.out.println("Input: add(" + a + ", " + b + ")");
        System.out.println("Output: " + result);
        System.out.println();
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(2.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH));

        demonstrateAddition(new QuantityLength(12.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(36.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.YARDS));

        demonstrateAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCH));

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCH));

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET));
    }
}