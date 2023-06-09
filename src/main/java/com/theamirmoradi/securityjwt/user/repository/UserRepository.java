package com.theamirmoradi.securityjwt.user.repository;

import com.theamirmoradi.securityjwt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndEnable(String username, boolean enable);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndEnable(String email, boolean enable);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndEnable(String phoneNumber, boolean enable);


}
