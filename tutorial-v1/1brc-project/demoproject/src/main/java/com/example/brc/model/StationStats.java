package com.example.brc.model;

/**
 * Holds aggregated temperature statistics for a weather station
 * This replaces storing millions of individual temperature readings
 */
public class StationStats {
    private double min = Double.MAX_VALUE;  // Start with highest possible value
    private double max = Double.MIN_VALUE;  // Start with lowest possible value
    private double sum = 0.0;               // Running total of all temperatures
    private int count = 0;                  // How many readings we've processed

    /**
     * Updates statistics with a new temperature reading
     * This is where the "aggregation" magic happens
     */
    public void update(double temperature) {
        // Update minimum if this temperature is lower
        if (temperature < min) {
            min = temperature;
        }

        // Update maximum if this temperature is higher
        if (temperature > max) {
            max = temperature;
        }

        // Add to running total
        sum += temperature;

        // Increment count of readings
        count++;
    }

    /**
     * Calculate average temperature for this station
     */
    public double getAverage() {
        if (count == 0) {
            return 0.0;  // Avoid division by zero
        }
        return sum / count;
    }

    // Getters
    public double getMin() {
        return min == Double.MAX_VALUE ? 0.0 : min;
    }

    public double getMax() {
        return max == Double.MIN_VALUE ? 0.0 : max;
    }

    public double getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("Stats{min=%.1f, max=%.1f, avg=%.1f, count=%d}",
                getMin(), getAverage(), getMax(), count);
    }
}
