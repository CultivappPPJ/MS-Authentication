package com.cultivapp.authentication.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testUserCreation() {
        // Crear un usuario válido
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        // Verificar que el usuario se creó correctamente
        assertNotNull(user);
        assertEquals("Marcial", user.getFirstName());
        assertEquals("Diaz", user.getLastName());
        assertEquals("978030199", user.getPhoneNumber());
        assertEquals("mdiaz@gmail.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testUserValidation() {
        // Crear un usuario con datos inválidos
        User invalidUser = User.builder()
                .id(1L)
                .firstName(null)
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email(null)
                .password(null)
                .role(Role.USER)
                .build();

        // Verificar que las anotaciones de validación funcionan correctamente
        assertThrows(ConstraintViolationException.class, () -> {
            validator.validate(invalidUser);
        });
    }

    @Test
    public void testGetAuthorities() {
        // Crear un usuario y verificar los roles
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Verificar que el usuario tiene los roles correctos
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.USER.name())));
    }

    @Test
    public void testUserDetailsMethods() {
        // Crear un usuario y verificar los métodos de UserDetails
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        // Verificar que los métodos de UserDetails devuelven los valores esperados
        assertEquals("mdiaz@gmail.com", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testConstructorWithId() {
        // Crear un usuario con un ID específico
        User user = new User(1L, "Marcial", "Diaz", "978030199", "marcial.diaz@gmail.com", "password", Role.USER);

        // Verificar que el usuario se creó correctamente con el ID proporcionado
        assertEquals(1L, user.getId());
        assertEquals("Marcial", user.getFirstName());
        assertEquals("Diaz", user.getLastName());
        assertEquals("978030199", user.getPhoneNumber());
        assertEquals("marcial.diaz@gmail.com", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testGetterAndSetter() {
        // Create user
        User user = new User();
        user.setFirstName("Eduardo");

        // Verificar que el getter devuelve el valor correcto
        assertEquals("Eduardo", user.getFirstName());
    }
}
