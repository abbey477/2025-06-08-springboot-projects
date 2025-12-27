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