package com.pacoprojects.webmvnsbamigoscodespringsecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtAuthenticationService jwtAuthenticationService;

    public JwtTokenVerifier(JwtAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            jwtAuthenticationService.getAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            Map<String, String> map = new HashMap<>();
            if (exception instanceof AuthorizationServiceException) {
                map.put("error", exception.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else if (exception instanceof ExpiredJwtException) {
                map.put("error", "Session invalid, please try to login again");
                response.setStatus(HttpStatus.FORBIDDEN.value());
            } else if (exception instanceof MalformedJwtException) {
                map.put("error", "Invalid Token, contact the admin");
                response.setStatus(HttpStatus.FORBIDDEN.value());
            } else {
                map.put("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
            new ObjectMapper().writeValue(response.getOutputStream(),map);
        }

    }
}
