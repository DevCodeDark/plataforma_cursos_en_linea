package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.security.CustomUserDetailsService;
import com.devcodedark.plataforma_cursos.service.ICursoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estudiante")
@PreAuthorize("hasRole('ESTUDIANTE') or hasRole('DOCENTE') or hasRole('ADMINISTRADOR') or hasRole('ADMIN')")
public class EstudianteController {
    
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);
    
    // Constantes
    private static final String ESTUDIANTE_DASHBOARD = "estudiante/dashboard";
    private static final String ERROR_ATTR = "error";
    private static final String ERROR_500 = "error/500";
    
    private final ICursoService cursoService;
    private final CustomUserDetailsService userDetailsService;

    public EstudianteController(ICursoService cursoService,
                              CustomUserDetailsService userDetailsService) {
        this.cursoService = cursoService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Dashboard principal del estudiante
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        try {
            // Obtener información del usuario actual
            String email = authentication.getName();
            Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
            
            // Estadísticas del estudiante
            var cursosDisponibles = cursoService.buscarCursosPublicados();
            long totalCursosDisponibles = cursosDisponibles.size();
              // Por ahora simulamos datos, luego se implementará con inscripciones reales
            long totalCursosInscritos = 0;
            long cursosCompletados = 0;
            long cursosEnProgreso = 0;
            long horasEstudio = 0;
            long certificadosObtenidos = 0;
            
            // DTOs para formularios
            PerfilUpdateDTO perfilDTO = new PerfilUpdateDTO();
            if (usuarioActual != null) {
                perfilDTO.setNombre(usuarioActual.getNombre());
                perfilDTO.setApellido(usuarioActual.getApellido());
                perfilDTO.setEmail(usuarioActual.getEmail());
                perfilDTO.setUsuario(usuarioActual.getUsuario());
                perfilDTO.setTelefono(usuarioActual.getTelefono());
                perfilDTO.setFechaNacimiento(usuarioActual.getFechaNacimiento());
                perfilDTO.setGenero(usuarioActual.getGenero());
            }
            CambioPasswordDTO passwordDTO = new CambioPasswordDTO();
              // Agregar datos al modelo
            model.addAttribute("usuarioActual", usuarioActual);
            model.addAttribute("totalCursosDisponibles", totalCursosDisponibles);
            model.addAttribute("totalCursosInscritos", totalCursosInscritos);
            model.addAttribute("cursosCompletados", cursosCompletados);
            model.addAttribute("cursosEnProgreso", cursosEnProgreso);
            model.addAttribute("horasEstudio", horasEstudio);
            model.addAttribute("certificadosObtenidos", certificadosObtenidos);
            
            // Listas para mostrar
            model.addAttribute("cursosDisponibles", cursosDisponibles);
            
            // DTOs para formularios
            model.addAttribute("perfilDTO", perfilDTO);
            model.addAttribute("passwordDTO", passwordDTO);
            
            logger.info("Acceso al dashboard estudiante por: {}", email);
            
            return ESTUDIANTE_DASHBOARD;
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard estudiante: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar el dashboard");
            return ERROR_500;
        }
    }
}
