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
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978030199", "mdiaz@gmail.com", "contrasena");

        when(authServiceMock.register(request)).thenReturn(AuthResponse.builder().build());

        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authServiceMock, times(1)).register(request);
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");

        when(authServiceMock.authenticate(request)).thenReturn(AuthResponse.builder().build());

        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testRegisterError() {
        RegisterRequest request = new RegisterRequest("Marcial", "Diaz", "978030199", "mdiaz@gmail.com", "contrasena");

        when(authServiceMock.register(request)).thenThrow(new EmailAlreadyExistsException("Email already exists"));

        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authServiceMock, times(1)).register(request);
    }

    @Test
    public void testAuthenticateError() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena");

        when(authServiceMock.authenticate(request)).thenThrow(new EmailNotFoundException("Email not found"));

        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testAuthenticateIncorrectCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("mdiaz@gmail.com", "contrasena_incorrecta");

        when(authServiceMock.authenticate(request)).thenThrow(new BadCredentialsException("Credentials are incorrect"));

        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(authServiceMock, times(1)).authenticate(request);
    }

    @Test
    public void testInvalidInputForRegister() {
        RegisterRequest request = new RegisterRequest(null, "Diaz", null, "mdiaz@gmail.com", "contrasena");

        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authServiceMock, never()).register(request);
    }



    @Test
    public void testNullFieldsInAuthenticationRequest() {
        AuthenticationRequest request = new AuthenticationRequest(null, "contrasena");

        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals("Null fields", responseEntity.getBody().getError());

        verify(authServiceMock, never()).authenticate(request);
    }

    @Test
    public void testRegisterNullFields() {
        RegisterRequest request = new RegisterRequest(null, "Diaz", null, "mdiaz@gmail.com", "contrasena");

        ResponseEntity<AuthResponse> responseEntity = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authServiceMock, never()).register(request);

        assertEquals("Null fields", responseEntity.getBody().getError());
    }

    @Test
    public void testAuthenticateNullFields() {
        AuthenticationRequest request = new AuthenticationRequest(null, "contrasena");

        ResponseEntity<AuthResponse> responseEntity = userController.authenticate(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals("Null fields", responseEntity.getBody().getError());

        verify(authServiceMock, never()).authenticate(request);
    }

}
