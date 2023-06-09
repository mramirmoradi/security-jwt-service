package com.theamirmoradi.securityjwt.user.Exception;

import com.theamirmoradi.securityjwt.user.constants.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super(ExceptionMessages.NOT_FOUND_EXCEPTION_USERNAME);
    }
}
