package com.example.webapp;

import com.example.common.JsonUtils;
import com.example.common.StringUtils;
import com.example.data.config.DatabaseConfig;
import com.example.data.model.User;
import com.example.data.repository.UserRepository;
import com.example.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) {
        logger.info("🚀 Starting Base Multi-Module Application...");
        
        try {
            // Initialize components
            DataSource dataSource = DatabaseConfig.createDataSource();
            UserRepository userRepository = new UserRepository(dataSource);
            UserService userService = new UserService(userRepository);
            
            // Demonstrate functionality
            demonstrateStringUtils();
            demonstrateUserOperations(userService);
            demonstrateJsonSerialization();
            
            logger.info("✅ Application completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Application failed with error", e);
            System.exit(1);
        }
    }
    
    private static void demonstrateStringUtils() {
        logger.info("=== String Utils Demo ===");
        
        String[] testStrings = {"hello world", "maven BOM", "multi module"};
        
        for (String str : testStrings) {
            String capitalized = StringUtils.capitalize(str);
            String reversed = StringUtils.reverse(str);
            
            System.out.println("Original: " + str);
            System.out.println("Capitalized: " + capitalized);
            System.out.println("Reversed: " + reversed);
            System.out.println("---");
        }
        
        List<String> items = List.of("Java", "Maven", "JUnit", "H2");
        String joined = StringUtils.joinWithComma(items);
        System.out.println("Technologies: " + joined);
        System.out.println();
    }
    
    private static void demonstrateUserOperations(UserService userService) {
        logger.info("=== User Operations Demo ===");
        
        // Create sample users
        User user1 = userService.createUser("Alice Johnson", "alice@example.com", 28);
        User user2 = userService.createUser("Bob Smith", "bob@example.com", 35);
        User user3 = userService.createUser("Carol Williams", "carol@example.com", 42);
        
        System.out.println("Created users:");
        System.out.println("- " + user1);
        System.out.println("- " + user2);
        System.out.println("- " + user3);
        System.out.println();
        
        // List all users
        List<User> allUsers = userService.getAllUsers();
        System.out.println("All users in database:");
        allUsers.forEach(user -> System.out.println("- " + user));
        System.out.println("Total users: " + allUsers.size());
        System.out.println();
        
        // Update a user
        user1.setAge(29);
        User updatedUser = userService.updateUser(user1);
        System.out.println("Updated user: " + updatedUser);
        System.out.println();
        
        // Delete a user
        boolean deleted = userService.deleteUser(user2.getId());
        System.out.println("User deleted: " + deleted);
        
        // List remaining users
        List<User> remainingUsers = userService.getAllUsers();
        System.out.println("Remaining users: " + remainingUsers.size());
        remainingUsers.forEach(user -> System.out.println("- " + user));
        System.out.println();
    }
    
    private static void demonstrateJsonSerialization() {
        logger.info("=== JSON Serialization Demo ===");
        
        User user = new User("Dave Wilson", "dave@example.com", 33);
        user.setId(100L);
        
        // Convert to JSON
        String json = JsonUtils.toJson(user);
        System.out.println("User as JSON:");
        System.out.println(json);
        System.out.println();
        
        // Convert back from JSON
        User deserializedUser = JsonUtils.fromJson(json, User.class);
        System.out.println("Deserialized user:");
        System.out.println(deserializedUser);
        System.out.println("Equal to original: " + user.equals(deserializedUser));
        System.out.println();
    }
}
