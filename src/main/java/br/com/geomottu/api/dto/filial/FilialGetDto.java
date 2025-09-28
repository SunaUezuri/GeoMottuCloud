package br.com.geomottu.api.dto.filial;

import br.com.geomottu.api.model.entities.Endereco;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.enums.PaisesFilial;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.model.entities.Patio;

import java.util.List;

public record FilialGetDto(
        Long id,
        String nome,
        PaisesFilial pais,
        Endereco endereco,
        String telefone,
        String email,
        List<String> usuarios,
        List<String> patios
) {
    public FilialGetDto(Filial filial) {
        this(filial.getId(), filial.getNome(), filial.getPais(), filial.getEndereco(), filial.getTelefone(),
                filial.getEmail(), filial.getUsuarios().stream().map(Usuario::getNome).toList(),
                filial.getPatios().stream().map(Patio::getNome).toList());
    }
}
