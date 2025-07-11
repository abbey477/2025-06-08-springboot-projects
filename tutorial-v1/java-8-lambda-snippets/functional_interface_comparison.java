import java.util.function.*;
import java.util.Arrays;
import java.util.List;

// ===============================
// JAVA'S BUILT-IN FUNCTIONAL INTERFACES
// ===============================

public class FunctionalInterfaceComparison {
    
    public static void main(String[] args) {
        demonstrateBuiltInInterfaces();
        demonstrateCustomInterfaces();
        demonstrateWhenToUseWhich();
    }
    
    private static void demonstrateBuiltInInterfaces() {
        System.out.println("=== BUILT-IN FUNCTIONAL INTERFACES ===\n");
        
        // 1. SUPPLIER<T> - No input, returns T
        Supplier<String> stringSupplier = () -> "Hello World";
        Supplier<Integer> randomNumber = () -> (int)(Math.random() * 100);
        
        System.out.println("Supplier examples:");
        System.out.println("String: " + stringSupplier.get());
        System.out.println("Random: " + randomNumber.get());
        
        // 2. CONSUMER<T> - Takes T, returns void
        Consumer<String> printer = System.out::println;
        Consumer<Integer> doubler = x -> System.out.println("Double: " + (x * 2));
        
        System.out.println("\nConsumer examples:");
        printer.accept("Hello from Consumer");
        doubler.accept(5);
        
        // 3. PREDICATE<T> - Takes T, returns boolean
        Predicate<Integer> isEven = x -> x % 2 == 0;
        Predicate<String> isLongString = s -> s.length() > 5;
        
        System.out.println("\nPredicate examples:");
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 'Hello World' long? " + isLongString.test("Hello World"));
        
        // 4. FUNCTION<T, R> - Takes T, returns R
        Function<String, Integer> stringLength = String::length;
        Function<Integer, String> numberToString = x -> "Number: " + x;
        
        System.out.println("\nFunction examples:");
        System.out.println("Length of 'Java': " + stringLength.apply("Java"));
        System.out.println("Convert 42: " + numberToString.apply(42));
        
        // 5. BIFUNCTION<T, U, R> - Takes T and U, returns R
        BiFunction<String, String, String> concatenate = (a, b) -> a + " " + b;
        BiFunction<Integer, Integer, Integer> add = Integer::sum;
        
        System.out.println("\nBiFunction examples:");
        System.out.println("Concat: " + concatenate.apply("Hello", "World"));
        System.out.println("Add: " + add.apply(5, 3));
        
        // 6. BICONSUMER<T, U> - Takes T and U, returns void
        BiConsumer<String, Integer> printWithCount = (s, count) -> {
            for (int i = 0; i < count; i++) {
                System.out.println(s);
            }
        };
        
        System.out.println("\nBiConsumer example:");
        printWithCount.accept("Hi", 2);
        
        // 7. UNARYOPERATOR<T> - Takes T, returns T (extends Function<T,T>)
        UnaryOperator<String> toUpperCase = String::toUpperCase;
        UnaryOperator<Integer> square = x -> x * x;
        
        System.out.println("\nUnaryOperator examples:");
        System.out.println("Uppercase: " + toUpperCase.apply("hello"));
        System.out.println("Square: " + square.apply(5));
        
        // 8. BINARYOPERATOR<T> - Takes T, T, returns T (extends BiFunction<T,T,T>)
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        BinaryOperator<String> concat = (a, b) -> a + b;
        
        System.out.println("\nBinaryOperator examples:");
        System.out.println("Multiply: " + multiply.apply(4, 3));
        System.out.println("Concat: " + concat.apply("Java", "Script"));
    }
    
    private static void demonstrateCustomInterfaces() {
        System.out.println("\n\n=== CUSTOM FUNCTIONAL INTERFACES ===\n");
        
        // Custom interfaces for specific business logic
        System.out.println("Custom interfaces for specific use cases:");
        
        // 1. Domain-specific naming
        TaxCalculator incomeTax = (income, rate) -> income * rate / 100;
        System.out.println("Tax on $50000 at 20%: $" + incomeTax.calculate(50000, 20));
        
        // 2. Multiple parameters with clear meaning
        EmailSender emailSender = (to, subject, body) -> 
            System.out.println("Email sent to " + to + " with subject: " + subject);
        emailSender.send("user@example.com", "Welcome", "Hello there!");
        
        // 3. Exception handling built-in
        FileProcessor processor = filename -> {
            if (filename.endsWith(".txt")) {
                System.out.println("Processing text file: " + filename);
            } else {
                throw new UnsupportedOperationException("Only .txt files supported");
            }
        };
        
        try {
            processor.process("document.txt");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // 4. Builder pattern integration
        PersonCreator creator = (name, age, email) -> 
            new Person(name, age, email);
        
        Person person = creator.create("Alice", 25, "alice@example.com");
        System.out.println("Created: " + person);
        
        // 5. Event handling with context
        EventProcessor<String> eventProcessor = (event, timestamp) -> 
            System.out.println("[" + timestamp + "] Processing: " + event);
        
        eventProcessor.process("User Login", System.currentTimeMillis());
    }
    
    private static void demonstrateWhenToUseWhich() {
        System.out.println("\n\n=== WHEN TO USE WHICH ===\n");
        
        System.out.println("1. USE BUILT-IN when the signature matches standard patterns:");
        
        // Standard patterns - use built-in
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        
        // Use Predicate for filtering
        words.stream()
             .filter(word -> word.length() > 5)  // Predicate<String>
             .forEach(System.out::println);      // Consumer<String>
        
        // Use Function for transformation
        words.stream()
             .map(String::toUpperCase)           // Function<String, String>
             .forEach(System.out::println);      // Consumer<String>
        
        System.out.println("\n2. USE CUSTOM when you need:");
        
        // Domain-specific operations
        OrderValidator validator = (order, customer) -> 
            order.getTotal() > 0 && customer.isActive();
        
        // Multiple specific parameters
        ReportGenerator generator = (startDate, endDate, format, includeGraphs) -> 
            "Generated report from " + startDate + " to " + endDate;
        
        // Exception handling
        DatabaseOperation dbOp = query -> {
            if (query.trim().isEmpty()) {
                throw new IllegalArgumentException("Query cannot be empty");
            }
            return "Executed: " + query;
        };
        
        System.out.println("Custom interfaces provide better readability and type safety");
    }
}

// ===============================
// CUSTOM FUNCTIONAL INTERFACES
// ===============================

@FunctionalInterface
interface TaxCalculator {
    double calculate(double income, double rate);
}

@FunctionalInterface
interface EmailSender {
    void send(String to, String subject, String body);
}

@FunctionalInterface
interface FileProcessor {
    void process(String filename) throws Exception;
}

@FunctionalInterface
interface PersonCreator {
    Person create(String name, int age, String email);
}

@FunctionalInterface
interface EventProcessor<T> {
    void process(T event, long timestamp);
}

@FunctionalInterface
interface OrderValidator {
    boolean isValid(Order order, Customer customer);
}

@FunctionalInterface
interface ReportGenerator {
    String generate(String startDate, String endDate, String format, boolean includeGraphs);
}

@FunctionalInterface
interface DatabaseOperation {
    String execute(String query) throws Exception;
}

// ===============================
// SUPPORT CLASSES
// ===============================

class Person {
    private String name;
    private int age;
    private String email;
    
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, email='%s'}", name, age, email);
    }
}

class Order {
    private double total;
    
    public Order(double total) { this.total = total; }
    public double getTotal() { return total; }
}

class Customer {
    private boolean active;
    
    public Customer(boolean active) { this.active = active; }
    public boolean isActive() { return active; }
}

// ===============================
// COMPARISON SUMMARY
// ===============================

/*
BUILT-IN FUNCTIONAL INTERFACES:
┌─────────────────┬─────────────────┬─────────────────┐
│ Interface       │ Signature       │ Use Case        │
├─────────────────┼─────────────────┼─────────────────┤
│ Supplier<T>     │ () -> T         │ Factory/Provider│
│ Consumer<T>     │ T -> void       │ Side effects    │
│ Predicate<T>    │ T -> boolean    │ Testing/Filter  │
│ Function<T,R>   │ T -> R          │ Transformation  │
│ BiFunction<T,U,R>│(T,U) -> R      │ Two-input ops   │
│ UnaryOperator<T>│ T -> T          │ Same type ops   │
│ BinaryOperator<T>│(T,T) -> T      │ Combining ops   │
└─────────────────┴─────────────────┴─────────────────┘

WHEN TO USE BUILT-IN:
✓ Standard operations (map, filter, reduce)
✓ Stream API operations
✓ Generic utility functions
✓ Simple parameter patterns

WHEN TO CREATE CUSTOM:
✓ Domain-specific naming (TaxCalculator vs BiFunction)
✓ Multiple parameters with clear meaning
✓ Exception handling required
✓ Better code readability
✓ Type safety for specific use cases
✓ Integration with existing APIs
*/