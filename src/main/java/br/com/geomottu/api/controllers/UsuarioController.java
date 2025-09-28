package br.com.geomottu.api.controllers;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.usuario.UpdateRoleDto;
import br.com.geomottu.api.dto.usuario.UsuarioDto;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.repository.FilialRepository;
import br.com.geomottu.api.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final FilialRepository filialRepository;

    // ADMIN: Lista todos os usuários
    @GetMapping
    public String listAll(Model model, @RequestParam(name = "query", required = false) String query) {
        List<Usuario> usuarios;

        if (query != null && !query.isBlank()) {
            try {
                // Busca por nome e coloca o resultado único em uma lista
                usuarios = List.of(usuarioService.getByName(query));
            } catch (Exception e) {
                // Se não encontrar, retorna uma lista vazia e uma mensagem de erro
                usuarios = Collections.emptyList();
                model.addAttribute("errorMessage", "Usuário '" + query + "' não encontrado.");
            }
        } else {
            // Se não houver busca, lista todos os usuários
            usuarios = usuarioService.getAll();
        }

        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    // USER e ADMIN: Mostra o próprio perfil
    @GetMapping("/perfil")
    public String viewProfile(Model model) {
        try {
            Usuario usuarioLogado = securityUtils.getUsuarioLogado();
            model.addAttribute("usuario", usuarioService.getById(usuarioLogado.getId()));
            return "usuarios/perfil";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    // USER e ADMIN: Página para editar o perfil
    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.getById(id);
            // O DTO é criado para preencher o formulário.
            UsuarioDto dto = new UsuarioDto(usuario.getNome(), usuario.getTipoPerfil(), "", usuario.getFilial().getId());

            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioDto", dto);

            // Se o usuário logado for ADMIN, enviamos a lista de filiais para o formulário
            if (securityUtils.isAdmin(securityUtils.getUsuarioLogado())) {
                model.addAttribute("filiais", filialRepository.findAll());
            }

            return "usuarios/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    // USER e ADMIN: Processa a atualização do perfil
    @PostMapping("/{id}/editar")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("usuarioDto") UsuarioDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (result.hasErrors()) {
            try {
                model.addAttribute("usuario", usuarioService.getById(id));
                if (securityUtils.isAdmin(securityUtils.getUsuarioLogado())) {
                    model.addAttribute("filiais", filialRepository.findAll()); // Reenvia a lista de filiais para o admin
                }
            } catch (Exception e) {
                // Em caso de erro ao buscar os dados, redireciona para a lista
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao recarregar dados do formulário.");
                return "redirect:/usuarios";
            }
            return "usuarios/form";
        }

        try {
            Usuario usuarioLogado = securityUtils.getUsuarioLogado();
            usuarioService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Usuário atualizado com sucesso!");

            if (securityUtils.isAdmin(usuarioLogado) && !usuarioLogado.getId().equals(id)) {
                return "redirect:/usuarios";
            } else {
                return "redirect:/usuarios/perfil";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar perfil: " + e.getMessage());
            // Mantém os dados do DTO no formulário em caso de erro
            redirectAttributes.addFlashAttribute("usuarioDto", dto);
            return "redirect:/usuarios/" + id + "/editar";
        }
    }

    // ADMIN: Processa a mudança de perfil de um usuário
    @PostMapping("/{id}/role")
    public String updateUserRole(@PathVariable Long id, @ModelAttribute("updateRoleDto") UpdateRoleDto dto, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.updateRole(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Perfil do usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar perfil: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/deletar")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            Usuario usuarioLogado = securityUtils.getUsuarioLogado();
            boolean isSelfDelete = usuarioLogado.getId().equals(id);

            // Pega a autenticação ANTES de deletar o usuário
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            usuarioService.delete(id);

            if (isSelfDelete) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return "redirect:/login?logout";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Usuário deletado com sucesso!");
            return "redirect:/usuarios";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar usuário: " + e.getMessage());
            return "redirect:/usuarios";
        }
    }
}
