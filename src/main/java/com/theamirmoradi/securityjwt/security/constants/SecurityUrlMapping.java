package com.theamirmoradi.securityjwt.security.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SecurityUrlMapping {

    public static final String API_VERSION = "/v1";
    public static final String API_BASE = "/auth";

    public static final String API_LOGIN = "/login";
    public static final String API_SIGNUP = "/signup";

    public static final String WEBSERVICE_BASE_URL = API_VERSION + API_BASE;
}
