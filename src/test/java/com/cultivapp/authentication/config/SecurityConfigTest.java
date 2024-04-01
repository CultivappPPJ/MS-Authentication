package com.cultivapp.authentication.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndPoints_ConfigurationIsCorrect() throws Exception {
        SecurityConfig securityConfig = new SecurityConfig(null, null);

        Method method = SecurityConfig.class.getDeclaredMethod("publicEndPoints");
        method.setAccessible(true);

        RequestMatcher publicEndPoints = (RequestMatcher) method.invoke(securityConfig);

        MockHttpServletRequest publicRequest = new MockHttpServletRequest("GET", "/api/v01/auth/public");

        MockHttpServletRequest protectedRequest = new MockHttpServletRequest("GET", "/api/v01/some/protected/endpoint");

        assertTrue(publicEndPoints.matches(publicRequest));
        assertFalse(publicEndPoints.matches(protectedRequest));
    }

    @Test
    void protectedEndPoints_RequireAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v01/some/protected/endpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void securityFilterChain_ConfigurationIsCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/any/endpoint"))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().string("WWW-Authenticate", "Bearer realm=\"realm\""));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/public"))
                .andExpect(status().isOk());
    }
}
