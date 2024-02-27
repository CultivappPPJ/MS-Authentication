package com.cultivapp.authentication.config;

import com.cultivapp.authentication.entity.User;
import com.cultivapp.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.name=application-test"})
public class AppConfigTest {

    /*

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppConfig appConfig;

    @Test
    public void testUserDetailsService() {
        // Configurar comportamiento simulado del repositorio
        when(userRepository.findUserByEmail("marcial.diaz@gmail.com"))
                .thenReturn(Optional.of(User.builder()
                        .firstName("Marcial")
                        .lastName("Diaz")
                        .phoneNumber("978030199")
                        .email("marcial.diaz@gmail.com")
                        .password("securePassword")
                        .build()));

        // Llamar al método y verificar resultados
        UserDetailsService userDetailsService = appConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("marcial.diaz@gmail.com");

        assertNotNull(userDetails);
        assertEquals("marcial.diaz@gmail.com", userDetails.getUsername());
        // Agregar más verificaciones según las propiedades del usuario
        // Por ejemplo, puedes verificar el nombre, apellido, número de teléfono, etc.
        assertEquals("Marcial", ((User) userDetails).getFirstName());
        assertEquals("Diaz", ((User) userDetails).getLastName());
        assertEquals("978030199", ((User) userDetails).getPhoneNumber());
    }

    */
}
