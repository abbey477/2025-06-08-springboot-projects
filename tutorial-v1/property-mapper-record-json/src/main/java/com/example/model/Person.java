package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Person(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Date of birth cannot be null")
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dob,

        @NotNull(message = "Timestamp cannot be null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp) {

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public PersonBuilder toBuilder() {
        return new PersonBuilder()
                .name(this.name)
                .email(this.email)
                .dob(this.dob)
                .timestamp(this.timestamp);
    }

    public static class PersonBuilder {
        private String name;
        private String email;
        private LocalDate dob;
        private LocalDateTime timestamp;

        PersonBuilder() {
        }

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PersonBuilder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public PersonBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Person build() {
            return new Person(name, email, dob, timestamp);
        }

        @Override
        public String toString() {
            return "PersonBuilder{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", dob=" + dob +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}