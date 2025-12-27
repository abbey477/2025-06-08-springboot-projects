package com.example.main;

import com.example.model.Person;
import com.example.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonDemoRunner implements CommandLineRunner {
    
    private final PersonService personService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Person Demo Application");
        
        // Demo 1: Create Person using builder pattern
        Person person1 = Person.builder()
            .name("Alice Johnson")
            .email("alice.johnson@example.com")
            .dob(LocalDate.of(1988, 3, 12))
            .timestamp(LocalDateTime.now())
            .build();
        
        log.info("Created person: {}", person1);
        
        // Demo 2: Marshal to JSON
        String json1 = personService.marshalToJson(person1);
        log.info("Marshalled to JSON: {}", json1);
        
        // Demo 3: Unmarshal from JSON
        Person unmarshalledPerson = personService.unmarshalFromJson(json1);
        log.info("Unmarshalled from JSON: {}", unmarshalledPerson);
        
        // Demo 4: Use toBuilder to create modified version
        Person modifiedPerson = person1.toBuilder()
            .name("Alice Smith")
            .email("alice.smith@example.com")
            .build();
        
        log.info("Modified person using toBuilder: {}", modifiedPerson);
        
        // Demo 5: Update timestamp
        Person personWithUpdatedTimestamp = personService.updateTimestamp(person1);
        log.info("Person with updated timestamp: {}", personWithUpdatedTimestamp);
        
        // Demo 6: Work with list of persons
        Person person2 = Person.builder()
            .name("Bob Wilson")
            .email("bob.wilson@example.com")
            .dob(LocalDate.of(1992, 7, 25))
            .timestamp(LocalDateTime.now())
            .build();
        
        List<Person> persons = Arrays.asList(person1, person2, modifiedPerson);
        
        // Demo 7: Marshal list to JSON
        String jsonList = personService.marshalListToJson(persons);
        log.info("Marshalled list to JSON: {}", jsonList);
        
        // Demo 8: Unmarshal list from JSON
        List<Person> unmarshalledPersons = personService.unmarshalListFromJson(jsonList);
        log.info("Unmarshalled {} persons from JSON", unmarshalledPersons.size());
        
        unmarshalledPersons.forEach(person -> 
            log.info("Person from list: {}", person));
        
        log.info("Person Demo Application completed successfully");
    }
}