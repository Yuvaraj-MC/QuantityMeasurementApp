package org.example;


class QuantityMeasurementApp {

    // ---------- LENGTH static API (UC5-UC8) ----------
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return target.convertFromBaseUnit(source.convertToBaseUnit(value));
    }

    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null || b == null) throw new IllegalArgumentException("Operands cannot be null");
        return a.add(b);
    }

    public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
        if (a == null || b == null) throw new IllegalArgumentException("Operands cannot be null");
        return a.add(b, targetUnit);
    }

    // ---------- WEIGHT static API (UC9) ----------
    public static double convert(double value, WeightUnit source, WeightUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        return target.convertFromBaseUnit(source.convertToBaseUnit(value));
    }

    public static QuantityWeight add(QuantityWeight a, QuantityWeight b) {
        if (a == null || b == null) throw new IllegalArgumentException("Operands cannot be null");
        return a.add(b);
    }

    public static QuantityWeight add(QuantityWeight a, QuantityWeight b, WeightUnit targetUnit) {
        if (a == null || b == null) throw new IllegalArgumentException("Operands cannot be null");
        return a.add(b, targetUnit);
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        System.out.println("=== LENGTH (UC1-UC8) ===");
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("1 ft == 12 inch : " +
                oneFoot.equals(new QuantityLength(12.0, LengthUnit.INCH)));

        System.out.println("\n=== WEIGHT (UC9) ===");

        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM))");
        System.out.println("Output: " +
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .equals(new QuantityWeight(1000.0, WeightUnit.GRAM)));

        System.out.println("\nInput: Quantity(1.0, KILOGRAM).convertTo(GRAM)");
        System.out.println("Output: " +
                new QuantityWeight(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM));

        System.out.println("\nInput: Quantity(2.0, POUND).convertTo(KILOGRAM)");
        System.out.println("Output: " +
                new QuantityWeight(2.0, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM));

        System.out.println("\nInput: Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM))");
        System.out.println("Output: " +
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM)));

        System.out.println("\nInput: Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM)");
        System.out.println("Output: " +
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM));

        System.out.println("\n=== CATEGORY SAFETY ===");
        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FEET))");
        boolean crossCategory = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityLength(1.0, LengthUnit.FEET));
        System.out.println("Output: " + crossCategory + " (weight != length)");
    }
}