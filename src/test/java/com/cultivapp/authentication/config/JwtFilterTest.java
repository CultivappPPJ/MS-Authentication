package com.cultivapp.authentication.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        MockitoAnnotations.initMocks(this);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String validToken = "validToken";
        String userEmail = "user@example.com";
        UserDetails userDetails = new User(userEmail, "", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.getUserName(validToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.validateToken(validToken, userDetails)).thenReturn(true);

        SecurityContextHolder.clearContext();

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).getUserName(validToken);
        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).validateToken(validToken, userDetails);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
        MockitoAnnotations.initMocks(this);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String invalidToken = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtService.getUserName(invalidToken)).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).getUserName(invalidToken);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void testDoFilterInternal_WithValidToken_NoExistingAuthentication() throws ServletException, IOException {
        MockitoAnnotations.initMocks(this);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String validToken = "validToken";
        String userEmail = "user@example.com";
        UserDetails userDetails = new User(userEmail, "", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.getUserName(validToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.validateToken(validToken, userDetails)).thenReturn(true);

        SecurityContextHolder.clearContext();

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).getUserName(validToken);
        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).validateToken(validToken, userDetails);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}
