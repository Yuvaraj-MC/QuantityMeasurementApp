package org.example;

public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    default boolean supportsArithmetic() {
        return true;
    }

    default void validateOperationSupport(String operation) {
    }

    default String getMeasurementType() {
        return this.getClass().getSimpleName();
    }
}