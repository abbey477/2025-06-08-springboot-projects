package com.example.service;

import com.example.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PersonService {

    private final ObjectMapper objectMapper;

    public PersonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Marshal a Person object to JSON string
     * @param person the Person object to marshal
     * @return JSON string representation
     * @throws JsonProcessingException if marshalling fails
     */
    public String marshalToJson(Person person) throws JsonProcessingException {
        if (person == null) {
            log.warn("Attempted to marshal null Person object");
            throw new IllegalArgumentException("Person cannot be null");
        }

        try {
            String json = objectMapper.writeValueAsString(person);
            log.info("Successfully marshalled Person to JSON: {}", person.name());
            return json;
        } catch (JsonProcessingException e) {
            log.error("Failed to marshal Person to JSON: {}", person.name(), e);
            throw e;
        }
    }

    /**
     * Unmarshal JSON string to Person object
     * @param json the JSON string to unmarshal
     * @return Person object
     * @throws IOException if unmarshalling fails
     */
    public Person unmarshalFromJson(String json) throws IOException {
        log.info("Attempting to unmarshal JSON string to Person:\n {}", json);

        if (json == null || json.trim().isEmpty()) {
            log.warn("====>  Attempted to unmarshal null or empty JSON string");
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }

        try {
            Person person = objectMapper.readValue(json, Person.class);
            log.info("Successfully unmarshalled JSON to Person: {}", person.name());
            return person;
        } catch (IOException e) {
            log.error("Failed to unmarshal JSON to Person: {}", json, e);
            throw e;
        }
    }

    /**
     * Marshal a list of Person objects to JSON string
     * @param persons the list of Person objects to marshal
     * @return JSON string representation
     * @throws JsonProcessingException if marshalling fails
     */
    public String marshalListToJson(List<Person> persons) throws JsonProcessingException {
        if (persons == null) {
            log.warn("Attempted to marshal null Person list");
            throw new IllegalArgumentException("Person list cannot be null");
        }

        try {
            String json = objectMapper.writeValueAsString(persons);
            log.info("Successfully marshalled {} Person objects to JSON", persons.size());
            return json;
        } catch (JsonProcessingException e) {
            log.error("Failed to marshal Person list to JSON", e);
            throw e;
        }
    }

    /**
     * Unmarshal JSON string to list of Person objects
     * @param json the JSON string to unmarshal
     * @return List of Person objects
     * @throws IOException if unmarshalling fails
     */
    public List<Person> unmarshalListFromJson(String json) throws IOException {
        if (json == null || json.trim().isEmpty()) {
            log.warn("Attempted to unmarshal null or empty JSON string to Person list");
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }

        try {
            List<Person> persons = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
            log.info("Successfully unmarshalled JSON to {} Person objects", persons.size());
            return persons;
        } catch (IOException e) {
            log.error("Failed to unmarshal JSON to Person list: {}", json, e);
            throw e;
        }
    }

    /**
     * Create a new Person with updated timestamp
     * @param person the original Person
     * @return new Person with current timestamp
     */
    public Person updateTimestamp(Person person) {
        if (person == null) {
            log.warn("Attempted to update timestamp for null Person");
            throw new IllegalArgumentException("Person cannot be null");
        }

        Person updatedPerson = person.toBuilder()
                .timestamp(LocalDateTime.now())
                .build();

        log.info("Updated timestamp for Person: {}", person.name());
        return updatedPerson;
    }
}