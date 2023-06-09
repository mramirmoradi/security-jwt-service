package com.theamirmoradi.securityjwt.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class ApplicationPropertiesHandler {

    private final Environment environment;

    public String getSecretKey() {
        return environment.getProperty("security.jwt.secret-key");
    }

    public Long getExpiration() {
        return Long.valueOf(environment.getProperty("security.jwt.expiration"));
    }
}
