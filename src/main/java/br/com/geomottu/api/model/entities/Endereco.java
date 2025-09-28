package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.endereco.EnderecoDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Endereco {

    private String estado;
    private String siglaEstado;
    private String cidade;
    private String rua;

    public Endereco(EnderecoDto json){
        this.estado = json.estado();
        this.siglaEstado = json.siglaEstado();
        this.cidade = json.cidade();
        this.rua = json.rua();
    }
}
