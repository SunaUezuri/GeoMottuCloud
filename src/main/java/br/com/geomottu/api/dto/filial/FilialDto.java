package br.com.geomottu.api.dto.filial;

import br.com.geomottu.api.dto.endereco.EnderecoDto;
import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.model.enums.PaisesFilial;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record FilialDto(
        @NotBlank(message = "O nome não pode estar vazio")
        String nome,
        @NotNull(message = "O campo pais é obrigatório")
        PaisesFilial pais,
        @NotNull(message = "O endereço é obrigatório") @Valid
        EnderecoDto endereco,
        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres")
        String telefone,
        @Email(message = "O formato de email está incorreto")
        @Size(max = 30, message = "O máximo de caracteres do email deve ser 30")
        String email

) {
}