package com.cultivapp.authentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationRequestTest {

    @Test
    public void testNoArgsConstructor() {
        // Crear una instancia usando el constructor sin argumentos
        AuthenticationRequest request = new AuthenticationRequest();

        // Verificar que la instancia no es nula
        assertNotNull(request);
    }

    @Test
    public void testAllArgsConstructor() {
        // Crear una instancia usando el constructor con todos los argumentos
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password123");

        // Verificar que los valores se han establecido correctamente
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void testSetterAndGetter() {
        // Crear una instancia usando el constructor sin argumentos
        AuthenticationRequest request = new AuthenticationRequest();

        // Establecer valores usando setters
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Verificar que los valores se han establecido correctamente
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void testBuilderPattern() {
        // Crear una instancia utilizando el patrón Builder
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        // Verificar que los valores se han establecido correctamente
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }
}
