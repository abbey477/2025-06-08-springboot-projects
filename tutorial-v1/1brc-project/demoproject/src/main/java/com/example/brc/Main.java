package com.example.brc;

import com.example.brc.processor.TemperatureProcessor;
import com.example.brc.util.TestDataGenerator;

/**
 * Main class for the 1 Billion Row Challenge
 * Simple, non-optimized version for learning
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== 1 Billion Row Challenge - Simple Version ===\n");

        // For testing, generate small sample data first
        String testFile = "test_data.txt";
        int testLines = 100_000; // Start small for testing

        System.out.println("1. Generating test data...");
        TestDataGenerator.generateTestData(testFile, testLines);

        System.out.println("\n2. Processing the data...");

        // Create processor and process the file
        TemperatureProcessor processor = new TemperatureProcessor();
        processor.processFile(testFile);

        System.out.println("\n3. Showing results...");
        processor.printResults();

        System.out.println("\n4. Memory usage...");
        processor.printMemoryStats();

        System.out.println("\n=== EXPLANATION ===");
        System.out.println("Notice how we processed " + testLines + " lines,");
        System.out.println("but only keep a few KB of aggregated data in memory!");
        System.out.println("This is the key to handling 1 billion rows.");
    }
}