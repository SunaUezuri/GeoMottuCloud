package br.com.geomottu.api.services;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.filial.FilialDto;
import br.com.geomottu.api.dto.filial.FilialGetDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Endereco;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.repository.FilialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilialService {

    private final FilialRepository filialRepository;
    private final SecurityUtils securityUtils;

    public Filial save(FilialDto dto) {
        securityUtils.checkAdminAccess();

        Filial filial = new Filial(dto);

        return filialRepository.save(filial);
    }

    public List<Filial> getAll() {
        securityUtils.checkAdminAccess();
        return filialRepository.findAll();
    }

    public Filial getById(Long id) throws IdNaoEncontradoException {
        securityUtils.checkAdminAccess();
        return filialRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + id));
    }

    public Filial update(Long id, FilialDto dto) throws IdNaoEncontradoException {
        securityUtils.checkAdminAccess();
        Filial filial = getById(id);

        filial.setNome(dto.nome());
        filial.setPais(dto.pais());
        filial.setEndereco(new Endereco(dto.endereco()));
        filial.setTelefone(dto.telefone());
        filial.setEmail(dto.email());

        return filialRepository.save(filial);
    }

    public void delete(Long id) throws IdNaoEncontradoException {
        securityUtils.checkAdminAccess();
        Filial filial = getById(id);

        filialRepository.delete(filial);
    }
}
