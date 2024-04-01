package com.cultivapp.authentication.service;

import com.cultivapp.authentication.dto.*;
import com.cultivapp.authentication.entity.Role;
import com.cultivapp.authentication.entity.User;
import com.cultivapp.authentication.repository.UserRepository;
import com.cultivapp.authentication.exceptions.EmailAlreadyExistsException;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    @Transactional
    public DeleteResponse deleteUser(DeleteRequest request) {
        Optional<User> existUserOptional = userRepository.findUserByEmail(request.getEmail());
        User existUser = existUserOptional.orElseThrow(() ->
                new EmailNotFoundException("Usuario de email " + request.getEmail() + " no encontrado"));
        existUser.setEnabled(false);
        userRepository.save(existUser);
        return DeleteResponse.builder().build();
    }
}