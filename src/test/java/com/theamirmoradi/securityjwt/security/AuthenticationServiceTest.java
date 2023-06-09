package com.theamirmoradi.securityjwt.security;

import com.theamirmoradi.securityjwt.security.DTO.AuthenticationRequest;
import com.theamirmoradi.securityjwt.security.DTO.AuthenticationResponse;
import com.theamirmoradi.securityjwt.security.service.JWT.JwtService;
import com.theamirmoradi.securityjwt.security.service.athentication.AuthenticationService;
import com.theamirmoradi.securityjwt.security.service.athentication.AuthenticationServiceImp;
import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.constants.Role;
import com.theamirmoradi.securityjwt.user.entity.User;
import com.theamirmoradi.securityjwt.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationServiceTest {

    private User user;
    private String SAMPLE_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJBbWlyIiwibGFzdE5hbWUiOiJNb3JhZGkiLCJwaG9uZU51bWJlciI6IjEyMzIxMjQiLCJyb2xlIjoiVVNFUiIsImlkIjoiMSIsImVtYWlsIjoidGhlYW1pcm1vcmFkaUBnbWFpbC5jb20iLCJzdWIiOiJhbWlybW9yYWRpIiwiaWF0IjoxNjg2MjM2ODIzLCJleHAiOjE2ODYyMzk4MjN9.DTeGxujBtBFmN7VoBC05vX8Px6eobbRbZPOLcshPdjw";
    AuthenticationResponse response;

    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();

        response = AuthenticationResponse.builder()
                .token(SAMPLE_TOKEN)
                .expiration(null)
                .build();

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
        authenticationService = new AuthenticationServiceImp(userService, authenticationManager, jwtService);
    }

    @Test
    void login() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(user.getUsername())
                .password("12345")
                .build();


        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(SAMPLE_TOKEN);

        assertThat(authenticationService.login(request))
                .isEqualTo(response);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
    }

    @Test
    void signup() {
        UserRegisterRequest requestUser = UserRegisterRequest.builder()
                .firstName("amir")
                .lastName("moradi")
                .username("amirmoradi")
                .password("12345")
                .email("theamirmoradi@gmail.com")
                .phoneNumber("0910")
                .build();
        when(userService.register(requestUser)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(SAMPLE_TOKEN);

        assertThat(authenticationService.signup(requestUser)).isEqualTo(response);
    }
}
