package com.cultivapp.authentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationRequestTest {

    @Test
    public void testNoArgsConstructor() {
        AuthenticationRequest request = new AuthenticationRequest();

        assertNotNull(request);
    }

    @Test
    public void testAllArgsConstructor() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void testSetterAndGetter() {
        AuthenticationRequest request = new AuthenticationRequest();

        request.setEmail("test@example.com");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void testBuilderPattern() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }
}
