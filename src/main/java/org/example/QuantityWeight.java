package org.example;

import java.util.Objects;

/**
 * Immutable weight measurement (value + unit).
 * QuantityLength ni mirror chestundi - weight category kosam.
 * Conversion logic ni WeightUnit ki delegate chestundi.
 */
public class QuantityWeight {
    private static final double EPSILON = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
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

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double converted = targetUnit.convertFromBaseUnit(toBaseUnit());
        return new QuantityWeight(converted, targetUnit);
    }

    private QuantityWeight addInternal(QuantityWeight other, WeightUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sumInBase = this.toBaseUnit() + other.toBaseUnit();
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);
        return new QuantityWeight(resultValue, targetUnit);
    }

    // result first operand unit lo
    public QuantityWeight add(QuantityWeight other) {
        return addInternal(other, this.unit);
    }

    // result explicit target unit lo
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        return addInternal(other, targetUnit);
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        // getClass() check valla QuantityLength tho compare cheste false vastundi
        // (weight vs length incompatible - category type safety)
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityWeight other = (QuantityWeight) obj;
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}