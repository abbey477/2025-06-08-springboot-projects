# Jackson Advanced Annotations

This guide covers advanced Jackson annotations for dynamic properties, filtering, naming strategies, and schema generation.

## @JsonAnyGetter

**Purpose**: Serializes a map's entries as regular JSON properties at the same level as other object properties.

**Usage**: Applied to methods that return a Map to flatten map entries into the parent object.

```java
public class DynamicProperties {
    private String name;
    private int age;
    
    // Regular properties stored in a map
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    public DynamicProperties() {}
    
    public DynamicProperties(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // This method flattens map entries as top-level properties
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    
    // Helper method to add dynamic properties
    public void addProperty(String key, Object value) {
        additionalProperties.put(key, value);
    }
    
    // Regular getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

DynamicProperties person = new DynamicProperties("John Doe", 30);
person.addProperty("email", "john@email.com");
person.addProperty("department", "Engineering");
person.addProperty("salary", 75000);
person.addProperty("skills", Arrays.asList("Java", "Python", "JavaScript"));

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
System.out.println("Object with @JsonAnyGetter:");
System.out.println(json);

// Deserialize (requires @JsonAnySetter to work properly)
DynamicProperties deserializedPerson = mapper.readValue(json, DynamicProperties.class);
System.out.println("\nDeserialized name: " + deserializedPerson.getName());
System.out.println("Additional properties: " + deserializedPerson.getAdditionalProperties());
```

**Output**:
```json
Object with @JsonAnyGetter:
{
  "name" : "John Doe",
  "age" : 30,
  "email" : "john@email.com",
  "department" : "Engineering",
  "salary" : 75000,
  "skills" : [ "Java", "Python", "JavaScript" ]
}

Deserialized name: John Doe
Additional properties: {}
```

---

## @JsonGetter

**Purpose**: Marks a method as a getter for a JSON property with a custom name.

**Usage**: Applied to methods to customize property names or create computed properties.

```java
public class Person {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String phoneNumber;
    
    public Person() {}
    
    public Person(String firstName, String lastName, Date birthDate, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Custom getter with computed property
    @JsonGetter("fullName")
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Custom getter with different property name
    @JsonGetter("email_address")
    public String getEmail() {
        return email;
    }
    
    // Computed property: age based on birth date
    @JsonGetter("age")
    public int getAge() {
        if (birthDate == null) return 0;
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
    
    // Format phone number
    @JsonGetter("formatted_phone")
    public String getFormattedPhone() {
        if (phoneNumber == null || phoneNumber.length() != 10) {
            return phoneNumber;
        }
        return String.format("(%s) %s-%s", 
                           phoneNumber.substring(0, 3),
                           phoneNumber.substring(3, 6),
                           phoneNumber.substring(6));
    }
    
    // Conditional property
    @JsonGetter("is_adult")
    public boolean isAdult() {
        return getAge() >= 18;
    }
    
    // Regular getters (these won't appear in JSON due to custom getters above)
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Calendar cal = Calendar.getInstance();
cal.set(1990, Calendar.JUNE, 15); // June 15, 1990

Person person = new Person("John", "Doe", cal.getTime(), "john.doe@email.com", "1234567890");

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
System.out.println("Person with custom getters:");
System.out.println(json);
```

**Output**:
```json
Person with custom getters:
{
  "fullName" : "John Doe",
  "email_address" : "john.doe@email.com",
  "age" : 34,
  "formatted_phone" : "(123) 456-7890",
  "is_adult" : true
}
```

---

## @JsonFilter

**Purpose**: Applies dynamic filtering to control which properties are included during serialization.

**Usage**: Applied to classes to enable runtime property filtering.

```java
@JsonFilter("userFilter")
public class FilterableUser {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String ssn;
    private double salary;
    private String department;
    private Date lastLogin;
    
    public FilterableUser() {}
    
    public FilterableUser(Long id, String username, String email, String firstName, 
                         String lastName, String phoneNumber, String ssn, 
                         double salary, String department, Date lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.ssn = ssn;
        this.salary = salary;
        this.department = department;
        this.lastLogin = lastLogin;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
}

// Usage with different filter scenarios
ObjectMapper mapper = new ObjectMapper();

FilterableUser user = new FilterableUser(
    1L, "johndoe", "john@email.com", "John", "Doe",
    "123-456-7890", "123-45-6789", 75000.0, "Engineering", new Date()
);

// Scenario 1: Public information only
SimpleFilterProvider publicFilter = new SimpleFilterProvider();
publicFilter.addFilter("userFilter", 
    SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "firstName", "lastName", "department"));

String publicJson = mapper.writer(publicFilter).writeValueAsString(user);
System.out.println("Public information:");
System.out.println(publicJson);

// Scenario 2: Internal HR view (exclude SSN)
SimpleFilterProvider hrFilter = new SimpleFilterProvider();
hrFilter.addFilter("userFilter", 
    SimpleBeanPropertyFilter.serializeAllExcept("ssn"));

String hrJson = mapper.writer(hrFilter).writeValueAsString(user);
System.out.println("\nHR view (no SSN):");
System.out.println(hrJson);

// Scenario 3: Manager view (exclude salary and SSN)
SimpleFilterProvider managerFilter = new SimpleFilterProvider();
managerFilter.addFilter("userFilter", 
    SimpleBeanPropertyFilter.serializeAllExcept("salary", "ssn"));

String managerJson = mapper.writer(managerFilter).writeValueAsString(user);
System.out.println("\nManager view (no salary/SSN):");
System.out.println(managerJson);

// Scenario 4: Custom filter with conditional logic
SimpleFilterProvider customFilter = new SimpleFilterProvider();
customFilter.addFilter("userFilter", new SimpleBeanPropertyFilter() {
    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, 
                                SerializerProvider provider, PropertyWriter writer) 
                                throws Exception {
        // Custom logic: exclude sensitive fields for non-admin users
        String fieldName = writer.getName();
        boolean isAdmin = true; // This would come from security context
        
        if (!isAdmin && (fiel