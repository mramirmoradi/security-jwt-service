package com.theamirmoradi.securityjwt.user.service;

import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.Exception.*;
import com.theamirmoradi.securityjwt.user.constants.Role;
import com.theamirmoradi.securityjwt.user.entity.User;
import com.theamirmoradi.securityjwt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(long id) {
        return repository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public User register(UserRegisterRequest request) {
        // todo username, email, phone number validation check
        usernameValidation(request.getUsername());
        emailValidation(request.getEmail());
        return repository.save(
                User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .enable(true)
                        .build());
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);
    }

    @Override
    public User findByUsernameAndEnable(String username, boolean enable) {
        return repository.findByUsernameAndEnable(username, enable)
                .orElseThrow(UsernameNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }

    @Override
    public User findByEmailAndEnable(String email, boolean enable) {
        return repository.findByEmailAndEnable(email, enable)
                .orElseThrow(EmailNotFoundException::new);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(PhoneNumberNotFoundException::new);
    }

    @Override
    public User findByPhoneNumberAndEnable(String phoneNumber, boolean enable) {
        return repository.findByPhoneNumberAndEnable(phoneNumber, enable)
                .orElseThrow(PhoneNumberNotFoundException::new);
    }

    private void usernameValidation(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent())
            throw new UsernameDuplicateException();
    }

    private void emailValidation(String email) {
        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent())
            throw new EmailDuplicateException();

    }
}
