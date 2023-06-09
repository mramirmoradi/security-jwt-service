package com.theamirmoradi.securityjwt.security.service.JWT;

import com.theamirmoradi.securityjwt.security.configuration.ApplicationPropertiesHandler;
import com.theamirmoradi.securityjwt.user.constants.Role;
import com.theamirmoradi.securityjwt.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImp implements JwtService{

    private final ApplicationPropertiesHandler propertiesHandler;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public long extractId(String token) {
        return Long.parseLong(extractAllClaims(token).get("id").toString());
    }

    @Override
    public Role extractRole(String token) {
        return Role.valueOf(extractAllClaims(token).get("role").toString());
    }

    @Override
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email").toString();
    }

    @Override
    public String extractFirstName(String token) {
        return extractAllClaims(token).get("firstName").toString();
    }

    @Override
    public String extractLastName(String token) {
        return extractAllClaims(token).get("lastName").toString();
    }

    @Override
    public String extractPhoneNumber(String token) {
        return extractAllClaims(token).get("phoneNumber").toString();
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, User user) {
        extraClaims.putAll(getClaims(user));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationGenerator())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateToken(User user) {

        return Jwts.builder()
                .setClaims(getClaims(user))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationGenerator())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token))) && (!isTokenExpired(token)) ;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] bytesKey = Decoders.BASE64.decode(propertiesHandler.getSecretKey());
        return Keys.hmacShaKeyFor(bytesKey);
    }

    private Date expirationGenerator() {
        return new Date(System.currentTimeMillis() + propertiesHandler.getExpiration());
    }

    private Map<String, Object> getClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("phoneNumber", user.getPhoneNumber());
        extraClaims.put("id", String.valueOf(user.getId()));
        extraClaims.put("role", user.getRole());

        return extraClaims;
    }
}
