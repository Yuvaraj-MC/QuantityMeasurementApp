package org.example;

public class QuantityModel<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> toQuantity() {
        return new Quantity<>(value, unit);
    }
}