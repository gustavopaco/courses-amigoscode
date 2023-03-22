package com.pacoprojects.security;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.pacoprojects.model.Pessoa;
import com.pacoprojects.repository.PessoaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtAuthenticationService {

    private final JwtConfig jwtConfig;
    private final PessoaRepository pessoaRepository;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
//                .setExpiration(Date.from(Instant.now())) // Para debugg de Invalid Token
                .setExpiration(Date.from(Instant.now().plus(Period.ofDays(jwtConfig.getTokenExpirationAfterDays()))))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void getAuthentication(HttpServletRequest request) {

        Map<String, Object> objectMap = breakToken(request);

        if (!objectMap.isEmpty()) {
            String username = objectMap.get("username").toString();
            String basicToken = objectMap.get("basicToken").toString();

            Optional<Pessoa> pessoaOptional = pessoaRepository.findPessoaByUsername(username);

            if (pessoaOptional.isPresent() && pessoaOptional.get().getJwtBasicToken().equals(basicToken)) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,pessoaOptional.get().getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AuthorizationServiceException("Usuário não tem permissão para acessar essa página.");
            }
        }
    }

    public Map<String, Object> breakToken(HttpServletRequest request) {

        Map<String,Object> objectMap = new HashMap<>();
        String fullToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!Strings.isNullOrEmpty(fullToken)) {

            String basicToken = fullToken.replace(jwtConfig.getTokenPrefix(), "");

            if (!isTokenExpired(basicToken)) {
                String username = extractUsernameClaim(basicToken);

                objectMap.put("basicToken", basicToken);
                objectMap.put("username", username);
                return objectMap;
            }
        }

        return objectMap;
    }

    private boolean isTokenExpired(String basicToken) {
        Date dateExpiredTime = extractExpirationClaim(basicToken);
        return new Date().after(dateExpiredTime);
    }

    public Date extractExpirationClaim(String basicToken) {
        return extractClaim(basicToken, Claims::getExpiration);
    }

    public String extractUsernameClaim(String basicToken) {
        return extractClaim(basicToken, Claims::getSubject);
    }

    /*Note: Metodo que extrai um claim especifico*/
    public <T> T extractClaim(String basicToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(basicToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String basicToken) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(basicToken)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}
