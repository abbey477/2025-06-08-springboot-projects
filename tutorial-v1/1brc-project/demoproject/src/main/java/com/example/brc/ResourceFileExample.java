package com.example.brc;

import com.example.brc.processor.TemperatureProcessor;
import com.example.brc.util.TestDataGenerator;
import java.io.File;

/**
 * Example showing different ways to access files for the 1BRC challenge
 * This version creates test data in the resources directory structure
 */
public class ResourceFileExample {

    public static void main(String[] args) {
        System.out.println("=== Resource File Example ===\n");

        // Step 1: Create resources directory structure and generate test data
        createResourcesAndTestData();

        // Step 2: Try processing from different locations
        testDifferentFileAccessMethods();
    }

    private static void createResourcesAndTestData() {
        System.out.println("1. Creating test data in resources structure...");

        // Create directories
        File resourcesDir = new File("src/main/resources");
        File dataDir = new File("src/main/resources/data");

        resourcesDir.mkdirs();
        dataDir.mkdirs();

        // Generate test data
        String resourceFile = "src/main/resources/data/measurements.txt";
        TestDataGenerator.generateTestData(resourceFile, 25_000);
        System.out.println("Created: " + resourceFile + "\n");
    }

    private static void testDifferentFileAccessMethods() {
        System.out.println("2. Testing different file access methods...\n");

        TemperatureProcessor processor = new TemperatureProcessor();

        // Method 1: Direct file path
        System.out.println("--- Method 1: Direct file path ---");
        if (processor.processFile("src/main/resources/data/measurements.txt")) {
            processor.printResults();
        }

        processor.reset();

        // Method 2: Relative path (if file exists)
        System.out.println("\n--- Method 2: Simple filename ---");
        if (processor.processFile("measurements.txt")) {
            processor.printResults();
        } else {
            System.out.println("File not found with simple name - this is expected.");
        }

        processor.reset();

        // Method 3: Show memory usage
        processor.processFile("src/main/resources/data/measurements.txt");
        System.out.println("\n--- Memory Usage Analysis ---");
        processor.printMemoryStats();
    }
}