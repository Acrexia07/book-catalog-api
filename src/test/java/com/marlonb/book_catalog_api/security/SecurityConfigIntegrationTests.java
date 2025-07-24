package com.marlonb.book_catalog_api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setup () {
        baseUrl = "http://localhost:" + port;
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("STP-001: Should authenticate with configured credentials")
        void shouldAuthenticateWithConfiguredCredentials () {

            // Arrange + Act
            ResponseEntity<String> response =
                    restTemplate.withBasicAuth("acrexia", "dummy")
                                .getForEntity(baseUrl + "/api/categories", String.class);

            System.out.println("Authenticated status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

            // Assert
           assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    class NegativeTest {
        @Test
        @DisplayName("STP-001: Should restrict unauthorized access")
        void shouldRestrictUnauthorizedAccess () {

            // Arrange + Act
            ResponseEntity<String> response =
                    restTemplate.withBasicAuth("wrongUser", "wrongPassword")
                            .getForEntity(baseUrl + "/api/categories", String.class);

            System.out.println("Authenticated status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }
}
