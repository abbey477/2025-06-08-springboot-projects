package com.example.brc.util;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Utility to generate test data for the 1BRC challenge
 * Creates a file with random temperature readings
 */
public class TestDataGenerator {

    private static final String[] STATION_NAMES = {
            "Hamburg", "Berlin", "Munich", "Frankfurt", "Stuttgart",
            "Düsseldorf", "Dortmund", "Essen", "Leipzig", "Bremen",
            "Cologne", "Dresden", "Hannover", "Nuremberg", "Duisburg"
    };

    private static final Random random = new Random();

    /**
     * Generate test data file
     * @param filename Output file name
     * @param numberOfLines How many lines to generate
     */
    public static void generateTestData(String filename, int numberOfLines) {
        System.out.printf("Generating %,d lines of test data...%n", numberOfLines);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < numberOfLines; i++) {
                // Pick random station
                String station = STATION_NAMES[random.nextInt(STATION_NAMES.length)];

                // Generate random temperature between -30 and 50 degrees
                double temperature = -30.0 + (random.nextDouble() * 80.0);

                // Write line in format: StationName;Temperature
                writer.printf("%s;%.1f%n", station, temperature);

                // Progress indicator
                if ((i + 1) % 50_000 == 0) {
                    System.out.printf("Generated %,d lines...%n", i + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error generating test data: " + e.getMessage());
        }

        System.out.println("Test data generation completed: " + filename);
    }

    /**
     * Generate test data directly to resources directory if it exists
     */
    public static void generateResourceTestData(int numberOfLines) {
        // Try to create in resources directory structure
        String[] possiblePaths = {
                "src/main/resources/data/measurements.txt",
                "resources/data/measurements.txt",
                "data/measurements.txt",
                "measurements.txt"
        };

        for (String path : possiblePaths) {
            try {
                // Create directories if they don't exist
                java.io.File file = new java.io.File(path);
                file.getParentFile().mkdirs();

                generateTestData(path, numberOfLines);
                System.out.println("Test data saved to: " + path);
                return;
            } catch (Exception e) {
                // Try next path
            }
        }

        // Fallback to current directory
        generateTestData("measurements.txt", numberOfLines);
    }
}