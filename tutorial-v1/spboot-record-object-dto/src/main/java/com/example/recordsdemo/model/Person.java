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
        List<String> hobbies) {             // Collection of strings

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