package com.example.webapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Application Tests")
class ApplicationTest {
    
    @Test
    @DisplayName("Should run application main method without exceptions")
    void testApplicationMain() {
        // Test that the main method runs without throwing exceptions
        assertDoesNotThrow(() -> {
            Application.main(new String[]{});
        });
    }
}
