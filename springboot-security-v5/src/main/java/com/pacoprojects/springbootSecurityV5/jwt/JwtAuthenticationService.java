package com.pacoprojects.springbootSecurityV5.jwt;

import com.google.common.base.Strings;
import com.pacoprojects.springbootSecurityV5.auth.ApplicationUser;
import com.pacoprojects.springbootSecurityV5.auth.FakeApplicationUserDAORepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;


@Service
public class JwtAuthenticationService {

    private final FakeApplicationUserDAORepository fakeApplicationUserDAORepository;
    private final JwtConfig jwtConfig;

    public JwtAuthenticationService(FakeApplicationUserDAORepository fakeApplicationUserDAORepository, JwtConfig jwtConfig) {
        this.fakeApplicationUserDAORepository = fakeApplicationUserDAORepository;
        this.jwtConfig = jwtConfig;
    }

    /* Metodo responsavel por criar Token*/
    public String generateJwt(ApplicationUser applicationUser) {

        String basicToken = Jwts.builder()
                /* Nome do usuario que esta emitindo o Token */
                .setSubject(applicationUser.getUsername())
                /* Autorizacoes do usuario */
                .claim("authorities", applicationUser.getAuthorities())
                /* Quando Token foi gerado... */
                .setIssuedAt(Date.from(Instant.now()))
                /* Por quanto tempo o Token eh valido - 2 semanas */
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
//                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                /* Senha utilizada para criptografar o Token em Bytes + Nome do Algoritmo */
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()), SignatureAlgorithm.HS512)
//                .signWith(Keys.hmacShaKeyFor("SenhaExtremamenteSecretaSenhaExtremamenteSecretaSenhaExtremamenteSecreta".getBytes()), SignatureAlgorithm.HS512)
                .compact();

        return basicToken;
    }

    /* Metodo responsavel por Apos criar Token | Salvar token no Banco | Setar Authorization no HEADER */
    public void setAuthentication(ApplicationUser applicationUser, HttpServletResponse response) {

        String basicToken = generateJwt(applicationUser);

        if (basicToken != null) {

            String token = jwtConfig.getTokenPrefix().concat(basicToken);
//            String token = "Bearer ".concat(basicToken);

            /*Salvar no banco jwt gerado para o usuario*/

            /* Adicionando Token no Header */
            response.addHeader(HttpHeaders.AUTHORIZATION, token);
        }
    }


    public void getAuthentication(HttpServletRequest request) {

        Map<String, Object> map = breakToken(request);

        if (!map.isEmpty()) {

            String username = map.get("username").toString();
            String basicToken = map.get("basicToken").toString();
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) map.get("authorities");

            Optional<ApplicationUser> userOptional = fakeApplicationUserDAORepository.selectApplicationUserByUsername(username);

            /* Verificar se: userOptional.isPresent() && userOptional.get().getJwt().equals(basicToken)*/
            if (userOptional.isPresent()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AuthorizationServiceException("User has no authorization to access this page");
            }
        }
    }

    public Map<String, Object> breakToken(HttpServletRequest request) {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Map<String, Object> map = new HashMap<>();

        if (!Strings.isNullOrEmpty(token)) {

            String basicToken = token.replace(jwtConfig.getTokenPrefix(), "");
//            String basicToken = token.replace("Bearer ", "");

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
//                    .setSigningKey(Keys.hmacShaKeyFor("SenhaExtremamenteSecretaSenhaExtremamenteSecretaSenhaExtremamenteSecreta".getBytes()))
                    .parseClaimsJws(basicToken);
//                    .getBody();

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> genericAuthoritiesSet = (List<Map<String, String>>) body.get("authorities");

            /* 1) Coletando lista de SETs authority */
            Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
            genericAuthoritiesSet.forEach(object -> authorities.add(new SimpleGrantedAuthority(object.get("authority"))));
            /* 2) Coletando lista de SETs authority */
//            Set<SimpleGrantedAuthority> authorities = genericAuthoritiesSet.stream()
//                    .map(mapElement -> new SimpleGrantedAuthority(mapElement.get("authority"))).collect(Collectors.toSet());

            map.put("basicToken", basicToken);
            map.put("username", username);
            map.put("authorities", authorities);
        }
        return map;
    }
}
