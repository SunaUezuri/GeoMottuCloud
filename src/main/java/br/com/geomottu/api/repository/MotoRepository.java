package br.com.geomottu.api.repository;

import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long> {

    Optional<Moto> findByPlacaIgnoreCase(String placa);
    Optional<Moto> findByChassiIgnoreCase(String chassi);

    @Query("SELECT m FROM Moto m WHERE m.patio.filial = :filial")
    List<Moto> findAllByFilial(@Param("filial") Filial filial);

    @Query("SELECT m FROM Moto m WHERE m.id = :id AND m.patio.filial = :filial")
    Optional<Moto> findByIdAndFilial(@Param("id") Long id, @Param("filial") Filial filial);

    @Query("SELECT m FROM Moto m WHERE upper(m.placa) = upper(:placa) AND m.patio.filial = :filial")
    Optional<Moto> findByPlacaIgnoreCaseAndFilial(@Param("placa") String placa, @Param("filial") Filial filial);

    @Query("SELECT m FROM Moto m WHERE upper(m.chassi) = upper(:chassi) AND m.patio.filial = :filial")
    Optional<Moto> findByChassiIgnoreCaseAndFilial(@Param("chassi") String chassi, @Param("filial") Filial filial);

    @Query("SELECT m.estadoMoto as estado, COUNT(m) as total FROM Moto m GROUP BY m.estadoMoto")
    List<Map<String, Object>> countMotosByEstado();

    @Query("SELECT m.tipoMoto as modelo, COUNT(m) as total FROM Moto m GROUP BY m.tipoMoto")
    List<Map<String, Object>> countMotosByModelo();
}
