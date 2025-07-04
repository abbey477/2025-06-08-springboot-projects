# Jackson Validation & Behavior Annotations

This guide covers Jackson annotations that control validation, object relationships, and behavioral aspects of JSON processing.

## @JsonInclude

**Purpose**: Controls when properties are included in JSON based on their values or state.

**Usage**: Applied to fields, methods, or classes to specify inclusion criteria.

### Include Strategies

```java
public class InclusionExample {
    // Always include (default behavior)
    private String name;
    
    // Include only if not null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    
    // Include only if not empty (null, empty string, empty collection/map)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> hobbies;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> metadata;
    
    // Include only if not default value (0 for numbers, false for boolean, null for objects)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age;
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean active;
    
    // Include only if not absent (for Optional, AtomicReference, etc.)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Optional<String> phoneNumber;
    
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private AtomicReference<String> reference;
    
    public InclusionExample() {}
    
    public InclusionExample(String name, String email, List<String> hobbies, 
                           String description, Map<String, Object> metadata,
                           int age, boolean active, Optional<String> phoneNumber,
                           AtomicReference<String> reference) {
        this.name = name;
        this.email = email;
        this.hobbies = hobbies;
        this.description = description;
        this.metadata = metadata;
        this.age = age;
        this.active = active;
        this.phoneNumber = phoneNumber;
        this.reference = reference;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Optional<String> getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Optional<String> phoneNumber) { this.phoneNumber = phoneNumber; }
    public AtomicReference<String> getReference() { return reference; }
    public void setReference(AtomicReference<String> reference) { this.reference = reference; }
}

// Usage examples
ObjectMapper mapper = new ObjectMapper();

// Example 1: User with all values
InclusionExample user1 = new InclusionExample(
    "John Doe",
    "john@email.com",
    Arrays.asList("reading", "swimming"),
    "Software developer",
    Map.of("department", "Engineering", "level", "Senior"),
    30,
    true,
    Optional.of("123-456-7890"),
    new AtomicReference<>("reference-value")
);

// Example 2: User with null/empty/default values
InclusionExample user2 = new InclusionExample(
    "Jane Smith",
    null,                           // NON_NULL: excluded
    new ArrayList<>(),              // NON_EMPTY: excluded
    "",                             // NON_EMPTY: excluded
    new HashMap<>(),                // NON_EMPTY: excluded
    0,                              // NON_DEFAULT: excluded
    false,                          // NON_DEFAULT: excluded
    Optional.empty(),               // NON_ABSENT: excluded
    new AtomicReference<>(null)     // NON_ABSENT: excluded
);

String json1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user1);
String json2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user2);

System.out.println("User with all values:");
System.out.println(json1);
System.out.println("\nUser with null/empty/default values:");
System.out.println(json2);
```

**Output**:
```json
User with all values:
{
  "name" : "John Doe",
  "email" : "john@email.com",
  "hobbies" : [ "reading", "swimming" ],
  "description" : "Software developer",
  "metadata" : {
    "department" : "Engineering",
    "level" : "Senior"
  },
  "age" : 30,
  "active" : true,
  "phoneNumber" : "123-456-7890",
  "reference" : "reference-value"
}

User with null/empty/default values:
{
  "name" : "Jane Smith"
}
```

---

### Custom Inclusion Filter

```java
public class CustomInclusionFilter {
    
    // Custom filter for excluding certain values
    public static class NotBlankFilter {
        @Override
        public boolean equals(Object other) {
            // Exclude if null, empty string, or only whitespace
            if (other == null) return true;
            if (other instanceof String) {
                return ((String) other).trim().isEmpty();
            }
            return false;
        }
    }
    
    // Custom filter for numeric values
    public static class PositiveNumberFilter {
        @Override
        public boolean equals(Object other) {
            if (other instanceof Number) {
                return ((Number) other).doubleValue() <= 0;
            }
            return other == null;
        }
    }
}

public class CustomInclusionExample {
    private String name;
    
    @JsonInclude(value = JsonInclude.Include.CUSTOM, 
                 valueFilter = CustomInclusionFilter.NotBlankFilter.class)
    private String description;
    
    @JsonInclude(value = JsonInclude.Include.CUSTOM,
                 valueFilter = CustomInclusionFilter.PositiveNumberFilter.class)
    private Double price;
    
    @JsonInclude(value = JsonInclude.Include.CUSTOM,
                 valueFilter = CustomInclusionFilter.PositiveNumberFilter.class)
    private Integer quantity;
    
    public CustomInclusionExample() {}
    
    public CustomInclusionExample(String name, String description, Double price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Test cases
CustomInclusionExample[] examples = {
    new CustomInclusionExample("Product A", "Great product", 99.99, 10),     // All included
    new CustomInclusionExample("Product B", "   ", 0.0, -5),                // description and numeric values excluded
    new CustomInclusionExample("Product C", null, null, null),              // Only name included
    new CustomInclusionExample("Product D", "Good product", 50.0, 0)        // quantity excluded (0 <= 0)
};

for (int i = 0; i < examples.length; i++) {
    String json = mapper.writeValueAsString(examples[i]);
    System.out.println("Example " + (i + 1) + ": " + json);
}
```

**Output**:
```
Example 1: {"name":"Product A","description":"Great product","price":99.99,"quantity":10}
Example 2: {"name":"Product B"}
Example 3: {"name":"Product C"}
Example 4: {"name":"Product D","description":"Good product","price":50.0}
```

---

## @JsonIgnoreType

**Purpose**: Ignores all properties of a specific type during serialization/deserialization.

**Usage**: Applied to classes to exclude them completely from JSON processing.

```java
@JsonIgnoreType
public class InternalMetadata {
    private String internalId;
    private String debugInfo;
    private Date lastModified;
    
    public InternalMetadata(String internalId, String debugInfo, Date lastModified) {
        this.internalId = internalId;
        this.debugInfo = debugInfo;
        this.lastModified = lastModified;
    }
    
    // Getters
    public String getInternalId() { return internalId; }
    public String getDebugInfo() { return debugInfo; }
    public Date getLastModified() { return lastModified; }
}

public class Product {
    private String name;
    private double price;
    private String description;
    
    // This field will be completely ignored due to @JsonIgnoreType
    private InternalMetadata metadata;
    
    // Regular fields
    private String category;
    private boolean available;
    
    public Product() {}
    
    public Product(String name, double price, String description, 
                   InternalMetadata metadata, String category, boolean available) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.metadata = metadata;
        this.category = category;
        this.available = available;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public InternalMetadata getMetadata() { return metadata; }
    public void setMetadata(InternalMetadata metadata) { this.metadata = metadata; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

InternalMetadata metadata = new InternalMetadata("INT-001", "Debug info here", new Date());

Product product = new Product(
    "Gaming Laptop",
    1299.99,
    "High-performance gaming laptop",
    metadata,
    "Electronics",
    true
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(product);
System.out.println("Product JSON (metadata ignored):");
System.out.println(json);

// Deserialization also ignores the type
Product deserializedProduct = mapper.readValue(json, Product.class);
System.out.println("\nDeserialized product metadata: " + deserializedProduct.getMetadata());
```

**Output**:
```json
Product JSON (metadata ignored):
{
  "name" : "Gaming Laptop",
  "price" : 1299.99,
  "description" : "High-performance gaming laptop",
  "category" : "Electronics",
  "available" : true
}

Deserialized product metadata: null
```

---

## @JsonManagedReference and @JsonBackReference

**Purpose**: Handles bidirectional relationships to prevent infinite recursion during serialization.

**Usage**: Applied to establish parent-child relationships in object graphs.

```java
public class User {
    private Long id;
    private String name;
    private String email;
    
    // Forward reference - will be serialized
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();
    
    public User() {}
    
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}

public class Order {
    private Long id;
    private String productName;
    private double amount;
    private Date orderDate;
    
    // Back reference - will NOT be serialized to prevent cycles
    @JsonBackReference
    private User user;
    
    public Order() {}
    
    public Order(Long id, String productName, double amount, Date orderDate) {
        this.id = id;
        this.productName = productName;
        this.amount = amount;
        this.orderDate = orderDate;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

User user = new User(1L, "John Doe", "john@email.com");

Order order1 = new Order(101L, "Laptop", 1299.99, new Date());
Order order2 = new Order(102L, "Mouse", 29.99, new Date());

user.addOrder(order1);
user.addOrder(order2);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
System.out.println("User with Orders (back references excluded):");
System.out.println(json);

// Deserialize
User deserializedUser = mapper.readValue(json, User.class);
System.out.println("\nDeserialized user: " + deserializedUser.getName());
System.out.println("Number of orders: " + deserializedUser.getOrders().size());

// Check if back references are restored
for (Order order : deserializedUser.getOrders()) {
    System.out.println("Order " + order.getId() + " user reference: " + 
                      (order.getUser() != null ? order.getUser().getName() : "null"));
}
```

**Output**:
```json
User with Orders (back references excluded):
{
  "id" : 1,
  "name" : "John Doe",
  "email" : "john@email.com",
  "orders" : [ {
    "id" : 101,
    "productName" : "Laptop",
    "amount" : 1299.99,
    "orderDate" : 1719847800000
  }, {
    "id" : 102,
    "productName" : "Mouse",
    "amount" : 29.99,
    "orderDate" : 1719847800000
  } ]
}

Deserialized user: John Doe
Number of orders: 2
Order 101 user reference: John Doe
Order 102 user reference: John Doe
```

---

## @JsonIdentityInfo

**Purpose**: Handles object identity and circular references using object IDs.

**Usage**: Applied to classes to enable identity-based serialization/deserialization.

```java
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Department {
    private Long id;
    private String name;
    private String location;
    
    // Circular reference with employees
    private List<Employee> employees = new ArrayList<>();
    
    // Reference to manager (who is also an employee)
    private Employee manager;
    
    public Department() {}
    
    public Department(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }
    
    public void setManager(Employee manager) {
        this.manager = manager;
        if (!employees.contains(manager)) {
            addEmployee(manager);
        }
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }
}

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Employee {
    private Long id;
    private String name;
    private String position;
    private double salary;
    
    // Circular reference back to department
    private Department department;
    
    // Self-reference for reporting structure
    private Employee supervisor;
    private List<Employee> subordinates = new ArrayList<>();
    
    public Employee() {}
    
    public Employee(Long id, String name, String position, double salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }
    
    public void addSubordinate(Employee subordinate) {
        subordinates.add(subordinate);
        subordinate.setSupervisor(this);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Employee getSupervisor() { return supervisor; }
    public void setSupervisor(Employee supervisor) { this.supervisor = supervisor; }
    public List<Employee> getSubordinates() { return subordinates; }
    public void setSubordinates(List<Employee> subordinates) { this.subordinates = subordinates; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Create department
Department engineering = new Department(1L, "Engineering", "Building A");

// Create employees
Employee manager = new Employee(1L, "Alice Johnson", "Engineering Manager", 120000);
Employee dev1 = new Employee(2L, "Bob Smith", "Senior Developer", 90000);
Employee dev2 = new Employee(3L, "Carol Williams", "Junior Developer", 70000);

// Set up relationships
engineering.setManager(manager);
engineering.addEmployee(dev1);
engineering.addEmployee(dev2);

// Set up reporting structure
manager.addSubordinate(dev1);
dev1.addSubordinate(dev2);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(engineering);
System.out.println("Department with circular references (using object identity):");
System.out.println(json);

// Deserialize
Department deserializedDept = mapper.readValue(json, Department.class);
System.out.println("\nDeserialized department: " + deserializedDept.getName());
System.out.println("Manager: " + deserializedDept.getManager().getName());
System.out.println("Number of employees: " + deserializedDept.getEmployees().size());

// Verify circular references are maintained
Employee deserializedManager = deserializedDept.getManager();
System.out.println("Manager's department: " + deserializedManager.getDepartment().getName());
System.out.println("Manager's subordinates: " + deserializedManager.getSubordinates().size());
```

**Output**:
```json
Department with circular references (using object identity):
{
  "id" : 1,
  "name" : "Engineering",
  "location" : "Building A",
  "employees" : [ {
    "id" : 1,
    "name" : "Alice Johnson",
    "position" : "Engineering Manager",
    "salary" : 120000.0,
    "department" : 1,
    "supervisor" : null,
    "subordinates" : [ {
      "id" : 2,
      "name" : "Bob Smith",
      "position" : "Senior Developer",
      "salary" : 90000.0,
      "department" : 1,
      "supervisor" : 1,
      "subordinates" : [ {
        "id" : 3,
        "name" : "Carol Williams",
        "position" : "Junior Developer",
        "salary" : 70000.0,
        "department" : 1,
        "supervisor" : 2,
        "subordinates" : [ ]
      } ]
    } ]
  }, 2, 3 ],
  "manager" : 1
}

Deserialized department: Engineering
Manager: Alice Johnson
Number of employees: 3
Manager's department: Engineering
Manager's subordinates: 1
```

---

## Alternative @JsonIdentityInfo Generators

### Using IntSequenceGenerator

```java
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class Node {
    private String name;
    private String value;
    private List<Node> children = new ArrayList<>();
    private Node parent;
    
    public Node() {}
    
    public Node(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public List<Node> getChildren() { return children; }
    public void setChildren(List<Node> children) { this.children = children; }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Node root = new Node("root", "Root Node");
Node child1 = new Node("child1", "First Child");
Node child2 = new Node("child2", "Second Child");
Node grandchild = new Node("grandchild", "Grand Child");

root.addChild(child1);
root.addChild(child2);
child1.addChild(grandchild);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
System.out.println("Tree structure with IntSequenceGenerator:");
System.out.println(json);
```

**Output**:
```json
Tree structure with IntSequenceGenerator:
{
  "@id" : 1,
  "name" : "root",
  "value" : "Root Node",
  "children" : [ {
    "@id" : 2,
    "name" : "child1",
    "value" : "First Child",
    "children" : [ {
      "@id" : 3,
      "name" : "grandchild",
      "value" : "Grand Child",
      "children" : [ ],
      "parent" : 2
    } ],
    "parent" : 1
  }, {
    "@id" : 4,
    "name" : "child2",
    "value" : "Second Child",
    "children" : [ ],
    "parent" : 1
  } ],
  "parent" : null
}
```

---

## Complete Validation & Behavior Example

```java
// Custom validation class
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Project {
    private Long id;
    private String name;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date endDate;
    
    @JsonManagedReference("project-tasks")
    private List<Task> tasks = new ArrayList<>();
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<String> tags = new HashSet<>();
    
    // Internal metadata ignored completely
    @JsonIgnore
    private ProjectMetadata metadata;
    
    public Project() {}
    
    public Project(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.metadata = new ProjectMetadata();
    }
    
    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
    public ProjectMetadata getMetadata() { return metadata; }
    public void setMetadata(ProjectMetadata metadata) { this.metadata = metadata; }
}

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Task {
    private Long id;
    private String title;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int priority; // 0 = default, excluded
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dueDate;
    
    @JsonBackReference("project-tasks")
    private Project project;
    
    // Self-referencing for dependencies
    @JsonManagedReference("task-dependencies")
    private List<Task> dependencies = new ArrayList<>();
    
    @JsonBackReference("task-dependencies")
    private Task dependentTask;
    
    public Task() {}
    
    public Task(Long id, String title, String description, int priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    
    public void addDependency(Task dependency) {
        dependencies.add(dependency);
        dependency.setDependentTask(this);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public List<Task> getDependencies() { return dependencies; }
    public void setDependencies(List<Task> dependencies) { this.dependencies = dependencies; }
    public Task getDependentTask() { return dependentTask; }
    public void setDependentTask(Task dependentTask) { this.dependentTask = dependentTask; }
}

@JsonIgnoreType
public class ProjectMetadata {
    private String internalId;
    private Date createdAt;
    private String createdBy;
    
    public ProjectMetadata() {
        this.internalId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.createdBy = "system";
    }
    
    // Getters
    public String getInternalId() { return internalId; }
    public Date getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Project project = new Project(1L, "Website Redesign", "Complete redesign of company website");
project.setStartDate(new Date());
project.getTags().addAll(Arrays.asList("web", "design", "priority"));

Task task1 = new Task(1L, "Design Mockups", "Create initial design mockups", 3);
Task task2 = new Task(2L, "Backend API", "", 2); // Empty description will be excluded
Task task3 = new Task(3L, "Frontend Implementation", "Implement frontend components", 0); // Priority 0 will be excluded
Task task4 = new Task(4L, "Testing", null, 1); // Null description will be excluded

// Set up dependencies
task2.addDependency(task1); // Backend depends on Design
task3.addDependency(task2); // Frontend depends on Backend
task4.addDependency(task3); // Testing depends on Frontend

project.addTask(task1);
project.addTask(task2);
project.addTask(task3);
project.addTask(task4);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(project);
System.out.println("Complete Project with Validation & Behavior Annotations:");
System.out.println(json);

// Deserialize and verify relationships
Project deserializedProject = mapper.readValue(json, Project.class);
System.out.println("\nDeserialized project: " + deserializedProject.getName());
System.out.println("Number of tasks: " + deserializedProject.getTasks().size());
System.out.println("Metadata (should be null due to @JsonIgnoreType): " + deserializedProject.getMetadata());

// Verify relationships are maintained
for (Task task : deserializedProject.getTasks()) {
    System.out.println("Task " + task.getId() + " belongs to project: " + 
                      (task.getProject() != null ? task.getProject().getName() : "null"));
    if (!task.getDependencies().isEmpty()) {
        System.out.println("  Dependencies: " + task.getDependencies().size());
    }
}
```

This comprehensive example demonstrates how Jackson's validation and behavior annotations work together to provide fine-grained control over JSON processing, object relationships, and data validation while maintaining object integrity and preventing common serialization issues.