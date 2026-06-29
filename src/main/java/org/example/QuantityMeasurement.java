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

    public static void main(String[] args) {
        System.out.println("=== LENGTH (UC1-UC8) ===");
        demonstrateEquality(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH));
        demonstrateConversion(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCH);
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH), LengthUnit.FEET);

        System.out.println("=== WEIGHT (UC9) ===");
        demonstrateEquality(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM));
        demonstrateConversion(new Quantity<>(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        demonstrateAddition(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM);

        System.out.println("=== CROSS-CATEGORY PREVENTION ===");
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> oneKg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        System.out.println("Input: Quantity(1.0, FEET).equals(Quantity(1.0, KILOGRAM))");
        System.out.println("Output: " + oneFoot.equals(oneKg));
    }
}