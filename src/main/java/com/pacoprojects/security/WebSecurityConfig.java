package com.pacoprojects.security;

import com.pacoprojects.security.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final ApplicationConfig applicationConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /* REST - Desabilitando CSRF*/
                .csrf().disable()
                /* Configurando Session Spring para Stateless */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /* Adicionando Filtro de getAuthentication e Autorizacoes ANTES do Spring verificar Usuario Logado*/
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                /* Urls liberadas*/
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                /* Qualquer outra Url bloqueada*/
                .anyRequest()
                .authenticated()
                .and()
                /* Fornecedor de Autenticacao*/
                .authenticationProvider(applicationConfig.authenticationProvider());

        return http.build();
    }
}
