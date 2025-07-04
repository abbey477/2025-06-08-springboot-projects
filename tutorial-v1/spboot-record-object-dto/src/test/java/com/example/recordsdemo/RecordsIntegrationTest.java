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