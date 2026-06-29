package org.example;

class QuantityMeasurementApp {

    public static <U extends IMeasurable> void demonstrateEquality(Quantity<U> a, Quantity<U> b) {
        System.out.println("Input: " + a + ".equals(" + b + ")");
        System.out.println("Output: " + a.equals(b));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateConversion(Quantity<U> q, U target) {
        System.out.println("Input: " + q + ".convertTo(" + target.getUnitName() + ")");
        System.out.println("Output: " + q.convertTo(target));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateAddition(Quantity<U> a, Quantity<U> b, U target) {
        System.out.println("Input: " + a + ".add(" + b + ", " + target.getUnitName() + ")");
        System.out.println("Output: " + a.add(b, target));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateSubtraction(Quantity<U> a, Quantity<U> b) {
        System.out.println("Input: " + a + ".subtract(" + b + ")");
        System.out.println("Output: " + a.subtract(b));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateSubtraction(Quantity<U> a, Quantity<U> b, U target) {
        System.out.println("Input: " + a + ".subtract(" + b + ", " + target.getUnitName() + ")");
        System.out.println("Output: " + a.subtract(b, target));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateDivision(Quantity<U> a, Quantity<U> b) {
        System.out.println("Input: " + a + ".divide(" + b + ")");
        System.out.println("Output: " + a.divide(b));
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== SUBTRACTION (Implicit Target) ===");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(6.0, LengthUnit.INCH));
        demonstrateSubtraction(new Quantity<>(10.0, WeightUnit.KILOGRAM),
                new Quantity<>(5000.0, WeightUnit.GRAM));
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE),
                new Quantity<>(500.0, VolumeUnit.MILLILITRE));

        System.out.println("=== SUBTRACTION (Explicit Target) ===");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE),
                new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);

        System.out.println("=== SUBTRACTION (Negative / Zero) ===");
        demonstrateSubtraction(new Quantity<>(5.0, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET));
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(120.0, LengthUnit.INCH));

        System.out.println("=== DIVISION ===");
        demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(24.0, LengthUnit.INCH),
                new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(10.0, WeightUnit.KILOGRAM),
                new Quantity<>(5.0, WeightUnit.KILOGRAM));
        demonstrateDivision(new Quantity<>(5.0, VolumeUnit.LITRE),
                new Quantity<>(10.0, VolumeUnit.LITRE));

        System.out.println("=== ERROR CASES ===");
        try {
            new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET));
        } catch (ArithmeticException e) {
            System.out.println("Divide by zero -> " + e.getMessage());
        }
        try {
            new Quantity<>(10.0, LengthUnit.FEET).subtract(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Null operand -> " + e.getMessage());
        }
    }
}