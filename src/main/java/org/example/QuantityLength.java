package org.example;

import java.util.Objects;

/**
 * Immutable length measurement (value + unit).
 * Conversion logic ni LengthUnit ki DELEGATE chestundi.
 * Ee class kevalam comparison, arithmetic meeda focus chestundi.
 */
public class QuantityLength {
    private static final double EPSILON = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    // Ippudu conversion logic ikkada ledu - unit ki delegate chestunnam
    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double converted = targetUnit.convertFromBaseUnit(toBaseUnit());
        return new QuantityLength(converted, targetUnit);
    }

    // Private utility - rendu add methods idi reuse chestayi (DRY)
    private QuantityLength addInternal(QuantityLength other, LengthUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sumInBase = this.toBaseUnit() + other.toBaseUnit();
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);
        return new QuantityLength(resultValue, targetUnit);
    }

    // UC6: result first operand unit lo
    public QuantityLength add(QuantityLength other) {
        return addInternal(other, this.unit);
    }

    // UC7: result explicit target unit lo
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        return addInternal(other, targetUnit);
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityLength other = (QuantityLength) obj;
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