package com.example.recordsdemo.model;

/**
 * Address record - represents a nested record structure
 */
public record Address(
        String street,
        String city,
        String state,
        String zipCode,
        String country) {

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