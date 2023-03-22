package com.pacoprojects.security;

import com.pacoprojects.model.Pessoa;
import com.pacoprojects.repository.PessoaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PessoaDetailsService implements UserDetailsService {

    private final PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findPessoaByUsername(username);

        if (pessoaOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario não foi encontrado");
        }
        return pessoaOptional.get();
    }
}
