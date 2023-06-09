package com.theamirmoradi.securityjwt.user;

import com.theamirmoradi.securityjwt.user.DTO.UserRegisterRequest;
import com.theamirmoradi.securityjwt.user.Exception.*;
import com.theamirmoradi.securityjwt.user.constants.ExceptionMessages;
import com.theamirmoradi.securityjwt.user.entity.User;
import com.theamirmoradi.securityjwt.user.repository.UserRepository;
import com.theamirmoradi.securityjwt.user.service.UserService;
import com.theamirmoradi.securityjwt.user.service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    private UserService service;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        service = new UserServiceImp(repository, passwordEncoder);
    }

    //Register tests
    @Test
    void register() {
        UserRegisterRequest requestUser = UserRegisterRequest.builder()
                .firstName("amir")
                .lastName("moradi")
                .username("amirmoradi")
                .password("12345")
                .email("theamirmoradi@gmail.com")
                .phoneNumber("0910")
                .build();

        service.register(requestUser);
        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .phoneNumber(requestUser.getPhoneNumber())
                .username(requestUser.getUsername())
                .enable(true)
                .build();

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();
        assertThat(userCaptor.getFirstName()).isEqualTo(requestUser.getFirstName());
        assertThat(userCaptor.getLastName()).isEqualTo(requestUser.getLastName());
        assertThat(userCaptor.getEmail()).isEqualTo(requestUser.getEmail());
        assertThat(userCaptor.getUsername()).isEqualTo(requestUser.getUsername());
        assertThat(userCaptor.getPhoneNumber()).isEqualTo(requestUser.getPhoneNumber());
    }

    @Test
    void RegisterUsernameDuplicateException() {
        UserRegisterRequest requestUser = UserRegisterRequest.builder()
                .firstName("amir")
                .lastName("moradi")
                .username("amirmoradi")
                .password("12345")
                .email("theamirmoradi@gmail.com")
                .phoneNumber("0910")
                .build();

        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .phoneNumber(requestUser.getPhoneNumber())
                .username(requestUser.getUsername())
                .enable(true)
                .build();

        given(repository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        assertThatThrownBy(() -> service.register(requestUser))
                .isInstanceOf(UsernameDuplicateException.class)
                .hasMessageContaining(ExceptionMessages.DUPLICATE_EXCEPTION_USERNAME);
    }

    @Test
    void RegisterEmailDuplicateException() {
        UserRegisterRequest requestUser = UserRegisterRequest.builder()
                .firstName("amir")
                .lastName("moradi")
                .username("amirmoradi")
                .password("12345")
                .email("theamirmoradi@gmail.com")
                .phoneNumber("0910")
                .build();

        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .email(requestUser.getEmail())
                .password(passwordEncoder.encode(requestUser.getPassword()))
                .phoneNumber(requestUser.getPhoneNumber())
                .username(requestUser.getUsername())
                .enable(true)
                .build();

        given(repository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        assertThatThrownBy(() -> service.register(requestUser))
                .isInstanceOf(EmailDuplicateException.class)
                .hasMessageContaining(ExceptionMessages.DUPLICATE_EXCEPTION_EMAIL);
    }

    //Find by ID tests

    @Test
    void findById() {
        long id = 1L;
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();
        given(repository.findById(id)).willReturn(Optional.of(user));
        assertThat(service.findById(id))
                .isEqualTo(user);
    }

    @Test
    void findByIdNotFoundException() {
        long id = 1L;
        given(repository.findById(id))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_USER);
    }

    //Find by username tests
    @Test
    void findByUsername() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        assertThat(service.findByUsername("amirmoradi"))
                .isEqualTo(user);
    }

    @Test
    void findByUsernameAndEnable() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByUsernameAndEnable(user.getUsername(), user.isEnable())).willReturn(Optional.of(user));

        assertThat(service.findByUsernameAndEnable("amirmoradi", true))
                .isEqualTo(user);
    }

    @Test
    void findByUsernameAndEnableNotFoundException() {
        given(repository.findByUsernameAndEnable("no-one", true)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUsernameAndEnable("no-one", true))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_USERNAME);
    }

    @Test
    void findByUsernameNotFoundException() {
        given(repository.findByUsername("no-one")).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUsername("no-one"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_USERNAME);
    }
    //End of find by username tests

    //Find by Email tests
    @Test
    void findByEmail() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThat(service.findByEmail("theamirmoradi@gmail.com"))
                .isEqualTo(user);
    }

    @Test
    void findByEmailAndEnable() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByEmailAndEnable(user.getEmail(), true)).willReturn(Optional.of(user));

        assertThat(service.findByEmailAndEnable("theamirmoradi@gmail.com", true))
                .isEqualTo(user);
    }

    @Test
    void findByEmailNotFoundException() {
        given(repository.findByEmail("no-one@gmail.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByEmail("no-one@gmail.com"))
                .isInstanceOf(EmailNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_EMAIL);
    }

    @Test
    void findByEmailAndEnableNotFoundException() {
        given(repository.findByEmailAndEnable("no-one@gmail.com", true)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByEmailAndEnable("no-one@gmail.com", true))
                .isInstanceOf(EmailNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_EMAIL);
    }
    //End of find by Email tests

    //Find by Phone number tests
    @Test
    void findByPhoneNumber() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByPhoneNumber(user.getPhoneNumber())).willReturn(Optional.of(user));

        assertThat(service.findByPhoneNumber("0910"))
                .isEqualTo(user);
    }

    @Test
    void findByPhoneNumberAndEnable() {
        User user = User.builder()
                .firstName("amir")
                .lastName("moradi")
                .email("theamirmoradi@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .phoneNumber("0910")
                .username("amirmoradi")
                .enable(true)
                .build();

        given(repository.findByPhoneNumberAndEnable(user.getPhoneNumber(), user.isEnable())).willReturn(Optional.of(user));

        assertThat(service.findByPhoneNumberAndEnable("0910", true))
                .isEqualTo(user);
    }

    @Test
    void findByPhoneNumberNotFoundException() {
        given(repository.findByPhoneNumber("0910")).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByPhoneNumber("0910"))
                .isInstanceOf(PhoneNumberNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_PHONE_NUMBER);
    }

    @Test
    void findByPhoneNumberAndEnableNotFoundException() {
        given(repository.findByPhoneNumberAndEnable("0910", true)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByPhoneNumberAndEnable("0910", true))
                .isInstanceOf(PhoneNumberNotFoundException.class)
                .hasMessageContaining(ExceptionMessages.NOT_FOUND_EXCEPTION_PHONE_NUMBER);
    }
    //End of find by phone number tests
}
