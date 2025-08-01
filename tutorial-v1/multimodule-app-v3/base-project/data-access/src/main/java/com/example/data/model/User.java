package com.example.data.model;

import com.example.common.ValidationUtils;

public class User {
    private Long id;
    private String name;
    private String email;
    private int age;
    
    public User() {}
    
    public User(String name, String email, int age) {
        ValidationUtils.requireNonNull(name, "Name cannot be null");
        ValidationUtils.requireNonNull(email, "Email cannot be null");
        
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', age=%d}", 
                           id, name, email, age);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return age == user.age && 
               java.util.Objects.equals(id, user.id) &&
               java.util.Objects.equals(name, user.name) &&
               java.util.Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, email, age);
    }
}
