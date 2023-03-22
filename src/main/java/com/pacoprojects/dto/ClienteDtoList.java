package com.pacoprojects.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.pacoprojects.model.Cliente} entity
 */
public record ClienteDtoList(Integer id, String nome, Integer idade) implements Serializable {
}
