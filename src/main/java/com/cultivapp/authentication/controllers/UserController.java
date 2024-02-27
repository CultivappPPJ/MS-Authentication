package com.cultivapp.authentication.controllers;

import com.cultivapp.authentication.dto.AuthResponse;
import com.cultivapp.authentication.dto.AuthenticationRequest;
import com.cultivapp.authentication.dto.RegisterRequest;
import com.cultivapp.authentication.exceptions.EmailAlreadyExistsException;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import com.cultivapp.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v01/auth")
@CrossOrigin("http://localhost:5173/")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (request == null || nullFields(request)) {
            // Returns a ResponseEntity with status code BAD_REQUEST if fields are null
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().error("Null fields").build());
        }
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse.builder().error(e.getMessage()).build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request){
        if (request == null || nullFields(request)) {
            // Returns a ResponseEntity with status code BAD_REQUEST if fields are null
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder().error("Null fields").build());
        }
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthResponse.builder()
                            .error(e.getMessage())
                            .build());
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthResponse.builder()
                            .error("Credentials are incorrect.")
                            .build());
        }
    }

    // Method that verifies if there are null fields in input
    private boolean nullFields(RegisterRequest request) {
        return request.getFirstName() == null ||
                request.getLastName() == null ||
                request.getPhoneNumber() == null ||
                request.getEmail() == null ||
                request.getPassword() == null;
    }

    private boolean nullFields(AuthenticationRequest request) {
        return  request.getEmail() == null ||
                request.getPassword() == null;
    }
}