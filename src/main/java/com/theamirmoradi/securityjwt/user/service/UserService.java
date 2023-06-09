package com.theamirmoradi.securityjwt.user.service;

import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService{

    User findById(long id);

    User register(UserRegisterRequest request);

    User findByUsername(String username);

    User findByUsernameAndEnable(String username, boolean enable);

    User findByEmail(String email);

    User findByEmailAndEnable(String email, boolean enable);

    User findByPhoneNumber(String phoneNumber);

    User findByPhoneNumberAndEnable(String phoneNumber, boolean enable);
}
