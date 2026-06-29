package org.example;

public interface IQuantityMeasurementService {
    boolean compareEquality(QuantityDTO a, QuantityDTO b);
    QuantityDTO convert(QuantityDTO dto, IMeasurable targetUnit);
    QuantityDTO add(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit);
    QuantityDTO subtract(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit);
    double divide(QuantityDTO a, QuantityDTO b);
}