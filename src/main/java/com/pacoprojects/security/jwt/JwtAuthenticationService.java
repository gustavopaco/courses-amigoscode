package com.pacoprojects.security.jwt;

import com.pacoprojects.model.Pessoa;
import com.pacoprojects.repository.PessoaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtAuthenticationService {

    private final JwtUtilService jwtUtilService;
    private final PessoaRepository pessoaRepository;

    public void getAuthentication(HttpServletRequest request) {

        Map<String, Object> objectMap = jwtUtilService.breakToken(request);

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
}
