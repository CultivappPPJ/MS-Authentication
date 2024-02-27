import com.cultivapp.authentication.config.AppConfig;
import com.cultivapp.authentication.entity.User;
import com.cultivapp.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppConfigTest {

    @Test
    void userDetailsServiceTest() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new AppConfig(userRepository).userDetailsService();

        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn("test@gmail.com");
        when(mockUser.getPassword()).thenReturn("password");

        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@gmail.com");

        assertEquals(mockUser.getEmail(), userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void userDetailsServiceUsernameNotFoundExceptionTest() {

        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new AppConfig(userRepository).userDetailsService();
        when(userRepository.findUserByEmail("nonexistent@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistent@gmail.com"));
    }

    @Test
    void authenticationProviderTest() {

        UserRepository userRepository = mock(UserRepository.class);
        AppConfig appConfig = new AppConfig(userRepository);
        AuthenticationProvider authenticationProvider = appConfig.authenticationProvider();
        assertEquals(DaoAuthenticationProvider.class, authenticationProvider.getClass());
    }

    @Test
    void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = new AppConfig(mock(UserRepository.class)).passwordEncoder();

        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void authenticationManagerTest() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);

        when(config.getAuthenticationManager()).thenAnswer(invocation -> {
            AppConfig appConfig = new AppConfig(userRepository);
            return appConfig.authenticationManager(config);
        });

        AuthenticationManager authenticationManager = config.getAuthenticationManager();

        assertNotNull(authenticationManager);
    }
}
