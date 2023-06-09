package com.theamirmoradi.securityjwt.security.service.JWT;


import com.theamirmoradi.securityjwt.user.constants.Role;
import com.theamirmoradi.securityjwt.user.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);
    long extractId(String token);
    Role extractRole(String token);
    String extractEmail(String token);
    String extractFirstName(String token);
    String extractLastName(String token);
    String extractPhoneNumber(String token);

    Date extractExpiration(String token);

    String generateToken(Map<String, Object> extraClaims, User user);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

}
