package com.theamirmoradi.securityjwt.user.Exception;

import com.theamirmoradi.securityjwt.user.constants.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UsernameDuplicateException extends RuntimeException{

    public UsernameDuplicateException() {
        super(ExceptionMessages.DUPLICATE_EXCEPTION_USERNAME);
    }
}
