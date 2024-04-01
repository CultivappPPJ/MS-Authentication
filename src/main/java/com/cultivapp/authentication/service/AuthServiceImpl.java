package com.cultivapp.authentication.service;

import com.cultivapp.authentication.config.JwtService;
import com.cultivapp.authentication.dto.AuthResponse;
import com.cultivapp.authentication.dto.AuthenticationRequest;
import com.cultivapp.authentication.dto.RegisterRequest;
import com.cultivapp.authentication.entity.Role;
import com.cultivapp.authentication.entity.User;
import com.cultivapp.authentication.repository.UserRepository;
import com.cultivapp.authentication.exceptions.EmailAlreadyExistsException;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse register(RegisterRequest request) {
        Optional<User> existUser = userRepository.findUserByEmail(request.getEmail());
        if (existUser.isPresent()) {
            throw new EmailAlreadyExistsException("El email ya está registrado!");
        } else {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        }
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );
            User user = userOptional.get();
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        } else {
            throw new EmailNotFoundException("El email no está registrado!");
        }
    }
}