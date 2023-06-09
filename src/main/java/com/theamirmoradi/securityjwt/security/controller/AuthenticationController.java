package com.theamirmoradi.securityjwt.security.controller;

import com.theamirmoradi.securityjwt.security.DTO.AuthenticationRequest;
import com.theamirmoradi.securityjwt.security.DTO.AuthenticationResponse;
import com.theamirmoradi.securityjwt.security.constants.SecurityUrlMapping;
import com.theamirmoradi.securityjwt.security.service.athentication.AuthenticationService;
import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.Exception.UsernameDuplicateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SecurityUrlMapping.WEBSERVICE_BASE_URL)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(SecurityUrlMapping.API_LOGIN)
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping(SecurityUrlMapping.API_SIGNUP)
    public ResponseEntity<AuthenticationResponse> signup(@Valid @RequestBody UserRegisterRequest request) throws UsernameDuplicateException{
        return ResponseEntity.ok(authenticationService.signup(request));
    }

}
