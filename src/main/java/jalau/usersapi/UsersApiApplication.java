package jalau.usersapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Users API
 * 
 * This class serves as the entry point for the Spring Boot application.
 * It initializes the Spring context and starts the embedded web server.
 */
@SpringBootApplication
public class UsersApiApplication {

    /**
     * Main method that starts the Spring Boot application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Load .env file before Spring Boot starts
        loadEnvironmentVariables();
        
        SpringApplication.run(UsersApiApplication.class, args);
    }
    
    /**
     * Load environment variables from .env file
     */
    private static void loadEnvironmentVariables() {
        try {
            System.out.println("🔧 Loading .env variables...");
            
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Set system properties so Spring can use them
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
                System.out.println("✅ Set " + entry.getKey() + " = " + 
                    (entry.getKey().contains("PASSWORD") ? "***" : entry.getValue()));
            });
            
            System.out.println("🔧 .env variables loaded successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading .env file: " + e.getMessage());
            System.err.println("⚠️  Continuing with default configuration...");
        }
    }
}
