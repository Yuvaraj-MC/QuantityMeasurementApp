package org.example;


class QuantityMeasurementApp {

    // ---------- Static convert API (UC5) ----------
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }

        double base = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(base);
    }

    // ---------- Static add API ----------
    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return a.add(b);
    }

    public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return a.add(b, targetUnit);
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("Input: Quantity(1.0, FEET).convertTo(INCH)");
        System.out.println("Output: " + oneFoot.convertTo(LengthUnit.INCH));
        System.out.println();

        System.out.println("Input: Quantity(1.0, FEET).add(Quantity(12.0, INCH), FEET)");
        System.out.println("Output: " +
                oneFoot.add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET));
        System.out.println();

        System.out.println("Input: Quantity(36.0, INCH).equals(Quantity(1.0, YARDS))");
        System.out.println("Output: " +
                new QuantityLength(36.0, LengthUnit.INCH)
                        .equals(new QuantityLength(1.0, LengthUnit.YARDS)));
        System.out.println();

        System.out.println("Input: LengthUnit.INCH.convertToBaseUnit(12.0)");
        System.out.println("Output: " + LengthUnit.INCH.convertToBaseUnit(12.0));
        System.out.println();

        System.out.println("Input: LengthUnit.CENTIMETERS.convertToBaseUnit(30.48)");
        System.out.println("Output: " + LengthUnit.CENTIMETERS.convertToBaseUnit(30.48));
    }
}