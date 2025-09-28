package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.patio.PatioDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "T_GEOMOTTU_PATIO")
@SequenceGenerator(name = "patio", sequenceName = "SQ_T_GEOMOTTU_PATIO", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Patio {

    @Id
    @Column(name = "id_patio")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patio")
    private Long id;

    @Column(name = "nm_patio", length = 50)
    private String nome;

    @Column(nullable = false, precision = 4, name = "nr_capacidade")
    private Integer capacidadeTotal;

    @ManyToOne
    @JoinColumn(name = "id_filial")
    private Filial filial;

    @OneToMany(mappedBy = "patio", fetch = FetchType.EAGER)
    private List<Moto> motos;

    public Patio(PatioDto dto){
        this.nome = dto.nome();
        this.capacidadeTotal = dto.capacidadeTotal();
    }
}