package com.cultivapp.authentication.config;

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
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key", "value");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGetUserName() {
        String token = jwtService.generateToken(User.withUsername("testUser").password("password").roles("USER").build());

        String username = jwtService.getUserName(token);

        assertEquals("testUser", username);
    }

    @Test
    public void testGetClaim() {
        String token = jwtService.generateToken(User.withUsername("testUser").password("password").roles("USER").build());

        String subject = jwtService.getClaim(token, Claims::getSubject);

        assertEquals("testUser", subject);
    }

    @Test
    public void testValidateToken() {
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);

        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, token);

        assertFalse(isExpired);
    }

    @Test
    public void testGetExpiration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        Method getExpirationMethod = JwtService.class.getDeclaredMethod("getExpiration", String.class);
        getExpirationMethod.setAccessible(true);

        Date expiration = (Date) getExpirationMethod.invoke(jwtService, token);

        assertNotNull(expiration);
    }
}
