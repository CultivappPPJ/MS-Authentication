package com.cultivapp.authentication.controllers;

import com.cultivapp.authentication.dto.AuthResponse;
import com.cultivapp.authentication.dto.AuthenticationRequest;
import com.cultivapp.authentication.dto.RegisterRequest;
import com.cultivapp.authentication.exceptions.EmailAlreadyExistsException;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import com.cultivapp.authentication.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private AuthService authServiceMock;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testRegister() {
        // Configurar datos de prueba para RegisterRequest
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978030199", "mdiaz@gmail.com", "contrasena");

        // Configurar el comportamiento simulado del servicio
        when(authServiceMock.register(request)).thenReturn(AuthResponse.builder().build());

        // Llamar al método del controlador
        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        // Verificar que la respuesta es OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(authServiceMock, times(1)).register(request);
    }

    @Test
    public void testAuthenticate() {
        // Configurar datos de prueba para AuthenticationRequest
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");

        // Configurar el comportamiento simulado del servicio
        when(authServiceMock.authenticate(request)).thenReturn(AuthResponse.builder().build());

        // Llamar al método del controlador
        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        // Verificar que la respuesta es OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testRegisterError() {
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978030199", "mdiaz@gmail.com", "contrasena");

        // Configurar el comportamiento simulado del servicio para lanzar una excepción
        when(authServiceMock.register(request)).thenThrow(new EmailAlreadyExistsException("Email already exists"));

        // Llamar al método del controlador
        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        // Verificar que la respuesta es HttpStatus.BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(authServiceMock, times(1)).register(request);
    }

    @Test
    public void testAuthenticateError() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");

        // Configurar el comportamiento simulado del servicio para lanzar una excepción
        when(authServiceMock.authenticate(request)).thenThrow(new EmailNotFoundException("Email not found"));

        // Llamar al método del controlador
        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        // Verificar que la respuesta es HttpStatus.UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testAuthenticateIncorrectCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena_incorrecta");

        // Configurar el comportamiento simulado del servicio para lanzar BadCredentialsException
        when(authServiceMock.authenticate(request)).thenThrow(new BadCredentialsException("Credentials are incorrect"));

        // Llamar al método del controlador
        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        // Verificar que la respuesta es HttpStatus.UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testInvalidInputForRegister() {
        // Configurar datos de prueba para RegisterRequest con campos nulos
        RegisterRequest request = new RegisterRequest(null, "Diaz", null, "mdiaz@gmail.com", "contrasena");

        // Llamar al método del controlador con datos de entrada inválidos
        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        // Verificar que la respuesta es HttpStatus.BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Verificar que el servicio no fue llamado
        verify(authServiceMock, never()).register(request);
    }



    @Test
    public void testNullFieldsInAuthenticationRequest() {
        // Create an AuthenticationRequest with null fields
        AuthenticationRequest request = new AuthenticationRequest(null, "contrasena");

        // Call the controller method
        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        // Verify that the response is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Verify that the error message is "Null fields"
        assertEquals("Null fields", responseEntity.getBody().getError());

        // Verify that the authService.authenticate was not called
        verify(authServiceMock, never()).authenticate(request);
    }
}
