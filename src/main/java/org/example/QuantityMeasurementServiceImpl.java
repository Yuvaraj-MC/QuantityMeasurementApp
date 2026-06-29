package org.example;

@SuppressWarnings({"unchecked", "rawtypes"})
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public boolean compareEquality(QuantityDTO a, QuantityDTO b) {
        validateDTO(a);
        validateDTO(b);
        Quantity q1 = new QuantityModel(a.getValue(), a.getUnit()).toQuantity();
        Quantity q2 = new QuantityModel(b.getValue(), b.getUnit()).toQuantity();
        boolean result = q1.equals(q2);
        repository.save(new QuantityMeasurementEntity("COMPARE",
                a.getValue(), a.getUnit().getUnitName(),
                b.getValue(), b.getUnit().getUnitName(), String.valueOf(result)));
        return result;
    }

    @Override
    public QuantityDTO convert(QuantityDTO dto, IMeasurable targetUnit) {
        validateDTO(dto);
        if (targetUnit == null) {
            throw new QuantityMeasurementException("Target unit cannot be null");
        }
        if (dto.getUnit().getClass() != targetUnit.getClass()) {
            throw new QuantityMeasurementException("Cannot convert across different measurement categories");
        }
        try {
            Quantity q = new QuantityModel(dto.getValue(), dto.getUnit()).toQuantity();
            Quantity r = q.convertTo(targetUnit);
            QuantityDTO out = new QuantityDTO(r.getValue(), (IMeasurable) r.getUnit());
            repository.save(new QuantityMeasurementEntity("CONVERT",
                    dto.getValue(), dto.getUnit().getUnitName(), out.toString()));
            return out;
        } catch (RuntimeException e) {
            throw wrap("CONVERT", e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit) {
        return arithmetic("ADD", a, b, targetUnit, true);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO a, QuantityDTO b, IMeasurable targetUnit) {
        return arithmetic("SUBTRACT", a, b, targetUnit, false);
    }

    private QuantityDTO arithmetic(String op, QuantityDTO a, QuantityDTO b,
                                   IMeasurable targetUnit, boolean isAdd) {
        validateDTO(a);
        validateDTO(b);
        if (targetUnit == null) {
            throw new QuantityMeasurementException("Target unit cannot be null");
        }
        if (targetUnit.getClass() != a.getUnit().getClass()) {
            throw new QuantityMeasurementException("Target unit must be same category as operands");
        }
        try {
            Quantity q1 = new QuantityModel(a.getValue(), a.getUnit()).toQuantity();
            Quantity q2 = new QuantityModel(b.getValue(), b.getUnit()).toQuantity();
            Quantity r = isAdd ? q1.add(q2, targetUnit) : q1.subtract(q2, targetUnit);
            QuantityDTO out = new QuantityDTO(r.getValue(), (IMeasurable) r.getUnit());
            repository.save(new QuantityMeasurementEntity(op,
                    a.getValue(), a.getUnit().getUnitName(),
                    b.getValue(), b.getUnit().getUnitName(), out.toString()));
            return out;
        } catch (RuntimeException e) {
            throw wrap(op, e);
        }
    }

    @Override
    public double divide(QuantityDTO a, QuantityDTO b) {
        validateDTO(a);
        validateDTO(b);
        try {
            Quantity q1 = new QuantityModel(a.getValue(), a.getUnit()).toQuantity();
            Quantity q2 = new QuantityModel(b.getValue(), b.getUnit()).toQuantity();
            double result = q1.divide(q2);
            repository.save(new QuantityMeasurementEntity("DIVIDE",
                    a.getValue(), a.getUnit().getUnitName(),
                    b.getValue(), b.getUnit().getUnitName(), String.valueOf(result)));
            return result;
        } catch (RuntimeException e) {
            throw wrap("DIVIDE", e);
        }
    }

    private void validateDTO(QuantityDTO dto) {
        if (dto == null) {
            throw new QuantityMeasurementException("Quantity input cannot be null");
        }
        if (dto.getUnit() == null) {
            throw new QuantityMeasurementException("Unit cannot be null");
        }
    }

    private QuantityMeasurementException wrap(String op, RuntimeException e) {
        repository.save(new QuantityMeasurementEntity(op, e.getMessage()));
        if (e instanceof QuantityMeasurementException) {
            return (QuantityMeasurementException) e;
        }
        return new QuantityMeasurementException(op + " failed: " + e.getMessage(), e);
    }
}