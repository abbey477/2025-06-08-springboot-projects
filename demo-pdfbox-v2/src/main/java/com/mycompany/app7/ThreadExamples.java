package com.mycompany.app7;

public class ThreadExamples {

    public static void main(String[] args) {
        System.out.println("=== Thread Examples Demo ===\n");

        // Example 1: Original one-liner with static method
        System.out.println("1. One-liner with static method:");
        System.out.println("Main thread: " + Thread.currentThread().getName());
        new Thread(() -> doWorkStatic()).start();

        sleep(100); // Small delay to see output order

        // Example 2: Old way with anonymous class
        System.out.println("\n2. Old way with anonymous Runnable:");
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWorkStatic();
            }
        }).start();

        sleep(100);

        // Example 3: Separate Runnable variable
        System.out.println("\n3. Using separate Runnable variable:");
        Runnable task = () -> doWorkStatic();
        new Thread(task).start();

        sleep(100);

        // Example 4: Storing thread first
        System.out.println("\n4. Storing thread before starting:");
        Thread thread = new Thread(() -> doWorkStatic());
        thread.setName("MyCustomThread");
        thread.start();

        sleep(100);

        // Example 5: Instance-based approach
        System.out.println("\n5. Instance-based approach:");
        ThreadExamples example = new ThreadExamples();
        example.runInstanceExample();

        sleep(100);

        // Example 6: Multiple threads
        System.out.println("\n6. Multiple threads:");
        for (int i = 1; i <= 3; i++) {
            final int threadNum = i;
            new Thread(() -> doWorkWithNumber(threadNum)).start();
        }

        // Example 7: Thread with parameters
        System.out.println("\n7. Thread with parameters:");
        String message = "Hello from lambda!";
        new Thread(() -> doWorkWithMessage(message)).start();

        sleep(100);
        System.out.println("\nMain thread continues and will finish...");
    }

    // Static method - can be called from static main()
    private static void doWorkStatic() {
        System.out.println("  [STATIC] Working on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(50); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  [STATIC] Work completed!");
    }

    // Static method with parameter
    private static void doWorkWithNumber(int number) {
        System.out.println("  [THREAD-" + number + "] Starting work on: " + Thread.currentThread().getName());
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  [THREAD-" + number + "] Finished work!");
    }

    // Static method with message
    private static void doWorkWithMessage(String message) {
        System.out.println("  [MESSAGE] " + message + " on thread: " + Thread.currentThread().getName());
    }

    // Instance method example
    public void runInstanceExample() {
        new Thread(() -> doWorkInstance()).start(); // Can call non-static method
    }

    // Non-static method - requires instance
    private void doWorkInstance() {
        System.out.println("  [INSTANCE] Working on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  [INSTANCE] Instance work completed!");
    }

    // Utility method to add small delays for better output readability
    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}