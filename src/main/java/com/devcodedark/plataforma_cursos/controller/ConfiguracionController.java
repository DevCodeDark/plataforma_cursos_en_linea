package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.ConfiguracionSistemaDTO;
import com.devcodedark.plataforma_cursos.service.ConfiguracionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Controlador para la gestión de configuraciones del sistema
 * Solo accesible por administradores
 */
@Controller
@RequestMapping("/admin/configuracion")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ConfiguracionController {

    private static final String REDIRECT_CONFIGURACION = "redirect:/admin/configuracion/sistema";
    private static final String ERROR_CONFIGURACION = "errorConfiguracion";
    private static final String SUCCESS_CONFIGURACION = "successConfiguracion";
    private static final String ERROR_INTERNO = "Error interno: ";
    
    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }    /**
     * Muestra la página principal de configuración
     */
    @GetMapping("/sistema")
    public String mostrarConfiguracion(Model model) {try {
            // Obtener todas las configuraciones del sistema
            Map<String, Map<String, Object>> configuraciones = configuracionService.obtenerTodasLasConfiguraciones();
            
            // Crear DTO para la configuración del sistema
            ConfiguracionSistemaDTO configuracionDTO = configuracionService.obtenerConfiguracionSistema();
            
            model.addAttribute("configuraciones", configuraciones);
            model.addAttribute("configuracionDTO", configuracionDTO);
            model.addAttribute("categorias", configuracionService.obtenerCategorias());
            
            return "admin/configuracion";
        } catch (Exception e) {
            model.addAttribute("error", ERROR_INTERNO + e.getMessage());
            return "error/500";
        }
    }

    /**
     * Actualiza la configuración del sistema
     */
    @PostMapping("/sistema")
    public String actualizarConfiguracionSistema(@ModelAttribute ConfiguracionSistemaDTO configuracionDTO,
                                                  BindingResult result,
                                                  RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Por favor, corrige los errores en el formulario");
                return REDIRECT_CONFIGURACION;
            }

            boolean actualizado = configuracionService.actualizarConfiguracionSistema(configuracionDTO);

            if (actualizado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Configuración del sistema actualizada correctamente");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al actualizar la configuración del sistema");
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Actualiza una configuración específica
     */
    @PostMapping("/actualizar")
    public String actualizarConfiguracion(@RequestParam String clave,
                                          @RequestParam String valor,
                                          @RequestParam(required = false) String descripcion,
                                          RedirectAttributes redirectAttributes) {
        try {
            boolean actualizado = configuracionService.actualizarConfiguracion(clave, valor, descripcion);

            if (actualizado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Configuración actualizada: " + clave);
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al actualizar la configuración: " + clave);
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Crea una nueva configuración
     */
    @PostMapping("/crear")
    public String crearConfiguracion(@RequestParam String categoria,
                                     @RequestParam String clave,
                                     @RequestParam String valor,
                                     @RequestParam String tipo,
                                     @RequestParam(required = false) String descripcion,
                                     RedirectAttributes redirectAttributes) {
        try {
            boolean creado = configuracionService.crearConfiguracion(categoria, clave, valor, tipo, descripcion);

            if (creado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Nueva configuración creada: " + clave);
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al crear la configuración: " + clave);
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Elimina una configuración
     */
    @PostMapping("/eliminar")
    public String eliminarConfiguracion(@RequestParam String clave,
                                        RedirectAttributes redirectAttributes) {
        try {
            boolean eliminado = configuracionService.eliminarConfiguracion(clave);

            if (eliminado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Configuración eliminada: " + clave);
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al eliminar la configuración: " + clave);
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Reinicia las configuraciones a valores por defecto
     */
    @PostMapping("/reiniciar")
    public String reiniciarConfiguraciones(RedirectAttributes redirectAttributes) {
        try {
            boolean reiniciado = configuracionService.reiniciarConfiguraciones();

            if (reiniciado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Configuraciones reiniciadas a valores por defecto");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al reiniciar las configuraciones");
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Exporta las configuraciones
     */
    @GetMapping("/exportar")
    @ResponseBody
    public Map<String, Object> exportarConfiguraciones() {
        try {
            return configuracionService.exportarConfiguraciones();
        } catch (Exception e) {
            return Map.of("error", ERROR_INTERNO + e.getMessage());
        }
    }

    /**
     * Importa configuraciones desde JSON
     */
    @PostMapping("/importar")
    public String importarConfiguraciones(@RequestParam String configuracionesJson,
                                          RedirectAttributes redirectAttributes) {
        try {
            boolean importado = configuracionService.importarConfiguraciones(configuracionesJson);

            if (importado) {
                redirectAttributes.addFlashAttribute(SUCCESS_CONFIGURACION, "Configuraciones importadas correctamente");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, "Error al importar las configuraciones");
            }

            return REDIRECT_CONFIGURACION;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_CONFIGURACION, ERROR_INTERNO + e.getMessage());
            return REDIRECT_CONFIGURACION;
        }
    }

    /**
     * Obtiene configuraciones de una categoría específica
     */
    @GetMapping("/categoria/{categoria}")
    @ResponseBody
    public Map<String, Object> obtenerConfiguracionesCategoria(@PathVariable String categoria) {
        try {
            Map<String, Object> configuraciones = configuracionService.obtenerPorCategoria(categoria.toUpperCase())
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                    com.devcodedark.plataforma_cursos.model.Configuracion::getClave,
                    com.devcodedark.plataforma_cursos.model.Configuracion::getValorTipado
                ));
            return configuraciones;
        } catch (Exception e) {
            return Map.of("error", ERROR_INTERNO + e.getMessage());
        }
    }

    /**
     * Guarda configuraciones de una categoría específica
     */
    @PostMapping("/categoria/{categoria}")
    @ResponseBody
    public Map<String, Object> guardarConfiguracionesCategoria(@PathVariable String categoria,
                                                               @RequestBody Map<String, String> configuraciones) {
        try {
            boolean todosActualizados = true;
            StringBuilder errores = new StringBuilder();

            for (Map.Entry<String, String> entry : configuraciones.entrySet()) {
                try {
                    boolean actualizado = configuracionService.actualizarConfiguracion(entry.getKey(), entry.getValue(), null);
                    if (!actualizado) {
                        todosActualizados = false;
                        errores.append("Error al actualizar: ").append(entry.getKey()).append("; ");
                    }
                } catch (Exception e) {
                    todosActualizados = false;
                    errores.append(entry.getKey()).append(": ").append(e.getMessage()).append("; ");
                }
            }

            if (todosActualizados) {
                return Map.of("success", true, "message", "Configuraciones guardadas correctamente");
            } else {
                return Map.of("success", false, "message", "Algunos errores: " + errores.toString());
            }
        } catch (Exception e) {
            return Map.of("success", false, "message", ERROR_INTERNO + e.getMessage());
        }
    }

    /**
     * Resetea configuraciones de una categoría específica
     */
    @PostMapping("/resetear/{categoria}")
    @ResponseBody
    public Map<String, Object> resetearCategoria(@PathVariable String categoria) {
        try {
            boolean reseteado = configuracionService.resetearCategoria(categoria.toUpperCase());
            
            if (reseteado) {
                return Map.of("success", true, "message", "Categoría reseteada correctamente");
            } else {
                return Map.of("success", false, "message", "Error al resetear la categoría");
            }
        } catch (Exception e) {
            return Map.of("success", false, "message", ERROR_INTERNO + e.getMessage());
        }
    }
}
