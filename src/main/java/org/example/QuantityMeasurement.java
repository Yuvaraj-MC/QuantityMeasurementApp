package org.example;

class QuantityMeasurementApp {

    public static void main(String[] args) {
        // Wiring: Repository -> Service -> Controller (dependency injection)
        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        System.out.println("=== EQUALITY ===");
        controller.performEquality(new QuantityDTO(1.0, LengthUnit.FEET),
                new QuantityDTO(12.0, LengthUnit.INCH));

        System.out.println("\n=== CONVERSION ===");
        controller.performConversion(new QuantityDTO(1.0, LengthUnit.FEET), LengthUnit.INCH);

        System.out.println("\n=== ADDITION ===");
        controller.performAddition(new QuantityDTO(1.0, WeightUnit.KILOGRAM),
                new QuantityDTO(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM);

        System.out.println("\n=== DIVISION ===");
        controller.performDivision(new QuantityDTO(10.0, LengthUnit.FEET),
                new QuantityDTO(2.0, LengthUnit.FEET));

        System.out.println("\n=== TEMPERATURE (unsupported arithmetic) ===");
        controller.performAddition(new QuantityDTO(100.0, TemperatureUnit.CELSIUS),
                new QuantityDTO(50.0, TemperatureUnit.CELSIUS), TemperatureUnit.CELSIUS);

        System.out.println("\n=== CROSS-CATEGORY ===");
        controller.performEquality(new QuantityDTO(1.0, LengthUnit.FEET),
                new QuantityDTO(1.0, WeightUnit.KILOGRAM));

        System.out.println("\n=== REPOSITORY HISTORY ===");
        System.out.println("Total records: " + repository.findAll().size());
    }
}