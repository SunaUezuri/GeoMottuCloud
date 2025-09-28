package br.com.geomottu.api.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UpdateRoleDto(
        @NotNull(message = "O tipo de perfil não pode ser nulo.")
        Integer tipoPerfil
) {
}
