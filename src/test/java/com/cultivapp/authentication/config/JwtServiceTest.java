package com.cultivapp.authentication.config;

import com.cultivapp.authentication.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    private final String SECRET_KEY = "dad6d5e1415185e725d60db88a967a3dff04fcdf88d9740be6ebf18ccc228501";

    private final JwtService jwtService = new JwtService();

    @Test
    public void testGenerateToken() {
        // Arrange
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        // Arrange
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key", "value");

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGetUserName() {
        // Arrange
        String token = jwtService.generateToken(User.withUsername("testUser").password("password").roles("USER").build());

        // Act
        String username = jwtService.getUserName(token);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    public void testGetClaim() {
        // Arrange
        String token = jwtService.generateToken(User.withUsername("testUser").password("password").roles("USER").build());

        // Act
        String subject = jwtService.getClaim(token, Claims::getSubject);

        // Assert
        assertEquals("testUser", subject);
    }

    @Test
    public void testValidateToken() {
        // Arrange
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        // Use reflection to access private method
        Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);

        // Act
        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    public void testGetExpiration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        // Use reflection to access private method
        Method getExpirationMethod = JwtService.class.getDeclaredMethod("getExpiration", String.class);
        getExpirationMethod.setAccessible(true);

        // Act
        Date expiration = (Date) getExpirationMethod.invoke(jwtService, token);

        // Assert
        assertNotNull(expiration);
    }
}
