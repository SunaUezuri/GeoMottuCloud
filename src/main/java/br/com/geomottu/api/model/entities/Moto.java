package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.moto.MotoDto;
import br.com.geomottu.api.model.enums.EstadoMoto;
import br.com.geomottu.api.model.enums.TipoMoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_GEOMOTTU_MOTO")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "moto", sequenceName = "SQ_T_GEOMOTTU_MOTO", allocationSize = 1)
public class Moto {

    @Id @Column(name = "id_moto")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moto")
    private Long id;

    @Column(name = "nr_placa", length = 8, unique = true)
    private String placa;

    @Column(name = "nr_chassi", length = 50, unique = true)
    private String chassi;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_modelo", nullable = false)
    private TipoMoto tipoMoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_operacional", nullable = false)
    private EstadoMoto estadoMoto;

    @ManyToOne
    @JoinColumn(name = "id_patio")
    private Patio patio;

    public Moto(MotoDto dto, Patio patio) {
        this.placa = dto.placa();
        this.chassi = dto.chassi();
        this.tipoMoto = dto.tipoMoto();
        this.estadoMoto = dto.estadoMoto();
        this.patio = patio;
    }
}
