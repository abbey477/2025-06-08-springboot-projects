# Jackson Serialization Annotations

This guide covers Jackson annotations that control how Java objects are serialized to JSON.

## @JsonProperty

**Purpose**: Customizes the property name in the JSON output.

**Usage**: Applied to fields, getters, or setters to specify the JSON property name.

```java
public class User {
    @JsonProperty("full_name")
    private String name;
    
    @JsonProperty("user_age")
    private int age;
    
    // Constructors, getters, setters...
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

// Usage
ObjectMapper mapper = new ObjectMapper();
User user = new User("John Doe", 30);
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "full_name": "John Doe",
  "user_age": 30
}
```

---

## @JsonIgnore

**Purpose**: Excludes a property from serialization completely.

**Usage**: Applied to fields or getter methods to prevent them from appearing in JSON.

```java
public class User {
    private String username;
    
    @JsonIgnore
    private String password;
    
    @JsonIgnore
    private String ssn;
    
    private String email;
    
    // Constructors, getters, setters...
    public User(String username, String password, String ssn, String email) {
        this.username = username;
        this.password = password;
        this.ssn = ssn;
        this.email = email;
    }
}

// Usage
User user = new User("johndoe", "secret123", "123-45-6789", "john@email.com");
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "username": "johndoe",
  "email": "john@email.com"
}
```

---

## @JsonIgnoreProperties

**Purpose**: Ignores multiple properties at the class level.

**Usage**: Applied to class to specify which properties to ignore during serialization.

```java
@JsonIgnoreProperties({"password", "ssn", "internalId"})
public class User {
    private String username;
    private String password;
    private String ssn;
    private String email;
    private Long internalId;
    private String firstName;
    private String lastName;
    
    // Constructor
    public User(String username, String password, String ssn, 
                String email, Long internalId, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.ssn = ssn;
        this.email = email;
        this.internalId = internalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and setters...
}

// Usage
User user = new User("johndoe", "secret", "123-45-6789", 
                    "john@email.com", 12345L, "John", "Doe");
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "username": "johndoe",
  "email": "john@email.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

---

## @JsonInclude

**Purpose**: Controls when properties are included in JSON based on their values.

**Usage**: Applied to fields or classes to specify inclusion criteria.

```java
public class User {
    private String name;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> hobbies;
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age; // 0 is default for int
    
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Optional<String> phoneNumber;
    
    // Constructor
    public User(String name, String email, List<String> hobbies, 
                int age, Optional<String> phoneNumber) {
        this.name = name;
        this.email = email;
        this.hobbies = hobbies;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters...
}

// Usage examples
// Case 1: User with all values
User user1 = new User("John", "john@email.com", 
                     Arrays.asList("reading", "swimming"), 
                     25, Optional.of("123-456-7890"));

// Case 2: User with null/empty values
User user2 = new User("Jane", null, new ArrayList<>(), 
                     0, Optional.empty());

String json1 = mapper.writeValueAsString(user1);
String json2 = mapper.writeValueAsString(user2);

System.out.println("User 1: " + json1);
System.out.println("User 2: " + json2);
```

**Output**:
```json
User 1: {
  "name": "John",
  "email": "john@email.com",
  "hobbies": ["reading", "swimming"],
  "age": 25,
  "phoneNumber": "123-456-7890"
}

User 2: {
  "name": "Jane"
}
```

---

## @JsonPropertyOrder

**Purpose**: Specifies the order of properties in the JSON output.

**Usage**: Applied to class to define property ordering.

```java
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "age", "department"})
public class Employee {
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private Long id;
    private String department;
    
    // Constructor
    public Employee(Long id, String firstName, String lastName, 
                   String email, int age, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.department = department;
    }
    
    // Getters...
}

// Usage
Employee emp = new Employee(1001L, "John", "Doe", 
                          "john.doe@company.com", 30, "Engineering");
String json = mapper.writeValueAsString(emp);
System.out.println(json);
```

**Output**:
```json
{
  "id": 1001,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "age": 30,
  "department": "Engineering"
}
```

**Alphabetical ordering**:
```java
@JsonPropertyOrder(alphabetic = true)
public class Product {
    private String name;
    private double price;
    private String category;
    private boolean available;
    
    // Constructor, getters...
}
```

---

## @JsonRootName

**Purpose**: Wraps the JSON output in a root element.

**Usage**: Applied to class, requires enabling WRAP_ROOT_VALUE feature.

```java
@JsonRootName("user")
public class User {
    private String name;
    private int age;
    private String email;
    
    // Constructor
    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Getters...
}

// Usage
ObjectMapper mapper = new ObjectMapper();
mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

User user = new User("John Doe", 30, "john@email.com");
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "user": {
    "name": "John Doe",
    "age": 30,
    "email": "john@email.com"
  }
}
```

---

## @JsonValue

**Purpose**: Serializes the entire object as a single value instead of a JSON object.

**Usage**: Applied to a method or field to use its value as the JSON representation.

```java
public enum Status {
    ACTIVE("A", "Active"),
    INACTIVE("I", "Inactive"),
    PENDING("P", "Pending");
    
    private String code;
    private String description;
    
    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    public String getCode() {
        return code;
    }
}

public class User {
    private String name;
    private Status status;
    
    public User(String name, Status status) {
        this.name = name;
        this.status = status;
    }
    
    // Getters...
}

// Usage
User user = new User("John", Status.ACTIVE);
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "name": "John",
  "status": "A"
}
```

**Alternative example with custom class**:
```java
public class Money {
    private double amount;
    private String currency;
    
    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    @JsonValue
    public String toString() {
        return amount + " " + currency;
    }
}

// Usage
Money price = new Money(99.99, "USD");
String json = mapper.writeValueAsString(price);
System.out.println(json); // Output: "99.99 USD"
```

---

## @JsonRawValue

**Purpose**: Serializes a property value as raw JSON without escaping.

**Usage**: Applied to String fields containing JSON content.

```java
public class Configuration {
    private String name;
    
    @JsonRawValue
    private String settings;
    
    @JsonRawValue
    private String metadata;
    
    // Constructor
    public Configuration(String name, String settings, String metadata) {
        this.name = name;
        this.settings = settings;
        this.metadata = metadata;
    }
    
    // Getters...
}

// Usage
String settingsJson = "{\"theme\":\"dark\",\"notifications\":true,\"language\":\"en\"}";
String metadataJson = "{\"version\":\"1.0\",\"author\":\"John Doe\"}";

Configuration config = new Configuration("AppConfig", settingsJson, metadataJson);
String json = mapper.writeValueAsString(config);
System.out.println(json);
```

**Output**:
```json
{
  "name": "AppConfig",
  "settings": {"theme":"dark","notifications":true,"language":"en"},
  "metadata": {"version":"1.0","author":"John Doe"}
}
```

**Without @JsonRawValue (for comparison)**:
```json
{
  "name": "AppConfig",
  "settings": "{\"theme\":\"dark\",\"notifications\":true,\"language\":\"en\"}",
  "metadata": "{\"version\":\"1.0\",\"author\":\"John Doe\"}"
}
```

---

## @JsonUnwrapped

**Purpose**: Unwraps nested object properties to the parent level.

**Usage**: Applied to nested object fields to flatten the JSON structure.

```java
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    
    // Constructor
    public Address(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
    
    // Getters...
}

public class User {
    private String name;
    private int age;
    
    @JsonUnwrapped
    private Address address;
    
    @JsonUnwrapped(prefix = "work_")
    private Address workAddress;
    
    // Constructor
    public User(String name, int age, Address address, Address workAddress) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.workAddress = workAddress;
    }
    
    // Getters...
}

// Usage
Address homeAddr = new Address("123 Main St", "New York", "NY", "10001");
Address workAddr = new Address("456 Business Ave", "New York", "NY", "10002");

User user = new User("John Doe", 30, homeAddr, workAddr);
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "name": "John Doe",
  "age": 30,
  "street": "123 Main St",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "work_street": "456 Business Ave",
  "work_city": "New York",
  "work_state": "NY",
  "work_zipCode": "10002"
}
```

**Without @JsonUnwrapped (for comparison)**:
```json
{
  "name": "John Doe",
  "age": 30,
  "address": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001"
  },
  "workAddress": {
    "street": "456 Business Ave",
    "city": "New York",
    "state": "NY",
    "zipCode": "10002"
  }
}
```

---

## Complete Example

Here's a comprehensive example combining multiple serialization annotations:

```java
@JsonPropertyOrder({"id", "personalInfo", "contactInfo", "metadata"})
@JsonIgnoreProperties({"internalNotes"})
public class CompleteUser {
    @JsonProperty("user_id")
    private Long id;
    
    @JsonUnwrapped(prefix = "personal_")
    private PersonalInfo personalInfo;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    
    @JsonIgnore
    private String password;
    
    @JsonRawValue
    private String metadata;
    
    private String internalNotes; // Will be ignored due to class-level annotation
    
    // Nested class
    public static class PersonalInfo {
        private String firstName;
        private String lastName;
        private int age;
        
        public PersonalInfo(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        
        // Getters...
    }
    
    // Constructor
    public CompleteUser(Long id, PersonalInfo personalInfo, String email, 
                       String password, String metadata, String internalNotes) {
        this.id = id;
        this.personalInfo = personalInfo;
        this.email = email;
        this.password = password;
        this.metadata = metadata;
        this.internalNotes = internalNotes;
    }
    
    // Getters...
}

// Usage
PersonalInfo info = new PersonalInfo("John", "Doe", 30);
String metadataJson = "{\"created\":\"2024-01-01\",\"source\":\"web\"}";

CompleteUser user = new CompleteUser(
    1001L, info, "john@email.com", "secret123", 
    metadataJson, "Internal notes here"
);

String json = mapper.writeValueAsString(user);
System.out.println(json);
```

**Output**:
```json
{
  "user_id": 1001,
  "personal_firstName": "John",
  "personal_lastName": "Doe",
  "personal_age": 30,
  "email": "john@email.com",
  "metadata": {"created":"2024-01-01","source":"web"}
}
```

This example demonstrates how multiple serialization annotations work together to create precisely formatted JSON output while controlling which data is included and how it's structured.