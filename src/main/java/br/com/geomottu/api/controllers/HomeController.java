package br.com.geomottu.api.controllers;

import br.com.geomottu.api.dto.usuario.UsuarioDto;
import br.com.geomottu.api.repository.FilialRepository;
import br.com.geomottu.api.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioService usuarioService;
    private final FilialRepository filialRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String accessDeniedPage() {
        return "acesso-negado";
    }

    @GetMapping("/registrar")
    public String showRegistrationForm(Model model) {
        // Garante que o formulário tenha um objeto para vincular os dados
        if (!model.containsAttribute("usuarioDto")) {
            model.addAttribute("usuarioDto", new UsuarioDto(null, null, null, null));
        }
        model.addAttribute("filiais", filialRepository.findAll());
        return "registro";
    }

    @PostMapping("/registrar")
    public String registerUser(@Valid @ModelAttribute("usuarioDto") UsuarioDto usuarioDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            // Se houver erros de validação, retorna para o formulário, mantendo os dados
            model.addAttribute("filiais", filialRepository.findAll());
            return "registro";
        }
        try {
            usuarioService.register(usuarioDto);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça o login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao realizar cadastro: " + e.getMessage());
            return "redirect:/registrar";
        }
    }
}