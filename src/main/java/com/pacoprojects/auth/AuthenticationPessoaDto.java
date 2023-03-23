package com.pacoprojects.auth;

import java.io.Serializable;

/**
 * A DTO for the {@link com.pacoprojects.model.Pessoa} entity
 */
public record AuthenticationPessoaDto(String username, String password) implements Serializable {
}
