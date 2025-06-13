package com.devcodedark.plataforma_cursos.controller;

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
            model.addAttribute("roles", roles);
            
            logger.info("Acceso al dashboard administrativo por: {}", email);
            
            return "admin/dashboard";
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard administrativo: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el dashboard");
            return "error/500";
        }
    }

    /**
     * Gestión de usuarios
     */
    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        try {
            model.addAttribute("usuarios", usuarioService.buscarTodos());
            model.addAttribute("roles", rolService.buscarTodos());
            return "admin/usuarios";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de usuarios: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar usuarios");
            return "admin/dashboard";
        }
    }

    /**
     * Gestión de cursos
     */
    @GetMapping("/cursos")
    public String gestionCursos(Model model) {
        try {
            model.addAttribute("cursos", cursoService.buscarTodos());
            return "admin/cursos";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de cursos: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar cursos");
            return "admin/dashboard";
        }
    }

    /**
     * Gestión de roles y permisos
     */
    @GetMapping("/roles")
    public String gestionRoles(Model model) {
        try {
            model.addAttribute("roles", rolService.buscarTodos());
            return "admin/roles";
        } catch (Exception e) {
            logger.error("Error al cargar gestión de roles: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar roles");
            return "admin/dashboard";
        }
    }

    /**
     * Reportes y estadísticas
     */
    @GetMapping("/reportes")
    public String reportes(Model model) {
        try {
            // TODO: Implementar lógica de reportes
            return "admin/reportes";
        } catch (Exception e) {
            logger.error("Error al cargar reportes: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar reportes");
            return "admin/dashboard";
        }
    }

    /**
     * Configuración del sistema
     */
    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        try {
            // TODO: Implementar configuraciones del sistema
            return "admin/configuracion";
        } catch (Exception e) {
            logger.error("Error al cargar configuración: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar configuración");
            return "admin/dashboard";
        }
    }
}
