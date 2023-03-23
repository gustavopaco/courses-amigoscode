package com.pacoprojects.security.jwt;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class JwtUtilService {

    private final JwtConfig jwtConfig;

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
        final Claims claims = extractAllClaims(basicToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String basicToken) {
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
