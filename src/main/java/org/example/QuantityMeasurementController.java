package org.example;

public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    public boolean performEquality(QuantityDTO a, QuantityDTO b) {
        boolean result = service.compareEquality(a, b);
        System.out.println(a + " equals " + b + " -> " + result);
        return result;
    }

    public QuantityDTO performConversion(QuantityDTO dto, IMeasurable targetUnit) {
        try {
            QuantityDTO result = service.convert(dto, targetUnit);
            System.out.println(dto + " convertTo " + targetUnit.getUnitName() + " -> " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public QuantityDTO performAddition(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit) {
        try {
            QuantityDTO result = service.add(a, b, targetUnit);
            System.out.println(a + " + " + b + " -> " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public QuantityDTO performSubtraction(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit) {
        try {
            QuantityDTO result = service.subtract(a, b, targetUnit);
            System.out.println(a + " - " + b + " -> " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Double performDivision(QuantityDTO a, QuantityDTO b) {
        try {
            double result = service.divide(a, b);
            System.out.println(a + " / " + b + " -> " + result);
            return result;
        } catch (QuantityMeasurementException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}