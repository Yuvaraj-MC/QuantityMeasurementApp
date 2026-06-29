package org.example;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operationType;
    private double value1;
    private String unit1;
    private Double value2;
    private String unit2;
    private String result;
    private boolean error;
    private String errorMessage;

    // binary operation success
    public QuantityMeasurementEntity(String operationType, double value1, String unit1,
                                     double value2, String unit2, String result) {
        this.operationType = operationType;
        this.value1 = value1;
        this.unit1 = unit1;
        this.value2 = value2;
        this.unit2 = unit2;
        this.result = result;
        this.error = false;
    }

    // single operand success (conversion)
    public QuantityMeasurementEntity(String operationType, double value1, String unit1, String result) {
        this.operationType = operationType;
        this.value1 = value1;
        this.unit1 = unit1;
        this.result = result;
        this.error = false;
    }

    // error
    public QuantityMeasurementEntity(String operationType, String errorMessage) {
        this.operationType = operationType;
        this.errorMessage = errorMessage;
        this.error = true;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getResult() {
        return result;
    }

    public boolean hasError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (error) {
            return "[" + operationType + "] ERROR: " + errorMessage;
        }
        String operands = value2 == null
                ? value1 + " " + unit1
                : value1 + " " + unit1 + ", " + value2 + " " + unit2;
        return "[" + operationType + "] " + operands + " => " + result;
    }
}