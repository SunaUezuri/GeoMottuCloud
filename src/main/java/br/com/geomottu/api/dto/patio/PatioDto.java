package br.com.geomottu.api.dto.patio;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatioDto(
        @Size(max = 50, message = "O nome só pode ter no máximo 50 caracteres")
        String nome,
        @NotNull(message = "A capacidade não pode ser nula")
        Integer capacidadeTotal,
        Long filialId
) {
}
