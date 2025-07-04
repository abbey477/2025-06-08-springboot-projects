package com.example.recordsdemo.model;

/**
 * Contact record - used in collections within Person record
 */
public record Contact(
        String type,    // EMAIL, PHONE, LINKEDIN, etc.
        String value) {

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