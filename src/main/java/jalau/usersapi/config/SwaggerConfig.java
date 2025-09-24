package jalau.usersapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for Users API documentation.
 * 
 * <p>This configuration class sets up Swagger UI with the following features:
 * <ul>
 *   <li>Interactive API documentation at /swagger-ui/index.html</li>
 *   <li>Basic Authentication support for testing protected endpoints</li>
 *   <li>Comprehensive API information and server configuration</li>
 *   <li>Security scheme definition for HTTP Basic Authentication</li>
 * </ul>
 * 
 * <p>Users can authenticate in Swagger UI by clicking the "Authorize" button
 * and entering the credentials: username=admin, password=admin
 * 
 * @author Users API Development Team
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "basicAuth";

    /**
     * Creates the OpenAPI specification configuration.
     * 
     * <p>Configures:
     * <ul>
     *   <li>API metadata (title, description, version)</li>
     *   <li>Development server information</li>
     *   <li>HTTP Basic Authentication security scheme</li>
     * </ul>
     * 
     * <p>The security scheme allows users to authenticate directly in Swagger UI
     * to test protected endpoints without manually adding Authorization headers.
     * 
     * @return configured OpenAPI specification
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .description("API for user management with Clean Architecture and Basic Authentication")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Basic Authentication (Username: admin, Password: admin)")));
    }
}