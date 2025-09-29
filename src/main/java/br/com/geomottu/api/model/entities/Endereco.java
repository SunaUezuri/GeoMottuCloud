package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.endereco.EnderecoDto;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@AttributeOverrides({
        @AttributeOverride(name = "rua", column = @Column(name = "rua")),
        @AttributeOverride(name = "cidade", column = @Column(name = "cidade")),
        @AttributeOverride(name = "estado", column = @Column(name = "estado")),
        @AttributeOverride(name = "siglaEstado", column = @Column(name = "sigla_estado"))
})
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
