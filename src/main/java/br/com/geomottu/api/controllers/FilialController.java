package br.com.geomottu.api.controllers;

import br.com.geomottu.api.dto.endereco.EnderecoDto;
import br.com.geomottu.api.dto.filial.FilialDto;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.services.FilialService;
import jakarta.validation.Valid; // Importe o @Valid
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Importe o BindingResult
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/filiais")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("filiais", filialService.getAll());
        return "filiais/lista";
    }

    @GetMapping("/nova")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("filialDto")) {
            model.addAttribute("filialDto", new FilialDto(null, null, new EnderecoDto(null, null, null, null), null, null));
        }
        return "filiais/form";
    }

    @PostMapping("/nova")
    // Adicionamos @Valid e BindingResult
    public String createFilial(@Valid @ModelAttribute("filialDto") FilialDto dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) { // Adicionamos o Model

        // Se houver erros de validação, retorna para o formulário
        if (result.hasErrors()) {
            return "filiais/form";
        }

        try {
            filialService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Filial criada com sucesso!");
            return "redirect:/filiais"; // Redireciona em caso de sucesso
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar filial: " + e.getMessage());
            redirectAttributes.addFlashAttribute("filialDto", dto);
            return "redirect:/filiais/nova";
        }
    }

    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Filial filial = filialService.getById(id);
            // Preenche o DTO com os dados existentes se não houver um no modelo (útil após erro de validação)
            if (!model.containsAttribute("filialDto")) {
                FilialDto dto = new FilialDto(
                        filial.getNome(),
                        filial.getPais(),
                        new EnderecoDto(
                                filial.getEndereco().getEstado(),
                                filial.getEndereco().getSiglaEstado(),
                                filial.getEndereco().getCidade(),
                                filial.getEndereco().getRua()
                        ),
                        filial.getTelefone(),
                        filial.getEmail()
                );
                model.addAttribute("filialDto", dto);
            }
            model.addAttribute("filialId", id);
            return "filiais/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Filial não encontrada: " + e.getMessage());
            return "redirect:/filiais";
        }
    }

    @PostMapping("/{id}/editar")
    // Adicionamos @Valid e BindingResult aqui também
    public String updateFilial(@PathVariable Long id,
                               @Valid @ModelAttribute("filialDto") FilialDto dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) { // Adicionamos o Model

        // Se houver erros de validação, retorna para a página de edição
        if (result.hasErrors()) {
            model.addAttribute("filialId", id);
            return "filiais/form";
        }

        try {
            filialService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Filial atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar filial: " + e.getMessage());
        }
        return "redirect:/filiais";
    }

    @PostMapping("/{id}/deletar")
    public String deleteFilial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            filialService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Filial deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar filial: " + e.getMessage());
        }
        return "redirect:/filiais";
    }
}