package com.example.brc;

import com.example.brc.processor.TemperatureProcessor;
import com.example.brc.util.TestDataGenerator;

/**
 * Simple main class for the 1 Billion Row Challenge
 * Fixed version that handles file access properly
 */
public class SimpleMain {

    public static void main(String[] args) {
        System.out.println("=== 1 Billion Row Challenge - Simple Version ===\n");

        TemperatureProcessor processor = new TemperatureProcessor();

        // List of possible file locations to try
        String[] possibleFiles = {
                "data/measurements.txt",              // Relative path
                "measurements.txt",                   // Current directory
                "src/main/resources/data/measurements.txt", // Maven structure
                "resources/data/measurements.txt"     // Alternative structure
        };

        boolean fileProcessed = false;

        // Try to find and process existing data file
        for (String filePath : possibleFiles) {
            System.out.println("Looking for file: " + filePath);
            if (processor.processFile(filePath)) {
                fileProcessed = true;
                break;
            }
        }

        // If no file found, generate test data
        if (!fileProcessed) {
            System.out.println("\nNo existing data file found. Generating test data...");
            String testFile = "test_measurements.txt";
            int testLines = 50_000; // Smaller for quick testing

            TestDataGenerator.generateTestData(testFile, testLines);

            // Process the generated test file
            processor.reset(); // Clear any partial data
            if (processor.processFile(testFile)) {
                System.out.println("Successfully processed generated test data.");
            } else {
                System.err.println("Failed to process generated test data.");
                return;
            }
        }

        // Show results
        processor.printResults();
        processor.printMemoryStats();

        // Explain what happened
        System.out.println("\n=== EXPLANATION ===");
        System.out.println("The program processed temperature data line by line,");
        System.out.printf("keeping only aggregated statistics for %d stations in memory.\n",
                processor.getStationCount());
        System.out.println("This approach allows processing billions of rows without running out of memory!");
    }
}
