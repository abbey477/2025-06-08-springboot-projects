package com.example.brc.processor;

import com.example.brc.model.StationStats;
import java.io.*;
import java.util.*;

/**
 * Main processor for temperature data
 * Reads file line by line and maintains aggregated statistics
 */
public class TemperatureProcessor {

    // This map holds our aggregated results
    // Key: Station name (e.g., "Hamburg")
    // Value: Statistics for that station
    private Map<String, StationStats> stationAggregations;

    public TemperatureProcessor() {
        this.stationAggregations = new HashMap<String, StationStats>();
    }

    /**
     * Process a single line from the input file
     * Format expected: "StationName;Temperature"
     * Example: "Hamburg;12.5"
     */
    public void processLine(String line) {
        // Skip empty lines
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        try {
            // Split the line on semicolon
            String[] parts = line.split(";");
            if (parts.length != 2) {
                System.err.println("Invalid line format: " + line);
                return;
            }

            String stationName = parts[0].trim();
            double temperature = Double.parseDouble(parts[1].trim());

            // Get existing stats for this station, or create new ones
            StationStats stats = stationAggregations.get(stationName);
            if (stats == null) {
                stats = new StationStats();
                stationAggregations.put(stationName, stats);
            }

            // Update the aggregated statistics
            // This is where we "throw away" the individual reading
            // and keep only the summary
            stats.update(temperature);

            // The original line data is now eligible for garbage collection
            // We only keep the aggregated stats in memory

        } catch (NumberFormatException e) {
            System.err.println("Invalid temperature format in line: " + line);
        }
    }

    /**
     * Process file from various sources
     * Handles regular files, resource files, and fallback options
     */
    public boolean processFile(String filePath) {
        System.out.println("Attempting to process file: " + filePath);

        // Try multiple approaches to find and read the file
        BufferedReader reader = null;

        try {
            // Method 1: Try as regular file
            File regularFile = new File(filePath);
            if (regularFile.exists() && regularFile.canRead()) {
                reader = new BufferedReader(new FileReader(regularFile));
                System.out.println("Reading as regular file: " + regularFile.getAbsolutePath());
            }

            // Method 2: Try as resource with leading slash
            if (reader == null) {
                InputStream resourceStream = this.getClass().getResourceAsStream("/" + filePath);
                if (resourceStream != null) {
                    reader = new BufferedReader(new InputStreamReader(resourceStream));
                    System.out.println("Reading as resource: /" + filePath);
                }
            }

            // Method 3: Try as resource without leading slash
            if (reader == null) {
                InputStream resourceStream = this.getClass().getResourceAsStream(filePath);
                if (resourceStream != null) {
                    reader = new BufferedReader(new InputStreamReader(resourceStream));
                    System.out.println("Reading as resource: " + filePath);
                }
            }

            // Method 4: Try with ClassLoader
            if (reader == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream resourceStream = classLoader.getResourceAsStream(filePath);
                if (resourceStream != null) {
                    reader = new BufferedReader(new InputStreamReader(resourceStream));
                    System.out.println("Reading via ClassLoader: " + filePath);
                }
            }

            if (reader == null) {
                System.err.println("Could not find file: " + filePath);
                return false;
            }

            // Process the file
            return processWithReader(reader);

        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing reader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Internal method to process data using a BufferedReader
     */
    private boolean processWithReader(BufferedReader reader) {
        long startTime = System.currentTimeMillis();
        long linesProcessed = 0;

        try {
            String line;

            // Read one line at a time (streaming)
            while ((line = reader.readLine()) != null) {
                processLine(line);
                linesProcessed++;

                // Progress indicator for large files
                if (linesProcessed % 100_000 == 0) {
                    System.out.printf("Processed %,d lines...%n", linesProcessed);
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.printf("Finished processing %,d lines in %,d ms%n",
                    linesProcessed, (endTime - startTime));
            System.out.printf("Found %d unique weather stations%n",
                    stationAggregations.size());
            return true;

        } catch (IOException e) {
            System.err.println("Error reading data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get results sorted by station name (as required by 1BRC)
     */
    public Map<String, StationStats> getResults() {
        // Return a sorted map for consistent output
        return new TreeMap<String, StationStats>(stationAggregations);
    }

    /**
     * Print results in the format required by 1BRC
     * Format: StationName=min/avg/max
     */
    public void printResults() {
        Map<String, StationStats> sortedResults = getResults();

        System.out.println("\n=== RESULTS ===");
        if (sortedResults.isEmpty()) {
            System.out.println("No data processed.");
            return;
        }

        for (Map.Entry<String, StationStats> entry : sortedResults.entrySet()) {
            String stationName = entry.getKey();
            StationStats stats = entry.getValue();

            System.out.printf("%s=%.1f/%.1f/%.1f%n",
                    stationName,
                    stats.getMin(),
                    stats.getAverage(),
                    stats.getMax());
        }
    }

    /**
     * Get memory usage statistics
     */
    public void printMemoryStats() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.printf("\n=== MEMORY USAGE ===\n");
        System.out.printf("Used Memory: %,d bytes (%.2f MB)\n",
                usedMemory, usedMemory / 1024.0 / 1024.0);
        System.out.printf("Stations in memory: %,d\n", stationAggregations.size());
        System.out.printf("Estimated aggregation size: %,d bytes\n",
                stationAggregations.size() * 32); // rough estimate
    }

    /**
     * Clear all processed data (useful for testing multiple files)
     */
    public void reset() {
        stationAggregations.clear();
    }

    /**
     * Get the number of stations processed
     */
    public int getStationCount() {
        return stationAggregations.size();
    }
}
