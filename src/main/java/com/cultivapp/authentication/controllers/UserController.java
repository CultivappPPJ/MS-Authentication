package com.cultivapp.authentication.controllers;

import com.cultivapp.authentication.dto.*;
import com.cultivapp.authentication.exceptions.EmailNotFoundException;
import com.cultivapp.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin("http://localhost:5173/")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate;

    // Define la URL del endpoint en el controlador de terrenos como una constante
    private static final String TERRAIN_SERVICE_URL = "http://localhost:8080/api/v1/terrain/crud/deleteByUser/";

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> deleteUser(@RequestBody DeleteRequest request, @RequestHeader("Authorization") String token) {
        try {
            DeleteResponse response = userService.deleteUser(request);
            log.info("Delete response OK{}", response);
            // Llama al endpoint correspondiente en el controlador de terrenos
            String email = request.getEmail();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // Usa el token recibido en la solicitud
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> terrainResponse = restTemplate.exchange(TERRAIN_SERVICE_URL + email,
                    HttpMethod.DELETE, entity, String.class);

            if (terrainResponse.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response);
            } else {
                // Maneja el caso en el que la eliminación de terrenos falla
                log.error("Error al eliminar terrenos asociados al usuario {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DeleteResponse.builder().error("Error al eliminar terrenos asociados al usuario").build());
            }
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DeleteResponse.builder().error(e.getMessage()).build());
        }
    }
}