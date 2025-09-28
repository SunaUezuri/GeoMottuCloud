package br.com.geomottu.api.dto.moto;

import br.com.geomottu.api.model.entities.Moto;
import br.com.geomottu.api.model.enums.EstadoMoto;
import br.com.geomottu.api.model.enums.TipoMoto;

public record MotoGetDto(
        Long id,
        String placa,
        String chassi,
        TipoMoto tipoMoto,
        EstadoMoto estadoMoto,
        Long patioId
) {
    public MotoGetDto(Moto moto) {
        this(moto.getId(), moto.getPlaca(), moto.getChassi(),
                moto.getTipoMoto(), moto.getEstadoMoto(), moto.getPatio().getId());
    }
}
