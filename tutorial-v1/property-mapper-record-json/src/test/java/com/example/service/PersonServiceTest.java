package com.example.service;

import com.example.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;
    private Person testPerson;
    private Person testPerson2;

    @BeforeEach
    void setUp() {
        
        testPerson = Person.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .dob(LocalDate.of(1990, 5, 15))
            .timestamp(LocalDateTime.of(2024, 1, 1, 10, 30, 0))
            .build();
            
        testPerson2 = Person.builder()
            .name("Jane Smith")
            .email("jane.smith@example.com")
            .dob(LocalDate.of(1985, 8, 22))
            .timestamp(LocalDateTime.of(2024, 1, 2, 14, 45, 30))
            .build();
    }

    @Test
    @DisplayName("Should marshal Person to JSON successfully")
    void shouldMarshalPersonToJson() throws JsonProcessingException {
        // When
        String json = personService.marshalToJson(testPerson);
        
        // Then
        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertTrue(json.contains("John Doe"));
        assertTrue(json.contains("john.doe@example.com"));
        assertTrue(json.contains("1990-05-15"));
        assertTrue(json.contains("2024-01-01T10:30:00"));
    }

    @Test
    @DisplayName("Should unmarshal JSON to Person successfully")
    void shouldUnmarshalJsonToPerson() throws IOException {
        // Given
        String json = """
                {
                                "name": "John Doe",
                                "email": "john.doe@example.com",
                                "dob": "1990-05-15",
                                "timestamp": "2024-01-01T10:30:00"
                              }
            """;
        // When
        Person person = personService.unmarshalFromJson(json);
        
        // Then
        assertNotNull(person);
        assertEquals("John Doe", person.name());
        assertEquals("john.doe@example.com", person.email());
        assertEquals(LocalDate.of(1990, 5, 15), person.dob());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 30, 0), person.timestamp());
    }

    @Test
    @DisplayName("Should marshal and unmarshal Person maintaining data integrity")
    void shouldMarshalAndUnmarshalPersonWithDataIntegrity() throws Exception {
        // When
        String json = personService.marshalToJson(testPerson);
        Person unmarshalledPerson = personService.unmarshalFromJson(json);
        
        // Then
        assertEquals(testPerson, unmarshalledPerson);
        assertEquals(testPerson.name(), unmarshalledPerson.name());
        assertEquals(testPerson.email(), unmarshalledPerson.email());
        assertEquals(testPerson.dob(), unmarshalledPerson.dob());
        assertEquals(testPerson.timestamp(), unmarshalledPerson.timestamp());
    }

    @Test
    @DisplayName("Should throw exception when marshalling null Person")
    void shouldThrowExceptionWhenMarshallingNullPerson() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personService.marshalToJson(null)
        );
        assertEquals("Person cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when unmarshalling null JSON")
    void shouldThrowExceptionWhenUnmarshallingNullJson() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personService.unmarshalFromJson(null)
        );
        assertEquals("JSON string cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when unmarshalling empty JSON")
    void shouldThrowExceptionWhenUnmarshallingEmptyJson() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personService.unmarshalFromJson("")
        );
        assertEquals("JSON string cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when unmarshalling invalid JSON")
    void shouldThrowExceptionWhenUnmarshallingInvalidJson() {
        // Given
        String invalidJson = "{invalid json}";
        
        // When & Then
        assertThrows(IOException.class, () -> personService.unmarshalFromJson(invalidJson));
    }

    @Test
    @DisplayName("Should marshal list of Persons to JSON successfully")
    void shouldMarshalListOfPersonsToJson() throws JsonProcessingException {
        // Given
        List<Person> persons = Arrays.asList(testPerson, testPerson2);
        
        // When
        String json = personService.marshalListToJson(persons);
        
        // Then
        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertTrue(json.contains("John Doe"));
        assertTrue(json.contains("Jane Smith"));
    }

    @Test
    @DisplayName("Should unmarshal JSON to list of Persons successfully")
    void shouldUnmarshalJsonToListOfPersons() throws IOException {
        // Given
        String json = "[{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"dob\":\"1990-05-15\",\"timestamp\":\"2024-01-01T10:30:00\"}," +
                     "{\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\",\"dob\":\"1985-08-22\",\"timestamp\":\"2024-01-02T14:45:30\"}]";
        
        // When
        List<Person> persons = personService.unmarshalListFromJson(json);
        
        // Then
        assertNotNull(persons);
        assertEquals(2, persons.size());
        assertEquals("John Doe", persons.get(0).name());
        assertEquals("Jane Smith", persons.get(1).name());
    }

    @Test
    @DisplayName("Should marshal and unmarshal list maintaining data integrity")
    void shouldMarshalAndUnmarshalListWithDataIntegrity() throws Exception {
        // Given
        List<Person> originalPersons = Arrays.asList(testPerson, testPerson2);
        
        // When
        String json = personService.marshalListToJson(originalPersons);
        List<Person> unmarshalledPersons = personService.unmarshalListFromJson(json);
        
        // Then
        assertEquals(originalPersons.size(), unmarshalledPersons.size());
        assertEquals(originalPersons, unmarshalledPersons);
    }

    @Test
    @DisplayName("Should throw exception when marshalling null Person list")
    void shouldThrowExceptionWhenMarshallingNullPersonList() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personService.marshalListToJson(null)
        );
        assertEquals("Person list cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should update Person timestamp successfully")
    void shouldUpdatePersonTimestampSuccessfully() {
        // Given
        LocalDateTime originalTimestamp = testPerson.timestamp();
        
        // When
        Person updatedPerson = personService.updateTimestamp(testPerson);
        
        // Then
        assertNotNull(updatedPerson);
        assertEquals(testPerson.name(), updatedPerson.name());
        assertEquals(testPerson.email(), updatedPerson.email());
        assertEquals(testPerson.dob(), updatedPerson.dob());
        assertNotEquals(originalTimestamp, updatedPerson.timestamp());
        assertTrue(updatedPerson.timestamp().isAfter(originalTimestamp));
    }

    @Test
    @DisplayName("Should throw exception when updating timestamp for null Person")
    void shouldThrowExceptionWhenUpdatingTimestampForNullPerson() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> personService.updateTimestamp(null)
        );
        assertEquals("Person cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should demonstrate builder pattern functionality")
    void shouldDemonstrateBuilderPatternFunctionality() {
        // Given & When
        Person person = Person.builder()
            .name("Test User")
            .email("test@example.com")
            .dob(LocalDate.of(1995, 3, 10))
            .timestamp(LocalDateTime.now())
            .build();
        
        Person modifiedPerson = person.toBuilder()
            .name("Modified User")
            .email("modified@example.com")
            .build();
        
        // Then
        assertNotNull(person);
        assertNotNull(modifiedPerson);
        assertEquals("Test User", person.name());
        assertEquals("Modified User", modifiedPerson.name());
        assertEquals("test@example.com", person.email());
        assertEquals("modified@example.com", modifiedPerson.email());
        assertEquals(person.dob(), modifiedPerson.dob()); // Should remain same
        assertEquals(person.timestamp(), modifiedPerson.timestamp()); // Should remain same
    }
}