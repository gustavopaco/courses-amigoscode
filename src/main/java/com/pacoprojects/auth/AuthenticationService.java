package com.pacoprojects.auth;

import com.pacoprojects.model.Pessoa;
import com.pacoprojects.model.Role;
import com.pacoprojects.repository.PessoaRepository;
import com.pacoprojects.security.BCryptUtil;
import com.pacoprojects.security.JwtAuthenticationService;
import com.pacoprojects.security.JwtConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PessoaRepository pessoaRepository;
    private final BCryptUtil passwordEncode;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request, HttpServletResponse response) {

        var pessoa = Pessoa.builder()
                .nome(request.getNome())
                .sobrenome(request.getSobrenome())
                .username(request.getUsername())
                .password(passwordEncode.getPasswordEncoder().encode(request.getPassword()))
                .role(Role.PESSOA_FISICA)
                .build();

        return getAuthenticationResponseResponseEntity(pessoa, response);
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var pessoa = pessoaRepository
                .findPessoaByUsername(request.getUsername())
                .orElseThrow();

        return getAuthenticationResponseResponseEntity(pessoa, response);
    }

    private ResponseEntity<AuthenticationResponse> getAuthenticationResponseResponseEntity(Pessoa pessoa, HttpServletResponse response) {
        var basicToken = jwtAuthenticationService.generateToken(pessoa);
        pessoa.setJwtBasicToken(basicToken);

        pessoaRepository.save(pessoa);

        String fullToken = jwtConfig.getTokenPrefix().concat(basicToken);
        response.addHeader(HttpHeaders.AUTHORIZATION, fullToken);

        return ResponseEntity
                .ok(AuthenticationResponse
                        .builder()
                        .token(fullToken)
                        .build());
    }
}
