package com.theamirmoradi.securityjwt.user.constants;

public interface ExceptionMessages {

    String DUPLICATE_EXCEPTION_USERNAME = "The username is duplicated, try another username.";
    String DUPLICATE_EXCEPTION_EMAIL = "The email is duplicated, insert correct email.";

    String NOT_FOUND_EXCEPTION_USER = "user not found.";
    String NOT_FOUND_EXCEPTION_EMAIL = "not found user with this email.";
    String NOT_FOUND_EXCEPTION_USERNAME = "not found user with this username.";
    String NOT_FOUND_EXCEPTION_PHONE_NUMBER = "not found user with this phone number.";
}
