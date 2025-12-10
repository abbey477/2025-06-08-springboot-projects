/**
* SIMPLE 1BRC PROJECT - GETTING STARTED
*
* This is a basic implementation of the 1 Billion Row Challenge.
* It focuses on clarity over performance.
*
* PROJECT STRUCTURE:
* your-project/
* ├── src/
* │   ├── main/
* │   │   ├── java/
* │   │   │   └── com/example/brc/...
* │   │   └── resources/
* │   │       └── data/
* │   │           └── measurements.txt  <-- Put your data file here
* │   └── test/
* └── pom.xml (or build.gradle)
*
* ACCESSING RESOURCE FILES:
*
* 1. PLACE YOUR DATA FILE:
*    - Put measurements.txt in src/main/resources/data/
*    - The program will automatically find it
*
* 2. ALTERNATIVE LOCATIONS:
*    - src/main/resources/measurements.txt (root of resources)
*    - Absolute path: /path/to/your/file.txt
*    - Relative path: ./data/measurements.txt
*
* 3. METHODS TO ACCESS RESOURCES:
*    - getClass().getResource("/data/measurements.txt")  // from resources root
*    - getClass().getResourceAsStream("/data/measurements.txt")  // as stream
*    - ClassLoader.getResource("data/measurements.txt")  // no leading slash
*
* KEY CONCEPTS DEMONSTRATED:
*
* 1. STREAMING PROCESSING:
*    - Read file line by line, never load entire file
*    - Process each line and immediately discard raw data
*    - Only keep aggregated statistics in memory
*
* 2. AGGREGATION:
*    - Transform millions of temperature readings into 4 numbers per station
*    - min, max, sum, count for each weather station
*    - Massive memory reduction: GB of data → KB of aggregations
*
* 3. MEMORY EFFICIENCY:
*    - Raw data: streamed and discarded
*    - Working set: only HashMap<String, StationStats>
*    - Total memory usage: <1MB for typical datasets
*
* HOW TO RUN:
*
* OPTION 1 - With your data file:
* 1. Put measurements.txt in src/main/resources/data/
* 2. Run Main.java - it will automatically find and process your file
*
* OPTION 2 - Generate test data:
* 1. Run Main.java without any data file
* 2. It will generate 100K test lines and process them
*
* OPTION 3 - Command line compilation:
* 1. Compile: javac -d out src/main/java/com/example/brc/*.java src/main/java/com/example/brc/**/*.java
* 2. Run: java -cp out:src/main/resources com.example.brc.Main
*
* IDE SETUP TIPS:
*
* IntelliJ IDEA:
* - Right-click src/main/resources → Mark Directory as → Resources Root
* - Files in resources/ are automatically available on classpath
*
* Eclipse:
* - Resources folder is automatically recognized in Maven/Gradle projects
* - Make sure src/main/resources is in build path
*
* VS Code:
* - Use Java Extension Pack
* - Resources are handled automatically in Maven/Gradle projects
*
* TROUBLESHOOTING:
*
* File not found?
* - Check file is in src/main/resources/data/measurements.txt
* - Verify resource path starts with / for absolute resource path
* - Try different access methods (see TemperatureProcessor class)
*
* NEXT STEPS FOR OPTIMIZATION:
* - Multi-threading (parallel processing of file chunks)
* - Custom number parsing (faster than Double.parseDouble)
* - Memory-mapped files
* - Primitive collections
* - JVM tuning
*
* But start with this simple version to understand the core concepts!
  */

To get started:

Copy the code into your IDE with the proper package structure
Run Main.java - it will generate 100K test lines and process them
Observe how 100K lines become just a few KB of aggregated data

The Magic:

Input: 100,000 lines (~1.5MB of text data)
Output: ~10 stations × 32 bytes = ~320 bytes of aggregations
Memory reduction: 99.98%!

This gives you the foundation to understand how 1 billion rows can be processed 
without running out of memory. Once you understand this version, 
you can add optimizations like multithreading and faster parsing.