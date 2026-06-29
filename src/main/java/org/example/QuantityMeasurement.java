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
     * Supports equality, conversion, and addition (with/without target unit).
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


        private QuantityLength addInternal(QuantityLength other, LengthUnit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Cannot add a null quantity");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double sumInBase = this.toBaseUnit() + other.toBaseUnit();
            double resultValue = sumInBase / targetUnit.getConversionFactor();
            return new QuantityLength(resultValue, targetUnit);
        }

        /**
         * UC6: result first operand (this) unit lo.
         */
        public QuantityLength add(QuantityLength other) {
            return addInternal(other, this.unit);
        }

        /**
         * UC7: result EXPLICIT ga ichina targetUnit lo (OVERLOAD).
         */
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            return addInternal(other, targetUnit);
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

    // ---------- Static add API ----------

    // UC6: result first operand unit lo
    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return a.add(b);
    }

    // UC7: result explicit target unit lo (OVERLOAD)
    public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return a.add(b, targetUnit);
    }

    // ---------- Demonstration ----------
    private static void demonstrateAddition(QuantityLength a, QuantityLength b, LengthUnit target) {
        QuantityLength result = add(a, b, target);
        System.out.println("Input: add(" + a + ", " + b + ", " + target + ")");
        System.out.println("Output: " + result);
        System.out.println();
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET);

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH);

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS);

        demonstrateAddition(new QuantityLength(36.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.YARDS), LengthUnit.FEET);

        demonstrateAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS);

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARDS);

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH);
    }
}