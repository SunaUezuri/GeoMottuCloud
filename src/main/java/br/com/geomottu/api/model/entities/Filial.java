package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.filial.FilialDto;
import br.com.geomottu.api.model.enums.PaisesFilial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_GEOMOTTU_FILIAL")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filial {

    @Id @Column(name = "id_filial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nm_filial", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "pais_filial", nullable = false)
    private PaisesFilial pais;

    @Embedded
    private Endereco endereco;

    @Column(name = "nm_telefone", length = 15)
    private String telefone;

    @Column(name = "ds_email", length = 30)
    private String email;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Patio> patios;

    public Filial(FilialDto json) {
        this.nome = json.nome();
        this.pais = json.pais();
        this.endereco = new Endereco(json.endereco());
        this.telefone = json.telefone();
        this.email = json.email();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filial filial = (Filial) o;
        return Objects.equals(id, filial.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
