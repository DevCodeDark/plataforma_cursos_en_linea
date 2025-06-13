package com.devcodedark.plataforma_cursos.service;

import com.devcodedark.plataforma_cursos.dto.ConfiguracionSistemaDTO;
import com.devcodedark.plataforma_cursos.model.Configuracion;
import com.devcodedark.plataforma_cursos.repository.ConfiguracionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de configuraciones del sistema
 */
@Service
@Transactional
public class ConfiguracionService {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Constantes para configuraciones del sistema
    private static final String CATEGORIA_SISTEMA = "SISTEMA";
    private static final String CATEGORIA_APARIENCIA = "APARIENCIA";
    private static final String CATEGORIA_NOTIFICACIONES = "NOTIFICACIONES";
    private static final String CATEGORIA_ARCHIVOS = "ARCHIVOS";
    private static final String CATEGORIA_LIMITES = "LIMITES";

    /**
     * Inicializa las configuraciones por defecto del sistema
     */
    @Transactional
    public void inicializarConfiguracionesPorDefecto() {
        if (configuracionRepository.count() == 0) {
            crearConfiguracionesPorDefecto();
        }
    }

    /**
     * Crea las configuraciones por defecto
     */
    private void crearConfiguracionesPorDefecto() {
        List<Configuracion> configuraciones = Arrays.asList(
            // Configuraciones del sistema
            new Configuracion("sitio.nombre", "Plataforma de Cursos", "Plataforma de Cursos", CATEGORIA_SISTEMA, "STRING", "Nombre del sitio web"),
            new Configuracion("sitio.descripcion", "Plataforma educativa online", "Plataforma educativa online", CATEGORIA_SISTEMA, "STRING", "Descripción del sitio web"),
            new Configuracion("sitio.email_contacto", "admin@plataforma.com", "admin@plataforma.com", CATEGORIA_SISTEMA, "STRING", "Email de contacto principal"),
            new Configuracion("sitio.telefono_contacto", "+1234567890", "+1234567890", CATEGORIA_SISTEMA, "STRING", "Teléfono de contacto"),
            new Configuracion("sitio.direccion", "", "", CATEGORIA_SISTEMA, "STRING", "Dirección física"),
            new Configuracion("sistema.registro_publico", "true", "true", CATEGORIA_SISTEMA, "BOOLEAN", "Permitir registro público de usuarios"),
            new Configuracion("sistema.modo_mantenimiento", "false", "false", CATEGORIA_SISTEMA, "BOOLEAN", "Activar modo mantenimiento"),
            new Configuracion("sistema.log_actividades", "true", "true", CATEGORIA_SISTEMA, "BOOLEAN", "Registrar log de actividades"),
            new Configuracion("sistema.idioma_defecto", "es", "es", CATEGORIA_SISTEMA, "STRING", "Idioma por defecto del sistema"),
            new Configuracion("sistema.zona_horaria", "America/Lima", "America/Lima", CATEGORIA_SISTEMA, "STRING", "Zona horaria del sistema"),
            new Configuracion("sistema.formato_fecha", "dd/MM/yyyy", "dd/MM/yyyy", CATEGORIA_SISTEMA, "STRING", "Formato de fecha"),

            // Límites del sistema
            new Configuracion("limites.usuarios_max", "1000", "1000", CATEGORIA_LIMITES, "INTEGER", "Número máximo de usuarios"),
            new Configuracion("limites.cursos_max", "100", "100", CATEGORIA_LIMITES, "INTEGER", "Número máximo de cursos"),
            new Configuracion("archivos.tamano_maximo", "50", "50", CATEGORIA_ARCHIVOS, "INTEGER", "Tamaño máximo de archivo en MB"),
            new Configuracion("archivos.tipos_permitidos", "jpg,jpeg,png,gif,pdf,doc,docx,ppt,pptx,xls,xlsx", "jpg,jpeg,png,gif,pdf,doc,docx,ppt,pptx,xls,xlsx", CATEGORIA_ARCHIVOS, "STRING", "Tipos de archivos permitidos"),

            // Apariencia
            new Configuracion("apariencia.color_primario", "#007bff", "#007bff", CATEGORIA_APARIENCIA, "STRING", "Color primario del tema"),
            new Configuracion("apariencia.color_secundario", "#6c757d", "#6c757d", CATEGORIA_APARIENCIA, "STRING", "Color secundario del tema"),
            new Configuracion("apariencia.color_acento", "#28a745", "#28a745", CATEGORIA_APARIENCIA, "STRING", "Color de acento"),
            new Configuracion("apariencia.logo_url", "", "", CATEGORIA_APARIENCIA, "STRING", "URL del logo"),
            new Configuracion("apariencia.favicon_url", "", "", CATEGORIA_APARIENCIA, "STRING", "URL del favicon"),

            // Notificaciones
            new Configuracion("notificaciones.email_habilitado", "true", "true", CATEGORIA_NOTIFICACIONES, "BOOLEAN", "Habilitar notificaciones por email"),
            new Configuracion("notificaciones.smtp_servidor", "", "", CATEGORIA_NOTIFICACIONES, "STRING", "Servidor SMTP"),
            new Configuracion("notificaciones.smtp_puerto", "587", "587", CATEGORIA_NOTIFICACIONES, "INTEGER", "Puerto SMTP"),
            new Configuracion("notificaciones.smtp_usuario", "", "", CATEGORIA_NOTIFICACIONES, "STRING", "Usuario SMTP"),
            new Configuracion("notificaciones.smtp_password", "", "", CATEGORIA_NOTIFICACIONES, "STRING", "Contraseña SMTP"),
            new Configuracion("notificaciones.smtp_ssl", "true", "true", CATEGORIA_NOTIFICACIONES, "BOOLEAN", "Usar SSL para SMTP")
        );

        configuracionRepository.saveAll(configuraciones);
    }    /**
     * Obtiene todas las configuraciones agrupadas por categoría
     */
    @Transactional(readOnly = true)
    public Map<String, Map<String, Object>> obtenerTodasLasConfiguraciones() {
        List<Configuracion> configuraciones = configuracionRepository.findAllVisibles();
        return configuraciones.stream()
            .collect(Collectors.groupingBy(
                Configuracion::getCategoria,
                LinkedHashMap::new,
                Collectors.toMap(
                    Configuracion::getClave,
                    Configuracion::getValorTipado,
                    (v1, v2) -> v1,
                    LinkedHashMap::new
                )
            ));
    }

    /**
     * Obtiene una configuración específica por clave
     */
    @Transactional(readOnly = true)
    public Optional<Configuracion> obtenerConfiguracion(String clave) {
        return configuracionRepository.findByClave(clave);
    }

    /**
     * Obtiene el valor de una configuración
     */
    @Transactional(readOnly = true)
    public String obtenerValor(String clave) {
        return configuracionRepository.findByClave(clave)
            .map(Configuracion::getValor)
            .orElse(null);
    }

    /**
     * Obtiene el valor tipado de una configuración
     */
    @Transactional(readOnly = true)
    public Object obtenerValorTipado(String clave) {
        return configuracionRepository.findByClave(clave)
            .map(Configuracion::getValorTipado)
            .orElse(null);
    }

    /**
     * Obtiene todas las categorías disponibles
     */
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        return configuracionRepository.findDistinctCategorias();
    }

    /**
     * Obtiene configuraciones por categoría
     */
    @Transactional(readOnly = true)
    public List<Configuracion> obtenerPorCategoria(String categoria) {
        return configuracionRepository.findByCategoriaAndEsVisibleTrue(categoria);
    }

    /**
     * Actualiza una configuración específica
     */
    @Transactional
    public boolean actualizarConfiguracion(String clave, String valor, String descripcion) {
        try {
            Optional<Configuracion> configOpt = configuracionRepository.findByClave(clave);
            if (configOpt.isPresent()) {
                Configuracion config = configOpt.get();
                if (config.getEsModificable()) {
                    config.setValor(valor);
                    if (descripcion != null && !descripcion.trim().isEmpty()) {
                        config.setDescripcion(descripcion);
                    }
                    configuracionRepository.save(config);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar configuración: " + e.getMessage(), e);
        }
    }

    /**
     * Crea una nueva configuración
     */
    @Transactional
    public boolean crearConfiguracion(String categoria, String clave, String valor, String tipo, String descripcion) {
        try {
            if (configuracionRepository.existsByClave(clave)) {
                throw new RuntimeException("Ya existe una configuración con la clave: " + clave);
            }

            Configuracion config = new Configuracion(clave, valor, valor, categoria, tipo.toUpperCase(), descripcion);
            configuracionRepository.save(config);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear configuración: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una configuración
     */
    @Transactional
    public boolean eliminarConfiguracion(String clave) {
        try {
            Optional<Configuracion> configOpt = configuracionRepository.findByClave(clave);
            if (configOpt.isPresent()) {
                Configuracion config = configOpt.get();
                if (config.getEsModificable()) {
                    configuracionRepository.delete(config);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar configuración: " + e.getMessage(), e);
        }
    }

    /**
     * Reinicia todas las configuraciones a valores por defecto
     */
    @Transactional
    public boolean reiniciarConfiguraciones() {
        try {
            List<Configuracion> configuraciones = configuracionRepository.findAllModificables();
            configuraciones.forEach(config -> {
                if (config.getValorPorDefecto() != null) {
                    config.setValor(config.getValorPorDefecto());
                    config.setFechaActualizacion(LocalDateTime.now());
                }
            });
            configuracionRepository.saveAll(configuraciones);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al reiniciar configuraciones: " + e.getMessage(), e);
        }
    }

    /**
     * Resetea las configuraciones de una categoría específica
     */
    @Transactional
    public boolean resetearCategoria(String categoria) {
        try {
            List<Configuracion> configuraciones = configuracionRepository.findByCategoriaAndEsModificableTrue(categoria);
            configuraciones.forEach(config -> {
                if (config.getValorPorDefecto() != null) {
                    config.setValor(config.getValorPorDefecto());
                    config.setFechaActualizacion(LocalDateTime.now());
                }
            });
            configuracionRepository.saveAll(configuraciones);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al resetear configuraciones de la categoría: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la configuración del sistema como DTO
     */
    @Transactional(readOnly = true)
    public ConfiguracionSistemaDTO obtenerConfiguracionSistema() {
        ConfiguracionSistemaDTO dto = new ConfiguracionSistemaDTO();
        
        dto.setNombreSitio(obtenerValor("sitio.nombre"));
        dto.setDescripcionSitio(obtenerValor("sitio.descripcion"));
        dto.setEmailContacto(obtenerValor("sitio.email_contacto"));
        dto.setTelefonoContacto(obtenerValor("sitio.telefono_contacto"));
        dto.setDireccion(obtenerValor("sitio.direccion"));
        dto.setUrlLogo(obtenerValor("apariencia.logo_url"));
        dto.setUrlFavicon(obtenerValor("apariencia.favicon_url"));
        
        dto.setLimiteUsuarios((Integer) obtenerValorTipado("limites.usuarios_max"));
        dto.setLimiteCursos((Integer) obtenerValorTipado("limites.cursos_max"));
        dto.setTamajoMaximoArchivo((Integer) obtenerValorTipado("archivos.tamano_maximo"));
        
        dto.setRegistroPublico((Boolean) obtenerValorTipado("sistema.registro_publico"));
        dto.setNotificacionesEmail((Boolean) obtenerValorTipado("notificaciones.email_habilitado"));
        dto.setModoMantenimiento((Boolean) obtenerValorTipado("sistema.modo_mantenimiento"));
        dto.setLogActividades((Boolean) obtenerValorTipado("sistema.log_actividades"));
        
        dto.setColorPrimario(obtenerValor("apariencia.color_primario"));
        dto.setColorSecundario(obtenerValor("apariencia.color_secundario"));
        dto.setColorAccento(obtenerValor("apariencia.color_acento"));
        
        dto.setIdiomaPorDefecto(obtenerValor("sistema.idioma_defecto"));
        dto.setZonaHoraria(obtenerValor("sistema.zona_horaria"));
        dto.setFormatoFecha(obtenerValor("sistema.formato_fecha"));
        
        return dto;
    }

    /**
     * Actualiza la configuración del sistema desde DTO
     */
    @Transactional
    public boolean actualizarConfiguracionSistema(ConfiguracionSistemaDTO dto) {
        try {
            actualizarConfiguracion("sitio.nombre", dto.getNombreSitio(), null);
            actualizarConfiguracion("sitio.descripcion", dto.getDescripcionSitio(), null);
            actualizarConfiguracion("sitio.email_contacto", dto.getEmailContacto(), null);
            actualizarConfiguracion("sitio.telefono_contacto", dto.getTelefonoContacto(), null);
            
            if (dto.getDireccion() != null) {
                actualizarConfiguracion("sitio.direccion", dto.getDireccion(), null);
            }
            if (dto.getUrlLogo() != null) {
                actualizarConfiguracion("apariencia.logo_url", dto.getUrlLogo(), null);
            }
            if (dto.getUrlFavicon() != null) {
                actualizarConfiguracion("apariencia.favicon_url", dto.getUrlFavicon(), null);
            }
            
            actualizarConfiguracion("limites.usuarios_max", dto.getLimiteUsuarios().toString(), null);
            actualizarConfiguracion("limites.cursos_max", dto.getLimiteCursos().toString(), null);
            actualizarConfiguracion("archivos.tamano_maximo", dto.getTamajoMaximoArchivo().toString(), null);
            
            if (dto.getRegistroPublico() != null) {
                actualizarConfiguracion("sistema.registro_publico", dto.getRegistroPublico().toString(), null);
            }
            if (dto.getNotificacionesEmail() != null) {
                actualizarConfiguracion("notificaciones.email_habilitado", dto.getNotificacionesEmail().toString(), null);
            }
            if (dto.getModoMantenimiento() != null) {
                actualizarConfiguracion("sistema.modo_mantenimiento", dto.getModoMantenimiento().toString(), null);
            }
            if (dto.getLogActividades() != null) {
                actualizarConfiguracion("sistema.log_actividades", dto.getLogActividades().toString(), null);
            }
            
            if (dto.getColorPrimario() != null) {
                actualizarConfiguracion("apariencia.color_primario", dto.getColorPrimario(), null);
            }
            if (dto.getColorSecundario() != null) {
                actualizarConfiguracion("apariencia.color_secundario", dto.getColorSecundario(), null);
            }
            if (dto.getColorAccento() != null) {
                actualizarConfiguracion("apariencia.color_acento", dto.getColorAccento(), null);
            }
            
            if (dto.getIdiomaPorDefecto() != null) {
                actualizarConfiguracion("sistema.idioma_defecto", dto.getIdiomaPorDefecto(), null);
            }
            if (dto.getZonaHoraria() != null) {
                actualizarConfiguracion("sistema.zona_horaria", dto.getZonaHoraria(), null);
            }
            if (dto.getFormatoFecha() != null) {
                actualizarConfiguracion("sistema.formato_fecha", dto.getFormatoFecha(), null);
            }
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar configuración del sistema: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta todas las configuraciones como JSON
     */
    @Transactional(readOnly = true)
    public Map<String, Object> exportarConfiguraciones() {
        try {
            Map<String, Object> export = new HashMap<>();
            export.put("timestamp", LocalDateTime.now());
            export.put("configuraciones", obtenerTodasLasConfiguraciones());
            return export;
        } catch (Exception e) {
            throw new RuntimeException("Error al exportar configuraciones: " + e.getMessage(), e);
        }
    }

    /**
     * Importa configuraciones desde JSON
     */
    @Transactional
    public boolean importarConfiguraciones(String json) {
        try {
            Map<String, Object> importData = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            
            if (importData.containsKey("configuraciones")) {
                @SuppressWarnings("unchecked")
                Map<String, Map<String, Object>> configuraciones = (Map<String, Map<String, Object>>) importData.get("configuraciones");
                
                for (Map.Entry<String, Map<String, Object>> categoria : configuraciones.entrySet()) {
                    for (Map.Entry<String, Object> config : categoria.getValue().entrySet()) {
                        String clave = config.getKey();
                        String valor = config.getValue() != null ? config.getValue().toString() : null;
                        actualizarConfiguracion(clave, valor, null);
                    }
                }
                return true;
            }
            return false;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar JSON de configuraciones: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al importar configuraciones: " + e.getMessage(), e);
        }
    }

    /**
     * Busca configuraciones por texto
     */
    @Transactional(readOnly = true)
    public List<Configuracion> buscarConfiguraciones(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return configuracionRepository.findAllVisibles();
        }
        return configuracionRepository.buscarPorClaveODescripcion(busqueda.trim());
    }
}
