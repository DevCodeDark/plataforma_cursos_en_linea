package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.security.CustomUserDetailsService;
import com.devcodedark.plataforma_cursos.service.ICursoService;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;
import com.devcodedark.plataforma_cursos.service.IRolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para el dashboard administrativo
 * Solo accesible por usuarios con rol ADMINISTRADOR
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    // Constantes para evitar duplicación de literales
    private static final String ADMIN_DASHBOARD = "admin/dashboard";
    private static final String ERROR_ATTR = "error";
    private static final String ROLES_ATTR = "roles";
    private static final String ERROR_500 = "error/500";
    
    private final IUsuarioService usuarioService;
    private final ICursoService cursoService;
    private final IRolService rolService;
    private final CustomUserDetailsService userDetailsService;

    public AdminController(IUsuarioService usuarioService, 
                          ICursoService cursoService,
                          IRolService rolService,
                          CustomUserDetailsService userDetailsService) {
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
        this.rolService = rolService;
        this.userDetailsService = userDetailsService;
    }    /**
     * Dashboard principal del administrador
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        try {
            // Obtener información del usuario actual
            String email = authentication.getName();
            Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
              // Estadísticas generales
            long totalUsuarios = usuarioService.contarUsuarios();
            long totalCursos = cursoService.contarCursos();
            long totalDocentes = usuarioService.contarUsuariosPorRol("Docente");
            long totalEstudiantes = usuarioService.contarUsuariosPorRol("Estudiante");
            
            // Obtener listas para las tablas
            var usuarios = usuarioService.buscarTodos();
            var cursos = cursoService.buscarTodos();
            var roles = rolService.buscarTodos();
            
            // DTOs para el perfil
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
            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalCursos", totalCursos);
            model.addAttribute("totalDocentes", totalDocentes);
            model.addAttribute("totalEstudiantes", totalEstudiantes);
            model.addAttribute("totalInscripciones", 0L); // Por implementar
            model.addAttribute("ingresosMensuales", "$0"); // Por implementar
              // Listas para tablas
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("cursos", cursos);
            model.addAttribute(ROLES_ATTR, roles);
            
            // DTOs para perfil
            model.addAttribute("perfilDTO", perfilDTO);
            model.addAttribute("passwordDTO", passwordDTO);
            
            logger.info("Acceso al dashboard administrativo por: {}", email);
            
            return ADMIN_DASHBOARD;
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard administrativo: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar el dashboard");
            return ERROR_500;
        }
    }

    /**
     * Gestión de usuarios
     */    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        try {
            model.addAttribute("usuarios", usuarioService.buscarTodos());
            model.addAttribute(ROLES_ATTR, rolService.buscarTodos());
            return "admin/usuarios";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de usuarios: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar usuarios");
            return ADMIN_DASHBOARD;
        }
    }

    /**
     * Gestión de cursos
     */    @GetMapping("/cursos")
    public String gestionCursos(Model model) {
        try {
            model.addAttribute("cursos", cursoService.buscarTodos());
            return "admin/cursos";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de cursos: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar cursos");
            return ADMIN_DASHBOARD;
        }
    }    /**
     * Gestión de roles y permisos
     */
    @GetMapping("/roles")
    public String gestionRoles(Model model) {
        try {
            model.addAttribute(ROLES_ATTR, rolService.buscarTodos());
            return "admin/roles";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de roles: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar roles");
            return ADMIN_DASHBOARD;        }
    }    /**
     * Configuración del sistema
     */
    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        try {
            // Ya no redirigir, la configuración se maneja dentro del dashboard
            return ADMIN_DASHBOARD;
        } catch (Exception e) {
            logger.error("Error al acceder a configuración: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar configuración");
            return ADMIN_DASHBOARD;
        }
    }
}
