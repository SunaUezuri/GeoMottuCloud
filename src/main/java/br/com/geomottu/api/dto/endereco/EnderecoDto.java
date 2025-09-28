package br.com.geomottu.api.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDto(
        @NotBlank(message = "O estado não pode estar vazio")
        String estado,
        @NotBlank(message = "A sigla do estado não pode estar vazia")
        @Size(max = 2, message = "A sigla não pode ter mais de 2 caracteres")
        String siglaEstado,
        @NotBlank(message = "A cidade não pode estar vazia")
        String cidade,
        @NotBlank(message = "A rua não pode estar vazia")
        String rua
) {
}
