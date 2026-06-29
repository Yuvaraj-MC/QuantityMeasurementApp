package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    private IQuantityMeasurementService newService() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        return new QuantityMeasurementServiceImpl(repo);
    }

    private QuantityDTO dto(double v, IMeasurable u) {
        return new QuantityDTO(v, u);
    }

    // ---------- Entity ----------
    @Test
    void testEntity_BinarySuccess() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity("ADD",
                1.0, "FEET", 12.0, "INCH", "2.0 FEET");
        assertFalse(e.hasError());
        assertEquals("ADD", e.getOperationType());
        assertEquals("2.0 FEET", e.getResult());
    }

    @Test
    void testEntity_SingleSuccess() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity("CONVERT", 1.0, "FEET", "12.0 INCH");
        assertFalse(e.hasError());
    }

    @Test
    void testEntity_Error() {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity("ADD", "some error");
        assertTrue(e.hasError());
        assertEquals("some error", e.getErrorMessage());
    }

    // ---------- Service: compare ----------
    @Test
    void testService_Compare_SameValue() {
        assertTrue(newService().compareEquality(dto(1.0, LengthUnit.FEET), dto(1.0, LengthUnit.FEET)));
    }

    @Test
    void testService_Compare_CrossUnit() {
        assertTrue(newService().compareEquality(dto(1.0, LengthUnit.FEET), dto(12.0, LengthUnit.INCH)));
    }

    @Test
    void testService_Compare_CrossCategory_False() {
        assertFalse(newService().compareEquality(dto(1.0, LengthUnit.FEET), dto(1.0, WeightUnit.KILOGRAM)));
    }

    // ---------- Service: convert ----------
    @Test
    void testService_Convert_Success() {
        QuantityDTO r = newService().convert(dto(1.0, LengthUnit.FEET), LengthUnit.INCH);
        assertEquals(12.0, r.getValue(), EPSILON);
        assertEquals(LengthUnit.INCH, r.getUnit());
    }

    @Test
    void testService_Convert_CrossCategory_Throws() {
        assertThrows(QuantityMeasurementException.class,
                () -> newService().convert(dto(1.0, LengthUnit.FEET), WeightUnit.KILOGRAM));
    }

    // ---------- Service: add ----------
    @Test
    void testService_Add_Success() {
        QuantityDTO r = newService().add(dto(1.0, LengthUnit.FEET),
                dto(12.0, LengthUnit.INCH), LengthUnit.FEET);
        assertEquals(2.0, r.getValue(), EPSILON);
    }

    @Test
    void testService_Add_CrossCategory_Throws() {
        assertThrows(QuantityMeasurementException.class,
                () -> newService().add(dto(1.0, LengthUnit.FEET),
                        dto(1.0, WeightUnit.KILOGRAM), LengthUnit.FEET));
    }

    @Test
    void testService_Add_Temperature_Unsupported() {
        QuantityMeasurementException ex = assertThrows(QuantityMeasurementException.class,
                () -> newService().add(dto(100.0, TemperatureUnit.CELSIUS),
                        dto(50.0, TemperatureUnit.CELSIUS), TemperatureUnit.CELSIUS));
        assertTrue(ex.getMessage().contains("Temperature"));
    }

    // ---------- Service: subtract ----------
    @Test
    void testService_Subtract_Success() {
        QuantityDTO r = newService().subtract(dto(10.0, LengthUnit.FEET),
                dto(6.0, LengthUnit.INCH), LengthUnit.FEET);
        assertEquals(9.5, r.getValue(), EPSILON);
    }

    // ---------- Service: divide ----------
    @Test
    void testService_Divide_Success() {
        assertEquals(5.0, newService().divide(dto(10.0, LengthUnit.FEET), dto(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testService_Divide_ByZero_Throws() {
        assertThrows(QuantityMeasurementException.class,
                () -> newService().divide(dto(10.0, LengthUnit.FEET), dto(0.0, LengthUnit.FEET)));
    }

    @Test
    void testService_NullDTO_Throws() {
        assertThrows(QuantityMeasurementException.class,
                () -> newService().compareEquality(null, dto(1.0, LengthUnit.FEET)));
    }

    // ---------- Repository ----------
    @Test
    void testRepository_SaveAndFindAll() {
        IQuantityMeasurementRepository repo = QuantityMeasurementCacheRepository.getInstance();
        int before = repo.findAll().size();
        repo.save(new QuantityMeasurementEntity("TEST", 1.0, "FEET", "result"));
        assertTrue(repo.findAll().size() > before);
    }

    @Test
    void testRepository_Singleton() {
        assertSame(QuantityMeasurementCacheRepository.getInstance(),
                QuantityMeasurementCacheRepository.getInstance());
    }

    // ---------- Controller (real service integration) ----------
    @Test
    void testController_Equality() {
        QuantityMeasurementController c = new QuantityMeasurementController(newService());
        assertTrue(c.performEquality(dto(1.0, LengthUnit.FEET), dto(12.0, LengthUnit.INCH)));
    }

    @Test
    void testController_NullService_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementController(null));
    }

    // ---------- Controller with mock service (layer independence) ----------
    @Test
    void testController_WithMockService() {
        IQuantityMeasurementService mock = new IQuantityMeasurementService() {
            public boolean compareEquality(QuantityDTO a, QuantityDTO b) { return true; }
            public QuantityDTO convert(QuantityDTO dto, IMeasurable t) { return dto; }
            public QuantityDTO add(QuantityDTO a, QuantityDTO b, IMeasurable t) { return a; }
            public QuantityDTO subtract(QuantityDTO a, QuantityDTO b, IMeasurable t) { return a; }
            public double divide(QuantityDTO a, QuantityDTO b) { return 42.0; }
        };
        QuantityMeasurementController c = new QuantityMeasurementController(mock);
        assertEquals(42.0, c.performDivision(dto(1.0, LengthUnit.FEET), dto(1.0, LengthUnit.FEET)), EPSILON);
    }

    // ---------- Backward compatibility (Quantity<U> still works) ----------
    @Test
    void testBackwardCompatibility_QuantityEquality() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCH));
    }

    @Test
    void testBackwardCompatibility_QuantityAddition() {
        Quantity<LengthUnit> r = new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCH));
        assertEquals(2.0, r.getValue(), EPSILON);
    }

    @Test
    void testIMeasurable_GetMeasurementType() {
        assertEquals("LengthUnit", LengthUnit.FEET.getMeasurementType());
        assertEquals("WeightUnit", WeightUnit.KILOGRAM.getMeasurementType());
        assertEquals("TemperatureUnit", TemperatureUnit.CELSIUS.getMeasurementType());
    }
}