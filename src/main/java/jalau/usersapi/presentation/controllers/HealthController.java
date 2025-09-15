package jalau.usersapi.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller
 * 
 * This controller provides a simple health check endpoint
 * to verify that the application is running properly.
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    
    /**
     * Health check endpoint
     * 
     * @return ResponseEntity with 200 OK status and health information
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("application", "Users API");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}
