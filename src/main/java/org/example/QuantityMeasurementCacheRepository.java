package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static QuantityMeasurementCacheRepository instance;
    private static final String FILE_NAME = "quantity_measurements.dat";

    private final List<QuantityMeasurementEntity> cache;

    private QuantityMeasurementCacheRepository() {
        cache = new ArrayList<>();
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return new ArrayList<>(cache);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {
        File file = new File(FILE_NAME);
        boolean append = file.exists() && file.length() > 0;
        try (ObjectOutputStream oos = append
                ? new AppendableObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file, true))) {
            oos.writeObject(entity);
        } catch (IOException e) {
            // persistence optional - ignore IO failures
        }
    }

    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                cache.add((QuantityMeasurementEntity) ois.readObject());
            }
        } catch (EOFException eof) {
            // end of file - done
        } catch (Exception e) {
            // corrupted/missing - start fresh
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}