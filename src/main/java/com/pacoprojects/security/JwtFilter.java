package com.pacoprojects.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtAuthenticationService jwtAuthenticationService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            jwtAuthenticationService.getAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            Map<String, String> map = new HashMap<>();
            if (exception instanceof AuthorizationServiceException) {
                map.put("error", exception.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else if (exception instanceof ExpiredJwtException) {
                map.put("error", "Sua sessão expirou, tente logar novamente");
                response.setStatus(HttpStatus.FORBIDDEN.value());
            } else if (exception instanceof MalformedJwtException) {
                map.put("error", "Token inválido, contate um Admin.");
                response.setStatus(HttpStatus.FORBIDDEN.value());
            } else {
                map.put("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
            new ObjectMapper().writeValue(response.getOutputStream(),map);
        }
    }
}
