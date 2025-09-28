package br.com.geomottu.api.services;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.moto.MotoDto;
import br.com.geomottu.api.dto.moto.MotoGetDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.exceptions.PatioLotadoException;
import br.com.geomottu.api.exceptions.PlacaNaoEncontradaException;
import br.com.geomottu.api.model.entities.Moto;
import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.model.enums.EstadoMoto;
import br.com.geomottu.api.repository.PatioRepository;
import br.com.geomottu.api.repository.MotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioRepository patioRepository;
    private final SecurityUtils securityUtils;

    public Moto save(MotoDto dto) throws IdNaoEncontradoException, PatioLotadoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        Patio patio = patioRepository.findById(dto.patioId())
                .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + dto.patioId()));

        if (!securityUtils.isAdmin(usuarioLogado)) {
            if (!patio.getFilial().equals(usuarioLogado.getFilial())) {
                throw new AccessDeniedException("Acesso negado. Você só pode adicionar motos a pátios da sua filial.");
            }
        }

        verificarCapacidadePatio(patio);

        Moto moto = new Moto(dto, patio);

        return motoRepository.save(moto);
    }

    public List<Moto> getAll() {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return motoRepository.findAll();
        } else {
            return motoRepository.findAllByFilial(usuarioLogado.getFilial());
        }
    }

    public Moto getById(Long id) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return motoRepository.findById(id)
                    .orElseThrow(() -> new IdNaoEncontradoException("Moto não encontrada com ID: " + id));
        } else {
            return motoRepository.findByIdAndFilial(id, usuarioLogado.getFilial())
                    .orElseThrow(() -> new IdNaoEncontradoException("Moto não encontrada ou não pertence à sua filial. ID: " + id));
        }
    }

    public Moto getByPlaca(String placa) throws PlacaNaoEncontradaException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return motoRepository.findByPlacaIgnoreCase(placa)
                    .orElseThrow(() -> new PlacaNaoEncontradaException("Moto não encontrada com placa: " + placa));
        } else {
            return motoRepository.findByPlacaIgnoreCaseAndFilial(placa, usuarioLogado.getFilial())
                    .orElseThrow(() -> new PlacaNaoEncontradaException("Moto não encontrada ou não pertence à sua filial. Placa: " + placa));
        }
    }

    public Moto getByChassi(String chassi) {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return motoRepository.findByChassiIgnoreCase(chassi)
                    .orElseThrow(() -> new NoSuchElementException("Moto não encontrada com chassi: " + chassi));
        } else {
            return motoRepository.findByChassiIgnoreCaseAndFilial(chassi, usuarioLogado.getFilial())
                    .orElseThrow(() -> new NoSuchElementException("Moto não encontrada ou não pertence à sua filial. Chassi: " + chassi));
        }
    }

    public Moto update(Long id, MotoDto dto) throws IdNaoEncontradoException, PatioLotadoException {

        Moto moto = getById(id);

        Patio patio = patioRepository.findById(dto.patioId())
                .orElseThrow(() -> new NoSuchElementException("Pátio não encontrado com ID: " + dto.patioId()));

        if (!moto.getPatio().getId().equals(patio.getId())) {
            verificarCapacidadePatio(patio);
        }

        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (!securityUtils.isAdmin(usuarioLogado)) {
            if (!patio.getFilial().equals(usuarioLogado.getFilial())) {
                throw new AccessDeniedException("Usuários não podem mover uma moto para um pátio de outra filial.");
            }
        }

        moto.setPlaca(dto.placa());
        moto.setChassi(dto.chassi());
        moto.setTipoMoto(dto.tipoMoto());
        moto.setEstadoMoto(dto.estadoMoto());
        moto.setPatio(patio);

        return motoRepository.save(moto);
    }

    public void delete(Long id) throws IdNaoEncontradoException {
        Moto motoParaDeletar = getById(id);
        motoRepository.delete(motoParaDeletar);
    }

    public long countTotal() {
        securityUtils.checkAdminAccess();
        return motoRepository.count();
    }

    public List<Map<String, Object>> countByEstado() {
        securityUtils.checkAdminAccess();

        return motoRepository.countMotosByEstado();
    }

    public List<Map<String, Object>> countByModelo() {
        securityUtils.checkAdminAccess();
        return motoRepository.countMotosByModelo();
    }

    public Moto updateStatus(Long id, EstadoMoto novoEstado) throws IdNaoEncontradoException {
        Moto moto = getById(id);

        moto.setEstadoMoto(novoEstado);

        return motoRepository.save(moto);
    }

    private void verificarCapacidadePatio(Patio patio) throws PatioLotadoException {
        int numeroAtualDeMotos = patio.getMotos().size();
        if (numeroAtualDeMotos >= patio.getCapacidadeTotal()) {
            throw new PatioLotadoException("Operação falhou: O pátio '" + patio.getNome() + "' já atingiu sua capacidade máxima de " + patio.getCapacidadeTotal() + " motos.");
        }
    }
}
