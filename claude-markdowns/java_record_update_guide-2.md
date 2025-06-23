# Java Records Immutability Patterns - Complete Guide

Records in Java are immutable by design - all fields are `final` and there are no setter methods. Here are several approaches to create new instances with updated values.

## 1. Basic Record with Manual Copy Methods

```java
public record Person(String name, int age, String email) {
    
    // Copy method to create new instance with updated name
    public Person withName(String newName) {
        return new Person(newName, this.age, this.email);
    }
    
    // Copy method to create new instance with updated age
    public Person withAge(int newAge) {
        return new Person(this.name, newAge, this.email);
    }
    
    // Copy method to create new instance with updated email
    public Person withEmail(String newEmail) {
        return new Person(this.name, this.age, newEmail);
    }
    
    public static void main(String[] args) {
        Person original = new Person("John Doe", 30, "john@example.com");
        System.out.println("Original: " + original);
        
        // Create new instances with updated values
        Person withNewName = original.withName("Jane Doe");
        Person withNewAge = original.withAge(31);
        Person withNewEmail = original.withEmail("jane@example.com");
        
        System.out.println("With new name: " + withNewName);
        System.out.println("With new age: " + withNewAge);
        System.out.println("With new email: " + withNewEmail);
        
        // Chain multiple updates
        Person updated = original.withName("Alice")
                                .withAge(25)
                                .withEmail("alice@example.com");
        System.out.println("Multiple updates: " + updated);
    }
}
```

## 2. Record with Lombok @With Annotation

```java
import lombok.With;

@With
public record Employee(String firstName, String lastName, int age, String department, double salary) {
    
    public static void main(String[] args) {
        Employee original = new Employee("John", "Smith", 30, "Engineering", 75000.0);
        System.out.println("Original: " + original);
        
        // Lombok automatically generates withXxx methods
        Employee withNewSalary = original.withSalary(80000.0);
        Employee withNewDepartment = original.withDepartment("Management");
        Employee withNewAge = original.withAge(31);
        
        System.out.println("With new salary: " + withNewSalary);
        System.out.println("With new department: " + withNewDepartment);
        System.out.println("With new age: " + withNewAge);
        
        // Chain updates
        Employee promoted = original.withDepartment("Senior Engineering")
                                  .withSalary(90000.0)
                                  .withAge(31);
        System.out.println("Promoted: " + promoted);
    }
}
```

## 3. Record with Builder Pattern

```java
public record Product(String name, String category, double price, boolean inStock, String description) {
    
    // Static method to create builder from existing record
    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .category(this.category)
                .price(this.price)
                .inStock(this.inStock)
                .description(this.description);
    }
    
    // Static method to create new builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String category;
        private double price;
        private boolean inStock;
        private String description;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder price(double price) {
            this.price = price;
            return this;
        }
        
        public Builder inStock(boolean inStock) {
            this.inStock = inStock;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Product build() {
            return new Product(name, category, price, inStock, description);
        }
    }
    
    public static void main(String[] args) {
        // Create original product
        Product original = Product.builder()
                .name("Laptop")
                .category("Electronics")
                .price(999.99)
                .inStock(true)
                .description("High-performance laptop")
                .build();
        
        System.out.println("Original: " + original);
        
        // Create updated version using builder
        Product updated = original.toBuilder()
                .price(899.99)
                .description("High-performance laptop - ON SALE!")
                .build();
        
        System.out.println("Updated: " + updated);
        
        // Create another variant
        Product outOfStock = original.toBuilder()
                .inStock(false)
                .build();
        
        System.out.println("Out of stock: " + outOfStock);
    }
}
```

## 4. Record with Lombok Builder

```java
import lombok.Builder;

@Builder(toBuilder = true)
public record Order(String orderId, String customerId, String productName, 
                   int quantity, double unitPrice, String status) {
    
    // Custom method to calculate total
    public double total() {
        return quantity * unitPrice;
    }
    
    public static void main(String[] args) {
        // Create original order using Lombok builder
        Order original = Order.builder()
                .orderId("ORD-001")
                .customerId("CUST-123")
                .productName("Widget")
                .quantity(5)
                .unitPrice(19.99)
                .status("PENDING")
                .build();
        
        System.out.println("Original order: " + original);
        System.out.println("Total: $" + original.total());
        
        // Update order status using toBuilder
        Order shipped = original.toBuilder()
                .status("SHIPPED")
                .build();
        
        System.out.println("Shipped order: " + shipped);
        
        // Update quantity and recalculate
        Order updatedQuantity = original.toBuilder()
                .quantity(10)
                .build();
        
        System.out.println("Updated quantity: " + updatedQuantity);
        System.out.println("New total: $" + updatedQuantity.total());
        
        // Create modified order with price change
        Order discounted = original.toBuilder()
                .unitPrice(15.99)
                .status("DISCOUNTED")
                .build();
        
        System.out.println("Discounted order: " + discounted);
        System.out.println("Discounted total: $" + discounted.total());
    }
}
```

## 5. Complex Record with Nested Objects

```java
import java.util.List;

public record Address(String street, String city, String state, String zipCode) {
    public Address withStreet(String newStreet) {
        return new Address(newStreet, this.city, this.state, this.zipCode);
    }
    
    public Address withCity(String newCity) {
        return new Address(this.street, newCity, this.state, this.zipCode);
    }
}

public record Customer(String id, String name, Address address, List<String> phoneNumbers) {
    
    // Method to update address
    public Customer withAddress(Address newAddress) {
        return new Customer(this.id, this.name, newAddress, this.phoneNumbers);
    }
    
    // Method to update name
    public Customer withName(String newName) {
        return new Customer(this.id, newName, this.address, this.phoneNumbers);
    }
    
    // Method to update phone numbers
    public Customer withPhoneNumbers(List<String> newPhoneNumbers) {
        return new Customer(this.id, this.name, this.address, newPhoneNumbers);
    }
    
    // Method to add phone number
    public Customer addPhoneNumber(String phoneNumber) {
        List<String> updatedPhones = new java.util.ArrayList<>(this.phoneNumbers);
        updatedPhones.add(phoneNumber);
        return new Customer(this.id, this.name, this.address, updatedPhones);
    }
    
    // Method to update nested address field
    public Customer withStreet(String newStreet) {
        Address updatedAddress = this.address.withStreet(newStreet);
        return this.withAddress(updatedAddress);
    }
    
    public static void main(String[] args) {
        Address originalAddress = new Address("123 Main St", "Springfield", "IL", "62701");
        Customer original = new Customer("C001", "John Doe", originalAddress, 
                                       List.of("555-1234", "555-5678"));
        
        System.out.println("Original: " + original);
        
        // Update nested address
        Customer movedCustomer = original.withStreet("456 Oak Ave");
        System.out.println("Moved customer: " + movedCustomer);
        
        // Add phone number
        Customer withExtraPhone = original.addPhoneNumber("555-9999");
        System.out.println("With extra phone: " + withExtraPhone);
        
        // Update entire address
        Address newAddress = new Address("789 Pine St", "Chicago", "IL", "60601");
        Customer relocated = original.withAddress(newAddress);
        System.out.println("Relocated: " + relocated);
        
        // Chain multiple updates
        Customer updated = original.withName("Jane Doe")
                                 .withStreet("321 Elm St")
                                 .addPhoneNumber("555-0000");
        System.out.println("Multiple updates: " + updated);
    }
}
```

## Key Patterns Summary

### Manual Copy Methods (`withXxx`)
- **Pros**: Most explicit and controllable, no external dependencies
- **Cons**: More boilerplate code
- **Best for**: Simple records with few fields

### Lombok @With
- **Pros**: Automatically generates `withXxx` methods, reduces boilerplate
- **Cons**: Requires Lombok dependency
- **Best for**: Straightforward field updates

### Builder Pattern (Manual)
- **Pros**: Most flexible for complex updates, good for records with many optional fields
- **Cons**: Significant boilerplate code
- **Best for**: Complex records where you need fine control

### Lombok @Builder with toBuilder
- **Pros**: Combines builder flexibility with minimal boilerplate, `toBuilder = true` enables creating builders from existing instances
- **Cons**: Requires Lombok dependency
- **Best for**: Complex records with many fields

## Best Practices

1. **Use manual `withXxx` methods** for simple records with few fields
2. **Use Lombok @With** for medium complexity records
3. **Use Builder pattern** for complex records with many fields or optional parameters
4. **For nested objects**, provide methods that handle deep updates
5. **Always return new instances**, never modify existing ones
6. **Consider providing convenience methods** for common update patterns
7. **Use immutable collections** (like `List.of()`) when possible
8. **Chain method calls** for multiple updates to improve readability

## Maven Dependencies

If using Lombok, add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```

## Gradle Dependencies

If using Lombok, add to your `build.gradle`:

```gradle
dependencies {
    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
}
```

The choice of pattern depends on your project's complexity, team preferences, and whether you're using Lombok in your build setup.