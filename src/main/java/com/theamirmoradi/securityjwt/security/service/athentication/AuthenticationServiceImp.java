package com.theamirmoradi.securityjwt.security.service.athentication;

import com.theamirmoradi.securityjwt.security.DTO.AuthenticationRequest;
import com.theamirmoradi.securityjwt.security.DTO.AuthenticationResponse;
import com.theamirmoradi.securityjwt.security.service.JWT.JwtService;
import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.Exception.UsernameDuplicateException;
import com.theamirmoradi.securityjwt.user.entity.User;
import com.theamirmoradi.securityjwt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService{

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.findByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        return generateAuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse signup(UserRegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);
        return generateAuthenticationResponse(token);
    }

    private AuthenticationResponse generateAuthenticationResponse(String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .expiration(jwtService.extractExpiration(token))
                .build();
    }
}
