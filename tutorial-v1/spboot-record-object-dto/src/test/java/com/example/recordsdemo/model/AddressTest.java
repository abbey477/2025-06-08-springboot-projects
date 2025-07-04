package com.example.recordsdemo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    @DisplayName("Should create Address using constructor")
    void shouldCreateAddressUsingConstructor() {
        // Given
        String street = "123 Main St";
        String city = "Houston";
        String state = "TX";
        String zipCode = "77001";
        String country = "USA";

        // When
        Address address = new Address(street, city, state, zipCode, country);

        // Then
        assertAll(
                () -> assertEquals(street, address.street()),
                () -> assertEquals(city, address.city()),
                () -> assertEquals(state, address.state()),
                () -> assertEquals(zipCode, address.zipCode()),
                () -> assertEquals(country, address.country())
        );
    }

    @Test
    @DisplayName("Should create Address using Builder pattern")
    void shouldCreateAddressUsingBuilder() {
        // When
        Address address = Address.builder()
                .street("456 Oak Ave")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .country("USA")
                .build();

        // Then
        assertAll(
                () -> assertEquals("456 Oak Ave", address.street()),
                () -> assertEquals("Dallas", address.city()),
                () -> assertEquals("TX", address.state()),
                () -> assertEquals("75201", address.zipCode()),
                () -> assertEquals("USA", address.country())
        );
    }

    @Test
    @DisplayName("Should use default country in Builder")
    void shouldUseDefaultCountryInBuilder() {
        // When
        Address address = Address.builder()
                .street("789 Pine St")
                .city("Austin")
                .state("TX")
                .zipCode("78701")
                .build();

        // Then
        assertEquals("USA", address.country());
    }

    @Test
    @DisplayName("Should update Address fields using with methods")
    void shouldUpdateAddressFieldsUsingWithMethods() {
        // Given
        Address original = Address.builder()
                .street("123 Main St")
                .city("Houston")
                .state("TX")
                .zipCode("77001")
                .country("USA")
                .build();

        // When
        Address updatedStreet = original.withStreet("456 New St");
        Address updatedCity = original.withCity("San Antonio");
        Address updatedState = original.withState("CA");
        Address updatedZipCode = original.withZipCode("90210");
        Address updatedCountry = original.withCountry("Canada");

        // Then
        assertAll(
                // Original should remain unchanged
                () -> assertEquals("123 Main St", original.street()),
                () -> assertEquals("Houston", original.city()),

                // New instances should have updated values
                () -> assertEquals("456 New St", updatedStreet.street()),
                () -> assertEquals("Houston", updatedStreet.city()), // Other fields unchanged

                () -> assertEquals("San Antonio", updatedCity.city()),
                () -> assertEquals("123 Main St", updatedCity.street()), // Other fields unchanged

                () -> assertEquals("CA", updatedState.state()),
                () -> assertEquals("90210", updatedZipCode.zipCode()),
                () -> assertEquals("Canada", updatedCountry.country())
        );
    }

    @Test
    @DisplayName("Should throw exception for null street")
    void shouldThrowExceptionForNullStreet() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Address(null, "Houston", "TX", "77001", "USA")
        );
        assertEquals("Street cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty street")
    void shouldThrowExceptionForEmptyStreet() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Address("   ", "Houston", "TX", "77001", "USA")
        );
        assertEquals("Street cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null city")
    void shouldThrowExceptionForNullCity() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Address("123 Main St", null, "TX", "77001", "USA")
        );
        assertEquals("City cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null zipCode")
    void shouldThrowExceptionForNullZipCode() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Address("123 Main St", "Houston", "TX", null, "USA")
        );
        assertEquals("ZipCode cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should demonstrate record equality")
    void shouldDemonstrateRecordEquality() {
        // Given
        Address address1 = new Address("123 Main St", "Houston", "TX", "77001", "USA");
        Address address2 = new Address("123 Main St", "Houston", "TX", "77001", "USA");
        Address address3 = new Address("456 Oak Ave", "Houston", "TX", "77001", "USA");

        // Then
        assertAll(
                () -> assertEquals(address1, address2),
                () -> assertNotEquals(address1, address3),
                () -> assertEquals(address1.hashCode(), address2.hashCode()),
                () -> assertNotEquals(address1.hashCode(), address3.hashCode())
        );
    }

    @Test
    @DisplayName("Should demonstrate record toString")
    void shouldDemonstrateRecordToString() {
        // Given
        Address address = new Address("123 Main St", "Houston", "TX", "77001", "USA");

        // When
        String toString = address.toString();

        // Then
        assertTrue(toString.contains("123 Main St"));
        assertTrue(toString.contains("Houston"));
        assertTrue(toString.contains("TX"));
        assertTrue(toString.contains("77001"));
        assertTrue(toString.contains("USA"));
    }
}