package com.pacoprojects.auth;

import com.pacoprojects.mapper.PessoaMapper;
import com.pacoprojects.model.Pessoa;
import com.pacoprojects.model.Role;
import com.pacoprojects.repository.PessoaRepository;
import com.pacoprojects.security.ApplicationConfig;
import com.pacoprojects.security.jwt.JwtConfig;
import com.pacoprojects.security.jwt.JwtUtilService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PessoaRepository pessoaRepository;
    private final ApplicationConfig applicationConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtUtilService jwtUtilService;
    private final PessoaMapper pessoaMapper;

    public ResponseEntity<AuthenticationResponse> register(RegisterPessoaDto request, HttpServletResponse response) {

        Pessoa entity = pessoaMapper.toEntity(request);
        entity.setPassword(applicationConfig.passwordEncoder().encode(entity.getPassword()));
        entity.setRole(Role.PESSOA_FISICA);

        return getAuthenticationResponseResponseEntity(entity, response);
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationPessoaDto request, HttpServletResponse response) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));

            Pessoa pessoa = (Pessoa) authenticate.getPrincipal();

            return getAuthenticationResponseResponseEntity(pessoa, response);
        } catch (Exception exception) {
            if (exception instanceof BadCredentialsException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login ou Senha incorretos");
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage());
        }
    }

    private ResponseEntity<AuthenticationResponse> getAuthenticationResponseResponseEntity(Pessoa pessoa, HttpServletResponse response) {
        var basicToken = jwtUtilService.generateToken(pessoa);
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
