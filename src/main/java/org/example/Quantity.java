package org.example;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {
    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double converted = targetUnit.convertFromBaseUnit(toBaseUnit());
        return new Quantity<>(converted, targetUnit);
    }

    private Quantity<U> addInternal(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sumInBase = this.toBaseUnit() + other.toBaseUnit();
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);
        return new Quantity<>(resultValue, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return addInternal(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        return addInternal(other, targetUnit);
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quantity<?> other = (Quantity<?>) obj;
        if (this.unit.getClass() != other.unit.getClass()) return false;
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.getClass(), Math.round(toBaseUnit() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}