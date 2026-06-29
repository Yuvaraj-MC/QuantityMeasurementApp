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

    public static void main(String[] args) {
        System.out.println("=== TEMPERATURE EQUALITY ===");
        demonstrateEquality(new Quantity<>(0.0, TemperatureUnit.CELSIUS),
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
        demonstrateEquality(new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT));
        demonstrateEquality(new Quantity<>(273.15, TemperatureUnit.KELVIN),
                new Quantity<>(0.0, TemperatureUnit.CELSIUS));
        demonstrateEquality(new Quantity<>(-40.0, TemperatureUnit.CELSIUS),
                new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT));

        System.out.println("=== TEMPERATURE CONVERSION ===");
        demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT);
        demonstrateConversion(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT), TemperatureUnit.CELSIUS);
        demonstrateConversion(new Quantity<>(0.0, TemperatureUnit.CELSIUS), TemperatureUnit.KELVIN);

        System.out.println("=== UNSUPPORTED OPERATIONS ===");
        try {
            new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                    .add(new Quantity<>(50.0, TemperatureUnit.CELSIUS));
        } catch (UnsupportedOperationException e) {
            System.out.println("ADD -> " + e.getMessage());
        }
        try {
            new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                    .divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS));
        } catch (UnsupportedOperationException e) {
            System.out.println("DIVIDE -> " + e.getMessage());
        }

        System.out.println("\n=== CROSS-CATEGORY PREVENTION ===");
        System.out.println("100 CELSIUS == 100 FEET : " +
                new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                        .equals(new Quantity<>(100.0, LengthUnit.FEET)));

        System.out.println("\n=== OTHER CATEGORIES STILL DO ARITHMETIC ===");
        System.out.println("1 ft + 12 inch = " +
                new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCH)));
    }
}