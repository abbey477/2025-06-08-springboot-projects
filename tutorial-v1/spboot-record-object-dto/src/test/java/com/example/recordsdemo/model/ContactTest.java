package com.example.recordsdemo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    @DisplayName("Should create Contact using constructor")
    void shouldCreateContactUsingConstructor() {
        // Given
        String type = "EMAIL";
        String value = "john.doe@example.com";

        // When
        Contact contact = new Contact(type, value);

        // Then
        assertAll(
                () -> assertEquals(type, contact.type()),
                () -> assertEquals(value, contact.value())
        );
    }

    @Test
    @DisplayName("Should create Contact using Builder pattern")
    void shouldCreateContactUsingBuilder() {
        // When
        Contact contact = Contact.builder()
                .type("PHONE")
                .value("+1-555-123-4567")
                .build();

        // Then
        assertAll(
                () -> assertEquals("PHONE", contact.type()),
                () -> assertEquals("+1-555-123-4567", contact.value())
        );
    }

    @Test
    @DisplayName("Should update Contact fields using with methods")
    void shouldUpdateContactFieldsUsingWithMethods() {
        // Given
        Contact original = new Contact("EMAIL", "old@example.com");

        // When
        Contact updatedType = original.withType("PHONE");
        Contact updatedValue = original.withValue("new@example.com");

        // Then
        assertAll(
                // Original should remain unchanged
                () -> assertEquals("EMAIL", original.type()),
                () -> assertEquals("old@example.com", original.value()),

                // New instances should have updated values
                () -> assertEquals("PHONE", updatedType.type()),
                () -> assertEquals("old@example.com", updatedType.value()), // Value unchanged

                () -> assertEquals("EMAIL", updatedValue.type()), // Type unchanged
                () -> assertEquals("new@example.com", updatedValue.value())
        );
    }

    @Test
    @DisplayName("Should throw exception for null type")
    void shouldThrowExceptionForNullType() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Contact(null, "john@example.com")
        );
        assertEquals("Contact type cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty type")
    void shouldThrowExceptionForEmptyType() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Contact("   ", "john@example.com")
        );
        assertEquals("Contact type cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null value")
    void shouldThrowExceptionForNullValue() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Contact("EMAIL", null)
        );
        assertEquals("Contact value cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty value")
    void shouldThrowExceptionForEmptyValue() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Contact("EMAIL", "   ")
        );
        assertEquals("Contact value cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should demonstrate record equality")
    void shouldDemonstrateRecordEquality() {
        // Given
        Contact contact1 = new Contact("EMAIL", "john@example.com");
        Contact contact2 = new Contact("EMAIL", "john@example.com");
        Contact contact3 = new Contact("PHONE", "john@example.com");

        // Then
        assertAll(
                () -> assertEquals(contact1, contact2),
                () -> assertNotEquals(contact1, contact3),
                () -> assertEquals(contact1.hashCode(), contact2.hashCode()),
                () -> assertNotEquals(contact1.hashCode(), contact3.hashCode())
        );
    }

    @Test
    @DisplayName("Should demonstrate various contact types")
    void shouldDemonstrateVariousContactTypes() {
        // When
        Contact email = new Contact("EMAIL", "john@example.com");
        Contact phone = new Contact("PHONE", "+1-555-123-4567");
        Contact linkedin = new Contact("LINKEDIN", "https://linkedin.com/in/johndoe");
        Contact github = new Contact("GITHUB", "https://github.com/johndoe");

        // Then
        assertAll(
                () -> assertEquals("EMAIL", email.type()),
                () -> assertEquals("PHONE", phone.type()),
                () -> assertEquals("LINKEDIN", linkedin.type()),
                () -> assertEquals("GITHUB", github.type())
        );
    }
}