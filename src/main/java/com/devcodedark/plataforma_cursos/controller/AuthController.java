package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.UsuarioRegistroDTO;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para manejar la autenticación y registro de usuarios
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Página de login
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model,
                       Authentication authentication) {
        
        // Si el usuario ya está autenticado, redirigir a su dashboard
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas. Por favor, verifica tu email y contraseña.");
        }
        
        if (logout != null) {
            model.addAttribute("logout", "Has cerrado sesión exitosamente.");
        }

        return "auth/login";
    }

    /**
     * Página de registro
     */
    @GetMapping("/registro")
    public String registro(Model model, Authentication authentication) {
        // Si el usuario ya está autenticado, redirigir a su dashboard
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "auth/registro";
    }

    /**
     * Procesar registro de usuario
     */    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO usuarioDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        try {
            // Verificar errores de validación
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "Por favor, corrige los errores en el formulario.");
                return "auth/registro";
            }

            // Verificar si el email ya existe
            if (usuarioService.existeEmail(usuarioDTO.getEmail())) {
                model.addAttribute("error", "Ya existe un usuario con ese email.");
                return "auth/registro";
            }

            // Verificar si el username ya existe
            if (usuarioService.existeUsuario(usuarioDTO.getUsuario())) {
                model.addAttribute("error", "Ya existe un usuario con ese nombre de usuario.");
                return "auth/registro";
            }

            // Registrar usuario
            usuarioService.registrarUsuario(usuarioDTO);
            
            logger.info("Usuario registrado exitosamente: {}", usuarioDTO.getEmail());
            
            redirectAttributes.addFlashAttribute("success", 
                "Registro exitoso. Ya puedes iniciar sesión con tu cuenta.");
            
            return "redirect:/auth/login";
            
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage(), e);
            model.addAttribute("error", "Error interno del servidor. Por favor, inténtalo más tarde.");
            return "auth/registro";
        }
    }

    /**
     * Página de recuperación de contraseña
     */
    @GetMapping("/recuperar-password")
    public String recuperarPassword() {
        return "auth/recuperar-password";
    }

    /**
     * Procesar solicitud de recuperación de contraseña
     */
    @PostMapping("/recuperar-password")
    public String procesarRecuperarPassword(@RequestParam("email") String email,
                                           RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.existeEmail(email)) {
                // TODO: Implementar envío de email de recuperación
                logger.info("Solicitud de recuperación de contraseña para: {}", email);
                redirectAttributes.addFlashAttribute("success", 
                    "Si el email existe, recibirás un enlace de recuperación.");
            } else {
                redirectAttributes.addFlashAttribute("success", 
                    "Si el email existe, recibirás un enlace de recuperación.");
            }
            
            return "redirect:/auth/login";
            
        } catch (Exception e) {
            logger.error("Error al procesar recuperación de contraseña: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", 
                "Error interno del servidor. Por favor, inténtalo más tarde.");
            return "redirect:/auth/recuperar-password";
        }
    }
}
