package br.com.geomottu.api.repository;

import br.com.geomottu.api.dto.patio.PatioOcupacaoDto;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    List<Patio> findAllByFilial(Filial filial);
    Optional<Patio> findByIdAndFilial(Long id, Filial filial);

    @Query("SELECT new br.com.geomottu.api.dto.patio.PatioOcupacaoDto(" +
            "p.nome, " +
            "SIZE(p.motos), " +
            "p.capacidadeTotal, " +
            "(CASE WHEN p.capacidadeTotal > 0 THEN (CAST(SIZE(p.motos) AS double) / p.capacidadeTotal) * 100.0 ELSE 0.0 END)) " +
            "FROM Patio p ORDER BY (CASE WHEN p.capacidadeTotal > 0 THEN (CAST(SIZE(p.motos) AS double) / p.capacidadeTotal) ELSE 0.0 END) DESC")
    List<PatioOcupacaoDto> findPatiosByOcupacao(Pageable pageable);

}
