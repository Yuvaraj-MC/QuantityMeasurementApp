package org.example;

import java.util.Objects;

 class QuantityMeasurementApp {

    // Inner class - oka feet measurement ni represent chestundi
    static class Feet {
        private final double value;   // final => immutable

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // 1. Same reference aithe true (reflexive)
            if (this == obj) return true;

            // 2. null aithe kani, different type aithe kani false
            if (obj == null || getClass() != obj.getClass()) return false;

            // 3. Safe ga cast cheyyi
            Feet other = (Feet) obj;

            // 4. double values ni Double.compare tho compare cheyyi (== vaddu)
            return Double.compare(value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static void main(String[] args) {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + f1.equals(f2) + ")");
    }
}