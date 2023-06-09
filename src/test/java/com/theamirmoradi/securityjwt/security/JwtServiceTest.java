package com.theamirmoradi.securityjwt.security;

import com.theamirmoradi.securityjwt.security.configuration.ApplicationPropertiesHandler;
import com.theamirmoradi.securityjwt.security.service.JWT.JwtService;
import com.theamirmoradi.securityjwt.security.service.JWT.JwtServiceImp;
import com.theamirmoradi.securityjwt.user.constants.Role;
import com.theamirmoradi.securityjwt.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class JwtServiceTest {

    private String TOKEN;
    private String SECRET_KEY = "7A25432A462D4A614E645267556B58703273357638792F413F4428472B4B6250";
    private Long EXPIRATION = 3000000L;
    private User user;

    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationPropertiesHandler propertiesHandler;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        when(propertiesHandler.getSecretKey()).thenReturn(SECRET_KEY);
        when(propertiesHandler.getExpiration()).thenReturn(EXPIRATION);

        user = User.builder()
                .id(1)
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .role(Role.USER)
                .enable(true)
                .build();

        jwtService = new JwtServiceImp(propertiesHandler);
        TOKEN = jwtService.generateToken(user);
    }

    @Test
    void extractUsername() {
        assertThat(jwtService.extractUsername(TOKEN)).isNotEmpty();
        assertThat(jwtService.extractUsername(TOKEN)).isEqualTo(user.getUsername());
    }

    @Test
    void extractEmail() {
        assertThat(jwtService.extractEmail(TOKEN)).isNotEmpty();
        assertThat(jwtService.extractEmail(TOKEN)).isEqualTo(user.getEmail());
    }

    @Test
    void extractId() {
        assertThat(jwtService.extractId(TOKEN)).isEqualTo(user.getId());
    }

    @Test
    void extractRole() {
        assertThat(jwtService.extractRole(TOKEN)).isEqualTo(user.getRole());
    }

    @Test
    void extractFirstName() {
        assertThat(jwtService.extractFirstName(TOKEN)).isEqualTo(user.getFirstName());
    }

    @Test
    void extractLastName() {
        assertThat(jwtService.extractLastName(TOKEN)).isEqualTo(user.getLastName());
    }

    @Test
    void extractPhoneNumber() {
        assertThat(jwtService.extractPhoneNumber(TOKEN)).isEqualTo(user.getPhoneNumber());
    }

    @Test
    void extractExpiration() {
        assertThat(jwtService.extractExpiration(TOKEN)).isBetween(new Date(System.currentTimeMillis() - 1000), new Date(System.currentTimeMillis() + EXPIRATION));
    }

    @Test
    void isTokenExpired() {
        assertThat(jwtService.isTokenExpired(TOKEN)).isFalse();
    }

    @Test
    void isTokenValid() {
        assertThat(jwtService.isTokenValid(TOKEN, user)).isTrue();
    }

    @Test
    void generateToken() {
        assertThat(jwtService.generateToken(user)).isNotEmpty();
    }

}
