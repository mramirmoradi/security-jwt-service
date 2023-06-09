package com.theamirmoradi.securityjwt.security.service.athentication;

import com.theamirmoradi.securityjwt.security.DTO.AuthenticationRequest;
import com.theamirmoradi.securityjwt.security.DTO.AuthenticationResponse;
import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.Exception.UsernameDuplicateException;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse signup(UserRegisterRequest request) throws UsernameDuplicateException;
}
