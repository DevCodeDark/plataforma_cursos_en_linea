package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.CursoDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.dto.RolDTO;
import com.devcodedark.plataforma_cursos.dto.UsuarioRegistroDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.security.CustomUserDetailsService;
import com.devcodedark.plataforma_cursos.service.ICategoriaService;
import com.devcodedark.plataforma_cursos.service.ICursoService;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;
import com.devcodedark.plataforma_cursos.service.IRolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

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
    private final ICategoriaService categoriaService;
    private final CustomUserDetailsService userDetailsService;

    public AdminController(IUsuarioService usuarioService, 
                          ICursoService cursoService,
                          IRolService rolService,
                          ICategoriaService categoriaService,
                          CustomUserDetailsService userDetailsService) {
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
        this.rolService = rolService;
        this.categoriaService = categoriaService;
        this.userDetailsService = userDetailsService;
    }/**
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
                perfilDTO.setGenero(usuarioActual.getGenero());            }            CambioPasswordDTO passwordDTO = new CambioPasswordDTO();
            UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
            CursoDTO cursoDTO = new CursoDTO();
            RolDTO rolDTO = new RolDTO();
              // Obtener listas para dropdowns del modal de curso
            var categorias = categoriaService.buscarTodos();
            var docentes = usuarioService.buscarDocentesActivos();
            
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
            model.addAttribute(ROLES_ATTR, roles);            // DTOs para perfil y modales
            model.addAttribute("perfilDTO", perfilDTO);
            model.addAttribute("passwordDTO", passwordDTO);
            model.addAttribute("usuarioRegistroDTO", usuarioRegistroDTO);
            model.addAttribute("cursoDTO", cursoDTO);
            model.addAttribute("rolDTO", rolDTO);
            
            // Listas para modals
            model.addAttribute("categorias", categorias);
            model.addAttribute("docentes", docentes);
            
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
    
    /**
     * Crear nuevo usuario desde el panel de administración
     */    @PostMapping("/usuarios/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuarioRegistroDTO") UsuarioRegistroDTO usuarioRegistroDTO,
                              BindingResult result,
                              @RequestParam("rolId") Integer rolId,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            // Validar que las contraseñas coincidan
            if (!usuarioRegistroDTO.getPassword().equals(usuarioRegistroDTO.getConfirmarPassword())) {
                result.rejectValue("confirmarPassword", "error.password", "Las contraseñas no coinciden");
            }
            
            // Verificar si el email ya existe
            if (usuarioService.existeEmail(usuarioRegistroDTO.getEmail())) {
                result.rejectValue("email", "error.email", "Ya existe un usuario con este email");
            }
            
            // Verificar si el usuario ya existe
            if (usuarioService.existeUsuario(usuarioRegistroDTO.getUsuario())) {
                result.rejectValue("usuario", "error.usuario", "Ya existe un usuario con este nombre de usuario");
            }
            
            if (result.hasErrors()) {
                // Recargar datos necesarios para el dashboard
                model.addAttribute("usuarios", usuarioService.buscarTodos());
                model.addAttribute("cursos", cursoService.buscarTodos());
                model.addAttribute(ROLES_ATTR, rolService.buscarTodos());
                
                // Estadísticas
                model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
                model.addAttribute("totalCursos", cursoService.contarCursos());
                model.addAttribute("totalDocentes", usuarioService.contarUsuariosPorRol("Docente"));
                model.addAttribute("totalEstudiantes", usuarioService.contarUsuariosPorRol("Estudiante"));
                
                model.addAttribute(ERROR_ATTR, "Por favor corrige los errores en el formulario");
                return ADMIN_DASHBOARD;
            }
            
            // Crear el usuario
            boolean usuarioCreado = usuarioService.registrarUsuarioConRol(usuarioRegistroDTO, rolId);
            
            if (usuarioCreado) {
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
                logger.info("Usuario creado exitosamente: {}", usuarioRegistroDTO.getEmail());
            } else {
                redirectAttributes.addFlashAttribute(ERROR_ATTR, "Error al crear el usuario");
                logger.error("Error al crear usuario: {}", usuarioRegistroDTO.getEmail());
            }
            
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Error interno al crear el usuario");
        }        
        return "redirect:/admin/dashboard";
    }

    /**
     * Crear nuevo curso
     */
    @PostMapping("/crear-curso")
    @PreAuthorize("hasRole('Administrador')")
    public String crearCurso(@ModelAttribute("cursoDTO") @Valid CursoDTO cursoDTO,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Authentication authentication,
                           Model model) {
        try {
            logger.info("Iniciando creación de curso por: {}", authentication.getName());
            
            // Validar el formulario
            if (result.hasErrors()) {
                logger.warn("Errores de validación en formulario de curso: {}", result.getAllErrors());
                
                // Recargar datos necesarios para el dashboard
                var usuarios = usuarioService.buscarTodos();
                var cursos = cursoService.buscarTodos();
                var roles = rolService.buscarTodos();
                var categorias = categoriaService.buscarTodos();
                var docentes = usuarioService.buscarDocentesActivos();
                
                // Obtener información del usuario actual
                String email = authentication.getName();
                Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
                
                // Estadísticas
                long totalUsuarios = usuarioService.contarUsuarios();
                long totalCursos = cursoService.contarCursos();
                long totalDocentes = usuarioService.contarUsuariosPorRol("Docente");
                long totalEstudiantes = usuarioService.contarUsuariosPorRol("Estudiante");
                
                // DTOs
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
                UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
                
                // Agregar todo al modelo
                model.addAttribute("usuarioActual", usuarioActual);
                model.addAttribute("totalUsuarios", totalUsuarios);
                model.addAttribute("totalCursos", totalCursos);
                model.addAttribute("totalDocentes", totalDocentes);
                model.addAttribute("totalEstudiantes", totalEstudiantes);
                model.addAttribute("totalInscripciones", 0L);
                model.addAttribute("ingresosMensuales", "$0");
                model.addAttribute("usuarios", usuarios);
                model.addAttribute("cursos", cursos);
                model.addAttribute(ROLES_ATTR, roles);
                model.addAttribute("perfilDTO", perfilDTO);
                model.addAttribute("passwordDTO", passwordDTO);
                model.addAttribute("usuarioRegistroDTO", usuarioRegistroDTO);
                model.addAttribute("categorias", categorias);
                model.addAttribute("docentes", docentes);
                
                model.addAttribute(ERROR_ATTR, "Por favor corrige los errores en el formulario");
                return ADMIN_DASHBOARD;
            }
            
            // Configurar valores por defecto
            if (cursoDTO.getEsGratuito() == null) {
                cursoDTO.setEsGratuito(true);
            }
            
            if (cursoDTO.getEstado() == null || cursoDTO.getEstado().isEmpty()) {
                cursoDTO.setEstado("borrador");
            }
            
            // Si es gratuito, precio debe ser 0
            if (cursoDTO.getEsGratuito() && cursoDTO.getPrecio() != null && cursoDTO.getPrecio().compareTo(java.math.BigDecimal.ZERO) > 0) {
                cursoDTO.setPrecio(java.math.BigDecimal.ZERO);
            }
            
            // Crear el curso
            cursoService.guardar(cursoDTO);
            
            redirectAttributes.addFlashAttribute("mensaje", "Curso creado exitosamente");
            logger.info("Curso creado exitosamente: {}", cursoDTO.getTitulo());
            
        } catch (Exception e) {
            logger.error("Error al crear curso: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Error interno al crear el curso");
        }        
        return "redirect:/admin/dashboard";
    }

    /**
     * Crear nuevo rol
     */
    @PostMapping("/crear-rol")
    @PreAuthorize("hasRole('Administrador')")
    public String crearRol(@ModelAttribute("rolDTO") @Valid RolDTO rolDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Authentication authentication,
                          Model model) {
        try {
            logger.info("Iniciando creación de rol por: {}", authentication.getName());
            
            // Validar el formulario
            if (result.hasErrors()) {
                logger.warn("Errores de validación en formulario de rol: {}", result.getAllErrors());
                
                // Recargar datos necesarios para el dashboard
                var usuarios = usuarioService.buscarTodos();
                var cursos = cursoService.buscarTodos();
                var roles = rolService.buscarTodos();
                var categorias = categoriaService.buscarTodos();
                var docentes = usuarioService.buscarDocentesActivos();
                
                // Obtener información del usuario actual
                String email = authentication.getName();
                Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
                
                // Estadísticas
                long totalUsuarios = usuarioService.contarUsuarios();
                long totalCursos = cursoService.contarCursos();
                long totalDocentes = usuarioService.contarUsuariosPorRol("Docente");
                long totalEstudiantes = usuarioService.contarUsuariosPorRol("Estudiante");
                
                // DTOs
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
                UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
                CursoDTO cursoDTO = new CursoDTO();
                
                // Agregar todo al modelo
                model.addAttribute("usuarioActual", usuarioActual);
                model.addAttribute("totalUsuarios", totalUsuarios);
                model.addAttribute("totalCursos", totalCursos);
                model.addAttribute("totalDocentes", totalDocentes);
                model.addAttribute("totalEstudiantes", totalEstudiantes);
                model.addAttribute("totalInscripciones", 0L);
                model.addAttribute("ingresosMensuales", "$0");
                model.addAttribute("usuarios", usuarios);
                model.addAttribute("cursos", cursos);
                model.addAttribute(ROLES_ATTR, roles);
                model.addAttribute("perfilDTO", perfilDTO);
                model.addAttribute("passwordDTO", passwordDTO);
                model.addAttribute("usuarioRegistroDTO", usuarioRegistroDTO);
                model.addAttribute("cursoDTO", cursoDTO);
                model.addAttribute("categorias", categorias);
                model.addAttribute("docentes", docentes);
                
                model.addAttribute(ERROR_ATTR, "Por favor corrige los errores en el formulario");
                return ADMIN_DASHBOARD;
            }
            
            // Validar que el nombre del rol no exista
            if (rolService.buscarPorNombre(rolDTO.getNombre()).isPresent()) {
                logger.warn("Intento de crear rol con nombre existente: {}", rolDTO.getNombre());
                redirectAttributes.addFlashAttribute(ERROR_ATTR, "Ya existe un rol con ese nombre");
                return "redirect:/admin/dashboard";
            }
            
            // Crear el rol
            rolService.guardar(rolDTO);
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol creado exitosamente");
            logger.info("Rol creado exitosamente: {}", rolDTO.getNombre());
            
        } catch (Exception e) {
            logger.error("Error al crear rol: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Error interno al crear el rol");
        }
        
        return "redirect:/admin/dashboard";
    }
}
