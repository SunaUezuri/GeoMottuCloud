package br.com.geomottu.api.repository;

import br.com.geomottu.api.model.entities.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilialRepository extends JpaRepository<Filial, Long> {
}
