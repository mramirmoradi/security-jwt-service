package com.theamirmoradi.securityjwt.user.Exception;

import com.theamirmoradi.securityjwt.user.constants.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhoneNumberNotFoundException extends RuntimeException{
    public PhoneNumberNotFoundException() {
        super(ExceptionMessages.NOT_FOUND_EXCEPTION_PHONE_NUMBER);
    }
}
