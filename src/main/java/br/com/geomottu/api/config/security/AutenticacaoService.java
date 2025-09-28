package br.com.geomottu.api.config.security;

import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    // Adiciona o Logger
    private static final Logger logger = LoggerFactory.getLogger(AutenticacaoService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Log para ver qual usuário está tentando logar
        logger.info("Tentando carregar usuário do banco: '{}'", username);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByNomeIgnoreCase(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Log se o usuário for encontrado
            logger.info("Usuário '{}' encontrado no banco. Verificando a senha...", username);
            return usuario;
        } else {
            // Log se o usuário NÃO for encontrado
            logger.error("Usuário '{}' NÃO encontrado no banco de dados.", username);
            throw new UsernameNotFoundException("Usuário: " + username + ". Não encontrado");
        }
    }
}