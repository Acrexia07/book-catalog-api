package com.marlonb.book_catalog_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class BasicAuthenticationConfig {

    // Configures HTTP security for API endpoints
    // - Requires authentication with ADMIN role for "/api/**"
    // - Disables CSRF (suitable for stateless REST APIs)
    // - Enables HTTP Basic authentication
    // - Sets session management to stateless (no HTTP sessions stored)
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(auth -> auth.requestMatchers
                                         ("/api/**").hasRole("ADMIN")
                                                              .anyRequest()
                                                              .permitAll())
                   .csrf(AbstractHttpConfigurer::disable)
                   .httpBasic(Customizer.withDefaults())
                   .sessionManagement(session ->
                                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .build();
    }

    // Provides in-memory user credentials for testing (H2 database)
    // Useful during development before integrating with persistent authentication
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("acrexia")
                        .password(passwordEncoder().encode("dummy"))
                        .authorities("ROLE_ADMIN")
                        .build()
        );
    }

    @Bean
    // Configures BCryptPasswordEncoder to hash passwords
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
