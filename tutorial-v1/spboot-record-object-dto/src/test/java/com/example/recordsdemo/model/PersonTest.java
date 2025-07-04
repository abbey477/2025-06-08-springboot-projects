package com.example.recordsdemo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

class PersonTest {

    private Address testAddress;
    private List<Contact> testContacts;
    private List<String> testHobbies;

    @BeforeEach
    void setUp() {
        testAddress = Address.builder()
                .street("123 Main St")
                .city("Houston")
                .state("TX")
                .zipCode("77001")
                .country("USA")
                .build();

        testContacts = List.of(
                new Contact("EMAIL", "john.doe@example.com"),
                new Contact("PHONE", "+1-555-123-4567")
        );

        testHobbies = List.of("Reading", "Swimming", "Coding");
    }

    @Test
    @DisplayName("Should create Person using constructor")
    void shouldCreatePersonUsingConstructor() {
        // Given
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 5, 15);

        // When
        Person person = new Person(id, firstName, lastName, birthDate, testAddress, testContacts, testHobbies);

        // Then
        assertAll(
                () -> assertEquals(id, person.id()),
                () -> assertEquals(firstName, person.firstName()),
                () -> assertEquals(lastName, person.lastName()),
                () -> assertEquals(birthDate, person.birthDate()),
                () -> assertEquals(testAddress, person.address()),
                () -> assertEquals(testContacts, person.contacts()),
                () -> assertEquals(testHobbies, person.hobbies())
        );
    }

    @Test
    @DisplayName("Should create Person using Builder pattern")
    void shouldCreatePersonUsingBuilder() {
        // When
        Person person = Person.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // Then
        assertAll(
                () -> assertEquals(2L, person.id()),
                () -> assertEquals("Jane", person.firstName()),
                () -> assertEquals("Smith", person.lastName()),
                () -> assertEquals(LocalDate.of(1985, 8, 20), person.birthDate()),
                () -> assertEquals(testAddress, person.address()),
                () -> assertEquals(testContacts, person.contacts()),
                () -> assertEquals(testHobbies, person.hobbies())
        );
    }

    @Test
    @DisplayName("Should create Person using Builder with individual additions")
    void shouldCreatePersonUsingBuilderWithIndividualAdditions() {
        // When
        Person person = Person.builder()
                .id(3L)
                .firstName("Bob")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1992, 12, 10))
                .address(testAddress)
                .addContact(new Contact("EMAIL", "bob@example.com"))
                .addContact(new Contact("PHONE", "+1-555-987-6543"))
                .addHobby("Photography")
                .addHobby("Hiking")
                .addHobby("Cooking")
                .build();

        // Then
        assertAll(
                () -> assertEquals(3L, person.id()),
                () -> assertEquals("Bob", person.firstName()),
                () -> assertEquals("Johnson", person.lastName()),
                () -> assertEquals(2, person.contacts().size()),
                () -> assertEquals(3, person.hobbies().size()),
                () -> assertTrue(person.hobbies().contains("Photography")),
                () -> assertTrue(person.hobbies().contains("Hiking")),
                () -> assertTrue(person.hobbies().contains("Cooking"))
        );
    }

    @Test
    @DisplayName("Should update Person fields using with methods")
    void shouldUpdatePersonFieldsUsingWithMethods() {
        // Given
        Person original = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Address newAddress = Address.builder()
                .street("456 Oak Ave")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .build();

        // When
        Person updatedId = original.withId(100L);
        Person updatedFirstName = original.withFirstName("Jonathan");
        Person updatedLastName = original.withLastName("Smith");
        Person updatedBirthDate = original.withBirthDate(LocalDate.of(1995, 1, 1));
        Person updatedAddress = original.withAddress(newAddress);

        // Then
        assertAll(
                // Original should remain unchanged
                () -> assertEquals(1L, original.id()),
                () -> assertEquals("John", original.firstName()),
                () -> assertEquals("Doe", original.lastName()),

                // Updated instances should have new values
                () -> assertEquals(100L, updatedId.id()),
                () -> assertEquals("John", updatedId.firstName()), // Other fields unchanged

                () -> assertEquals("Jonathan", updatedFirstName.firstName()),
                () -> assertEquals(1L, updatedFirstName.id()), // Other fields unchanged

                () -> assertEquals("Smith", updatedLastName.lastName()),
                () -> assertEquals(LocalDate.of(1995, 1, 1), updatedBirthDate.birthDate()),
                () -> assertEquals(newAddress, updatedAddress.address())
        );
    }

    @Test
    @DisplayName("Should add contacts and hobbies immutably")
    void shouldAddContactsAndHobbiesImmutably() {
        // Given
        Person original = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(new Contact("EMAIL", "john@example.com")))
                .hobbies(List.of("Reading"))
                .build();

        Contact newContact = new Contact("LINKEDIN", "https://linkedin.com/in/johndoe");
        String newHobby = "Gaming";

        // When
        Person withNewContact = original.addContact(newContact);
        Person withNewHobby = original.addHobby(newHobby);

        // Then
        assertAll(
                // Original should remain unchanged
                () -> assertEquals(1, original.contacts().size()),
                () -> assertEquals(1, original.hobbies().size()),

                // New instances should have additional items
                () -> assertEquals(2, withNewContact.contacts().size()),
                () -> assertTrue(withNewContact.contacts().contains(newContact)),
                () -> assertEquals(1, withNewContact.hobbies().size()), // Hobbies unchanged

                () -> assertEquals(2, withNewHobby.hobbies().size()),
                () -> assertTrue(withNewHobby.hobbies().contains(newHobby)),
                () -> assertEquals(1, withNewHobby.contacts().size()) // Contacts unchanged
        );
    }

    @Test
    @DisplayName("Should demonstrate defensive copying of collections")
    void shouldDemonstrateDefensiveCopyingOfCollections() {
        // Given
        List<Contact> mutableContacts = new ArrayList<>();
        mutableContacts.add(new Contact("EMAIL", "test@example.com"));

        List<String> mutableHobbies = new ArrayList<>();
        mutableHobbies.add("Reading");

        // When
        Person person = new Person(1L, "John", "Doe", LocalDate.of(1990, 5, 15),
                testAddress, mutableContacts, mutableHobbies);

        // Modify original lists
        mutableContacts.add(new Contact("PHONE", "123-456-7890"));
        mutableHobbies.add("Writing");

        // Then
        assertAll(
                () -> assertEquals(1, person.contacts().size()), // Person's list unchanged
                () -> assertEquals(1, person.hobbies().size()), // Person's list unchanged
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> person.contacts().add(new Contact("TEST", "test"))), // Immutable
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> person.hobbies().add("NewHobby")) // Immutable
        );
    }

    @Test
    @DisplayName("Should calculate full name correctly")
    void shouldCalculateFullNameCorrectly() {
        // Given
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        String fullName = person.fullName();

        // Then
        assertEquals("John Doe", fullName);
    }

    @Test
    @DisplayName("Should calculate age correctly")
    void shouldCalculateAgeCorrectly() {
        // Given
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(birthDate)
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        int age = person.age();

        // Then
        assertEquals(25, age);
    }

    @Test
    @DisplayName("Should check contact type correctly")
    void shouldCheckContactTypeCorrectly() {
        // Given
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // Then
        assertAll(
                () -> assertTrue(person.hasContactType("EMAIL")),
                () -> assertTrue(person.hasContactType("PHONE")),
                () -> assertTrue(person.hasContactType("email")), // Case insensitive
                () -> assertFalse(person.hasContactType("LINKEDIN"))
        );
    }

    @Test
    @DisplayName("Should throw exception for null firstName")
    void shouldThrowExceptionForNullFirstName() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(1L, null, "Doe", LocalDate.of(1990, 5, 15),
                        testAddress, testContacts, testHobbies)
        );
        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty firstName")
    void shouldThrowExceptionForEmptyFirstName() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(1L, "   ", "Doe", LocalDate.of(1990, 5, 15),
                        testAddress, testContacts, testHobbies)
        );
        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null lastName")
    void shouldThrowExceptionForNullLastName() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(1L, "John", null, LocalDate.of(1990, 5, 15),
                        testAddress, testContacts, testHobbies)
        );
        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null birthDate")
    void shouldThrowExceptionForNullBirthDate() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(1L, "John", "Doe", null, testAddress, testContacts, testHobbies)
        );
        assertEquals("Birth date cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null address")
    void shouldThrowExceptionForNullAddress() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(1L, "John", "Doe", LocalDate.of(1990, 5, 15),
                        null, testContacts, testHobbies)
        );
        assertEquals("Address cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle null collections gracefully")
    void shouldHandleNullCollectionsGracefully() {
        // When
        Person person = new Person(1L, "John", "Doe", LocalDate.of(1990, 5, 15),
                testAddress, null, null);

        // Then
        assertAll(
                () -> assertNotNull(person.contacts()),
                () -> assertTrue(person.contacts().isEmpty()),
                () -> assertNotNull(person.hobbies()),
                () -> assertTrue(person.hobbies().isEmpty())
        );
    }

    @Test
    @DisplayName("Should demonstrate record equality")
    void shouldDemonstrateRecordEquality() {
        // Given
        Person person1 = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person person3 = person1.withId(2L);

        // Then
        assertAll(
                () -> assertEquals(person1, person2),
                () -> assertNotEquals(person1, person3),
                () -> assertEquals(person1.hashCode(), person2.hashCode()),
                () -> assertNotEquals(person1.hashCode(), person3.hashCode())
        );
    }

    @Test
    @DisplayName("Should demonstrate complete Builder pattern workflow")
    void shouldDemonstrateCompleteBuilderPatternWorkflow() {
        // When - Create person step by step
        Person person = Person.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Cooper")
                .birthDate(LocalDate.of(1988, 3, 22))
                .address(Address.builder()
                        .street("789 Rock Ave")
                        .city("Detroit")
                        .state("MI")
                        .zipCode("48201")
                        .build())
                .addContact(Contact.builder()
                        .type("EMAIL")
                        .value("alice@rockstar.com")
                        .build())
                .addContact(Contact.builder()
                        .type("PHONE")
                        .value("+1-555-ROCK-123")
                        .build())
                .addHobby("Music")
                .addHobby("Performing")
                .addHobby("Songwriting")
                .build();

        // Then - Update person using immutable methods
        Person updatedPerson = person
                .withFirstName("Alice Marie")
                .addHobby("Guitar Playing")
                .addContact(new Contact("TWITTER", "@alicecooper"));

        // Verify original is unchanged
        assertAll(
                () -> assertEquals("Alice", person.firstName()),
                () -> assertEquals(2, person.contacts().size()),
                () -> assertEquals(3, person.hobbies().size()),

                // Verify updated person has changes
                () -> assertEquals("Alice Marie", updatedPerson.firstName()),
                () -> assertEquals(3, updatedPerson.contacts().size()),
                () -> assertEquals(4, updatedPerson.hobbies().size()),
                () -> assertTrue(updatedPerson.hobbies().contains("Guitar Playing")),
                () -> assertTrue(updatedPerson.hasContactType("TWITTER"))
        );
    }
}