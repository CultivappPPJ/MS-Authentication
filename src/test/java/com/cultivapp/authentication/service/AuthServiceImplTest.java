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
import org.springframework.security.core.AuthenticationException;
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
        // Set up test data for RegisterRequest
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978020189", "mdiaz@gmail.com", "password");

        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Set up simulated behavior for the repository
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        // Configurar el comportamiento simulado del jwtService
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // Set up simulated behavior for the jwtService
        AuthResponse response = authService.register(request);

        // Verify that the response is as expected
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        // Verify that userRepository.save was called with the correct user
        verify(userRepository, times(1)).save(argThat(user ->
                user.getFirstName().equals("Marcial") &&
                        user.getLastName().equals("Diaz") &&
                        user.getPhoneNumber().equals("978020189") &&
                        user.getEmail().equals("mdiaz@gmail.com") &&
                        user.getPassword().equals("hashedPassword") &&
                        user.getRole() == Role.USER
        ));
    }

    @Test
    void testRegister_EmailAlreadyExistsException() {
        // Set up test data for RegisterRequest
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978020189", "mdiaz@gmail.com", "password");

        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Verificar que se lanza la excepción esperada
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));

        // Verificar que userRepository.save no se llamó
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticate_Success() {
        // Configurar datos de prueba para AuthenticationRequest
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "password");

        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Configurar el comportamiento simulado del authenticationManager
        doNothing().when(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Configurar el comportamiento simulado del jwtService
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // Llamar al método del servicio
        AuthResponse response = authService.authenticate(request);

        // Verificar que la respuesta es la esperada
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
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
        // Configurar datos de prueba
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");
        User user = new User(1L, "Marcial", "Diaz", "978030199", "mdiaz@gmail.com", "hashedPassword", Role.USER);

        // Configurar comportamiento simulado del repositorio
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Configurar comportamiento simulado del servicio de autenticación
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // Configurar comportamiento simulado del servicio JWT
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        // Llamar al método del servicio
        AuthResponse authResponse = authService.authenticate(request);

        // Verificar que la respuesta contiene el token esperado
        assertEquals("jwtToken", authResponse.getToken());

        // Verificar que el método de autenticación fue llamado con los parámetros correctos
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    }

    @Test
    void testAuthenticate_AuthenticationException() {
        // Configurar datos de prueba para AuthenticationRequest
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "invalidPassword");

        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Configurar el comportamiento simulado del authenticationManager para lanzar AuthenticationException
        doThrow(AuthenticationException.class).when(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Verificar que se lanza la excepción esperada
        assertThrows(AuthenticationException.class, () -> authService.authenticate(request));

        // Verificar que jwtService.generateToken no se llamó
        verify(jwtService, never()).generateToken(any(User.class));
    }
}
