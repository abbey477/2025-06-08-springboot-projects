# Jackson Type Handling Annotations

This guide covers Jackson annotations that handle polymorphic types, custom serialization/deserialization, and type information.

## @JsonTypeInfo

**Purpose**: Enables polymorphic type handling by including type information in JSON.

**Usage**: Applied to base classes or interfaces to specify how type information is included.

```java
// Base class with type information
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dog.class, name = "dog"),
    @JsonSubTypes.Type(value = Cat.class, name = "cat"),
    @JsonSubTypes.Type(value = Bird.class, name = "bird")
})
public abstract class Animal {
    protected String name;
    protected int age;
    
    // Constructor
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Default constructor for Jackson
    public Animal() {}
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public abstract String makeSound();
}

// Concrete implementations
public class Dog extends Animal {
    private String breed;
    
    public Dog() {}
    
    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }
    
    @Override
    public String makeSound() {
        return "Woof!";
    }
    
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
}

public class Cat extends Animal {
    private boolean indoor;
    
    public Cat() {}
    
    public Cat(String name, int age, boolean indoor) {
        super(name, age);
        this.indoor = indoor;
    }
    
    @Override
    public String makeSound() {
        return "Meow!";
    }
    
    public boolean isIndoor() { return indoor; }
    public void setIndoor(boolean indoor) { this.indoor = indoor; }
}

public class Bird extends Animal {
    private double wingspan;
    
    public Bird() {}
    
    public Bird(String name, int age, double wingspan) {
        super(name, age);
        this.wingspan = wingspan;
    }
    
    @Override
    public String makeSound() {
        return "Tweet!";
    }
    
    public double getWingspan() { return wingspan; }
    public void setWingspan(double wingspan) { this.wingspan = wingspan; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Create different animals
List<Animal> animals = Arrays.asList(
    new Dog("Buddy", 3, "Golden Retriever"),
    new Cat("Whiskers", 2, true),
    new Bird("Tweety", 1, 15.5)
);

// Serialize
String json = mapper.writeValueAsString(animals);
System.out.println("Serialized JSON:");
System.out.println(json);

// Deserialize
List<Animal> deserializedAnimals = mapper.readValue(json, 
    new TypeReference<List<Animal>>() {});

System.out.println("\nDeserialized animals:");
for (Animal animal : deserializedAnimals) {
    System.out.println(animal.getClass().getSimpleName() + ": " + 
                      animal.getName() + " says " + animal.makeSound());
}
```

**Output**:
```json
Serialized JSON:
[
  {"type":"dog","name":"Buddy","age":3,"breed":"Golden Retriever"},
  {"type":"cat","name":"Whiskers","age":2,"indoor":true},
  {"type":"bird","name":"Tweety","age":1,"wingspan":15.5}
]

Deserialized animals:
Dog: Buddy says Woof!
Cat: Whiskers says Meow!
Bird: Tweety says Tweet!
```

---

## Different @JsonTypeInfo Strategies

### 1. As WRAPPER_OBJECT

```java
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Circle.class, name = "circle"),
    @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle")
})
public abstract class Shape {
    protected String color;
    
    public Shape() {}
    public Shape(String color) { this.color = color; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public abstract double getArea();
}

public class Circle extends Shape {
    private double radius;
    
    public Circle() {}
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle() {}
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    @Override
    public double getArea() {
        return width * height;
    }
}

// Usage
Shape circle = new Circle("red", 5.0);
Shape rectangle = new Rectangle("blue", 4.0, 6.0);

String circleJson = mapper.writeValueAsString(circle);
String rectangleJson = mapper.writeValueAsString(rectangle);

System.out.println("Circle JSON: " + circleJson);
System.out.println("Rectangle JSON: " + rectangleJson);
```

**Output**:
```json
Circle JSON: {"circle":{"color":"red","radius":5.0}}
Rectangle JSON: {"rectangle":{"color":"blue","width":4.0,"height":6.0}}
```

### 2. As WRAPPER_ARRAY

```java
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_ARRAY
)
public abstract class Vehicle {
    protected String brand;
    
    public Vehicle() {}
    public Vehicle(String brand) { this.brand = brand; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}

public class Car extends Vehicle {
    private int doors;
    
    public Car() {}
    public Car(String brand, int doors) {
        super(brand);
        this.doors = doors;
    }
    
    public int getDoors() { return doors; }
    public void setDoors(int doors) { this.doors = doors; }
}

// Usage
Vehicle car = new Car("Toyota", 4);
String json = mapper.writeValueAsString(car);
System.out.println("Car JSON: " + json);
```

**Output**:
```json
Car JSON: ["com.example.Car",{"brand":"Toyota","doors":4}]
```

---

## @JsonSubTypes

**Purpose**: Lists the subtypes for polymorphic deserialization.

**Usage**: Used with @JsonTypeInfo to map type names to concrete classes.

```java
// Interface with subtypes
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "paymentType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreditCardPayment.class, name = "credit_card"),
    @JsonSubTypes.Type(value = PayPalPayment.class, name = "paypal"),
    @JsonSubTypes.Type(value = BankTransferPayment.class, name = "bank_transfer")
})
public interface Payment {
    double getAmount();
    String getDescription();
    boolean process();
}

// Credit Card Payment
public class CreditCardPayment implements Payment {
    private double amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    
    public CreditCardPayment() {}
    
    public CreditCardPayment(double amount, String cardNumber, String expiryDate, String cvv) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public double getAmount() { return amount; }
    @Override
    public String getDescription() { return "Credit Card Payment"; }
    @Override
    public boolean process() {
        System.out.println("Processing credit card payment of $" + amount);
        return true;
    }
    
    // Getters and setters
    public void setAmount(double amount) { this.amount = amount; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}

// PayPal Payment
public class PayPalPayment implements Payment {
    private double amount;
    private String email;
    
    public PayPalPayment() {}
    
    public PayPalPayment(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }
    
    @Override
    public double getAmount() { return amount; }
    @Override
    public String getDescription() { return "PayPal Payment"; }
    @Override
    public boolean process() {
        System.out.println("Processing PayPal payment of $" + amount + " for " + email);
        return true;
    }
    
    public void setAmount(double amount) { this.amount = amount; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Bank Transfer Payment
public class BankTransferPayment implements Payment {
    private double amount;
    private String accountNumber;
    private String routingNumber;
    
    public BankTransferPayment() {}
    
    public BankTransferPayment(double amount, String accountNumber, String routingNumber) {
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
    }
    
    @Override
    public double getAmount() { return amount; }
    @Override
    public String getDescription() { return "Bank Transfer Payment"; }
    @Override
    public boolean process() {
        System.out.println("Processing bank transfer of $" + amount);
        return true;
    }
    
    public void setAmount(double amount) { this.amount = amount; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Test different payment types
String[] paymentJsons = {
    "{\"paymentType\":\"credit_card\",\"amount\":100.50,\"cardNumber\":\"1234-5678-9012-3456\",\"expiryDate\":\"12/25\",\"cvv\":\"123\"}",
    "{\"paymentType\":\"paypal\",\"amount\":75.00,\"email\":\"user@example.com\"}",
    "{\"paymentType\":\"bank_transfer\",\"amount\":200.00,\"accountNumber\":\"123456789\",\"routingNumber\":\"987654321\"}"
};

for (String json : paymentJsons) {
    try {
        Payment payment = mapper.readValue(json, Payment.class);
        System.out.println("Processed: " + payment.getDescription() + " - $" + payment.getAmount());
        payment.process();
        System.out.println();
    } catch (Exception e) {
        System.out.println("Error processing payment: " + e.getMessage());
    }
}
```

**Output**:
```
Processed: Credit Card Payment - $100.5
Processing credit card payment of $100.5

Processed: PayPal Payment - $75.0
Processing PayPal payment of $75.0 for user@example.com

Processed: Bank Transfer Payment - $200.0
Processing bank transfer of $200.0
```

---

## @JsonTypeName

**Purpose**: Specifies the type name for a subtype (alternative to using @JsonSubTypes.Type).

**Usage**: Applied to subclasses to define their type identifier.

```java
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "documentType"
)
public abstract class Document {
    protected String title;
    protected String author;
    
    public Document() {}
    
    public Document(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public abstract void display();
}

@JsonTypeName("pdf")
public class PdfDocument extends Document {
    private int pages;
    private boolean encrypted;
    
    public PdfDocument() {}
    
    public PdfDocument(String title, String author, int pages, boolean encrypted) {
        super(title, author);
        this.pages = pages;
        this.encrypted = encrypted;
    }
    
    @Override
    public void display() {
        System.out.println("Displaying PDF: " + title + " (" + pages + " pages)");
    }
    
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
}

@JsonTypeName("word")
public class WordDocument extends Document {
    private String version;
    private boolean trackChanges;
    
    public WordDocument() {}
    
    public WordDocument(String title, String author, String version, boolean trackChanges) {
        super(title, author);
        this.version = version;
        this.trackChanges = trackChanges;
    }
    
    @Override
    public void display() {
        System.out.println("Displaying Word Document: " + title + " (Version: " + version + ")");
    }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public boolean isTrackChanges() { return trackChanges; }
    public void setTrackChanges(boolean trackChanges) { this.trackChanges = trackChanges; }
}

@JsonTypeName("excel")
public class ExcelDocument extends Document {
    private int worksheets;
    private boolean hasFormulas;
    
    public ExcelDocument() {}
    
    public ExcelDocument(String title, String author, int worksheets, boolean hasFormulas) {
        super(title, author);
        this.worksheets = worksheets;
        this.hasFormulas = hasFormulas;
    }
    
    @Override
    public void display() {
        System.out.println("Displaying Excel Document: " + title + " (" + worksheets + " worksheets)");
    }
    
    public int getWorksheets() { return worksheets; }
    public void setWorksheets(int worksheets) { this.worksheets = worksheets; }
    public boolean isHasFormulas() { return hasFormulas; }
    public void setHasFormulas(boolean hasFormulas) { this.hasFormulas = hasFormulas; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Create documents
List<Document> documents = Arrays.asList(
    new PdfDocument("User Manual", "Tech Team", 50, true),
    new WordDocument("Project Proposal", "John Doe", "2019", false),
    new ExcelDocument("Budget Report", "Finance Team", 3, true)
);

// Serialize
String json = mapper.writeValueAsString(documents);
System.out.println("Serialized Documents:");
System.out.println(json);

// Deserialize
List<Document> deserializedDocs = mapper.readValue(json, new TypeReference<List<Document>>() {});

System.out.println("\nDeserialized Documents:");
for (Document doc : deserializedDocs) {
    doc.display();
    System.out.println("Type: " + doc.getClass().getSimpleName());
}
```

**Output**:
```json
Serialized Documents:
[
  {"documentType":"pdf","title":"User Manual","author":"Tech Team","pages":50,"encrypted":true},
  {"documentType":"word","title":"Project Proposal","author":"John Doe","version":"2019","trackChanges":false},
  {"documentType":"excel","title":"Budget Report","author":"Finance Team","worksheets":3,"hasFormulas":true}
]

Deserialized Documents:
Displaying PDF: User Manual (50 pages)
Type: PdfDocument
Displaying Word Document: Project Proposal (Version: 2019)
Type: WordDocument
Displaying Excel Document: Budget Report (3 worksheets)
Type: ExcelDocument
```

---

## @JsonDeserialize

**Purpose**: Specifies a custom deserializer for complex deserialization logic.

**Usage**: Applied to fields, methods, or classes to use custom deserialization.

```java
// Custom deserializer for LocalDateTime
public class CustomDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };
    
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        String dateString = p.getText();
        
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        
        throw new JsonParseException(p, "Unable to parse date: " + dateString);
    }
}

// Custom deserializer for Money class
public class MoneyDeserializer extends JsonDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        
        if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            // Handle string format like "100.50 USD"
            String value = p.getText();
            String[] parts = value.split(" ");
            if (parts.length == 2) {
                double amount = Double.parseDouble(parts[0]);
                String currency = parts[1];
                return new Money(amount, currency);
            }
        } else if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            // Handle object format
            JsonNode node = p.getCodec().readTree(p);
            double amount = node.get("amount").asDouble();
            String currency = node.get("currency").asText();
            return new Money(amount, currency);
        }
        
        throw new JsonParseException(p, "Cannot deserialize Money from: " + p.getCurrentToken());
    }
}

// Money class
public class Money {
    private double amount;
    private String currency;
    
    public Money() {}
    
    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}

// Usage class
public class Event {
    private String name;
    
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime startTime;
    
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime endTime;
    
    @JsonDeserialize(using = MoneyDeserializer.class)
    private Money ticketPrice;
    
    public Event() {}
    
    public Event(String name, LocalDateTime startTime, LocalDateTime endTime, Money ticketPrice) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Money getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Money ticketPrice) { this.ticketPrice = ticketPrice; }
    
    @Override
    public String toString() {
        return String.format("Event{name='%s', startTime=%s, endTime=%s, ticketPrice=%s}",
                name, startTime, endTime, ticketPrice);
    }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Test different date formats and money formats
String[] eventJsons = {
    "{\"name\":\"Concert\",\"startTime\":\"2024-06-15 19:00:00\",\"endTime\":\"2024-06-15 22:00:00\",\"ticketPrice\":\"50.00 USD\"}",
    "{\"name\":\"Conference\",\"startTime\":\"2024-07-01T09:00:00\",\"endTime\":\"2024-07-01T17:00:00\",\"ticketPrice\":{\"amount\":150.0,\"currency\":\"USD\"}}",
    "{\"name\":\"Workshop\",\"startTime\":\"06/20/2024 14:00:00\",\"endTime\":\"06/20/2024 18:00:00\",\"ticketPrice\":\"25.50 EUR\"}"
};

for (int i = 0; i < eventJsons.length; i++) {
    try {
        Event event = mapper.readValue(eventJsons[i], Event.class);
        System.out.println("Event " + (i + 1) + ": " + event);
    } catch (Exception e) {
        System.out.println("Error parsing event " + (i + 1) + ": " + e.getMessage());
    }
}
```

**Output**:
```
Event 1: Event{name='Concert', startTime=2024-06-15T19:00, endTime=2024-06-15T22:00, ticketPrice=50.00 USD}
Event 2: Event{name='Conference', startTime=2024-07-01T09:00, endTime=2024-07-01T17:00, ticketPrice=150.00 USD}
Event 3: Event{name='Workshop', startTime=2024-06-20T14:00, endTime=2024-06-20T18:00, ticketPrice=25.50 EUR}
```

---

## @JsonSerialize

**Purpose**: Specifies a custom serializer for complex serialization logic.

**Usage**: Applied to fields, methods, or classes to use custom serialization.

```java
// Custom serializer for LocalDateTime
public class CustomDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        gen.writeString(value.format(FORMATTER));
    }
}

// Custom serializer for Money class
public class MoneySerializer extends JsonSerializer<Money> {
    @Override
    public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("amount", value.getAmount());
        gen.writeStringField("currency", value.getCurrency());
        gen.writeStringField("formatted", String.format("%.2f %s", value.getAmount(), value.getCurrency()));
        gen.writeEndObject();
    }
}

// Custom serializer for sensitive data
public class SensitiveDataSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        if (value == null || value.length() <= 4) {
            gen.writeString("****");
        } else {
            // Show only last 4 characters
            String masked = "****" + value.substring(value.length() - 4);
            gen.writeString(masked);
        }
    }
}

// Usage class
public class Transaction {
    private String id;
    
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private LocalDateTime timestamp;
    
    @JsonSerialize(using = MoneySerializer.class)
    private Money amount;
    
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String creditCardNumber;
    
    private String description;
    
    public Transaction() {}
    
    public Transaction(String id, LocalDateTime timestamp, Money amount, 
                      String creditCardNumber, String description) {
        this.id = id;
        this.timestamp = timestamp;
        this.amount = amount;
        this.creditCardNumber = creditCardNumber;
        this.description = description;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }
    public String getCreditCardNumber() { return creditCardNumber; }
    public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Transaction transaction = new Transaction(
    "TXN-001",
    LocalDateTime.of(2024, 6, 15, 14, 30, 45),
    new Money(99.99, "USD"),
    "1234567890123456",
    "Online purchase"
);

String json = mapper.writeValueAsString(transaction);
System.out.println("Serialized Transaction:");
System.out.println(json);
```

**Output**:
```json
Serialized Transaction:
{
  "id": "TXN-001",
  "timestamp": "2024-06-15 14:30:45",
  "amount": {
    "amount": 99.99,
    "currency": "USD",
    "formatted": "99.99 USD"
  },
  "creditCardNumber": "****3456",
  "description": "Online purchase"
}
```

---

## Complete Type Handling Example

Here's a comprehensive example combining multiple type handling annotations:

```java
// Base notification class with type handling
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "notificationType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmailNotification.class, name = "email"),
    @JsonSubTypes.Type(value = SmsNotification.class, name = "sms"),
    @JsonSubTypes.Type(value = PushNotification.class, name = "push")
})
public abstract class Notification {
    protected String id;
    
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    protected LocalDateTime createdAt;
    
    protected String message;
    protected Priority priority;
    
    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }
    
    public Notification() {}
    
    public Notification(String id, LocalDateTime createdAt, String message, Priority priority) {
        this.id = id;
        this.createdAt = createdAt;
        this.message = message;
        this.priority = priority;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public abstract boolean send();
}

@JsonTypeName("email")
public class EmailNotification extends Notification {
    private String toEmail;
    private String subject;
    private boolean isHtml;
    
    public EmailNotification() {}
    
    public EmailNotification(String id, LocalDateTime createdAt, String message, 
                           Priority priority, String toEmail, String subject, boolean isHtml) {
        super(id, createdAt, message, priority);
        this.toEmail = toEmail;
        this.subject = subject;
        this.isHtml = isHtml;
    }
    
    @Override
    public boolean send() {
        System.out.println("Sending email to: " + toEmail);
        System.out.println("Subject: " + subject);
        return true;
    }
    
    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { isHtml = html; }
}

@JsonTypeName("sms")
public class SmsNotification extends Notification {
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String phoneNumber;
    
    private String carrier;
    
    public SmsNotification() {}
    
    public SmsNotification(String id, LocalDateTime createdAt, String message, 
                          Priority priority, String phoneNumber, String carrier) {
        super(id, createdAt, message, priority);
        this.phoneNumber = phoneNumber;
        this.carrier = carrier;
    }
    
    @Override
    public boolean send() {
        System.out.println("Sending SMS to: " + phoneNumber + " via " + carrier);
        return true;
    }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
}

@JsonTypeName("push")
public class PushNotification extends Notification {
    private String deviceToken;
    private String appId;
    private Map<String, Object> customData;
    
    public PushNotification() {}
    
    public PushNotification(String id, LocalDateTime createdAt, String message, 
                           Priority priority, String deviceToken, String appId, 
                           Map<String, Object> customData) {
        super(id, createdAt, message, priority);
        this.deviceToken = deviceToken;
        this.appId = appId;
        this.customData = customData;
    }
    
    @Override
    public boolean send() {
        System.out.println("Sending push notification to device: " + deviceToken);
        return true;
    }
    
    public String getDeviceToken() { return deviceToken; }
    public void setDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public Map<String, Object> getCustomData() { return customData; }
    public void setCustomData(Map<String, Object> customData) { this.customData = customData; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Create different types of notifications
List<Notification> notifications = Arrays.asList(
    new EmailNotification("EMAIL-001", LocalDateTime.now(), "Welcome to our service!", 
                         Notification.Priority.MEDIUM, "user@example.com", "Welcome!", true),
    new SmsNotification("SMS-001", LocalDateTime.now(), "Your verification code is 123456", 
                       Notification.Priority.HIGH, "+1234567890", "Verizon"),
    new PushNotification("PUSH-001", LocalDateTime.now(), "You have a new message", 
                        Notification.Priority.LOW, "device-token-123", "com.example.app", 
                        Map.of("action", "open_message", "messageId", "MSG-001"))
);

// Serialize all notifications
String json = mapper.writeValueAsString(notifications);
System.out.println("Serialized Notifications:");
System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(notifications));

// Deserialize and process
List<Notification> deserializedNotifications = mapper.readValue(json, 
    new TypeReference<List<Notification>>() {});

System.out.println("\nProcessing notifications:");
for (Notification notification : deserializedNotifications) {
    System.out.println("Processing " + notification.getClass().getSimpleName() + 
                      " with priority " + notification.getPriority());
    notification.send();
    System.out.println();
}
```

**Output**:
```json
Serialized Notifications:
[
  {
    "notificationType" : "email",
    "id" : "EMAIL-001",
    "createdAt" : "2024-07-01 15:30:45",
    "message" : "Welcome to our service!",
    "priority" : "MEDIUM",
    "toEmail" : "user@example.com",
    "subject" : "Welcome!",
    "html" : true
  },
  {
    "notificationType" : "sms",
    "id" : "SMS-001",
    "createdAt" : "2024-07-01 15:30:45",
    "message" : "Your verification code is 123456",
    "priority" : "HIGH",
    "phoneNumber" : "****7890",
    "carrier" : "Verizon"
  },
  {
    "notificationType" : "push",
    "id" : "PUSH-001",
    "createdAt" : "2024-07-01 15:30:45",
    "message" : "You have a new message",
    "priority" : "LOW",
    "deviceToken" : "device-token-123",
    "appId" : "com.example.app",
    "customData" : {
      "action" : "open_message",
      "messageId" : "MSG-001"
    }
  }
]

Processing notifications:
Processing EmailNotification with priority MEDIUM
Sending email to: user@example.com
Subject: Welcome!

Processing SmsNotification with priority HIGH
Sending SMS to: ****7890 via Verizon

Processing PushNotification with priority LOW
Sending push notification to device: device-token-123
```

This comprehensive example demonstrates how Jackson's type handling annotations work together to provide powerful polymorphic serialization and deserialization capabilities, with custom serializers for data formatting and security.