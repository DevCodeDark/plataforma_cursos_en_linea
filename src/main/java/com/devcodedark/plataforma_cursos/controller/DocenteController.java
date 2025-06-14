package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.CursoDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.security.CustomUserDetailsService;
import com.devcodedark.plataforma_cursos.service.ICategoriaService;
import com.devcodedark.plataforma_cursos.service.ICursoService;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/docente")
@PreAuthorize("hasRole('DOCENTE') or hasRole('ADMINISTRADOR') or hasRole('ADMIN')")
public class DocenteController {
    
    private static final Logger logger = LoggerFactory.getLogger(DocenteController.class);
    
    // Constantes
    private static final String DOCENTE_DASHBOARD = "docente/dashboard";
    private static final String ERROR_ATTR = "error";
    private static final String ERROR_500 = "error/500";
    
    private final IUsuarioService usuarioService;
    private final ICursoService cursoService;
    private final ICategoriaService categoriaService;
    private final CustomUserDetailsService userDetailsService;

    public DocenteController(IUsuarioService usuarioService, 
                           ICursoService cursoService,
                           ICategoriaService categoriaService,
                           CustomUserDetailsService userDetailsService) {
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
        this.categoriaService = categoriaService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Dashboard principal del docente
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        try {
            // Obtener información del usuario actual
            String email = authentication.getName();
            Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
            
            // Estadísticas del docente
            var misCursos = cursoService.buscarPorDocente(usuarioActual.getId());
            long totalMisCursos = misCursos.size();
            long cursosPublicados = misCursos.stream().filter(curso -> "publicado".equals(curso.getEstado())).count();
            long cursosBorrador = misCursos.stream().filter(curso -> "borrador".equals(curso.getEstado())).count();
            
            // Calcular total de estudiantes en mis cursos
            long totalEstudiantes = misCursos.stream()
                .mapToLong(curso -> curso.getTotalInscripciones() != null ? curso.getTotalInscripciones() : 0)
                .sum();
            
            // Obtener categorías para el modal de curso
            var categorias = categoriaService.buscarTodos();
            
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
            CursoDTO cursoDTO = new CursoDTO();
            
            // Agregar datos al modelo
            model.addAttribute("usuarioActual", usuarioActual);
            model.addAttribute("totalMisCursos", totalMisCursos);
            model.addAttribute("cursosPublicados", cursosPublicados);
            model.addAttribute("cursosBorrador", cursosBorrador);
            model.addAttribute("totalEstudiantes", totalEstudiantes);
            
            // Listas para tablas
            model.addAttribute("misCursos", misCursos);
            model.addAttribute("categorias", categorias);
            
            // DTOs para formularios
            model.addAttribute("perfilDTO", perfilDTO);
            model.addAttribute("passwordDTO", passwordDTO);
            model.addAttribute("cursoDTO", cursoDTO);
            
            logger.info("Acceso al dashboard docente por: {}", email);
            
            return DOCENTE_DASHBOARD;
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard docente: {}", e.getMessage(), e);
            model.addAttribute(ERROR_ATTR, "Error al cargar el dashboard");
            return ERROR_500;
        }
    }

    /**
     * Crear nuevo curso como docente
     */
    @PostMapping("/crear-curso")
    public String crearCurso(@ModelAttribute("cursoDTO") @Valid CursoDTO cursoDTO,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Authentication authentication,
                           Model model) {
        try {
            logger.info("Iniciando creación de curso por docente: {}", authentication.getName());
            
            // Obtener el docente actual
            String email = authentication.getName();
            Usuario usuarioActual = userDetailsService.getUsuarioByEmail(email);
            
            // Asignar el docente actual al curso
            cursoDTO.setDocenteId(usuarioActual.getId());
            
            // Validar el formulario
            if (result.hasErrors()) {
                logger.warn("Errores de validación en formulario de curso: {}", result.getAllErrors());
                
                // Recargar datos necesarios para el dashboard
                var misCursos = cursoService.buscarPorDocente(usuarioActual.getId());
                var categorias = categoriaService.buscarTodos();
                
                // Estadísticas
                long totalMisCursos = misCursos.size();
                long cursosPublicados = misCursos.stream().filter(curso -> "publicado".equals(curso.getEstado())).count();
                long cursosBorrador = misCursos.stream().filter(curso -> "borrador".equals(curso.getEstado())).count();
                long totalEstudiantes = misCursos.stream()
                    .mapToLong(curso -> curso.getTotalInscripciones() != null ? curso.getTotalInscripciones() : 0)
                    .sum();
                
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
                
                // Agregar todo al modelo
                model.addAttribute("usuarioActual", usuarioActual);
                model.addAttribute("totalMisCursos", totalMisCursos);
                model.addAttribute("cursosPublicados", cursosPublicados);
                model.addAttribute("cursosBorrador", cursosBorrador);
                model.addAttribute("totalEstudiantes", totalEstudiantes);
                model.addAttribute("misCursos", misCursos);
                model.addAttribute("categorias", categorias);
                model.addAttribute("perfilDTO", perfilDTO);
                model.addAttribute("passwordDTO", passwordDTO);
                
                model.addAttribute(ERROR_ATTR, "Por favor corrige los errores en el formulario");
                return DOCENTE_DASHBOARD;
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
            logger.info("Curso creado exitosamente por docente: {}", cursoDTO.getTitulo());
            
        } catch (Exception e) {
            logger.error("Error al crear curso: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Error interno al crear el curso");
        }
        
        return "redirect:/docente/dashboard";
    }
}
