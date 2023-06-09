package com.theamirmoradi.securityjwt.security.configuration;

import com.theamirmoradi.securityjwt.security.constants.SecurityConstant;
import com.theamirmoradi.securityjwt.security.service.JWT.JwtService;
import com.theamirmoradi.securityjwt.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authenticationHeader = request.getHeader(SecurityConstant.AUTHORIZATION);
        final String token;
        final String username;

        if (authenticationValidation(authenticationHeader)) {
            filterChain.doFilter(request, response);

        } else {
            token = authenticationHeader.substring(7);
            username = jwtService.extractUsername(token);

            if (!isUserAuthenticated()) {
                authenticateUser(username, token, request);
            }
        }
    }

    private void authenticateUser(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            setAuthenticationDetails(authenticationToken, request);

            updateSecurityContextHolder(authenticationToken);
        }
    }

    private boolean authenticationValidation(String authenticationHeader) {
        return (authenticationHeader == null || !authenticationHeader.startsWith(SecurityConstant.BEARER));
    }

    private boolean isUserAuthenticated() {
        return (SecurityContextHolder.getContext().getAuthentication() != null);
    }

    private void setAuthenticationDetails(UsernamePasswordAuthenticationToken authenticationToken, HttpServletRequest request) {
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    }

    private void updateSecurityContextHolder(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
