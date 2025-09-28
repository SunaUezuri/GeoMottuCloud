package br.com.geomottu.api.controllers;

import br.com.geomottu.api.dto.moto.MotoDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Moto;
import br.com.geomottu.api.model.enums.EstadoMoto;
import br.com.geomottu.api.services.MotoService;
import br.com.geomottu.api.services.PatioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/motos")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final PatioService patioService;

    @GetMapping
    public String listAll(Model model,
                          @RequestParam(required = false) String tipoBusca,
                          @RequestParam(required = false) String query) {

        List<Moto> motos;
        // Se o campo de busca foi preenchido
        if (query != null && !query.isBlank()) {
            try {
                if ("PLACA".equals(tipoBusca)) {
                    // Busca por placa e coloca o resultado único em uma lista
                    motos = List.of(motoService.getByPlaca(query));
                } else {
                    // Busca por chassi e coloca o resultado único em uma lista
                    motos = List.of(motoService.getByChassi(query));
                }
            } catch (Exception e) {
                // Se a busca falhar (não encontrar), retorna uma lista vazia e uma mensagem de erro
                motos = Collections.emptyList();
                model.addAttribute("errorMessage", "Moto não encontrada: " + e.getMessage());
            }
        } else {
            // Se não houver busca, lista todas as motos
            motos = motoService.getAll();
        }

        model.addAttribute("motos", motos);
        return "motos/lista";
    }

    @GetMapping("/nova")
    public String showCreateForm(Model model) {
        model.addAttribute("motoDto", new MotoDto(null, null, null, null, null));
        model.addAttribute("patios", patioService.getAll());
        return "motos/form";
    }

    @PostMapping("/nova")
    public String createMoto(@Valid @ModelAttribute("motoDto") MotoDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patios", patioService.getAll());
            return "motos/form";
        }
        try {
            motoService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Moto criada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar moto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("motoDto", dto);
            return "redirect:/motos/nova";
        }
    }

    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Moto moto = motoService.getById(id);
            // Converte a entidade para o DTO para preencher o formulário
            MotoDto dto = new MotoDto(moto.getPlaca(), moto.getChassi(), moto.getTipoMoto(), moto.getEstadoMoto(), moto.getPatio().getId());
            model.addAttribute("motoDto", dto);
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.getAll()); // Admin pode ver todos os pátios para realocar a moto
            return "motos/form";
        } catch (IdNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/motos";
        }
    }

    @PostMapping("/{id}/editar")
    public String updateMoto(@PathVariable Long id,
                             @Valid @ModelAttribute("motoDto") MotoDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.getAll());
            return "motos/form";
        }
        try {
            motoService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Moto atualizada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar moto: " + e.getMessage());
            return "redirect:/motos/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/status")
    public String updateMotoStatus(@PathVariable Long id,
                                   @RequestParam("novoEstado") EstadoMoto novoEstado,
                                   RedirectAttributes redirectAttributes) {
        try {
            motoService.updateStatus(id, novoEstado);
            redirectAttributes.addFlashAttribute("successMessage", "Status da moto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/motos";
    }

    @PostMapping("/{id}/deletar")
    public String deleteMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Moto deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }
}