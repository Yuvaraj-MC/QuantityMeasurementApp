package org.example;

public class QuantityDTO {
    private double value;
    private IMeasurable unit;

    public QuantityDTO() {
    }

    public QuantityDTO(double value, IMeasurable unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public IMeasurable getUnit() {
        return unit;
    }

    public void setUnit(IMeasurable unit) {
        this.unit = unit;
    }

    public String getMeasurementType() {
        return unit == null ? null : unit.getMeasurementType();
    }

    @Override
    public String toString() {
        return value + " " + (unit == null ? "null" : unit.getUnitName());
    }
}