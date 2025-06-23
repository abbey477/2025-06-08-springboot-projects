# Java Record Update Methods Guide

## 1. Basic Record with Manual Update Methods

Records are immutable by design, so we need to create new instances with updated values.

```java
public record Person(String name, int age, String email) {
    
    // Manual "update" methods that return new instances
    public Person withName(String newName) {
        return new Person(newName, this.age, this.email);
    }
    
    public Person withAge(int newAge) {
        return new Person(this.name, newAge, this.email);
    }
    
    public Person withEmail(String newEmail) {
        return new Person(this.name, this.age, newEmail);
    }
    
    // Bulk update method
    public Person update(String name, Integer age, String email) {
        return new Person(
            name != null ? name : this.name,
            age != null ? age : this.age,
            email != null ? email : this.email
        );
    }
}
```

### Usage Example:
```java
public class RecordUpdateExample {
    public static void main(String[] args) {
        Person original = new Person("John Doe", 30, "john@example.com");
        
        // Update individual fields
        Person updatedName = original.withName("Jane Doe");
        Person updatedAge = original.withAge(31);
        Person updatedEmail = original.withEmail("jane@example.com");
        
        // Bulk update
        Person bulkUpdated = original.update("Bob Smith", 25, null);
        
        System.out.println("Original: " + original);
        System.out.println("Updated Name: " + updatedName);
        System.out.println("Updated Age: " + updatedAge);
        System.out.println("Updated Email: " + updatedEmail);
        System.out.println("Bulk Updated: " + bulkUpdated);
    }
}
```

## 2. Using Lombok @With Annotation

Lombok provides `@With` annotation to automatically generate "wither" methods:

```java
import lombok.With;

@With
public record PersonLombok(String name, int age, String email) {
    // Lombok automatically generates:
    // - withName(String name)
    // - withAge(int age) 
    // - withEmail(String email)
}
```

### Usage with Lombok:
```java
public class LombokRecordExample {
    public static void main(String[] args) {
        PersonLombok original = new PersonLombok("Alice", 28, "alice@example.com");
        
        // Lombok-generated methods
        PersonLombok updatedName = original.withName("Alice Smith");
        PersonLombok updatedAge = original.withAge(29);
        PersonLombok chained = original
            .withName("Alice Johnson")
            .withAge(30)
            .withEmail("alice.johnson@example.com");
        
        System.out.println("Original: " + original);
        System.out.println("Updated Name: " + updatedName);
        System.out.println("Updated Age: " + updatedAge);
        System.out.println("Chained Updates: " + chained);
    }
}
```

## 3. Builder Pattern with Records (Lombok)

For more complex update scenarios, you can use Lombok's `@Builder` with records:

```java
import lombok.Builder;
import lombok.With;

@Builder
@With
public record Employee(
    String firstName,
    String lastName,
    String department,
    double salary,
    LocalDate hireDate
) {
    // Custom update method combining multiple fields
    public Employee updateName(String firstName, String lastName) {
        return this.withFirstName(firstName).withLastName(lastName);
    }
    
    // Salary increase method
    public Employee increaseSalary(double percentage) {
        return this.withSalary(this.salary * (1 + percentage / 100));
    }
}
```

### Usage with Builder Pattern:
```java
import java.time.LocalDate;

public class BuilderRecordExample {
    public static void main(String[] args) {
        Employee employee = Employee.builder()
            .firstName("John")
            .lastName("Doe")
            .department("Engineering")
            .salary(75000.0)
            .hireDate(LocalDate.of(2020, 1, 15))
            .build();
        
        // Using Lombok @With methods
        Employee promoted = employee
            .withDepartment("Senior Engineering")
            .increaseSalary(15.0);
        
        // Using custom update method
        Employee married = employee.updateName("John", "Smith");
        
        System.out.println("Original: " + employee);
        System.out.println("Promoted: " + promoted);
        System.out.println("Name Changed: " + married);
    }
}
```

## 5. Generic Update Utility Class

### A. Custom Update Builder Pattern

Create a custom builder that can update existing records:

```java
public record Product(
    String name,
    String category,
    double price,
    boolean inStock,
    List<String> tags
) {
    
    // Static factory method
    public static ProductBuilder builder() {
        return new ProductBuilder();
    }
    
    // Instance method for updates
    public ProductBuilder toBuilder() {
        return new ProductBuilder()
            .name(this.name)
            .category(this.category)
            .price(this.price)
            .inStock(this.inStock)
            .tags(new ArrayList<>(this.tags));
    }
    
    public static class ProductBuilder {
        private String name;
        private String category;
        private double price;
        private boolean inStock;
        private List<String> tags = new ArrayList<>();
        
        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public ProductBuilder category(String category) {
            this.category = category;
            return this;
        }
        
        public ProductBuilder price(double price) {
            this.price = price;
            return this;
        }
        
        public ProductBuilder inStock(boolean inStock) {
            this.inStock = inStock;
            return this;
        }
        
        public ProductBuilder tags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }
        
        public ProductBuilder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }
        
        public ProductBuilder removeTag(String tag) {
            this.tags.remove(tag);
            return this;
        }
        
        public ProductBuilder clearTags() {
            this.tags.clear();
            return this;
        }
        
        // Conditional builder methods
        public ProductBuilder applyDiscount(double percentage) {
            if (percentage > 0 && percentage <= 100) {
                this.price = this.price * (1 - percentage / 100);
            }
            return this;
        }
        
        public ProductBuilder markOutOfStock() {
            this.inStock = false;
            return this;
        }
        
        public ProductBuilder markInStock() {
            this.inStock = true;
            return this;
        }
        
        public Product build() {
            return new Product(name, category, price, inStock, List.copyOf(tags));
        }
    }
}
```

### B. Fluent Update Builder with Validation

```java
public record Customer(
    String id,
    String firstName,
    String lastName,
    String email,
    String phone,
    Address address,
    CustomerType type,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }
    
    public CustomerUpdateBuilder update() {
        return new CustomerUpdateBuilder(this);
    }
    
    public static class CustomerBuilder {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private Address address;
        private CustomerType type = CustomerType.REGULAR;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();
        
        // Builder methods...
        public CustomerBuilder id(String id) { this.id = id; return this; }
        public CustomerBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public CustomerBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public CustomerBuilder email(String email) { this.email = email; return this; }
        public CustomerBuilder phone(String phone) { this.phone = phone; return this; }
        public CustomerBuilder address(Address address) { this.address = address; return this; }
        public CustomerBuilder type(CustomerType type) { this.type = type; return this; }
        
        public Customer build() {
            validate();
            return new Customer(id, firstName, lastName, email, phone, address, type, createdAt, updatedAt);
        }
        
        private void validate() {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Valid email is required");
            }
        }
    }
    
    public static class CustomerUpdateBuilder {
        private final Customer original;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private Address address;
        private CustomerType type;
        private boolean hasChanges = false;
        
        public CustomerUpdateBuilder(Customer original) {
            this.original = original;
            this.firstName = original.firstName;
            this.lastName = original.lastName;
            this.email = original.email;
            this.phone = original.phone;
            this.address = original.address;
            this.type = original.type;
        }
        
        public CustomerUpdateBuilder firstName(String firstName) {
            this.firstName = firstName;
            this.hasChanges = true;
            return this;
        }
        
        public CustomerUpdateBuilder lastName(String lastName) {
            this.lastName = lastName;
            this.hasChanges = true;
            return this;
        }
        
        public CustomerUpdateBuilder email(String email) {
            this.email = email;
            this.hasChanges = true;
            return this;
        }
        
        public CustomerUpdateBuilder phone(String phone) {
            this.phone = phone;
            this.hasChanges = true;
            return this;
        }
        
        public CustomerUpdateBuilder address(Address address) {
            this.address = address;
            this.hasChanges = true;
            return this;
        }
        
        public CustomerUpdateBuilder type(CustomerType type) {
            this.type = type;
            this.hasChanges = true;
            return this;
        }
        
        // Convenience methods
        public CustomerUpdateBuilder upgradeToVip() {
            return type(CustomerType.VIP);
        }
        
        public CustomerUpdateBuilder fullName(String firstName, String lastName) {
            return firstName(firstName).lastName(lastName);
        }
        
        public Customer build() {
            if (!hasChanges) {
                return original; // Return same instance if no changes
            }
            
            validate();
            return new Customer(
                original.id,
                firstName,
                lastName,
                email,
                phone,
                address,
                type,
                original.createdAt,
                LocalDateTime.now() // Update timestamp
            );
        }
        
        private void validate() {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Valid email is required");
            }
        }
    }
}

// Supporting classes
public record Address(String street, String city, String state, String zipCode) {}

public enum CustomerType {
    REGULAR, VIP, PREMIUM
}
```

### C. Lombok Builder with Custom Update Methods

```java
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record Order(
    String orderId,
    String customerId,
    List<OrderItem> items,
    OrderStatus status,
    double totalAmount,
    LocalDateTime orderDate,
    Address shippingAddress
) {
    
    // Custom update methods using toBuilder()
    public Order addItem(OrderItem item) {
        List<OrderItem> newItems = new ArrayList<>(this.items);
        newItems.add(item);
        return this.toBuilder()
            .items(newItems)
            .totalAmount(calculateTotal(newItems))
            .build();
    }
    
    public Order removeItem(String itemId) {
        List<OrderItem> newItems = this.items.stream()
            .filter(item -> !item.id().equals(itemId))
            .collect(Collectors.toList());
        
        return this.toBuilder()
            .items(newItems)
            .totalAmount(calculateTotal(newItems))
            .build();
    }
    
    public Order updateStatus(OrderStatus newStatus) {
        return this.withStatus(newStatus);
    }
    
    public Order applyDiscount(double discountPercentage) {
        double discountedAmount = this.totalAmount * (1 - discountPercentage / 100);
        return this.withTotalAmount(discountedAmount);
    }
    
    public Order updateShippingAddress(Address newAddress) {
        return this.withShippingAddress(newAddress);
    }
    
    // Bulk update using builder
    public Order updateOrder(OrderStatus status, Address shippingAddress) {
        return this.toBuilder()
            .status(status)
            .shippingAddress(shippingAddress)
            .build();
    }
    
    private double calculateTotal(List<OrderItem> items) {
        return items.stream()
            .mapToDouble(item -> item.price() * item.quantity())
            .sum();
    }
}

// Supporting records
public record OrderItem(String id, String name, double price, int quantity) {}

public enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}
```

### Usage Examples for Advanced Builder Patterns:

```java
import java.time.LocalDateTime;
import java.util.List;

public class AdvancedBuilderExample {
    public static void main(String[] args) {
        // 1. Custom Product Builder
        Product laptop = Product.builder()
            .name("Gaming Laptop")
            .category("Electronics")
            .price(1299.99)
            .inStock(true)
            .tags(List.of("gaming", "laptop", "electronics"))
            .build();
        
        // Update using toBuilder()
        Product updatedLaptop = laptop.toBuilder()
            .applyDiscount(15.0)
            .addTag("sale")
            .build();
        
        // 2. Customer Update Builder
        Customer customer = Customer.builder()
            .id("CUST001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("555-1234")
            .address(new Address("123 Main St", "Anytown", "CA", "12345"))
            .build();
        
        // Update customer using fluent builder
        Customer updatedCustomer = customer.update()
            .upgradeToVip()
            .phone("555-5678")
            .build();
        
        // 3. Order with Lombok Builder
        Order order = Order.builder()
            .orderId("ORD001")
            .customerId("CUST001")
            .items(List.of(
                new OrderItem("ITEM001", "Laptop", 1299.99, 1),
                new OrderItem("ITEM002", "Mouse", 29.99, 2)
            ))
            .status(OrderStatus.PENDING)
            .totalAmount(1359.97)
            .orderDate(LocalDateTime.now())
            .shippingAddress(new Address("123 Main St", "Anytown", "CA", "12345"))
            .build();
        
        // Update order using custom methods
        Order updatedOrder = order
            .addItem(new OrderItem("ITEM003", "Keyboard", 89.99, 1))
            .updateStatus(OrderStatus.CONFIRMED)
            .applyDiscount(10.0);
        
        // Display results
        System.out.println("Original Product: " + laptop);
        System.out.println("Updated Product: " + updatedLaptop);
        System.out.println("Original Customer: " + customer);
        System.out.println("Updated Customer: " + updatedCustomer);
        System.out.println("Original Order Total: " + order.totalAmount());
        System.out.println("Updated Order Total: " + updatedOrder.totalAmount());
    }
}
```

## 4. Generic Update Utility Class

For advanced scenarios, you can create a generic update utility:

```java
import java.util.function.Function;

public class RecordUpdater {
    
    public static <T> T updateIf(T record, boolean condition, Function<T, T> updater) {
        return condition ? updater.apply(record) : record;
    }
    
    public static <T> T chain(T record, Function<T, T>... updaters) {
        T result = record;
        for (Function<T, T> updater : updaters) {
            result = updater.apply(result);
        }
        return result;
    }
}
```

### Usage of Generic Updater:
```java
public class GenericUpdaterExample {
    public static void main(String[] args) {
        Person person = new Person("John", 30, "john@example.com");
        
        // Conditional update
        Person updated = RecordUpdater.updateIf(
            person,
            person.age() < 35,
            p -> p.withAge(p.age() + 1)
        );
        
        // Chained updates
        Person chainedUpdate = RecordUpdater.chain(
            person,
            p -> p.withName("John Smith"),
            p -> p.withAge(31),
            p -> p.withEmail("john.smith@example.com")
        );
        
        System.out.println("Original: " + person);
        System.out.println("Conditional Update: " + updated);
        System.out.println("Chained Update: " + chainedUpdate);
    }
}
```

## 6. Record with Validation and Update

Adding validation to record updates:

```java
public record ValidatedPerson(String name, int age, String email) {
    
    public ValidatedPerson {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public ValidatedPerson withValidatedAge(int newAge) {
        return new ValidatedPerson(this.name, newAge, this.email);
    }
    
    public ValidatedPerson withValidatedEmail(String newEmail) {
        return new ValidatedPerson(this.name, this.age, newEmail);
    }
}
```

## Maven Dependencies

Add these dependencies to your `pom.xml` for Lombok support:

```xml
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Key Takeaways

1. **Records are immutable** - all update operations create new instances
2. **Manual methods** give you full control but require more boilerplate
3. **Lombok @With** automatically generates update methods
4. **Builder pattern** works well for complex records with many fields
5. **Validation** can be added to constructors and update methods
6. **Method chaining** enables fluent update patterns

Choose the approach that best fits your use case:
- Use manual methods for simple records with custom logic
- Use Lombok @With for reducing boilerplate
- Use Builder pattern for complex initialization and updates
- Combine approaches as needed for your specific requirements