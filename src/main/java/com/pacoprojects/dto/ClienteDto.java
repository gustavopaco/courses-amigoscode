package com.pacoprojects.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.pacoprojects.model.Cliente} entity
 */
public record ClienteDto(Integer id, String nome, String email, Integer idade) implements Serializable {
}
