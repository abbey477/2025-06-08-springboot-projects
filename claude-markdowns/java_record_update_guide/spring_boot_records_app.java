package com.example.recordsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Lombok imports for comparison
import lombok.Value;
import lombok.Builder;
import lombok.With;

@SpringBootApplication
public class RecordsDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecordsDemoApplication.class, args);
    }
}

// ===== RECORD DEFINITIONS =====

// Basic User Record with full features
record User(
    @NotBlank String name,
    @Email String email,
    @Min(18) @Max(100) int age,
    @NotNull LocalDate birthDate,
    @NotEmpty List<@NotBlank String> roles,
    UserPreferences preferences,
    LocalDateTime createdAt
) {
    // Compact constructor with validation and defaults
    public User {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (roles == null) {
            roles = List.of("USER");
        } else {
            roles = List.copyOf(roles); // Make immutable
        }
        if (preferences == null) {
            preferences = new UserPreferences("light", "en", true);
        }
    }
    
    // Factory methods
    public static User create(String name, String email, int age, LocalDate birthDate) {
        return new User(name, email, age, birthDate, List.of("USER"), null, null);
    }
    
    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String email;
        private int age;
        private LocalDate birthDate;
        private List<String> roles = new ArrayList<>();
        private UserPreferences preferences;
        private LocalDateTime createdAt;
        
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder roles(List<String> roles) { this.roles = new ArrayList<>(roles); return this; }
        public Builder addRole(String role) { this.roles.add(role); return this; }
        public Builder preferences(UserPreferences preferences) { this.preferences = preferences; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        
        public User build() {
            return new User(name, email, age, birthDate, roles, preferences, createdAt);
        }
    }
    
    // Immutable update methods
    public User withName(String newName) {
        return new User(newName, email, age, birthDate, roles, preferences, createdAt);
    }
    
    public User withEmail(String newEmail) {
        return new User(name, newEmail, age, birthDate, roles, preferences, createdAt);
    }
    
    public User withAge(int newAge) {
        return new User(name, email, newAge, birthDate, roles, preferences, createdAt);
    }
    
    public User withPreferences(UserPreferences newPreferences) {
        return new User(name, email, age, birthDate, roles, newPreferences, createdAt);
    }
    
    public User addRole(String role) {
        var newRoles = new ArrayList<>(roles);
        if (!newRoles.contains(role)) {
            newRoles.add(role);
        }
        return new User(name, email, age, birthDate, newRoles, preferences, createdAt);
    }
    
    public User removeRole(String role) {
        var newRoles = new ArrayList<>(roles);
        newRoles.remove(role);
        return new User(name, email, age, birthDate, newRoles, preferences, createdAt);
    }
    
    // Convenience methods
    public boolean hasRole(String role) { return roles.contains(role); }
    public boolean isAdmin() { return hasRole("ADMIN"); }
    public int getYearsOld() { return LocalDate.now().getYear() - birthDate.getYear(); }
}

// Nested UserPreferences record
record UserPreferences(
    @NotBlank String theme,
    @NotBlank String language,
    boolean emailNotifications
) {
    public UserPreferences {
        if (theme == null) theme = "light";
        if (language == null) language = "en";
    }
    
    public UserPreferences withTheme(String newTheme) {
        return new UserPreferences(newTheme, language, emailNotifications);
    }
    
    public UserPreferences withLanguage(String newLanguage) {
        return new UserPreferences(theme, newLanguage, emailNotifications);
    }
    
    public UserPreferences withEmailNotifications(boolean enabled) {
        return new UserPreferences(theme, language, enabled);
    }
}

// Complex nested record example
record Employee(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    @NotBlank String department,
    String position,
    @Min(0) int salary,
    LocalDate hireDate,
    List<@NotBlank String> skills,
    Address address
) {
    public Employee {
        if (skills == null) skills = List.of();
        else skills = List.copyOf(skills);
        if (hireDate == null) hireDate = LocalDate.now();
    }
    
    public record Address(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String state,
        String zipCode,
        String country
    ) {
        public Address {
            if (country == null) country = "USA";
        }
    }
    
    public String fullName() { return firstName + " " + lastName; }
    
    public Employee withSalary(int newSalary) {
        return new Employee(firstName, lastName, email, department, position, 
                          newSalary, hireDate, skills, address);
    }
    
    public Employee addSkill(String skill) {
        var newSkills = new ArrayList<>(skills);
        if (!newSkills.contains(skill)) {
            newSkills.add(skill);
        }
        return new Employee(firstName, lastName, email, department, position, 
                          salary, hireDate, newSkills, address);
    }
}

// Lombok comparison class
@Value
@Builder
@With
class UserLombok {
    @NotBlank String name;
    @Email String email;
    @Min(18) @Max(100) int age;
    @NotNull LocalDate birthDate;
    @Builder.Default List<String> roles = List.of("USER");
    UserPreferences preferences;
    @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
}

// ===== REQUEST/RESPONSE DTOs =====

record CreateUserRequest(
    @NotBlank String name,
    @Email String email,
    @Min(18) @Max(100) int age,
    @NotNull LocalDate birthDate,
    List<String> roles,
    UserPreferences preferences
) {}

record UpdateUserRequest(
    String name,
    @Email String email,
    @Min(18) @Max(100) Integer age,
    UserPreferences preferences
) {}

record CreateEmployeeRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    @NotBlank String department,
    String position,
    @Min(0) int salary,
    List<String> skills,
    Employee.Address address
) {}

record UserSummary(String name, String email, int age, List<String> roles) {
    public static UserSummary from(User user) {
        return new UserSummary(user.name(), user.email(), user.age(), user.roles());
    }
}

record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    LocalDateTime timestamp,
    Map<String, Object> metadata
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, LocalDateTime.now(), Map.of());
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), Map.of());
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), Map.of());
    }
    
    public static <T> ApiResponse<T> withMetadata(T data, Map<String, Object> metadata) {
        return new ApiResponse<>(true, "Success", data, LocalDateTime.now(), metadata);
    }
}

// ===== SERVICES =====

@Service
class UserService {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    
    public UserService() {
        // Initialize with sample data
        User admin = User.builder()
            .name("Admin User")
            .email("admin@example.com")
            .age(35)
            .birthDate(LocalDate.of(1988, 5, 15))
            .addRole("USER")
            .addRole("ADMIN")
            .preferences(new UserPreferences("dark", "en", true))
            .build();
            
        User regular = User.create("John Doe", "john@example.com", 28, LocalDate.of(1995, 8, 20));
        
        users.put("admin", admin);
        users.put("john_doe", regular);
    }
    
    public Collection<User> findAll() { return users.values(); }
    
    public Optional<User> findById(String id) { return Optional.ofNullable(users.get(id)); }
    
    public User save(String id, User user) {
        users.put(id, user);
        return user;
    }
    
    public boolean deleteById(String id) { return users.remove(id) != null; }
    
    public List<User> findByRole(String role) {
        return users.values().stream()
            .filter(user -> user.hasRole(role))
            .collect(Collectors.toList());
    }
    
    public Map<String, Long> getRoleStatistics() {
        return users.values().stream()
            .flatMap(user -> user.roles().stream())
            .collect(Collectors.groupingBy(role -> role, Collectors.counting()));
    }
}

@Service
class EmployeeService {
    private final Map<String, Employee> employees = new ConcurrentHashMap<>();
    
    public EmployeeService() {
        Employee.Address address = new Employee.Address("123 Main St", "Springfield", "IL", "62701", "USA");
        Employee emp = new Employee("Jane", "Smith", "jane@company.com", "Engineering", 
                                  "Senior Developer", 90000, LocalDate.of(2020, 1, 15),
                                  List.of("Java", "Spring", "PostgreSQL"), address);
        employees.put("jane_smith", emp);
    }
    
    public Collection<Employee> findAll() { return employees.values(); }
    public Optional<Employee> findById(String id) { return Optional.ofNullable(employees.get(id)); }
    public Employee save(String id, Employee employee) { employees.put(id, employee); return employee; }
    public boolean deleteById(String id) { return employees.remove(id) != null; }
}

// ===== CONTROLLERS =====

@RestController
@RequestMapping("/api/users")
@Validated
class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ApiResponse<Collection<UserSummary>> getAllUsers() {
        var users = userService.findAll().stream()
            .map(UserSummary::from)
            .toList();
        var metadata = Map.of("total", users.size(), "roles", userService.getRoleStatistics());
        return ApiResponse.withMetadata(users, metadata);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String id) {
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        String id = request.name().toLowerCase().replace(" ", "_");
        
        User user = User.builder()
            .name(request.name())
            .email(request.email())
            .age(request.age())
            .birthDate(request.birthDate())
            .roles(request.roles() != null ? request.roles() : List.of("USER"))
            .preferences(request.preferences())
            .build();
            
        userService.save(id, user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("User created", user));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String id, 
                                                       @Valid @RequestBody UpdateUserRequest request) {
        return userService.findById(id)
            .map(existingUser -> {
                User updatedUser = existingUser;
                if (request.name() != null) updatedUser = updatedUser.withName(request.name());
                if (request.email() != null) updatedUser = updatedUser.withEmail(request.email());
                if (request.age() != null) updatedUser = updatedUser.withAge(request.age());
                if (request.preferences() != null) updatedUser = updatedUser.withPreferences(request.preferences());
                
                userService.save(id, updatedUser);
                return ResponseEntity.ok(ApiResponse.success("User updated", updatedUser));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<User>> updateRoles(@PathVariable String id, 
                                                        @RequestBody Map<String, String> request) {
        return userService.findById(id)
            .map(user -> {
                String action = request.get("action");
                String role = request.get("role");
                
                User updatedUser = switch (action) {
                    case "add" -> user.addRole(role);
                    case "remove" -> user.removeRole(role);
                    default -> user;
                };
                
                userService.save(id, updatedUser);
                return ResponseEntity.ok(ApiResponse.success("Roles updated", updatedUser));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/roles/{role}")
    public ApiResponse<List<UserSummary>> getUsersByRole(@PathVariable String role) {
        var users = userService.findByRole(role).stream()
            .map(UserSummary::from)
            .toList();
        return ApiResponse.success(users);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.ok(ApiResponse.success("User deleted"));
        }
        return ResponseEntity.notFound().build();
    }
}

@RestController
@RequestMapping("/api/employees")
@Validated
class EmployeeController {
    
    private final EmployeeService employeeService;
    
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping
    public ApiResponse<Collection<Employee>> getAllEmployees() {
        return ApiResponse.success(employeeService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getEmployee(@PathVariable String id) {
        return employeeService.findById(id)
            .map(emp -> ResponseEntity.ok(ApiResponse.success(emp)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Employee>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        String id = (request.firstName() + "_" + request.lastName()).toLowerCase();
        
        Employee employee = new Employee(
            request.firstName(), request.lastName(), request.email(),
            request.department(), request.position(), request.salary(),
            LocalDate.now(), request.skills(), request.address()
        );
        
        employeeService.save(id, employee);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Employee created", employee));
    }
    
    @PutMapping("/{id}/salary")
    public ResponseEntity<ApiResponse<Employee>> updateSalary(@PathVariable String id, 
                                                             @RequestBody Map<String, Integer> request) {
        return employeeService.findById(id)
            .map(employee -> {
                Employee updated = employee.withSalary(request.get("salary"));
                employeeService.save(id, updated);
                return ResponseEntity.ok(ApiResponse.success("Salary updated", updated));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/skills")
    public ResponseEntity<ApiResponse<Employee>> addSkill(@PathVariable String id, 
                                                         @RequestBody Map<String, String> request) {
        return employeeService.findById(id)
            .map(employee -> {
                Employee updated = employee.addSkill(request.get("skill"));
                employeeService.save(id, updated);
                return ResponseEntity.ok(ApiResponse.success("Skill added", updated));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}

// Demo controller showing Record vs Lombok comparison
@RestController
@RequestMapping("/api/demo")
class DemoController {
    
    @GetMapping("/record-vs-lombok")
    public ApiResponse<Map<String, Object>> compareRecordVsLombok() {
        // Record example
        User recordUser = User.create("John Record", "john@record.com", 30, LocalDate.of(1993, 1, 1));
        User updatedRecord = recordUser.withAge(31);
        
        // Lombok example
        UserLombok lombokUser = UserLombok.builder()
            .name("John Lombok")
            .email("john@lombok.com")
            .age(30)
            .birthDate(LocalDate.of(1993, 1, 1))
            .build();
        UserLombok updatedLombok = lombokUser.withAge(31);
        
        var comparison = Map.of(
            "record_original", recordUser,
            "record_updated", updatedRecord,
            "lombok_original", lombokUser,
            "lombok_updated", updatedLombok,
            "immutability_proof", Map.of(
                "record_objects_equal", recordUser.equals(updatedRecord),
                "lombok_objects_equal", lombokUser.equals(updatedLombok)
            )
        );
        
        return ApiResponse.success("Comparison data", comparison);
    }
    
    @GetMapping("/builder-pattern")
    public ApiResponse<User> builderPatternDemo() {
        User complexUser = User.builder()
            .name("Complex User")
            .email("complex@example.com")
            .age(25)
            .birthDate(LocalDate.of(1998, 6, 15))
            .addRole("USER")
            .addRole("MODERATOR")
            .preferences(new UserPreferences("dark", "es", false))
            .build();
        
        return ApiResponse.success("Builder pattern demo", complexUser);
    }
}

// ===== EXCEPTION HANDLING =====

@Configuration
class WebConfig {
    
    @org.springframework.web.bind.annotation.ControllerAdvice
    public static class GlobalExceptionHandler {
        
        @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
                org.springframework.web.bind.MethodArgumentNotValidException ex) {
            
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Validation failed", errors, LocalDateTime.now(), Map.of()));
        }
        
        @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error: " + ex.getMessage()));
        }
    }
}