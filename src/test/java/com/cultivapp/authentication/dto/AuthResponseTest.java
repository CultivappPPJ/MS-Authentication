package com.cultivapp.authentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthResponseTest {

    @Test
    public void testBuilder() {
        AuthResponse authResponse = AuthResponse.builder()
                .token("testToken")
                .error("testError")
                .build();

        assertEquals("testToken", authResponse.getToken());
        assertEquals("testError", authResponse.getError());
    }

    @Test
    public void testAllArgsConstructor() {
        AuthResponse authResponse = new AuthResponse("testToken", "testError");

        assertEquals("testToken", authResponse.getToken());
        assertEquals("testError", authResponse.getError());
    }

    @Test
    public void testGettersAndSetters() {
        AuthResponse authResponse = new AuthResponse();

        authResponse.setToken("testToken");
        authResponse.setError("testError");

        assertEquals("testToken", authResponse.getToken());
        assertEquals("testError", authResponse.getError());
    }

    @Test
    public void testToString() {
        AuthResponse authResponse = new AuthResponse("testToken", "testError");
        String expectedToString = "AuthResponse(token=testToken, error=testError)";

        assertEquals(expectedToString, authResponse.toString());
    }
}