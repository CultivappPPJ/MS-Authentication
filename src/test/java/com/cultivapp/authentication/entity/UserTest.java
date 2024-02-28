package com.cultivapp.authentication.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        assertNotNull(user);
        assertEquals("Marcial", user.getFirstName());
        assertEquals("Diaz", user.getLastName());
        assertEquals("978030199", user.getPhoneNumber());
        assertEquals("mdiaz@gmail.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testGetAuthorities() {
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.USER.name())));
    }

    @Test
    public void testUserDetailsMethods() {
        User user = User.builder()
                .firstName("Marcial")
                .lastName("Diaz")
                .phoneNumber("978030199")
                .email("mdiaz@gmail.com")
                .password("password123")
                .role(Role.USER)
                .build();

        assertEquals("mdiaz@gmail.com", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testConstructorWithId() {
        User user = new User(1L, "Marcial", "Diaz", "978030199", "marcial.diaz@gmail.com", "password", Role.USER);

        assertEquals(1L, user.getId());
        assertEquals("Marcial", user.getFirstName());
        assertEquals("Diaz", user.getLastName());
        assertEquals("978030199", user.getPhoneNumber());
        assertEquals("marcial.diaz@gmail.com", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testGetterAndSetter() {
        User user = new User();
        user.setFirstName("Eduardo");

        assertEquals("Eduardo", user.getFirstName());
    }
}
