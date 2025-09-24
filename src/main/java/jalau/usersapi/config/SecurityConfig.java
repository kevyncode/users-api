package jalau.usersapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Users API implementing Basic Authentication.
 * 
 * <p>This configuration class sets up HTTP Basic Authentication with the following features:
 * <ul>
 *   <li>Protects all endpoints under /api/users/** with Basic Authentication</li>
 *   <li>Uses in-memory user store with admin:admin credentials</li>
 *   <li>Stateless session management for REST API compatibility</li>
 *   <li>Allows access to Swagger UI and OpenAPI documentation</li>
 *   <li>Disables CSRF protection for API usage</li>
 * </ul>
 * 
 * @author Users API Development Team
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain with HTTP Basic Authentication.
     * 
     * <p>Security rules applied:
     * <ul>
     *   <li>/swagger-ui/**, /v3/api-docs/** - Public access for API documentation</li>
     *   <li>/h2-console/** - Public access for H2 database console (development only)</li>
     *   <li>/api/users/** - Protected endpoints requiring Basic Authentication</li>
     *   <li>All other requests - Public access</li>
     * </ul>
     * 
     * @param http the HttpSecurity configuration object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(httpBasic -> {
                httpBasic.realmName("Users API");
            })
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .invalidSessionUrl("/swagger-ui/index.html")
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions().sameOrigin()
                .cacheControl(cache -> cache.disable())
            );

        return http.build();
    }

    /**
     * Configures in-memory user details service with default admin user.
     * 
     * <p>Creates a single admin user with the following credentials:
     * <ul>
     *   <li>Username: admin</li>
     *   <li>Password: admin (BCrypt encoded)</li>
     *   <li>Role: ADMIN</li>
     * </ul>
     * 
     * @return UserDetailsService with configured admin user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Provides BCrypt password encoder for secure password hashing.
     * 
     * <p>BCrypt is used for encoding passwords with adaptive hashing function
     * designed for secure password storage.
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}