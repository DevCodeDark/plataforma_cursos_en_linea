package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private static final String REDIRECT_PERFIL = "redirect:/perfil";
    private static final String ERROR_PERFIL = "errorPerfil";
    private static final String ERROR_PASSWORD = "errorPassword";
    private static final String ERROR_FOTO = "errorFoto";
    private static final String SUCCESS_PERFIL = "successPerfil";
    private static final String SUCCESS_PASSWORD = "successPassword";
    private static final String SUCCESS_FOTO = "successFoto";
    private static final String ERROR_INTERNO = "Error interno: ";

    private final PerfilService perfilService;

    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }    /**
     * Muestra la página de perfil del usuario
     */
    @GetMapping
    public String mostrarPerfil(Authentication authentication, Model model) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            
            Optional<Usuario> usuarioOpt = perfilService.obtenerUsuarioPorEmail(email);
            if (!usuarioOpt.isPresent()) {
                return "redirect:/auth/login?error=usuario_no_encontrado";
            }
            
            Usuario usuario = usuarioOpt.get();            // Crear DTO para la actualización del perfil
            PerfilUpdateDTO perfilDTO = new PerfilUpdateDTO();
            perfilDTO.setNombre(usuario.getNombre());
            perfilDTO.setApellido(usuario.getApellido());
            perfilDTO.setEmail(usuario.getEmail());
            perfilDTO.setUsuario(usuario.getUsuario());
            perfilDTO.setTelefono(usuario.getTelefono());
            perfilDTO.setFechaNacimiento(usuario.getFechaNacimiento());
            perfilDTO.setGenero(usuario.getGenero());
            perfilDTO.setFotoPerfil(usuario.getFotoPerfil());

            // Crear DTO para cambio de contraseña
            CambioPasswordDTO passwordDTO = new CambioPasswordDTO();

            model.addAttribute("usuario", usuario);
            model.addAttribute("perfilDTO", perfilDTO);
            model.addAttribute("passwordDTO", passwordDTO);

            return "perfil/mi-perfil";
        } catch (Exception e) {
            model.addAttribute("error", ERROR_INTERNO + e.getMessage());
            return "error/500";
        }
    }    /**
     * Actualiza los datos del perfil del usuario
     */
    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute("perfilDTO") PerfilUpdateDTO perfilDTO,
                                   BindingResult result,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute(ERROR_PERFIL, "Por favor, corrige los errores en el formulario");
                return REDIRECT_PERFIL;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String emailActual = userDetails.getUsername();

            boolean actualizado = perfilService.actualizarPerfil(emailActual, perfilDTO);

            if (actualizado) {
                redirectAttributes.addFlashAttribute(SUCCESS_PERFIL, "Perfil actualizado correctamente");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_PERFIL, "Error al actualizar el perfil");
            }

            return REDIRECT_PERFIL;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_PERFIL, ERROR_INTERNO + e.getMessage());
            return REDIRECT_PERFIL;
        }
    }    /**
     * Cambia la contraseña del usuario
     */
    @PostMapping("/cambiar-password")
    public String cambiarPassword(@ModelAttribute("passwordDTO") CambioPasswordDTO passwordDTO,
                                  BindingResult result,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute(ERROR_PASSWORD, "Por favor, corrige los errores en el formulario");
                return REDIRECT_PERFIL;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            boolean cambiado = perfilService.cambiarPassword(email, passwordDTO);

            if (cambiado) {
                redirectAttributes.addFlashAttribute(SUCCESS_PASSWORD, "Contraseña cambiada correctamente");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_PASSWORD, "La contraseña actual no es correcta");
            }

            return REDIRECT_PERFIL;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_PASSWORD, ERROR_INTERNO + e.getMessage());
            return REDIRECT_PERFIL;
        }
    }

    /**
     * Sube la foto de perfil del usuario
     */
    @PostMapping("/subir-foto")
    public String subirFotoPerfil(@RequestParam("fotoPerfil") MultipartFile archivo,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute(ERROR_FOTO, "Por favor, selecciona una imagen");
                return REDIRECT_PERFIL;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            String rutaFoto = perfilService.subirFotoPerfil(email, archivo);

            if (rutaFoto != null) {
                redirectAttributes.addFlashAttribute(SUCCESS_FOTO, "Foto de perfil actualizada correctamente");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_FOTO, "Error al subir la foto de perfil");
            }

            return REDIRECT_PERFIL;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_FOTO, ERROR_INTERNO + e.getMessage());
            return REDIRECT_PERFIL;
        }
    }

    /**
     * Redirecciona a la configuración apropiada según el rol del usuario
     */
    @GetMapping("/configuracion")
    public String configuracion(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Verificar si el usuario es administrador
            boolean esAdmin = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMINISTRADOR"));
            
            if (esAdmin) {
                return "redirect:/admin/configuracion/sistema";
            } else {
                // Para usuarios no administradores, redirigir al perfil
                return "redirect:/perfil";
            }
        } catch (Exception e) {
            // En caso de error, redirigir al perfil por defecto
            return "redirect:/perfil";
        }
    }
}
