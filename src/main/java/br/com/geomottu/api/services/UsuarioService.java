package br.com.geomottu.api.services;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.usuario.UpdateRoleDto;
import br.com.geomottu.api.dto.usuario.UsuarioDto;
import br.com.geomottu.api.dto.usuario.UsuarioGetDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.exceptions.UsuarioJaCadastradoException;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.repository.FilialRepository;
import br.com.geomottu.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FilialRepository filialRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    // Create (Auto cadastro disponível para todos os usuários)
    public Usuario register(UsuarioDto dto) throws UsuarioJaCadastradoException, IdNaoEncontradoException {
        if (usuarioRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new UsuarioJaCadastradoException();
        }

        Filial filial = filialRepository.findById(dto.filialId())
                .orElseThrow(IdNaoEncontradoException::new);

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setTipoPerfil(2);
        usuario.setFilial(filial);


        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAll() {
        securityUtils.checkAdminAccess();
        return usuarioRepository.findAll();
    }

    public Usuario getById(Long id) throws IdNaoEncontradoException {
        securityUtils.checkAdminOrOwnerAccess(id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    public Usuario getByName(String nome) {
        Usuario usuario = usuarioRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com nome: " + nome));
        securityUtils.checkAdminOrOwnerAccess(usuario.getId());
        return usuario;
    }

    public Usuario update(Long id, UsuarioDto dto) throws IdNaoEncontradoException {
        securityUtils.checkAdminOrOwnerAccess(id);

        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        Usuario usuarioParaAtualizar = usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com ID: " + id));

        usuarioParaAtualizar.setNome(dto.nome());

        // Atualiza a senha APENAS se uma nova senha for fornecida
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioParaAtualizar.setSenha(passwordEncoder.encode(dto.senha()));
        }

        // Apenas ADMINS podem alterar filial e tipo de perfil
        if (securityUtils.isAdmin(usuarioLogado)) {
            Filial filial = filialRepository.findById(dto.filialId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + dto.filialId()));
            usuarioParaAtualizar.setFilial(filial);

            // Impede que um admin mude o próprio perfil nesta tela
            if (!usuarioLogado.getId().equals(id)) {
                usuarioParaAtualizar.setTipoPerfil(dto.tipoPerfil());
            }
        }

        return usuarioRepository.save(usuarioParaAtualizar);
    }

    public Usuario updateRole(Long userId, UpdateRoleDto dto) throws IdNaoEncontradoException {
        securityUtils.checkAdminAccess();
        Usuario adminLogado = securityUtils.getUsuarioLogado();

        if (adminLogado.getId().equals(userId)) {
            throw new IllegalArgumentException("Administradores não podem alterar o próprio perfil através desta funcionalidade.");
        }
        if (dto.tipoPerfil() < 1 || dto.tipoPerfil() > 2) {
            throw new IllegalArgumentException("Tipo de perfil inválido. Use 1 para ADMIN ou 2 para USER.");
        }

        Usuario usuarioParaAtualizar = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com ID: " + userId));
        usuarioParaAtualizar.setTipoPerfil(dto.tipoPerfil());

        return usuarioRepository.save(usuarioParaAtualizar);
    }

    public void delete(Long id) throws IdNaoEncontradoException {
        securityUtils.checkAdminOrOwnerAccess(id);
        if (!usuarioRepository.existsById(id)) {
            throw new IdNaoEncontradoException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public long countTotal() {
        securityUtils.checkAdminAccess();

        return usuarioRepository.count();
    }

}
