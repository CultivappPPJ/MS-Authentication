package com.cultivapp.authentication.service;

import com.cultivapp.authentication.config.JwtService;
import com.cultivapp.authentication.dto.AuthResponse;
import com.cultivapp.authentication.dto.AuthenticationRequest;
import com.cultivapp.authentication.dto.RegisterRequest;
import com.cultivapp.authentication.entity.Role;
import com.cultivapp.authentication.entity.User;
import com.cultivapp.authentication.exceptions.EmailAlreadyExistsException;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import com.cultivapp.authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "mdiaz@gmail.com", "password");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        verify(userRepository, times(1)).save(argThat(user ->
                user.getFirstName().equals("Marcial") &&
                        user.getLastName().equals("Diaz") &&
                        user.getEmail().equals("mdiaz@gmail.com") &&
                        user.getPassword().equals("hashedPassword") &&
                        user.getRole() == Role.USER
        ));
    }

    @Test
    void testRegister_EmailAlreadyExistsException() {
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "mdiaz@gmail.com", "password");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticate_EmailNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "password");
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        assertThrows(EmailNotFoundException.class, () -> authService.authenticate(request));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    public void testAuthenticateUserExists() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");
        User user = new User(1L, "Marcial", "Diaz", "mdiaz@gmail.com", "hashedPassword", Role.USER);

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any())).thenReturn(null);

        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthResponse authResponse = authService.authenticate(request);

        assertEquals("jwtToken", authResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    }

    @Test
    void testAuthenticate_AuthenticationException() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "password");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> authService.authenticate(request));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "password");
        User user = new User(1L, "Marcial", "Diaz", "mdiaz@gmail.com", "hashedPassword", Role.USER);

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any())).thenReturn(null);

        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthResponse authResponse = authService.authenticate(request);

        assertEquals("jwtToken", authResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    }
}
