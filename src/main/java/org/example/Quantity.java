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

    private void validateSameCategory(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cannot operate across different measurement categories");
        }
    }

    private Quantity<U> addInternal(Quantity<U> other, U targetUnit) {
        validateSameCategory(other);
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sumInBase = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity<>(targetUnit.convertFromBaseUnit(sumInBase), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return addInternal(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        return addInternal(other, targetUnit);
    }

    private Quantity<U> subtractInternal(Quantity<U> other, U targetUnit) {
        validateSameCategory(other);
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double diffInBase = this.toBaseUnit() - other.toBaseUnit();
        return new Quantity<>(targetUnit.convertFromBaseUnit(diffInBase), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtractInternal(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        return subtractInternal(other, targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateSameCategory(other);
        double divisorBase = other.toBaseUnit();
        if (Math.abs(divisorBase) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero quantity");
        }
        return this.toBaseUnit() / divisorBase;
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