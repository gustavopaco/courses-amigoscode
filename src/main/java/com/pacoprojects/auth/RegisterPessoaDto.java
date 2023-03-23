package com.pacoprojects.auth;

import com.pacoprojects.model.Role;

import java.io.Serializable;

/**
 * A DTO for the {@link com.pacoprojects.model.Pessoa} entity
 */
public record RegisterPessoaDto(String nome, String sobrenome, String username, String password, Role role) implements Serializable {
}
