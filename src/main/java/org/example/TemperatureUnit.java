package org.example;

import java.util.function.DoubleUnaryOperator;

public enum TemperatureUnit implements IMeasurable {
    // toCelsius (base loki), fromCelsius (base nundi) - lambda ga
    CELSIUS(c -> c, c -> c),
    FAHRENHEIT(f -> (f - 32) * 5.0 / 9.0, c -> c * 9.0 / 5.0 + 32),
    KELVIN(k -> k - 273.15, c -> c + 273.15);

    private final DoubleUnaryOperator toCelsius;
    private final DoubleUnaryOperator fromCelsius;

    TemperatureUnit(DoubleUnaryOperator toCelsius, DoubleUnaryOperator fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public double getConversionFactor() {
        return 1.0; // temperature ki linear factor lekapovadam valla 1.0
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toCelsius.applyAsDouble(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromCelsius.applyAsDouble(baseValue);
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public boolean supportsArithmetic() {
        return false; // temperature arithmetic support cheyyadu
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                "Temperature does not support " + operation
                        + " operation (only equality and conversion are allowed)");
    }
}