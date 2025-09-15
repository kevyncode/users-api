package jalau.usersapi.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for HealthController
 * 
 * This test class validates the health check endpoint functionality
 * using Spring Boot's WebMvcTest for lightweight testing.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {
    
    /**
     * MockMvc instance for performing HTTP requests in tests
     * Automatically configured by @WebMvcTest annotation
     */
    @Autowired
    private MockMvc mockMvc;
    
    /**
     * Test that health endpoint returns 200 OK status with proper JSON content
     * 
     * This test verifies:
     * - HTTP status is 200 OK
     * - Content type is application/json
     * - Response contains required fields with expected values
     * - Status field equals "UP"
     * - Application field equals "Users API"
     * - Timestamp field exists
     */
    @Test
    void healthEndpoint_ShouldReturn200OK() throws Exception {
        mockMvc.perform(get("/api/health"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status").value("UP"))
               .andExpect(jsonPath("$.application").value("Users API"))
               .andExpect(jsonPath("$.timestamp").exists());
    }
    
    /**
     * Test that health endpoint returns proper JSON structure
     * 
     * This test verifies that all required fields are present in the response:
     * - status field exists
     * - timestamp field exists 
     * - application field exists
     * 
     * This test focuses on structure validation rather than specific values
     */
    @Test
    void healthEndpoint_ShouldReturnProperJsonStructure() throws Exception {
        mockMvc.perform(get("/api/health"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").exists())
               .andExpect(jsonPath("$.timestamp").exists())
               .andExpect(jsonPath("$.application").exists());
    }
}