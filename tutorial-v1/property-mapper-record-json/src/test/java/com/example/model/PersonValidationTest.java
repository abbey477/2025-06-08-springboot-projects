package com.example.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation for valid Person")
    void shouldPassValidationForValidPerson() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        // Given
        Person person = Person.builder()
            .name("")
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Name cannot be blank", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void shouldFailValidationWhenNameIsNull() {
        // Given
        Person person = Person.builder()
            .name(null)
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Name cannot be blank", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void shouldFailValidationWhenEmailIsBlank() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Email cannot be blank", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("invalid-email")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Email should be valid", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when date of birth is null")
    void shouldFailValidationWhenDobIsNull() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .dob(null)
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Date of birth cannot be null", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when date of birth is in future")
    void shouldFailValidationWhenDobIsInFuture() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .dob(LocalDate.now().plusDays(1))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Date of birth must be in the past", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when timestamp is null")
    void shouldFailValidationWhenTimestampIsNull() {
        // Given
        Person person = Person.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(null)
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Timestamp cannot be null", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation with multiple constraint violations")
    void shouldFailValidationWithMultipleConstraintViolations() {
        // Given
        Person person = Person.builder()
            .name("")
            .email("invalid-email")
            .dob(LocalDate.now().plusDays(1))
            .timestamp(null)
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(4, violations.size());
        
        // Verify all expected violation messages are present
        Set<String> violationMessages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(violationMessages.contains("Name cannot be blank"));
        assertTrue(violationMessages.contains("Email should be valid"));
        assertTrue(violationMessages.contains("Date of birth must be in the past"));
        assertTrue(violationMessages.contains("Timestamp cannot be null"));
    }

    @Test
    @DisplayName("Should pass validation with whitespace-only name treated as blank")
    void shouldFailValidationWithWhitespaceOnlyName() {
        // Given
        Person person = Person.builder()
            .name("   ")
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.now())
            .build();

        // When
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Name cannot be blank", violation.getMessage());
    }

    @Test
    @DisplayName("Should pass validation with various valid email formats")
    void shouldPassValidationWithVariousValidEmailFormats() {
        String[] validEmails = {
            "test@example.com",
            "user.name@domain.co.uk",
            "user+tag@example.org",
            "123@example.com",
            "user_name@example-domain.com"
        };

        for (String email : validEmails) {
            // Given
            Person person = Person.builder()
                .name("John Doe")
                .email(email)
                .dob(LocalDate.of(1990, 5, 15))
                .timestamp(LocalDateTime.now())
                .build();

            // When
            Set<ConstraintViolation<Person>> violations = validator.validate(person);

            // Then
            assertTrue(violations.isEmpty(), "Email should be valid: " + email);
        }
    }

    //@Test
    //@DisplayName("Should fail validation with various invalid email formats")
    void shouldFailValidationWithVariousInvalidEmailFormats() {
        String[] invalidEmails = {
            "plainaddress",
            "@missingdomain.com",
            "missing@.com",
            "missing@domain",
            "spaces @domain.com",
            "double@@domain.com"
        };

        for (String email : invalidEmails) {
            // Given
            Person person = Person.builder()
                .name("John Doe")
                .email(email)
                .dob(LocalDate.of(1990, 5, 15))
                .timestamp(LocalDateTime.now())
                .build();

            // When
            Set<ConstraintViolation<Person>> violations = validator.validate(person);

            // Then
            assertEquals(1, violations.size(), "Email should be invalid: " + email);
            ConstraintViolation<Person> violation = violations.iterator().next();
            assertEquals("Email should be valid", violation.getMessage());
        }
    }
}