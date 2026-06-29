package org.example;

import java.util.List;

public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> findAll();
    void clear();
}