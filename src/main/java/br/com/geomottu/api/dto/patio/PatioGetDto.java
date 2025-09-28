package br.com.geomottu.api.dto.patio;

import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.model.entities.Moto;

import java.util.List;

public record PatioGetDto(
        Long id,
        String nome,
        Integer capacidadeTotal,
        Long filialId,
        List<String> motos

) {
    public PatioGetDto(Patio patio) {
        this(patio.getId(), patio.getNome(), patio.getCapacidadeTotal(),
                patio.getFilial().getId(), patio.getMotos().stream().map(Moto::getPlaca).toList());
    }
}
