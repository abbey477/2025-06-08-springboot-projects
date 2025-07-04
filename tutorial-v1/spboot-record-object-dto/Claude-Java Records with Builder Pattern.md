# Java Records with Builder Pattern

**Created:** 7/2/2025 6:53  
**Updated:** 7/2/2025 6:59  
**Exported:** 7/2/2025 9:15  
**Link:** [https://claude.ai/chat/6cb272f0-9a3d-479e-9fb2-aa209c62d9cd](https://claude.ai/chat/6cb272f0-9a3d-479e-9fb2-aa209c62d9cd)  

## Prompt:
create a spring boot 3.5.0 project, not web, write test cases to using records with create a record class in java with fields, nested record, list collection
and show how to update the record using Builder Pattern with Records
add unit test to show it works



## Response:
I'll create a Spring Boot 3.5.0 project with records, nested records, collections, and demonstrate the Builder pattern with comprehensive unit tests.

**pom.xml - Spring Boot 3.5.0 Project**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>records-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>records-demo</name>
    <description>Demo project for Spring Boot with Records</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**RecordsDemoApplication.java - Main Application**

```java
package com.example.recordsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecordsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecordsDemoApplication.class, args);
    }
}
```

**Address.java - Nested Record**

```java
package com.example.recordsdemo.model;

/**
 * Address record - represents a nested record structure
 */
public record Address(
    String street,
    String city,
    String state,
    String zipCode,
    String country
) {
    
    /**
     * Compact constructor for validation
     */
    public Address {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("ZipCode cannot be null or empty");
        }
    }
    
    /**
     * Builder pattern for Address record
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country = "USA"; // default value
        
        public Builder street(String street) {
            this.street = street;
            return this;
        }
        
        public Builder city(String city) {
            this.city = city;
            return this;
        }
        
        public Builder state(String state) {
            this.state = state;
            return this;
        }
        
        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }
        
        public Builder country(String country) {
            this.country = country;
            return this;
        }
        
        public Address build() {
            return new Address(street, city, state, zipCode, country);
        }
    }
    
    /**
     * Method to create a new Address with updated fields
     */
    public Address withStreet(String newStreet) {
        return new Address(newStreet, city, state, zipCode, country);
    }
    
    public Address withCity(String newCity) {
        return new Address(street, newCity, state, zipCode, country);
    }
    
    public Address withState(String newState) {
        return new Address(street, city, newState, zipCode, country);
    }
    
    public Address withZipCode(String newZipCode) {
        return new Address(street, city, state, newZipCode, country);
    }
    
    public Address withCountry(String newCountry) {
        return new Address(street, city, state, zipCode, newCountry);
    }
}
```

**Contact.java - Simple Record for Collections**

```java
package com.example.recordsdemo.model;

/**
 * Contact record - used in collections within Person record
 */
public record Contact(
    String type,    // EMAIL, PHONE, LINKEDIN, etc.
    String value
) {
    
    public Contact {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact type cannot be null or empty");
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact value cannot be null or empty");
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String type;
        private String value;
        
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        
        public Builder value(String value) {
            this.value = value;
            return this;
        }
        
        public Contact build() {
            return new Contact(type, value);
        }
    }
    
    public Contact withType(String newType) {
        return new Contact(newType, value);
    }
    
    public Contact withValue(String newValue) {
        return new Contact(type, newValue);
    }
}
```

**Person.java - Main Record with Nested Records and Collections**

```java
package com.example.recordsdemo.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Person record - demonstrates nested records, collections, and builder pattern
 */
public record Person(
    Long id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    Address address,                    // Nested record
    List<Contact> contacts,            // Collection of records
    List<String> hobbies              // Collection of strings
) {
    
    /**
     * Compact constructor for validation and defensive copying
     */
    public Person {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        
        // Defensive copying for mutable collections
        contacts = contacts != null ? List.copyOf(contacts) : List.of();
        hobbies = hobbies != null ? List.copyOf(hobbies) : List.of();
    }
    
    /**
     * Builder pattern for Person record
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private Address address;
        private List<Contact> contacts = new ArrayList<>();
        private List<String> hobbies = new ArrayList<>();
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }
        
        public Builder address(Address address) {
            this.address = address;
            return this;
        }
        
        public Builder contacts(List<Contact> contacts) {
            this.contacts = new ArrayList<>(contacts);
            return this;
        }
        
        public Builder addContact(Contact contact) {
            this.contacts.add(contact);
            return this;
        }
        
        public Builder hobbies(List<String> hobbies) {
            this.hobbies = new ArrayList<>(hobbies);
            return this;
        }
        
        public Builder addHobby(String hobby) {
            this.hobbies.add(hobby);
            return this;
        }
        
        public Person build() {
            return new Person(id, firstName, lastName, birthDate, address, contacts, hobbies);
        }
    }
    
    /**
     * Methods to create updated copies of the record
     */
    public Person withId(Long newId) {
        return new Person(newId, firstName, lastName, birthDate, address, contacts, hobbies);
    }
    
    public Person withFirstName(String newFirstName) {
        return new Person(id, newFirstName, lastName, birthDate, address, contacts, hobbies);
    }
    
    public Person withLastName(String newLastName) {
        return new Person(id, firstName, newLastName, birthDate, address, contacts, hobbies);
    }
    
    public Person withBirthDate(LocalDate newBirthDate) {
        return new Person(id, firstName, lastName, newBirthDate, address, contacts, hobbies);
    }
    
    public Person withAddress(Address newAddress) {
        return new Person(id, firstName, lastName, birthDate, newAddress, contacts, hobbies);
    }
    
    public Person withContacts(List<Contact> newContacts) {
        return new Person(id, firstName, lastName, birthDate, address, newContacts, hobbies);
    }
    
    public Person withHobbies(List<String> newHobbies) {
        return new Person(id, firstName, lastName, birthDate, address, contacts, newHobbies);
    }
    
    public Person addContact(Contact contact) {
        List<Contact> newContacts = new ArrayList<>(contacts);
        newContacts.add(contact);
        return withContacts(newContacts);
    }
    
    public Person addHobby(String hobby) {
        List<String> newHobbies = new ArrayList<>(hobbies);
        newHobbies.add(hobby);
        return withHobbies(newHobbies);
    }
    
    /**
     * Convenience methods
     */
    public String fullName() {
        return firstName + " " + lastName;
    }
    
    public int age() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
    
    public boolean hasContactType(String type) {
        return contacts.stream()
                .anyMatch(contact -> contact.type().equalsIgnoreCase(type));
    }
}
```

**PersonService.java - Service Class for Business Logic**

```java
package com.example.recordsdemo.service;

import com.example.recordsdemo.model.Person;
import com.example.recordsdemo.model.Contact;
import com.example.recordsdemo.model.Address;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class to demonstrate working with records
 */
@Service
public class PersonService {
    
    private final ConcurrentHashMap<Long, Person> personStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * Create a new person
     */
    public Person createPerson(Person person) {
        Person personWithId = person.withId(idGenerator.getAndIncrement());
        personStorage.put(personWithId.id(), personWithId);
        return personWithId;
    }
    
    /**
     * Find person by ID
     */
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(personStorage.get(id));
    }
    
    /**
     * Update person - demonstrates immutable updates
     */
    public Optional<Person> updatePerson(Long id, Person updatedPerson) {
        return findById(id).map(existingPerson -> {
            Person updated = updatedPerson.withId(id);
            personStorage.put(id, updated);
            return updated;
        });
    }
    
    /**
     * Update person's address
     */
    public Optional<Person> updateAddress(Long id, Address newAddress) {
        return findById(id).map(person -> {
            Person updated = person.withAddress(newAddress);
            personStorage.put(id, updated);
            return updated;
        });
    }
    
    /**
     * Add contact to person
     */
    public Optional<Person> addContact(Long id, Contact contact) {
        return findById(id).map(person -> {
            Person updated = person.addContact(contact);
            personStorage.put(id, updated);
            return updated;
        });
    }
    
    /**
     * Add hobby to person
     */
    public Optional<Person> addHobby(Long id, String hobby) {
        return findById(id).map(person -> {
            Person updated = person.addHobby(hobby);
            personStorage.put(id, updated);
            return updated;
        });
    }
    
    /**
     * Get all persons
     */
    public List<Person> getAllPersons() {
        return List.copyOf(personStorage.values());
    }
    
    /**
     * Delete person
     */
    public boolean deletePerson(Long id) {
        return personStorage.remove(id) != null;
    }
    
    /**
     * Find persons by hobby
     */
    public List<Person> findByHobby(String hobby) {
        return personStorage.values().stream()
                .filter(person -> person.hobbies().contains(hobby))
                .toList();
    }
    
    /**
     * Find persons by contact type
     */
    public List<Person> findByContactType(String contactType) {
        return personStorage.values().stream()
                .filter(person -> person.hasContactType(contactType))
                .toList();
    }
}
```

Now let's create comprehensive unit tests:

**AddressTest.java - Unit Tests for Address Record**

```java
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
```

**ContactTest.java - Unit Tests for Contact Record**

```java
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
```

**PersonTest.java - Comprehensive Unit Tests for Person Record**

```java
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
```



## Prompt:
Continue



## Response:


**PersonTest.java - Comprehensive Unit Tests for Person Record** ∙ Version 2

```java
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
```

**PersonServiceTest.java - Unit Tests for PersonService**

```java
package com.example.recordsdemo.service;

import com.example.recordsdemo.model.Person;
import com.example.recordsdemo.model.Address;
import com.example.recordsdemo.model.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class PersonServiceTest {

    private PersonService personService;
    private Address testAddress;
    private List<Contact> testContacts;
    private List<String> testHobbies;

    @BeforeEach
    void setUp() {
        personService = new PersonService();
        
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
    @DisplayName("Should create person with auto-generated ID")
    void shouldCreatePersonWithAutoGeneratedId() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        Person createdPerson = personService.createPerson(personToCreate);

        // Then
        assertAll(
                () -> assertNotNull(createdPerson.id()),
                () -> assertEquals("John", createdPerson.firstName()),
                () -> assertEquals("Doe", createdPerson.lastName()),
                () -> assertEquals(LocalDate.of(1990, 5, 15), createdPerson.birthDate()),
                () -> assertEquals(testAddress, createdPerson.address()),
                () -> assertEquals(testContacts, createdPerson.contacts()),
                () -> assertEquals(testHobbies, createdPerson.hobbies())
        );
    }

    @Test
    @DisplayName("Should find person by ID")
    void shouldFindPersonById() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(personToCreate);

        // When
        Optional<Person> foundPerson = personService.findById(createdPerson.id());

        // Then
        assertAll(
                () -> assertTrue(foundPerson.isPresent()),
                () -> assertEquals(createdPerson, foundPerson.get())
        );
    }

    @Test
    @DisplayName("Should return empty when person not found")
    void shouldReturnEmptyWhenPersonNotFound() {
        // When
        Optional<Person> foundPerson = personService.findById(999L);

        // Then
        assertTrue(foundPerson.isEmpty());
    }

    @Test
    @DisplayName("Should update existing person")
    void shouldUpdateExistingPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);

        Person updatedPersonData = Person.builder()
                .firstName("Jonathan")
                .lastName("Smith")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Gaming"))
                .build();

        // When
        Optional<Person> updatedPerson = personService.updatePerson(createdPerson.id(), updatedPersonData);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(createdPerson.id(), updatedPerson.get().id()), // ID preserved
                () -> assertEquals("Jonathan", updatedPerson.get().firstName()),
                () -> assertEquals("Smith", updatedPerson.get().lastName()),
                () -> assertEquals(List.of("Reading", "Gaming"), updatedPerson.get().hobbies())
        );
    }

    @Test
    @DisplayName("Should return empty when updating non-existent person")
    void shouldReturnEmptyWhenUpdatingNonExistentPerson() {
        // Given
        Person updatedPersonData = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        Optional<Person> updatedPerson = personService.updatePerson(999L, updatedPersonData);

        // Then
        assertTrue(updatedPerson.isEmpty());
    }

    @Test
    @DisplayName("Should update person's address")
    void shouldUpdatePersonAddress() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);

        Address newAddress = Address.builder()
                .street("456 Oak Ave")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .country("USA")
                .build();

        // When
        Optional<Person> updatedPerson = personService.updateAddress(createdPerson.id(), newAddress);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(newAddress, updatedPerson.get().address()),
                () -> assertEquals("John", updatedPerson.get().firstName()), // Other fields unchanged
                () -> assertEquals("Doe", updatedPerson.get().lastName())
        );
    }

    @Test
    @DisplayName("Should add contact to person")
    void shouldAddContactToPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(new Contact("EMAIL", "john@example.com")))
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);
        Contact newContact = new Contact("LINKEDIN", "https://linkedin.com/in/johndoe");

        // When
        Optional<Person> updatedPerson = personService.addContact(createdPerson.id(), newContact);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(2, updatedPerson.get().contacts().size()),
                () -> assertTrue(updatedPerson.get().contacts().contains(newContact)),
                () -> assertTrue(updatedPerson.get().hasContactType("LINKEDIN"))
        );
    }

    @Test
    @DisplayName("Should add hobby to person")
    void shouldAddHobbyToPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading"))
                .build();

        Person createdPerson = personService.createPerson(originalPerson);
        String newHobby = "Photography";

        // When
        Optional<Person> updatedPerson = personService.addHobby(createdPerson.id(), newHobby);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(2, updatedPerson.get().hobbies().size()),
                () -> assertTrue(updatedPerson.get().hobbies().contains(newHobby)),
                () -> assertTrue(updatedPerson.get().hobbies().contains("Reading")) // Original hobby preserved
        );
    }

    @Test
    @DisplayName("Should get all persons")
    void shouldGetAllPersons() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);

        // When
        List<Person> allPersons = personService.getAllPersons();

        // Then
        assertAll(
                () -> assertEquals(2, allPersons.size()),
                () -> assertTrue(allPersons.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(allPersons.stream().anyMatch(p -> p.firstName().equals("Jane")))
        );
    }

    @Test
    @DisplayName("Should delete person")
    void shouldDeletePerson() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(personToCreate);

        // When
        boolean deleted = personService.deletePerson(createdPerson.id());
        Optional<Person> foundPerson = personService.findById(createdPerson.id());

        // Then
        assertAll(
                () -> assertTrue(deleted),
                () -> assertTrue(foundPerson.isEmpty())
        );
    }

    @Test
    @DisplayName("Should return false when deleting non-existent person")
    void shouldReturnFalseWhenDeletingNonExistentPerson() {
        // When
        boolean deleted = personService.deletePerson(999L);

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should find persons by hobby")
    void shouldFindPersonsByHobby() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Swimming"))
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Cooking"))
                .build();

        Person person3 = Person.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1992, 12, 10))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Gaming", "Coding"))
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);
        personService.createPerson(person3);

        // When
        List<Person> readingLovers = personService.findByHobby("Reading");
        List<Person> gamers = personService.findByHobby("Gaming");

        // Then
        assertAll(
                () -> assertEquals(2, readingLovers.size()),
                () -> assertTrue(readingLovers.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(readingLovers.stream().anyMatch(p -> p.firstName().equals("Jane"))),
                
                () -> assertEquals(1, gamers.size()),
                () -> assertEquals("Bob", gamers.get(0).firstName())
        );
    }

    @Test
    @DisplayName("Should find persons by contact type")
    void shouldFindPersonsByContactType() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("EMAIL", "john@example.com"),
                    new Contact("PHONE", "+1-555-123-4567")
                ))
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("EMAIL", "jane@example.com"),
                    new Contact("LINKEDIN", "https://linkedin.com/in/janesmith")
                ))
                .hobbies(testHobbies)
                .build();

        Person person3 = Person.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1992, 12, 10))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("PHONE", "+1-555-987-6543")
                ))
                .hobbies(testHobbies)
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);
        personService.createPerson(person3);

        // When
        List<Person> emailUsers = personService.findByContactType("EMAIL");
        List<Person> phoneUsers = personService.findByContactType("PHONE");
        List<Person> linkedinUsers = personService.findByContactType("LINKEDIN");

        // Then
        assertAll(
                () -> assertEquals(2, emailUsers.size()),
                () -> assertTrue(emailUsers.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(emailUsers.stream().anyMatch(p -> p.firstName().equals("Jane"))),
                
                () -> assertEquals(2, phoneUsers.size()),
                () -> assertTrue(phoneUsers.stream().anyMatch(p -> p.firstName().equals("John"))),
```



## Prompt:
Continue



## Response:


**PersonServiceTest.java - Unit Tests for PersonService** ∙ Version 2

```java
package com.example.recordsdemo.service;

import com.example.recordsdemo.model.Person;
import com.example.recordsdemo.model.Address;
import com.example.recordsdemo.model.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class PersonServiceTest {

    private PersonService personService;
    private Address testAddress;
    private List<Contact> testContacts;
    private List<String> testHobbies;

    @BeforeEach
    void setUp() {
        personService = new PersonService();
        
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
    @DisplayName("Should create person with auto-generated ID")
    void shouldCreatePersonWithAutoGeneratedId() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        Person createdPerson = personService.createPerson(personToCreate);

        // Then
        assertAll(
                () -> assertNotNull(createdPerson.id()),
                () -> assertEquals("John", createdPerson.firstName()),
                () -> assertEquals("Doe", createdPerson.lastName()),
                () -> assertEquals(LocalDate.of(1990, 5, 15), createdPerson.birthDate()),
                () -> assertEquals(testAddress, createdPerson.address()),
                () -> assertEquals(testContacts, createdPerson.contacts()),
                () -> assertEquals(testHobbies, createdPerson.hobbies())
        );
    }

    @Test
    @DisplayName("Should find person by ID")
    void shouldFindPersonById() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(personToCreate);

        // When
        Optional<Person> foundPerson = personService.findById(createdPerson.id());

        // Then
        assertAll(
                () -> assertTrue(foundPerson.isPresent()),
                () -> assertEquals(createdPerson, foundPerson.get())
        );
    }

    @Test
    @DisplayName("Should return empty when person not found")
    void shouldReturnEmptyWhenPersonNotFound() {
        // When
        Optional<Person> foundPerson = personService.findById(999L);

        // Then
        assertTrue(foundPerson.isEmpty());
    }

    @Test
    @DisplayName("Should update existing person")
    void shouldUpdateExistingPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);

        Person updatedPersonData = Person.builder()
                .firstName("Jonathan")
                .lastName("Smith")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Gaming"))
                .build();

        // When
        Optional<Person> updatedPerson = personService.updatePerson(createdPerson.id(), updatedPersonData);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(createdPerson.id(), updatedPerson.get().id()), // ID preserved
                () -> assertEquals("Jonathan", updatedPerson.get().firstName()),
                () -> assertEquals("Smith", updatedPerson.get().lastName()),
                () -> assertEquals(List.of("Reading", "Gaming"), updatedPerson.get().hobbies())
        );
    }

    @Test
    @DisplayName("Should return empty when updating non-existent person")
    void shouldReturnEmptyWhenUpdatingNonExistentPerson() {
        // Given
        Person updatedPersonData = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        Optional<Person> updatedPerson = personService.updatePerson(999L, updatedPersonData);

        // Then
        assertTrue(updatedPerson.isEmpty());
    }

    @Test
    @DisplayName("Should update person's address")
    void shouldUpdatePersonAddress() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);

        Address newAddress = Address.builder()
                .street("456 Oak Ave")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .country("USA")
                .build();

        // When
        Optional<Person> updatedPerson = personService.updateAddress(createdPerson.id(), newAddress);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(newAddress, updatedPerson.get().address()),
                () -> assertEquals("John", updatedPerson.get().firstName()), // Other fields unchanged
                () -> assertEquals("Doe", updatedPerson.get().lastName())
        );
    }

    @Test
    @DisplayName("Should add contact to person")
    void shouldAddContactToPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(new Contact("EMAIL", "john@example.com")))
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(originalPerson);
        Contact newContact = new Contact("LINKEDIN", "https://linkedin.com/in/johndoe");

        // When
        Optional<Person> updatedPerson = personService.addContact(createdPerson.id(), newContact);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(2, updatedPerson.get().contacts().size()),
                () -> assertTrue(updatedPerson.get().contacts().contains(newContact)),
                () -> assertTrue(updatedPerson.get().hasContactType("LINKEDIN"))
        );
    }

    @Test
    @DisplayName("Should add hobby to person")
    void shouldAddHobbyToPerson() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading"))
                .build();

        Person createdPerson = personService.createPerson(originalPerson);
        String newHobby = "Photography";

        // When
        Optional<Person> updatedPerson = personService.addHobby(createdPerson.id(), newHobby);

        // Then
        assertAll(
                () -> assertTrue(updatedPerson.isPresent()),
                () -> assertEquals(2, updatedPerson.get().hobbies().size()),
                () -> assertTrue(updatedPerson.get().hobbies().contains(newHobby)),
                () -> assertTrue(updatedPerson.get().hobbies().contains("Reading")) // Original hobby preserved
        );
    }

    @Test
    @DisplayName("Should get all persons")
    void shouldGetAllPersons() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);

        // When
        List<Person> allPersons = personService.getAllPersons();

        // Then
        assertAll(
                () -> assertEquals(2, allPersons.size()),
                () -> assertTrue(allPersons.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(allPersons.stream().anyMatch(p -> p.firstName().equals("Jane")))
        );
    }

    @Test
    @DisplayName("Should delete person")
    void shouldDeletePerson() {
        // Given
        Person personToCreate = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person createdPerson = personService.createPerson(personToCreate);

        // When
        boolean deleted = personService.deletePerson(createdPerson.id());
        Optional<Person> foundPerson = personService.findById(createdPerson.id());

        // Then
        assertAll(
                () -> assertTrue(deleted),
                () -> assertTrue(foundPerson.isEmpty())
        );
    }

    @Test
    @DisplayName("Should return false when deleting non-existent person")
    void shouldReturnFalseWhenDeletingNonExistentPerson() {
        // When
        boolean deleted = personService.deletePerson(999L);

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should find persons by hobby")
    void shouldFindPersonsByHobby() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Swimming"))
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Reading", "Cooking"))
                .build();

        Person person3 = Person.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1992, 12, 10))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(List.of("Gaming", "Coding"))
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);
        personService.createPerson(person3);

        // When
        List<Person> readingLovers = personService.findByHobby("Reading");
        List<Person> gamers = personService.findByHobby("Gaming");

        // Then
        assertAll(
                () -> assertEquals(2, readingLovers.size()),
                () -> assertTrue(readingLovers.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(readingLovers.stream().anyMatch(p -> p.firstName().equals("Jane"))),
                
                () -> assertEquals(1, gamers.size()),
                () -> assertEquals("Bob", gamers.get(0).firstName())
        );
    }

    @Test
    @DisplayName("Should find persons by contact type")
    void shouldFindPersonsByContactType() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("EMAIL", "john@example.com"),
                    new Contact("PHONE", "+1-555-123-4567")
                ))
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("EMAIL", "jane@example.com"),
                    new Contact("LINKEDIN", "https://linkedin.com/in/janesmith")
                ))
                .hobbies(testHobbies)
                .build();

        Person person3 = Person.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1992, 12, 10))
                .address(testAddress)
                .contacts(List.of(
                    new Contact("PHONE", "+1-555-987-6543")
                ))
                .hobbies(testHobbies)
                .build();

        personService.createPerson(person1);
        personService.createPerson(person2);
        personService.createPerson(person3);

        // When
        List<Person> emailUsers = personService.findByContactType("EMAIL");
        List<Person> phoneUsers = personService.findByContactType("PHONE");
        List<Person> linkedinUsers = personService.findByContactType("LINKEDIN");

        // Then
        assertAll(
                () -> assertEquals(2, emailUsers.size()),
                () -> assertTrue(emailUsers.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(emailUsers.stream().anyMatch(p -> p.firstName().equals("Jane"))),
                
                () -> assertEquals(2, phoneUsers.size()),
                () -> assertTrue(phoneUsers.stream().anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(phoneUsers.stream().anyMatch(p -> p.firstName().equals("Bob"))),
                
                () -> assertEquals(1, linkedinUsers.size()),
                () -> assertEquals("Jane", linkedinUsers.get(0).firstName())
        );
    }

    @Test
    @DisplayName("Should demonstrate complete workflow with Builder pattern")
    void shouldDemonstrateCompleteWorkflowWithBuilderPattern() {
        // Given - Create person using Builder pattern
        Person originalPerson = Person.builder()
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
                .build();

        // When - Create and update person through service
        Person createdPerson = personService.createPerson(originalPerson);
        
        // Add more contacts and hobbies
        Person updatedPerson1 = personService.addContact(createdPerson.id(), 
                new Contact("TWITTER", "@alicecooper")).orElseThrow();
        
        Person updatedPerson2 = personService.addHobby(updatedPerson1.id(), 
                "Songwriting").orElseThrow();
        
        // Update address
        Address newAddress = Address.builder()
                .street("123 New Stage St")
                .city("Nashville")
                .state("TN")
                .zipCode("37201")
                .build();
        
        Person finalPerson = personService.updateAddress(updatedPerson2.id(), newAddress).orElseThrow();

        // Then - Verify all changes
        assertAll(
                () -> assertNotNull(finalPerson.id()),
                () -> assertEquals("Alice", finalPerson.firstName()),
                () -> assertEquals("Cooper", finalPerson.lastName()),
                () -> assertEquals(3, finalPerson.contacts().size()),
                () -> assertEquals(3, finalPerson.hobbies().size()),
                () -> assertTrue(finalPerson.hasContactType("TWITTER")),
                () -> assertTrue(finalPerson.hobbies().contains("Songwriting")),
                () -> assertEquals("Nashville", finalPerson.address().city()),
                () -> assertEquals("123 New Stage St", finalPerson.address().street())
        );

        // Verify original person in storage is updated
        Optional<Person> retrievedPerson = personService.findById(finalPerson.id());
        assertTrue(retrievedPerson.isPresent());
        assertEquals(finalPerson, retrievedPerson.get());
    }

    @Test
    @DisplayName("Should handle concurrent ID generation")
    void shouldHandleConcurrentIdGeneration() {
        // Given
        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(testAddress)
                .contacts(testContacts)
                .hobbies(testHobbies)
                .build();

        // When
        Person created1 = personService.createPerson(person1);
        Person created2 = personService.createPerson(person2);

        // Then
        assertAll(
                () -> assertNotNull(created1.id()),
                () -> assertNotNull(created2.id()),
                () -> assertNotEquals(created1.id(), created2.id()),
                () -> assertTrue(created2.id() > created1.id()) // Sequential ID generation
        );
    }

    @Test
    @DisplayName("Should demonstrate immutability through service operations")
    void shouldDemonstrateImmutabilityThroughServiceOperations() {
        // Given
        Person originalPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(testAddress)
                .contacts(List.of(new Contact("EMAIL", "john@example.com")))
                .hobbies(List.of("Reading"))
                .build();

        Person createdPerson = personService.createPerson(originalPerson);
        
        // Store references to verify immutability
        List<Contact> originalContacts = createdPerson.contacts();
        List<String> originalHobbies = createdPerson.hobbies();
        Address originalAddress = createdPerson.address();

        // When - Perform various updates
        personService.addContact(createdPerson.id(), new Contact("PHONE", "123-456-7890"));
        personService.addHobby(createdPerson.id(), "Gaming");
        
        Address newAddress = Address.builder()
                .street("456 New St")
                .city("New City")
                .state("TX")
                .zipCode("12345")
                .build();
        personService.updateAddress(createdPerson.id(), newAddress);

        // Then - Verify original objects are unchanged
        assertAll(
                () -> assertEquals(1, originalContacts.size()),
                () -> assertEquals(1, originalHobbies.size()),
                () -> assertEquals("123 Main St", originalAddress.street()),
                () -> assertEquals("Houston", originalAddress.city())
        );

        // Verify service returns updated person
        Person updatedPerson = personService.findById(createdPerson.id()).orElseThrow();
        assertAll(
                () -> assertEquals(2, updatedPerson.contacts().size()),
                () -> assertEquals(2, updatedPerson.hobbies().size()),
                () -> assertEquals("456 New St", updatedPerson.address().street()),
                () -> assertEquals("New City", updatedPerson.address().city())
        );
    }
}
```

Now let's create an integration test that demonstrates the complete workflow:

**RecordsIntegrationTest.java - Integration Test**

```java
package com.example.recordsdemo;

import com.example.recordsdemo.model.Person;
import com.example.recordsdemo.model.Address;
import com.example.recordsdemo.model.Contact;
import com.example.recordsdemo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class RecordsIntegrationTest {

    @Autowired
    private PersonService personService;

    @Test
    @DisplayName("Should demonstrate complete records workflow with Builder pattern")
    void shouldDemonstrateCompleteRecordsWorkflow() {
        // Step 1: Create a person using nested Builder patterns
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address(Address.builder()
                        .street("123 Main St")
                        .city("Houston")
                        .state("TX")
                        .zipCode("77001")
                        .country("USA")
                        .build())
                .addContact(Contact.builder()
                        .type("EMAIL")
                        .value("john.doe@example.com")
                        .build())
                .addContact(Contact.builder()
                        .type("PHONE")
                        .value("+1-555-123-4567")
                        .build())
                .addHobby("Reading")
                .addHobby("Swimming")
                .addHobby("Programming")
                .build();

        // Step 2: Save person through service
        Person savedPerson = personService.createPerson(person);
        
        // Verify initial creation
        assertAll(
                () -> assertNotNull(savedPerson.id()),
                () -> assertEquals("John", savedPerson.firstName()),
                () -> assertEquals("Doe", savedPerson.lastName()),
                () -> assertEquals(LocalDate.of(1990, 5, 15), savedPerson.birthDate()),
                () -> assertEquals("123 Main St", savedPerson.address().street()),
                () -> assertEquals("Houston", savedPerson.address().city()),
                () -> assertEquals(2, savedPerson.contacts().size()),
                () -> assertEquals(3, savedPerson.hobbies().size()),
                () -> assertTrue(savedPerson.hasContactType("EMAIL")),
                () -> assertTrue(savedPerson.hasContactType("PHONE")),
                () -> assertEquals("John Doe", savedPerson.fullName())
        );

        // Step 3: Demonstrate immutable updates using Builder pattern
        
        // Update address using Builder
        Address newAddress = Address.builder()
                .street("456 Oak Avenue")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .country("USA")
                .build();
        
        Optional<Person> personWithNewAddress = personService.updateAddress(savedPerson.id(), newAddress);
        assertTrue(personWithNewAddress.isPresent());
        
        // Verify address update
        assertAll(
                () -> assertEquals("456 Oak Avenue", personWithNewAddress.get().address().street()),
                () -> assertEquals("Dallas", personWithNewAddress.get().address().city()),
                () -> assertEquals("75201", personWithNewAddress.get().address().zipCode()),
                // Verify other fields remain unchanged
                () -> assertEquals("John", personWithNewAddress.get().firstName()),
                () -> assertEquals(2, personWithNewAddress.get().contacts().size()),
                () -> assertEquals(3, personWithNewAddress.get().hobbies().size())
        );

        // Step 4: Add new contact using Builder
        Contact linkedinContact = Contact.builder()
                .type("LINKEDIN")
                .value("https://linkedin.com/in/johndoe")
                .build();
        
        Optional<Person> personWithLinkedin = personService.addContact(savedPerson.id(), linkedinContact);
        assertTrue(personWithLinkedin.isPresent());
        
        // Verify contact addition
        assertAll(
                () -> assertEquals(3, personWithLinkedin.get().contacts().size()),
                () -> assertTrue(personWithLinkedin.get().hasContactType("LINKEDIN")),
                () -> assertTrue(personWithLinkedin.get().hasContactType("EMAIL")),
                () -> assertTrue(personWithLinkedin.get().hasContactType("PHONE"))
        );

        // Step 5: Add new hobby
        Optional<Person> personWithNewHobby = personService.addHobby(savedPerson.id(), "Photography");
        assertTrue(personWithNewHobby.isPresent());
        
        // Verify hobby addition
        assertAll(
                () -> assertEquals(4, personWithNewHobby.get().hobbies().size()),
                () -> assertTrue(personWithNewHobby.get().hobbies().contains("Photography")),
                () -> assertTrue(personWithNewHobby.get().hobbies().contains("Reading")),
                () -> assertTrue(personWithNewHobby.get().hobbies().contains("Swimming")),
                () -> assertTrue(personWithNewHobby.get().hobbies().contains("Programming"))
        );

        // Step 6: Create another person to test search functionality
        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 8, 20))
                .address(Address.builder()
                        .street("789 Pine St")
                        .city("Austin")
                        .state("TX")
                        .zipCode("78701")
                        .build())
                .addContact(Contact.builder()
                        .type("EMAIL")
                        .value("jane.smith@example.com")
                        .build())
                .addContact(Contact.builder()
                        .type("TWITTER")
                        .value("@janesmith")
                        .build())
                .addHobby("Photography")
                .addHobby("Hiking")
                .addHobby("Cooking")
                .build();

        Person savedPerson2 = personService.createPerson(person2);

        // Step 7: Test search functionality
        List<Person> photographyEnthusiasts = personService.findByHobby("Photography");
        List<Person> emailUsers = personService.findByContactType("EMAIL");
        List<Person> twitterUsers = personService.findByContactType("TWITTER");

        // Verify search results
        assertAll(
                () -> assertEquals(2, photographyEnthusiasts.size()),
                () -> assertTrue(photographyEnthusiasts.stream()
                        .anyMatch(p -> p.firstName().equals("John"))),
                () -> assertTrue(photographyEnthusiasts.stream()
                        .anyMatch(p -> p.firstName().equals("Jane"))),
                
                () -> assertEquals(2, emailUsers.size()),
                () -> assertTrue(emailUsers.stream()
                        .allMatch(p -> p.hasContactType("EMAIL"))),
                
                () -> assertEquals(1, twitterUsers.size()),
                () -> assertEquals("Jane", twitterUsers.get(0).firstName())
        );

        // Step 8: Demonstrate record equality and immutability
        Person retrievedPerson = personService.findById(savedPerson.id()).orElseThrow();
        
        // Create a copy using Builder pattern
        Person personCopy = Person.builder()
                .id(retrievedPerson.id())
                .firstName(retrievedPerson.firstName())
                .lastName(retrievedPerson.lastName())
                .birthDate(retrievedPerson.birthDate())
                .address(retrievedPerson.address())
                .contacts(retrievedPerson.contacts())
                .hobbies(retrievedPerson.hobbies())
                .build();

        // Verify equality
        assertEquals(retrievedPerson, personCopy);
        assertEquals(retrievedPerson.hashCode(), personCopy.hashCode());

        // Step 9: Demonstrate immutable updates using with methods
        Person updatedFirstName = retrievedPerson.withFirstName("Jonathan");
        Person updatedLastName = retrievedPerson.withLastName("Johnson");
        
        // Verify original is unchanged
        assertAll(
                () -> assertEquals("John", retrievedPerson.firstName()),
                () -> assertEquals("Doe", retrievedPerson.lastName()),
                
                // Verify new instances have changes
                () -> assertEquals("Jonathan", updatedFirstName.firstName()),
                () -> assertEquals("Doe", updatedFirstName.lastName()), // Last name unchanged
                
                () -> assertEquals("John", updatedLastName.firstName()), // First name unchanged
                () -> assertEquals("Johnson", updatedLastName.lastName())
        );

        // Step 10: Demonstrate address updates using with methods
        Address currentAddress = retrievedPerson.address();
        Address updatedAddress = currentAddress
                .withStreet("999 Updated St")
                .withCity("San Antonio");
        
        // Verify original address is unchanged
        assertAll(
                () -> assertEquals("456 Oak Avenue", currentAddress.street()),
                () -> assertEquals("Dallas", currentAddress.city()),
                
                // Verify new address has changes
                () -> assertEquals("999 Updated St", updatedAddress.street()),
                () -> assertEquals("San Antonio", updatedAddress.city()),
                () -> assertEquals("TX", updatedAddress.state()), // State unchanged
                () -> assertEquals("75201", updatedAddress.zipCode()) // Zip unchanged
        );

        // Step 11: Test all persons retrieval
        List<Person> allPersons = personService.getAllPersons();
        assertEquals(2, allPersons.size());

        // Step 12: Clean up - test deletion
        boolean deleted = personService.deletePerson(savedPerson.id());
        assertTrue(deleted);
        
        Optional<Person> deletedPerson = personService.findById(savedPerson.id());
        assertTrue(deletedPerson.isEmpty());
        
        // Verify only one person remains
        List<Person> remainingPersons = personService.getAllPersons();
        assertEquals(1, remainingPersons.size());
        assertEquals("Jane", remainingPersons.get(0).firstName());
    }

    @Test
    @DisplayName("Should demonstrate validation in records")
    void shouldDemonstrateValidationInRecords() {
        // Test Address validation
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Address(null, "Houston", "TX", "77001", "USA")),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Address("123 Main St", null, "TX", "77001", "USA")),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Address("123 Main St", "Houston", "TX", null, "USA"))
        );

        // Test Contact validation
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Contact(null, "john@example.com")),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Contact("EMAIL", null))
        );

        // Test Person validation
        Address validAddress = Address.builder()
                .street("123 Main St")
                .city("Houston")
                .state("TX")
                .zipCode("77001")
                .build();

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Person(1L, null, "Doe", LocalDate.of(1990, 5, 15), 
                                   validAddress, List.of(), List.of())),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Person(1L, "John", null, LocalDate.of(1990, 5, 15), 
                                   validAddress, List.of(), List.of())),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Person(1L, "John", "Doe", null, 
                                   validAddress, List.of(), List.of())),
                () -> assertThrows(IllegalArgumentException.class, 
                    () -> new Person(1L, "John", "Doe", LocalDate.of(1990, 5, 15), 
                                   null, List.of(), List.of()))
        );
    }

    @Test
    @DisplayName("Should demonstrate defensive copying in collections")
    void shouldDemonstrateDefensiveCopyingInCollections() {
        // Create mutable lists
        java.util.List<Contact> mutableContacts = new java.util.ArrayList<>();
        mutableContacts.add(new Contact("EMAIL", "test@example.com"));
        
        java.util.List<String> mutableHobbies = new java.util.ArrayList<>();
        mutableHobbies.add("Reading");

        Address address = Address.builder()
                .street("123 Main St")
                .city("Houston")
                .state("TX")
                .zipCode("77001")
                .build();

        // Create person with mutable collections
        Person person = new Person(1L, "John", "Doe", LocalDate.of(1990, 5, 15), 
                                 address, mutableContacts, mutableHobbies);

        // Modify original collections
        mutableContacts.add(new Contact("PHONE", "123-456-7890"));
        mutableHobbies.add("Writing");

        // Verify person's collections are unchanged (defensive copying worked)
        assertAll(
                () -> assertEquals(1, person.contacts().size()),
                () -> assertEquals(1, person.hobbies().size()),
                
                // Verify collections are immutable
                () -> assertThrows(UnsupportedOperationException.class, 
                    () -> person.contacts().add(new Contact("TEST", "test"))),
                () -> assertThrows(UnsupportedOperationException.class, 
                    () -> person.hobbies().add("NewHobby"))
        );
    }
}
```

Finally, let's create the application.properties file:

**application.properties - Spring Boot Configuration**

```properties
# Spring Boot Application Configuration
spring.application.name=records-demo

# Logging Configuration
logging.level.com.example.recordsdemo=INFO
logging.level.org.springframework=WARN
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# JVM Configuration for Records
spring.main.lazy-initialization=false
```

I've created a comprehensive Spring Boot 3.5.0 project demonstrating Java records with nested records, collections, and the Builder pattern. Here's what the project includes:

## **Project Structure:**

1. **Main Application** (`RecordsDemoApplication.java`) - Standard Spring Boot application
2. **Record Classes:**
   - `Address.java` - Nested record with validation and Builder pattern
   - `Contact.java` - Simple record for collections with Builder pattern  
   - `Person.java` - Main record with nested records, collections, and Builder pattern
3. **Service** (`PersonService.java`) - Business logic demonstrating record operations
4. **Comprehensive Unit Tests:**
   - `AddressTest.java` - Tests for Address record
   - `ContactTest.java` - Tests for Contact record
   - `PersonTest.java` - Tests for Person record
   - `PersonServiceTest.java` - Tests for service operations
   - `RecordsIntegrationTest.java` - Integration test demonstrating complete workflow

## **Key Features Demonstrated:**

### **Records with Fields:**
- **Simple fields**: `id`, `firstName`, `lastName`, `birthDate`
- **Nested record**: `Address` within `Person`
- **Collections**: `List<Contact>` and `List<String>` for hobbies

### **Builder Pattern Implementation:**
- Custom Builder classes for all records
- Fluent API with method chaining
- Default values (e.g., country defaults to "USA")
- Individual item addition methods (`addContact`, `addHobby`)

### **Immutable Updates:**
- `with*` methods for creating updated copies
- Defensive copying for collections
- Immutable collections using `List.copyOf()`

### **Validation:**
- Compact constructors with validation
- Null and empty string checks
- Proper error messages

### **Unit Tests Coverage:**
- ✅ Record creation using constructors and builders
- ✅ Immutable updates using `with*` methods  
- ✅ Validation testing
- ✅ Collection defensive copying
- ✅ Record equality and hashCode
- ✅ Service operations (CRUD)
- ✅ Search functionality
- ✅ Complete workflow integration tests

## **To Run the Project:**

1. **Create the Maven project** with the `pom.xml`
2. **Add all Java files** to the appropriate packages:
   - `src/main/java/com/example/recordsdemo/` (main app)
   - `src/main/java/com/example/recordsdemo/model/` (records)
   - `src/main/java/com/example/recordsdemo/service/` (service)
   - `src/test/java/com/example/recordsdemo/` (tests)
3. **Add application.properties** to `src/main/resources/`
4. **Run tests**: `mvn test`
5. **Run application**: `mvn spring-boot:run`

The tests demonstrate all aspects of working with records, including the Builder pattern for creating and updating immutable record instances. The project showcases best practices for using Java records in a Spring Boot application with comprehensive test coverage.





---
Powered by [Claude Exporter](https://www.claudexporter.com)