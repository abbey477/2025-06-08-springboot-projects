package com.mycompany.app7;

import lombok.Builder;

@Builder(toBuilder = true)
record User(String name, int age, String email) {}

public class RecordLombokTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Record with Lombok Builder ===\n");

        // Test 1: Create user with builder
        System.out.println("1. Creating user with builder:");
        User originalUser = User.builder()
                .name("John Doe")
                .age(25)
                .email("john@example.com")
                .build();
        System.out.println("Original: " + originalUser);

        // Test 2: Update user with toBuilder() - single field
        System.out.println("\n2. Updating age using toBuilder():");
        User updatedAge = originalUser.toBuilder()
                .age(26)
                .build();
        System.out.println("Updated age: " + updatedAge);

        // Test 3: Update user with toBuilder() - multiple fields
        System.out.println("\n3. Updating multiple fields using toBuilder():");
        User updatedMultiple = originalUser.toBuilder()
                .name("John Smith")
                .email("johnsmith@example.com")
                .build();
        System.out.println("Updated multiple: " + updatedMultiple);

        // Test 4: Verify immutability
        System.out.println("\n4. Verifying immutability:");
        System.out.println("Original user unchanged: " + originalUser);
        System.out.println("Are they the same object? " + (originalUser == updatedAge));
        System.out.println("Are they equal? " + originalUser.equals(updatedAge));

        // Test 5: Chain multiple updates
        System.out.println("\n5. Chaining multiple updates:");
        User chainedUpdate = originalUser.toBuilder()
                .name("Jane Doe")
                .age(30)
                .email("jane@example.com")
                .build();
        System.out.println("Chained update: " + chainedUpdate);

        // Test 6: Partial builder (not all fields)
        System.out.println("\n6. Creating user with partial builder:");
        User partialUser = User.builder()
                .name("Bob")
                .age(22)
                // email will be null
                .build();
        System.out.println("Partial user: " + partialUser);
    }
}