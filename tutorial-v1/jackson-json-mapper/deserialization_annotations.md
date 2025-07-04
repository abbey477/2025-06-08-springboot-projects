# Jackson Deserialization Annotations

This guide covers Jackson annotations that control how JSON is deserialized into Java objects.

## @JsonProperty

**Purpose**: Maps a JSON property to a Java field or setter method.

**Usage**: Applied to fields, setters, or constructor parameters to specify the JSON property name.

```java
public class User {
    @JsonProperty("full_name")
    private String name;
    
    @JsonProperty("user_age")
    private int age;
    
    // Default constructor required for Jackson
    public User() {}
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}

// Usage
String json = "{\"full_name\":\"John Doe\",\"user_age\":30}";
ObjectMapper mapper = new ObjectMapper();
User user = mapper.readValue(json, User.class);

System.out.println("Name: " + user.getName());
System.out.println("Age: " + user.getAge());
```

**Output**:
```
Name: John Doe
Age: 30
```

---

## @JsonAlias

**Purpose**: Accepts multiple JSON property names for the same Java field.

**Usage**: Applied to fields or setters to define alternative property names.

```java
public class User {
    @JsonAlias({"full_name", "fullName", "name", "userName"})
    private String name;
    
    @JsonAlias({"user_age", "userAge", "years"})
    private int age;
    
    // Default constructor
    public User() {}
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}

// Usage - different JSON formats work
String json1 = "{\"full_name\":\"John Doe\",\"user_age\":30}";
String json2 = "{\"fullName\":\"Jane Smith\",\"userAge\":25}";
String json3 = "{\"name\":\"Bob Johnson\",\"years\":35}";

ObjectMapper mapper = new ObjectMapper();

User user1 = mapper.readValue(json1, User.class);
User user2 = mapper.readValue(json2, User.class);
User user3 = mapper.readValue(json3, User.class);

System.out.println("User 1: " + user1.getName() + ", " + user1.getAge());
System.out.println("User 2: " + user2.getName() + ", " + user2.getAge());
System.out.println("User 3: " + user3.getName() + ", " + user3.getAge());
```

**Output**:
```
User 1: John Doe, 30
User 2: Jane Smith, 25
User 3: Bob Johnson, 35
```

---

## @JsonIgnoreProperties

**Purpose**: Ignores unknown properties during deserialization or ignores specific properties.

**Usage**: Applied to class to handle unknown JSON properties gracefully.

```java
// Ignore unknown properties
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private int age;
    
    public User() {}
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}

// Ignore specific properties
@JsonIgnoreProperties({"password", "ssn"})
public class SecureUser {
    private String username;
    private String email;
    private String password; // Will be ignored
    private String ssn;      // Will be ignored
    
    public SecureUser() {}
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Usage
String jsonWithExtra = "{\"name\":\"John\",\"age\":30,\"unknownProperty\":\"value\",\"extraField\":123}";
String jsonWithSensitive = "{\"username\":\"john\",\"email\":\"john@email.com\",\"password\":\"secret\",\"ssn\":\"123-45-6789\"}";

ObjectMapper mapper = new ObjectMapper();

// This works because ignoreUnknown = true
User user = mapper.readValue(jsonWithExtra, User.class);
System.out.println("User: " + user.getName() + ", " + user.getAge());

// Password and SSN are ignored
SecureUser secureUser = mapper.readValue(jsonWithSensitive, SecureUser.class);
System.out.println("Secure User: " + secureUser.getUsername() + ", " + secureUser.getEmail());
```

**Output**:
```
User: John, 30
Secure User: john, john@email.com
```

---

## @JsonCreator

**Purpose**: Specifies which constructor or factory method to use for deserialization.

**Usage**: Applied to constructors or static factory methods with parameter annotations.

```java
public class User {
    private final String name;
    private final int age;
    private final String email;
    
    // Constructor-based deserialization
    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("age") int age,
                @JsonProperty("email") String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
}

// Factory method example
public class Product {
    private final String name;
    private final double price;
    private final String category;
    
    private Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    @JsonCreator
    public static Product create(@JsonProperty("productName") String name,
                                @JsonProperty("price") double price,
                                @JsonProperty("category") String category) {
        return new Product(name, price, category);
    }
    
    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}

// Usage
String userJson = "{\"name\":\"John Doe\",\"age\":30,\"email\":\"john@email.com\"}";
String productJson = "{\"productName\":\"Laptop\",\"price\":999.99,\"category\":\"Electronics\"}";

ObjectMapper mapper = new ObjectMapper();

User user = mapper.readValue(userJson, User.class);
Product product = mapper.readValue(productJson, Product.class);

System.out.println("User: " + user.getName() + ", " + user.getAge() + ", " + user.getEmail());
System.out.println("Product: " + product.getName() + ", $" + product.getPrice() + ", " + product.getCategory());
```

**Output**:
```
User: John Doe, 30, john@email.com
Product: Laptop, $999.99, Electronics
```

---

## @JsonSetter

**Purpose**: Specifies a setter method for deserialization with a specific JSON property name.

**Usage**: Applied to setter methods to customize property mapping.

```java
public class User {
    private String name;
    private int age;
    private String email;
    
    public User() {}
    
    @JsonSetter("full_name")
    public void setName(String name) {
        this.name = name.trim().toUpperCase(); // Custom processing
    }
    
    @JsonSetter("user_age")
    public void setAge(int age) {
        this.age = Math.max(0, age); // Ensure non-negative age
    }
    
    // Alternative property names
    @JsonSetter("alternative_email")
    public void setAlternativeEmail(String email) {
        this.email = email.toLowerCase();
    }
    
    // Regular setter
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
}

// Usage with different JSON structures
String json1 = "{\"full_name\":\"  john doe  \",\"user_age\":30,\"email\":\"John@Email.Com\"}";
String json2 = "{\"full_name\":\"jane smith\",\"user_age\":-5,\"alternative_email\":\"JANE@EMAIL.COM\"}";

ObjectMapper mapper = new ObjectMapper();

User user1 = mapper.readValue(json1, User.class);
User user2 = mapper.readValue(json2, User.class);

System.out.println("User 1: " + user1.getName() + ", " + user1.getAge() + ", " + user1.getEmail());
System.out.println("User 2: " + user2.getName() + ", " + user2.getAge() + ", " + user2.getEmail());
```

**Output**:
```
User 1: JOHN DOE, 30, John@Email.Com
User 2: JANE SMITH, 0, jane@email.com
```

---

## @JsonAnySetter

**Purpose**: Handles unknown properties dynamically by collecting them in a map or processing them with a method.

**Usage**: Applied to a method that accepts key-value pairs for unknown properties.

```java
public class FlexibleUser {
    private String name;
    private int age;
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    public FlexibleUser() {}
    
    // Handle unknown properties
    @JsonAnySetter
    public void setAdditionalProperty(String key, Object value) {
        additionalProperties.put(key, value);
        System.out.println("Setting additional property: " + key + " = " + value);
    }
    
    // Regular setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<String, Object> getAdditionalProperties() { return additionalProperties; }
}

// Alternative approach - direct map handling
public class ConfigurableUser {
    private String name;
    private int age;
    private Map<String, Object> properties = new HashMap<>();
    
    public ConfigurableUser() {}
    
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        // Custom logic for handling unknown properties
        if (key.startsWith("config_")) {
            properties.put(key.substring(7), value); // Remove prefix
        } else if (key.equals("fullName")) {
            this.name = (String) value; // Map to existing property
        } else {
            properties.put(key, value);
        }
    }
    
    // Setters and getters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<String, Object> getProperties() { return properties; }
}

// Usage
String json1 = "{\"name\":\"John\",\"age\":30,\"department\":\"Engineering\",\"salary\":75000,\"bonus\":5000}";
String json2 = "{\"fullName\":\"Jane Smith\",\"age\":25,\"config_theme\":\"dark\",\"config_language\":\"en\",\"unknownField\":\"value\"}";

ObjectMapper mapper = new ObjectMapper();

FlexibleUser user1 = mapper.readValue(json1, FlexibleUser.class);
ConfigurableUser user2 = mapper.readValue(json2, ConfigurableUser.class);

System.out.println("\nFlexible User:");
System.out.println("Name: " + user1.getName() + ", Age: " + user1.getAge());
System.out.println("Additional properties: " + user1.getAdditionalProperties());

System.out.println("\nConfigurable User:");
System.out.println("Name: " + user2.getName() + ", Age: " + user2.getAge());
System.out.println("Properties: " + user2.getProperties());
```

**Output**:
```
Setting additional property: department = Engineering
Setting additional property: salary = 75000
Setting additional property: bonus = 5000

Flexible User:
Name: John, Age: 30
Additional properties: {department=Engineering, salary=75000, bonus=5000}

Configurable User:
Name: Jane Smith, Age: 25
Properties: {theme=dark, language=en, unknownField=value}
```

---

## @JsonAutoDetect

**Purpose**: Controls the visibility of properties, fields, and methods for Jackson processing.

**Usage**: Applied to class to specify which members Jackson should consider.

```java
// Default - only public fields and public getters/setters
public class DefaultUser {
    public String publicName;
    private String privateName;
    protected String protectedName;
    
    public String getPublicName() { return publicName; }
    private String getPrivateName() { return privateName; }
}

// Custom visibility settings
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class FieldOnlyUser {
    private String name;
    private int age;
    private String email;
    
    // These getters/setters won't be used for JSON processing
    public String getName() { return "GETTER: " + name; }
    public void setName(String name) { this.name = "SETTER: " + name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Method-only processing
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY
)
public class MethodOnlyUser {
    private String firstName;
    private String lastName;
    
    public String getName() { return firstName + " " + lastName; }
    public void setName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}

// Usage
String json = "{\"name\":\"John Doe\",\"age\":30,\"email\":\"john@email.com\"}";

ObjectMapper mapper = new ObjectMapper();

FieldOnlyUser fieldUser = mapper.readValue(json, FieldOnlyUser.class);
MethodOnlyUser methodUser = mapper.readValue("{\"name\":\"Jane Smith\"}", MethodOnlyUser.class);

System.out.println("Field User - Name: " + fieldUser.getName()); // Will show "GETTER: John Doe"
System.out.println("Method User - Name: " + methodUser.getName());
System.out.println("Method User - First: " + methodUser.getFirstName() + ", Last: " + methodUser.getLastName());
```

**Output**:
```
Field User - Name: GETTER: John Doe
Method User - Name: Jane Smith
Method User - First: Jane, Last: Smith
```

---

## Complete Deserialization Example

Here's a comprehensive example combining multiple deserialization annotations:

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ComprehensiveUser {
    @JsonProperty("user_id")
    private Long id;
    
    @JsonAlias({"full_name", "fullName", "name"})
    private String name;
    
    private int age;
    private String email;
    private Map<String, Object> metadata = new HashMap<>();
    private List<String> preferences = new ArrayList<>();
    
    // Constructor for immutable fields
    @JsonCreator
    public ComprehensiveUser(@JsonProperty("user_id") Long id) {
        this.id = id;
    }
    
    // Custom setter with validation
    @JsonSetter("user_age")
    public void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.age = age;
    }
    
    // Handle dynamic properties
    @JsonAnySetter
    public void setMetadata(String key, Object value) {
        if (key.startsWith("pref_")) {
            // Handle preference properties
            if (value instanceof String) {
                preferences.add((String) value);
            }
        } else if (!key.equals("user_id") && !key.equals("name") && 
                   !key.equals("age") && !key.equals("email")) {
            // Store other unknown properties as metadata
            metadata.put(key, value);
        }
    }
    
    // Regular setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    
    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public Map<String, Object> getMetadata() { return metadata; }
    public List<String> getPreferences() { return preferences; }
    
    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', age=%d, email='%s', metadata=%s, preferences=%s}",
                id, name, age, email, metadata, preferences);
    }
}

// Usage with complex JSON
String complexJson = """
{
    "user_id": 1001,
    "full_name": "John Doe",
    "user_age": 30,
    "email": "john@email.com",
    "department": "Engineering",
    "salary": 75000,
    "pref_theme": "dark",
    "pref_language": "english",
    "pref_notifications": "enabled",
    "created_date": "2024-01-01",
    "last_login": "2024-01-15T10:30:00Z",
    "unknown_field": "some_value"
}
""";

ObjectMapper mapper = new ObjectMapper();

try {
    ComprehensiveUser user = mapper.readValue(complexJson, ComprehensiveUser.class);
    System.out.println(user);
} catch (Exception e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Output**:
```
User{id=1001, name='John Doe', age=30, email='john@email.com', 
metadata={department=Engineering, salary=75000, created_date=2024-01-01, last_login=2024-01-15T10:30:00Z, unknown_field=some_value}, 
preferences=[dark, english, enabled]}
```

---

## Error Handling in Deserialization

```java
public class RobustUser {
    private String name;
    private Integer age; // Using Integer to handle null values
    private String email;
    private List<String> tags = new ArrayList<>();
    
    public RobustUser() {}
    
    @JsonSetter("name")
    public void setName(String name) {
        this.name = (name != null) ? name.trim() : "Unknown";
    }
    
    @JsonSetter("age")
    public void setAge(Object age) {
        try {
            if (age instanceof String) {
                this.age = Integer.parseInt((String) age);
            } else if (age instanceof Number) {
                this.age = ((Number) age).intValue();
            } else if (age == null) {
                this.age = 0;
            }
        } catch (NumberFormatException e) {
            this.age = 0; // Default value for invalid age
        }
    }
    
    @JsonSetter("tags")
    public void setTags(Object tags) {
        this.tags.clear();
        if (tags instanceof List) {
            for (Object tag : (List<?>) tags) {
                if (tag != null) {
                    this.tags.add(tag.toString());
                }
            }
        } else if (tags instanceof String) {
            // Handle comma-separated string
            String[] tagArray = ((String) tags).split(",");
            for (String tag : tagArray) {
                this.tags.add(tag.trim());
            }
        }
    }
    
    // Getters
    public String getName() { return name; }
    public Integer getAge() { return age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getTags() { return tags; }
    
    @Override
    public String toString() {
        return String.format("RobustUser{name='%s', age=%d, email='%s', tags=%s}",
                name, age, email, tags);
    }
}

// Test with various problematic JSON inputs
String[] testJsons = {
    "{\"name\":\"John\",\"age\":\"30\",\"email\":\"john@email.com\",\"tags\":[\"dev\",\"senior\"]}",
    "{\"name\":\"  Jane  \",\"age\":null,\"tags\":\"junior,backend,java\"}",
    "{\"name\":null,\"age\":\"invalid\",\"email\":\"bob@email.com\",\"tags\":[null,\"lead\",\"\"]}",
    "{\"age\":25.5,\"tags\":[]}"
};

ObjectMapper mapper = new ObjectMapper();

for (int i = 0; i < testJsons.length; i++) {
    try {
        RobustUser user = mapper.readValue(testJsons[i], RobustUser.class);
        System.out.println("Test " + (i + 1) + ": " + user);
    } catch (Exception e) {
        System.out.println("Test " + (i + 1) + " failed: " + e.getMessage());
    }
}
```

**Output**:
```
Test 1: RobustUser{name='John', age=30, email='john@email.com', tags=[dev, senior]}
Test 2: RobustUser{name='Jane', age=0, email='null', tags=[junior, backend, java]}
Test 3: RobustUser{name='Unknown', age=0, email='bob@email.com', tags=[lead]}
Test 4: RobustUser{name='Unknown', age=25, email='null', tags=[]}
```

This comprehensive guide demonstrates how Jackson deserialization annotations provide powerful control over how JSON data is converted into Java objects, with robust error handling and flexible property mapping capabilities.