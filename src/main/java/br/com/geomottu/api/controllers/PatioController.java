package br.com.geomottu.api.controllers;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.patio.PatioDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.services.FilialService;
import br.com.geomottu.api.services.PatioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/patios")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;
    private final FilialService filialService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("patios", patioService.getAll());
        return "patios/lista";
    }

    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("patioDto", new PatioDto(null, null, null));
        // Se o usuário for admin, precisa da lista de filiais para o formulário
        if (securityUtils.isAdmin(securityUtils.getUsuarioLogado())) {
            try {
                model.addAttribute("filiais", filialService.getAll());
            } catch (Exception e) {
                // Lidar com o caso de não encontrar filiais, se necessário
                model.addAttribute("filiais", List.of());
            }
        }
        return "patios/form";
    }

    @PostMapping("/novo")
    public String createPatio(@Valid @ModelAttribute("patioDto") PatioDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            // Se houver erros de validação, recarrega a lista de filiais para o admin
            if (securityUtils.isAdmin(securityUtils.getUsuarioLogado())) {
                model.addAttribute("filiais", filialService.getAll());
            }
            return "patios/form";
        }
        try {
            patioService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Pátio criado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar pátio: " + e.getMessage());
            return "redirect:/patios/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Patio patio = patioService.getById(id);
            // Converte a entidade para o DTO para preencher o formulário
            PatioDto dto = new PatioDto(patio.getNome(), patio.getCapacidadeTotal(), patio.getFilial().getId());
            model.addAttribute("patioDto", dto);
            model.addAttribute("patioId", id);
            model.addAttribute("filiais", filialService.getAll()); // Admin sempre pode ver todas as filiais
            return "patios/form";
        } catch (IdNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patios";
        }
    }

    @PostMapping("/{id}/editar")
    public String updatePatio(@PathVariable Long id,
                              @Valid @ModelAttribute("patioDto") PatioDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patioId", id);
            model.addAttribute("filiais", filialService.getAll());
            return "patios/form";
        }
        try {
            patioService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Pátio atualizado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar pátio: " + e.getMessage());
            return "redirect:/patios/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deletePatio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patioService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pátio deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar pátio: " + e.getMessage());
        }
        return "redirect:/patios";
    }
}