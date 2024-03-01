package com.cultivapp.authentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterRequestTest {

    @Test
    public void testBuilder() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .email("marcial.diaz@gmail.com")
                .password("securePassword")
                .build();

        assertEquals("Marcial", registerRequest.getFirstName());
        assertEquals("Diaz", registerRequest.getLastName());
        assertEquals("marcial.diaz@gmail.com", registerRequest.getEmail());
        assertEquals("securePassword", registerRequest.getPassword());
    }

    @Test
    public void testAllArgsConstructor() {
        RegisterRequest registerRequest = new RegisterRequest("Marcial", "Diaz", "marcial.diaz@gmail.com", "securePassword");

        assertEquals("Marcial", registerRequest.getFirstName());
        assertEquals("Diaz", registerRequest.getLastName());
        assertEquals("marcial.diaz@gmail.com", registerRequest.getEmail());
        assertEquals("securePassword", registerRequest.getPassword());
    }

    @Test
    public void testNoArgsConstructor() {
        RegisterRequest registerRequest = new RegisterRequest();

        assertNull(registerRequest.getFirstName());
        assertNull(registerRequest.getLastName());
        assertNull(registerRequest.getEmail());
        assertNull(registerRequest.getPassword());
    }

    @Test
    public void testGettersAndSetters() {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setFirstName("Marcial");
        registerRequest.setLastName("Diaz");
        registerRequest.setEmail("marcial.diaz@gmail.com");
        registerRequest.setPassword("securePassword");

        assertEquals("Marcial", registerRequest.getFirstName());
        assertEquals("Diaz", registerRequest.getLastName());
        assertEquals("marcial.diaz@gmail.com", registerRequest.getEmail());
        assertEquals("securePassword", registerRequest.getPassword());
    }

    @Test
    public void testToString() {
        RegisterRequest registerRequest = new RegisterRequest("Marcial", "Diaz", "marcial.diaz@gmail.com", "securePassword");
        String expectedToString = "RegisterRequest(firstName=Marcial, lastName=Diaz, email=marcial.diaz@gmail.com, password=securePassword)";

        assertEquals(expectedToString, registerRequest.toString());
    }
}
