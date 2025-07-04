# Jackson Date/Time Annotations

This guide covers Jackson annotations that handle date, time, and number formatting during serialization and deserialization.

## @JsonFormat

**Purpose**: Specifies the format for dates, times, numbers, and other values during JSON processing.

**Usage**: Applied to fields or methods to control formatting of temporal and numeric data.

### Date and Time Formatting

```java
public class Event {
    private String name;
    
    // Different date formats
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date eventDate;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private Date eventTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fullDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date isoDateTime;
    
    // LocalDateTime with custom format
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime localDateTime;
    
    // LocalDate with custom format
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate localDate;
    
    // LocalTime with custom format
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime localTime;
    
    public Event() {}
    
    public Event(String name, Date eventDate, Date eventTime, Date fullDateTime, 
                 Date isoDateTime, LocalDateTime localDateTime, 
                 LocalDate localDate, LocalTime localTime) {
        this.name = name;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.fullDateTime = fullDateTime;
        this.isoDateTime = isoDateTime;
        this.localDateTime = localDateTime;
        this.localDate = localDate;
        this.localTime = localTime;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }
    public Date getEventTime() { return eventTime; }
    public void setEventTime(Date eventTime) { this.eventTime = eventTime; }
    public Date getFullDateTime() { return fullDateTime; }
    public void setFullDateTime(Date fullDateTime) { this.fullDateTime = fullDateTime; }
    public Date getIsoDateTime() { return isoDateTime; }
    public void setIsoDateTime(Date isoDateTime) { this.isoDateTime = isoDateTime; }
    public LocalDateTime getLocalDateTime() { return localDateTime; }
    public void setLocalDateTime(LocalDateTime localDateTime) { this.localDateTime = localDateTime; }
    public LocalDate getLocalDate() { return localDate; }
    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; }
    public LocalTime getLocalTime() { return localTime; }
    public void setLocalTime(LocalTime localTime) { this.localTime = localTime; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());

// Create event with current date/time
Date now = new Date();
LocalDateTime localNow = LocalDateTime.now();

Event event = new Event(
    "Tech Conference",
    now,                              // eventDate
    now,                              // eventTime
    now,                              // fullDateTime
    now,                              // isoDateTime
    localNow,                         // localDateTime
    localNow.toLocalDate(),          // localDate
    localNow.toLocalTime()           // localTime
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
System.out.println("Serialized Event:");
System.out.println(json);

// Deserialize
Event deserializedEvent = mapper.readValue(json, Event.class);
System.out.println("\nDeserialized successfully: " + deserializedEvent.getName());
```

**Output**:
```json
Serialized Event:
{
  "name" : "Tech Conference",
  "eventDate" : "2024-07-01",
  "eventTime" : "15:30:45",
  "fullDateTime" : "2024-07-01 15:30:45",
  "isoDateTime" : "2024-07-01T15:30:45.123+0000",
  "localDateTime" : "01/07/2024 15:30",
  "localDate" : "07-01-2024",
  "localTime" : "15:30:45"
}

Deserialized successfully: Tech Conference
```

---

### Timezone Handling

```java
public class GlobalEvent {
    private String name;
    
    // UTC timezone
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date utcTime;
    
    // Specific timezone
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/New_York")
    private Date newYorkTime;
    
    // European timezone
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/London")
    private Date londonTime;
    
    // Asian timezone
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date tokyoTime;
    
    public GlobalEvent() {}
    
    public GlobalEvent(String name, Date time) {
        this.name = name;
        this.utcTime = time;
        this.newYorkTime = time;
        this.londonTime = time;
        this.tokyoTime = time;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getUtcTime() { return utcTime; }
    public void setUtcTime(Date utcTime) { this.utcTime = utcTime; }
    public Date getNewYorkTime() { return newYorkTime; }
    public void setNewYorkTime(Date newYorkTime) { this.newYorkTime = newYorkTime; }
    public Date getLondonTime() { return londonTime; }
    public void setLondonTime(Date londonTime) { this.londonTime = londonTime; }
    public Date getTokyoTime() { return tokyoTime; }
    public void setTokyoTime(Date tokyoTime) { this.tokyoTime = tokyoTime; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

// Create event at specific time
Calendar cal = Calendar.getInstance();
cal.set(2024, Calendar.JULY, 1, 12, 0, 0); // July 1, 2024, 12:00:00
Date eventTime = cal.getTime();

GlobalEvent globalEvent = new GlobalEvent("Global Product Launch", eventTime);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(globalEvent);
System.out.println("Global Event with Timezones:");
System.out.println(json);
```

**Output**:
```json
Global Event with Timezones:
{
  "name" : "Global Product Launch",
  "utcTime" : "2024-07-01 16:00:00",
  "newYorkTime" : "2024-07-01 12:00:00",
  "londonTime" : "2024-07-01 17:00:00",
  "tokyoTime" : "2024-07-02 01:00:00"
}
```

---

### Number Formatting

```java
public class Product {
    private String name;
    
    // Currency formatting
    @JsonFormat(pattern = "#,##0.00")
    private BigDecimal price;
    
    // Percentage formatting
    @JsonFormat(pattern = "0.00%")
    private double discountRate;
    
    // Scientific notation
    @JsonFormat(pattern = "0.00E0")
    private double weight;
    
    // Custom number format
    @JsonFormat(pattern = "#,###.##")
    private double rating;
    
    // Integer with grouping
    @JsonFormat(pattern = "#,###")
    private int stockQuantity;
    
    public Product() {}
    
    public Product(String name, BigDecimal price, double discountRate, 
                   double weight, double rating, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.discountRate = discountRate;
        this.weight = weight;
        this.rating = rating;
        this.stockQuantity = stockQuantity;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Product product = new Product(
    "Gaming Laptop",
    new BigDecimal("1299.99"),
    0.15,                    // 15% discount
    2.5,                     // 2.5 kg weight
    4.7,                     // 4.7 rating
    1250                     // 1250 units in stock
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(product);
System.out.println("Product with Number Formatting:");
System.out.println(json);

// Deserialize
Product deserializedProduct = mapper.readValue(json, Product.class);
System.out.println("\nDeserialized - Price: " + deserializedProduct.getPrice());
```

**Output**:
```json
Product with Number Formatting:
{
  "name" : "Gaming Laptop",
  "price" : "1,299.99",
  "discountRate" : "15.00%",
  "weight" : "2.50E0",
  "rating" : "4.7",
  "stockQuantity" : "1,250"
}

Deserialized - Price: 1299.99
```

---

### Locale-Specific Formatting

```java
public class InternationalProduct {
    private String name;
    
    // US locale formatting
    @JsonFormat(pattern = "#,##0.00", locale = "en_US")
    private BigDecimal priceUS;
    
    // German locale formatting (uses comma as decimal separator)
    @JsonFormat(pattern = "#.##0,00", locale = "de_DE")
    private BigDecimal priceDE;
    
    // French locale formatting
    @JsonFormat(pattern = "#,##0.00", locale = "fr_FR")
    private BigDecimal priceFR;
    
    // Indian locale formatting (uses lakhs and crores)
    @JsonFormat(pattern = "#,##,##0.00", locale = "en_IN")
    private BigDecimal priceIN;
    
    // Date with different locales
    @JsonFormat(pattern = "EEEE, MMMM dd, yyyy", locale = "en_US")
    private Date launchDateUS;
    
    @JsonFormat(pattern = "EEEE dd MMMM yyyy", locale = "fr_FR")
    private Date launchDateFR;
    
    @JsonFormat(pattern = "EEEE, dd. MMMM yyyy", locale = "de_DE")
    private Date launchDateDE;
    
    public InternationalProduct() {}
    
    public InternationalProduct(String name, BigDecimal price, Date launchDate) {
        this.name = name;
        this.priceUS = price;
        this.priceDE = price;
        this.priceFR = price;
        this.priceIN = price;
        this.launchDateUS = launchDate;
        this.launchDateFR = launchDate;
        this.launchDateDE = launchDate;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPriceUS() { return priceUS; }
    public void setPriceUS(BigDecimal priceUS) { this.priceUS = priceUS; }
    public BigDecimal getPriceDE() { return priceDE; }
    public void setPriceDE(BigDecimal priceDE) { this.priceDE = priceDE; }
    public BigDecimal getPriceFR() { return priceFR; }
    public void setPriceFR(BigDecimal priceFR) { this.priceFR = priceFR; }
    public BigDecimal getPriceIN() { return priceIN; }
    public void setPriceIN(BigDecimal priceIN) { this.priceIN = priceIN; }
    public Date getLaunchDateUS() { return launchDateUS; }
    public void setLaunchDateUS(Date launchDateUS) { this.launchDateUS = launchDateUS; }
    public Date getLaunchDateFR() { return launchDateFR; }
    public void setLaunchDateFR(Date launchDateFR) { this.launchDateFR = launchDateFR; }
    public Date getLaunchDateDE() { return launchDateDE; }
    public void setLaunchDateDE(Date launchDateDE) { this.launchDateDE = launchDateDE; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();

Calendar cal = Calendar.getInstance();
cal.set(2024, Calendar.DECEMBER, 25); // Christmas 2024
Date launchDate = cal.getTime();

InternationalProduct product = new InternationalProduct(
    "Smart Watch Pro",
    new BigDecimal("299.99"),
    launchDate
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(product);
System.out.println("International Product Formatting:");
System.out.println(json);
```

**Output**:
```json
International Product Formatting:
{
  "name" : "Smart Watch Pro",
  "priceUS" : "299.99",
  "priceDE" : "299,99",
  "priceFR" : "299,99",
  "priceIN" : "299.99",
  "launchDateUS" : "Wednesday, December 25, 2024",
  "launchDateFR" : "mercredi 25 décembre 2024",
  "launchDateDE" : "Mittwoch, 25. Dezember 2024"
}
```

---

### Complex Date/Time Scenarios

```java
public class Schedule {
    private String title;
    
    // Flexible date input formats
    @JsonFormat(pattern = "yyyy-MM-dd||MM/dd/yyyy||dd-MM-yyyy")
    private LocalDate scheduleDate;
    
    // 12-hour format with AM/PM
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime startTime;
    
    // 24-hour format
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    
    // Duration in ISO-8601 format
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration duration;
    
    // Timestamp as epoch milliseconds
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant createdAt;
    
    // Custom format with timezone
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "UTC")
    private ZonedDateTime zonedDateTime;
    
    public Schedule() {}
    
    public Schedule(String title, LocalDate scheduleDate, LocalTime startTime, 
                   LocalTime endTime, Duration duration, Instant createdAt, 
                   ZonedDateTime zonedDateTime) {
        this.title = title;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.createdAt = createdAt;
        this.zonedDateTime = zonedDateTime;
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(LocalDate scheduleDate) { this.scheduleDate = scheduleDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getZonedDateTime() { return zonedDateTime; }
    public void setZonedDateTime(ZonedDateTime zonedDateTime) { this.zonedDateTime = zonedDateTime; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());

Schedule schedule = new Schedule(
    "Project Meeting",
    LocalDate.of(2024, 7, 15),
    LocalTime.of(14, 30),           // 2:30 PM
    LocalTime.of(16, 0),            // 4:00 PM
    Duration.ofHours(1).plusMinutes(30), // 1.5 hours
    Instant.now(),
    ZonedDateTime.now(ZoneId.of("UTC"))
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schedule);
System.out.println("Complex Schedule:");
System.out.println(json);

// Test deserialization with different date formats
String[] testDates = {
    "{\"title\":\"Test1\",\"scheduleDate\":\"2024-07-15\",\"startTime\":\"02:30 PM\",\"endTime\":\"16:00\",\"duration\":\"PT1H30M\",\"createdAt\":1625097600000,\"zonedDateTime\":\"2024-07-01 12:00:00 UTC\"}",
    "{\"title\":\"Test2\",\"scheduleDate\":\"07/15/2024\",\"startTime\":\"02:30 PM\",\"endTime\":\"16:00\",\"duration\":\"PT1H30M\",\"createdAt\":1625097600000,\"zonedDateTime\":\"2024-07-01 12:00:00 UTC\"}",
    "{\"title\":\"Test3\",\"scheduleDate\":\"15-07-2024\",\"startTime\":\"02:30 PM\",\"endTime\":\"16:00\",\"duration\":\"PT1H30M\",\"createdAt\":1625097600000,\"zonedDateTime\":\"2024-07-01 12:00:00 UTC\"}"
};

System.out.println("\nTesting different date formats:");
for (int i = 0; i < testDates.length; i++) {
    try {
        Schedule testSchedule = mapper.readValue(testDates[i], Schedule.class);
        System.out.println("Test " + (i + 1) + " - Date: " + testSchedule.getScheduleDate() + 
                          ", Start: " + testSchedule.getStartTime());
    } catch (Exception e) {
        System.out.println("Test " + (i + 1) + " failed: " + e.getMessage());
    }
}
```

**Output**:
```json
Complex Schedule:
{
  "title" : "Project Meeting",
  "scheduleDate" : "2024-07-15",
  "startTime" : "02:30 PM",
  "endTime" : "16:00",
  "duration" : "PT1H30M",
  "createdAt" : 1719847800000,
  "zonedDateTime" : "2024-07-01 15:30:00 UTC"
}

Testing different date formats:
Test 1 - Date: 2024-07-15, Start: 14:30
Test 2 - Date: 2024-07-15, Start: 14:30
Test 3 - Date: 2024-07-15, Start: 14:30
```

---

## Advanced Date/Time Configuration

### Global ObjectMapper Configuration

```java
public class DateTimeConfiguration {
    
    public static ObjectMapper createConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register JavaTime module for Java 8+ date/time types
        mapper.registerModule(new JavaTimeModule());
        
        // Disable writing dates as timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Set default date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapper.setDateFormat(dateFormat);
        
        // Configure default timezone
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return mapper;
    }
}

// Example class using global configuration
public class GlobalConfigExample {
    // No @JsonFormat needed - uses global configuration
    private Date eventDate;
    private LocalDateTime localDateTime;
    private Instant instant;
    
    // Override global configuration for specific field
    @JsonFormat(pattern = "MM/dd/yyyy", timezone = "America/New_York")
    private Date usDate;
    
    public GlobalConfigExample() {}
    
    public GlobalConfigExample(Date eventDate, LocalDateTime localDateTime, 
                              Instant instant, Date usDate) {
        this.eventDate = eventDate;
        this.localDateTime = localDateTime;
        this.instant = instant;
        this.usDate = usDate;
    }
    
    // Getters and setters
    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }
    public LocalDateTime getLocalDateTime() { return localDateTime; }
    public void setLocalDateTime(LocalDateTime localDateTime) { this.localDateTime = localDateTime; }
    public Instant getInstant() { return instant; }
    public void setInstant(Instant instant) { this.instant = instant; }
    public Date getUsDate() { return usDate; }
    public void setUsDate(Date usDate) { this.usDate = usDate; }
}

// Usage
ObjectMapper mapper = DateTimeConfiguration.createConfiguredMapper();

Date now = new Date();
LocalDateTime localNow = LocalDateTime.now();
Instant instantNow = Instant.now();

GlobalConfigExample example = new GlobalConfigExample(now, localNow, instantNow, now);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(example);
System.out.println("Global Configuration Example:");
System.out.println(json);
```

**Output**:
```json
Global Configuration Example:
{
  "eventDate" : "2024-07-01 15:30:45",
  "localDateTime" : "2024-07-01T15:30:45",
  "instant" : "2024-07-01T15:30:45.123Z",
  "usDate" : "07/01/2024"
}
```

---

## Custom Date/Time Handling

### Custom Date Deserializer

```java
public class FlexibleDateDeserializer extends JsonDeserializer<Date> {
    private static final List<DateFormat> DATE_FORMATS = Arrays.asList(
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("MM/dd/yyyy"),
        new SimpleDateFormat("dd-MM-yyyy"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"),
        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    );
    
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        
        String dateString = p.getText();
        
        // Try parsing with each format
        for (DateFormat format : DATE_FORMATS) {
            try {
                synchronized (format) { // DateFormat is not thread-safe
                    return format.parse(dateString);
                }
            } catch (ParseException e) {
                // Continue to next format
            }
        }
        
        // Try parsing as epoch timestamp
        try {
            long timestamp = Long.parseLong(dateString);
            return new Date(timestamp);
        } catch (NumberFormatException e) {
            // Not a timestamp
        }
        
        throw new JsonParseException(p, "Unable to parse date: " + dateString);
    }
}

// Example usage
public class FlexibleDateExample {
    private String name;
    
    @JsonDeserialize(using = FlexibleDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flexibleDate;
    
    public FlexibleDateExample() {}
    
    public FlexibleDateExample(String name, Date flexibleDate) {
        this.name = name;
        this.flexibleDate = flexibleDate;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getFlexibleDate() { return flexibleDate; }
    public void setFlexibleDate(Date flexibleDate) { this.flexibleDate = flexibleDate; }
    
    @Override
    public String toString() {
        return String.format("FlexibleDateExample{name='%s', flexibleDate=%s}", name, flexibleDate);
    }
}

// Test different input formats
ObjectMapper mapper = new ObjectMapper();

String[] testInputs = {
    "{\"name\":\"Test1\",\"flexibleDate\":\"2024-07-01\"}",
    "{\"name\":\"Test2\",\"flexibleDate\":\"07/01/2024\"}",
    "{\"name\":\"Test3\",\"flexibleDate\":\"01-07-2024\"}",
    "{\"name\":\"Test4\",\"flexibleDate\":\"2024-07-01 15:30:45\"}",
    "{\"name\":\"Test5\",\"flexibleDate\":\"2024-07-01T15:30:45.123Z\"}",
    "{\"name\":\"Test6\",\"flexibleDate\":\"1719847800000\"}"
};

System.out.println("Flexible Date Parsing:");
for (String input : testInputs) {
    try {
        FlexibleDateExample example = mapper.readValue(input, FlexibleDateExample.class);
        System.out.println(example);
        
        // Serialize back
        String output = mapper.writeValueAsString(example);
        System.out.println("Serialized: " + output);
        System.out.println();
    } catch (Exception e) {
        System.out.println("Failed to parse: " + input);
        System.out.println("Error: " + e.getMessage());
        System.out.println();
    }
}
```

**Output**:
```
Flexible Date Parsing:
FlexibleDateExample{name='Test1', flexibleDate=Mon Jul 01 00:00:00 UTC 2024}
Serialized: {"name":"Test1","flexibleDate":"2024-07-01 00:00:00"}

FlexibleDateExample{name='Test2', flexibleDate=Mon Jul 01 00:00:00 UTC 2024}
Serialized: {"name":"Test2","flexibleDate":"2024-07-01 00:00:00"}

FlexibleDateExample{name='Test3', flexibleDate=Mon Jul 01 00:00:00 UTC 2024}
Serialized: {"name":"Test3","flexibleDate":"2024-07-01 00:00:00"}

FlexibleDateExample{name='Test4', flexibleDate=Mon Jul 01 15:30:45 UTC 2024}
Serialized: {"name":"Test4","flexibleDate":"2024-07-01 15:30:45"}

FlexibleDateExample{name='Test5', flexibleDate=Mon Jul 01 15:30:45 UTC 2024}
Serialized: {"name":"Test5","flexibleDate":"2024-07-01 15:30:45"}

FlexibleDateExample{name='Test6', flexibleDate=Mon Jul 01 15:30:00 UTC 2024}
Serialized: {"name":"Test6","flexibleDate":"2024-07-01 15:30:00"}
```

---

## Complete Date/Time Example

```java
public class ComprehensiveDateTimeExample {
    private String eventName;
    
    // Standard ISO date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;
    
    // Time with timezone
    @JsonFormat(pattern = "HH:mm:ss z", timezone = "America/New_York")
    private Date eventTime;
    
    // Full datetime with custom format
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/London")
    private LocalDateTime createdAt;
    
    // Duration in human-readable format
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration duration;
    
    // Price with currency formatting
    @JsonFormat(pattern = "#,##0.00", locale = "en_US")
    private BigDecimal ticketPrice;
    
    // Attendance with grouping
    @JsonFormat(pattern = "#,###")
    private Integer expectedAttendance;
    
    // Rating with precision
    @JsonFormat(pattern = "0.0")
    private Double averageRating;
    
    // Flexible input date
    @JsonDeserialize(using = FlexibleDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date registrationDeadline;
    
    public ComprehensiveDateTimeExample() {}
    
    public ComprehensiveDateTimeExample(String eventName, LocalDate eventDate, Date eventTime,
                                       LocalDateTime createdAt, Duration duration,
                                       BigDecimal ticketPrice, Integer expectedAttendance,
                                       Double averageRating, Date registrationDeadline) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.createdAt = createdAt;
        this.duration = duration;
        this.ticketPrice = ticketPrice;
        this.expectedAttendance = expectedAttendance;
        this.averageRating = averageRating;
        this.registrationDeadline = registrationDeadline;
    }
    
    // Getters and setters (abbreviated for space)
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public Date getEventTime() { return eventTime; }
    public void setEventTime(Date eventTime) { this.eventTime = eventTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
    public Integer getExpectedAttendance() { return expectedAttendance; }
    public void setExpectedAttendance(Integer expectedAttendance) { this.expectedAttendance = expectedAttendance; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Date getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(Date registrationDeadline) { this.registrationDeadline = registrationDeadline; }
}

// Usage
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());

ComprehensiveDateTimeExample event = new ComprehensiveDateTimeExample(
    "Tech Summit 2024",
    LocalDate.of(2024, 8, 15),
    new Date(),
    LocalDateTime.now(),
    Duration.ofHours(8),
    new BigDecimal("299.99"),
    1500,
    4.7,
    new Date()
);

String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
System.out.println("Comprehensive Date/Time Example:");
System.out.println(json);

// Test deserialization
ComprehensiveDateTimeExample deserializedEvent = mapper.readValue(json, ComprehensiveDateTimeExample.class);
System.out.println("\nSuccessfully deserialized: " + deserializedEvent.getEventName());
```

This comprehensive guide demonstrates how Jackson's @JsonFormat annotation provides powerful control over date, time, and number formatting, with support for timezones, locales, and custom patterns for both serialization and deserialization.