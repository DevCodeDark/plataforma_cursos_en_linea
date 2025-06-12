package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.LogActividadDTO;
import com.devcodedark.plataforma_cursos.exception.UsuarioNoEncontradoException;
import com.devcodedark.plataforma_cursos.model.LogActividad;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.repository.LogActividadRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.ILogActividadService;

@Service
@Transactional
public class LogActividadServiceJpa implements ILogActividadService {    
    private final LogActividadRepository logActividadRepository;
    private final UsuarioRepository usuarioRepository;
    
    private static final String DESCONOCIDO = "Desconocido";
    private static final String HACE = "Hace ";
    private static final String OTROS = "Otros";
    
    public LogActividadServiceJpa(LogActividadRepository logActividadRepository,
                                 UsuarioRepository usuarioRepository) {
        this.logActividadRepository = logActividadRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    // Métodos de conversión entre Entity y DTO
    private LogActividadDTO convertToDTO(LogActividad logActividad) {
        if (logActividad == null) {
            return null;
        }
        
        LogActividadDTO dto = new LogActividadDTO();
        dto.setId(logActividad.getId());
        dto.setAccion(logActividad.getAccion());
        dto.setEntidad(logActividad.getEntidad());
        dto.setEntidadId(logActividad.getEntidadId());
        dto.setDetalles(logActividad.getDetalles());
        dto.setIpAddress(logActividad.getIpAddress());
        dto.setUserAgent(logActividad.getUserAgent());
        dto.setFechaAccion(logActividad.getFechaAccion());
        
        // Información del usuario
        if (logActividad.getUsuario() != null) {
            dto.setUsuarioId(logActividad.getUsuario().getId());
            dto.setUsuarioNombre(logActividad.getUsuario().getUsuario());
            dto.setUsuarioEmail(logActividad.getUsuario().getEmail());
            dto.setUsuarioNombreCompleto(logActividad.getUsuario().getNombre() + " " + 
                                        logActividad.getUsuario().getApellido());
        }
        
        // Campos calculados adicionales
        if (logActividad.getFechaAccion() != null) {
            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(logActividad.getFechaAccion()));
            dto.setEsAccionReciente(logActividad.getFechaAccion().isAfter(LocalDateTime.now().minusHours(24)));
        }
        
        if (logActividad.getUserAgent() != null) {
            dto.setNavegador(extraerNavegador(logActividad.getUserAgent()));
            dto.setSistemaOperativo(extraerSistemaOperativo(logActividad.getUserAgent()));
        }
        
        return dto;
    }
    
    private LogActividad convertToEntity(LogActividadDTO dto) {
        if (dto == null) {
            return null;
        }
        
        LogActividad logActividad = new LogActividad();
        logActividad.setId(dto.getId());
        logActividad.setAccion(dto.getAccion());
        logActividad.setEntidad(dto.getEntidad());
        logActividad.setEntidadId(dto.getEntidadId());
        logActividad.setDetalles(dto.getDetalles());
        logActividad.setIpAddress(dto.getIpAddress());
        logActividad.setUserAgent(dto.getUserAgent());
        logActividad.setFechaAccion(dto.getFechaAccion() != null ? dto.getFechaAccion() : LocalDateTime.now());
        
        // Cargar usuario
        if (dto.getUsuarioId() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuarioId());
            if (usuario.isPresent()) {
                logActividad.setUsuario(usuario.get());
            }
        }
        
        return logActividad;
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarTodos() {
        return logActividadRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    public void guardar(LogActividadDTO logActividadDTO) {
        LogActividad logActividad = convertToEntity(logActividadDTO);
        if (logActividad != null) {
            logActividadRepository.save(logActividad);
        }
    }

    @Override
    public void modificar(LogActividadDTO logActividadDTO) {
        if (logActividadDTO != null && logActividadDTO.getId() != null) {
            Optional<LogActividad> existente = logActividadRepository.findById(logActividadDTO.getId());
            if (existente.isPresent()) {
                LogActividad logActividad = convertToEntity(logActividadDTO);
                if (logActividad != null) {
                    logActividadRepository.save(logActividad);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LogActividadDTO> buscarId(Integer id) {
        return logActividadRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        logActividadRepository.deleteById(id);
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorUsuario(Integer usuarioId) {
        return logActividadRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorAccion(String accion) {
        return logActividadRepository.findByAccion(accion)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorEntidad(String entidad) {
        return logActividadRepository.findByEntidad(entidad)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorEntidadYEntidadId(String entidad, Integer entidadId) {
        return logActividadRepository.findByEntidadAndEntidadId(entidad, entidadId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorIP(String ip) {
        return logActividadRepository.findByIpAddress(ip)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return logActividadRepository.findByFechaAccionBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarLogsRecientes() {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        return logActividadRepository.findLogsRecientes(hace24Horas)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorUsuarioYAccion(Integer usuarioId, String accion) {
        return logActividadRepository.findByUsuarioIdAndAccion(usuarioId, accion)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarPorCategoriaAccion(String categoria) {
        // Implementar lógica para buscar por categoría basada en patrones de acción
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> determinarCategoriaAccion(log.getAccion()).equals(categoria))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> buscarAccionesCriticas() {
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> determinarSiEsCritica(log.getAccion()))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPorAccion(String accion) {
        return logActividadRepository.countByAccion(accion);
    }    
    
    @Override
    @Transactional(readOnly = true)
    public Long contarPorUsuarioEnPeriodo(Integer usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return logActividadRepository.countByUsuarioIdAndFechaAccionBetween(usuarioId, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAccionesCriticas() {
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> determinarSiEsCritica(log.getAccion()))
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPorCategoria(String categoria) {
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> determinarCategoriaAccion(log.getAccion()).equals(categoria))
                .count();
    }    
    
    @Override
    public void registrarActividad(Integer usuarioId, String accion, String entidad, Integer entidadId, String detalles) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        
        LogActividad log = new LogActividad();
        log.setUsuario(usuarioOpt.get());
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setEntidadId(entidadId);
        log.setDetalles(detalles);
        log.setFechaAccion(LocalDateTime.now());
        
        logActividadRepository.save(log);
    }

    @Override
    public void registrarActividadCompleta(Integer usuarioId, String accion, String entidad, Integer entidadId, 
                                         String detalles, String ipAddress, String userAgent) {        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        
        LogActividad log = new LogActividad();
        log.setUsuario(usuarioOpt.get());
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setEntidadId(entidadId);
        log.setDetalles(detalles);
        log.setFechaAccion(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        
        logActividadRepository.save(log);
    }    
    
    @Override
    public void limpiarLogsAntiguos(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        List<LogActividad> logsAntiguos = logActividadRepository.findAll().stream()
                .filter(log -> log.getFechaAccion().isBefore(fechaLimite))
                .toList();
        
        for (LogActividad log : logsAntiguos) {
            logActividadRepository.delete(log);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerEstadisticasActividad() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<LogActividad> todosLosLogs = logActividadRepository.findAll();
        estadisticas.put("totalLogs", todosLosLogs.size());
        
        // Top 5 acciones más frecuentes
        Map<String, Long> accionesFrecuentes = todosLosLogs.stream()
                .collect(Collectors.groupingBy(LogActividad::getAccion, Collectors.counting()));
        estadisticas.put("accionesFrecuentes", accionesFrecuentes);
        
        // Logs por categoría
        Map<String, Long> logsPorCategoria = todosLosLogs.stream()
                .collect(Collectors.groupingBy(
                    log -> determinarCategoriaAccion(log.getAccion()),
                    Collectors.counting()
                ));
        estadisticas.put("logsPorCategoria", logsPorCategoria);
        
        return estadisticas;
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> obtenerActividadRecientePorUsuario(Integer usuarioId, int limite) {
        return logActividadRepository.findByUsuarioId(usuarioId)
                .stream()
                .sorted((a, b) -> b.getFechaAccion().compareTo(a.getFechaAccion()))
                .limit(limite)
                .map(this::convertToDTO)
                .toList();
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> obtenerLogsAcceso() {
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> log.getAccion().toLowerCase().contains("login") || 
                              log.getAccion().toLowerCase().contains("acceso"))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividadDTO> obtenerLogsErrores() {
        return logActividadRepository.findAll()
                .stream()
                .filter(log -> log.getAccion().toLowerCase().contains("error") || 
                              log.getAccion().toLowerCase().contains("fallo"))
                .map(this::convertToDTO)
                .toList();
    }
      
    // Métodos helper
    private String calcularTiempoTranscurrido(LocalDateTime fechaAccion) {
        if (fechaAccion == null) return DESCONOCIDO;
        
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = Duration.between(fechaAccion, ahora).toMinutes();
        
        if (minutos < 1) {
            return "Hace menos de 1 minuto";
        } else if (minutos < 60) {
            return HACE + minutos + " minuto" + (minutos > 1 ? "s" : "");
        } else if (minutos < 1440) { // 24 horas
            long horas = minutos / 60;
            return HACE + horas + " hora" + (horas > 1 ? "s" : "");
        } else {
            long dias = minutos / 1440;
            return HACE + dias + " día" + (dias > 1 ? "s" : "");
        }
    }
    
    private String extraerNavegador(String userAgent) {
        if (userAgent == null) return DESCONOCIDO;
        
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Opera")) return "Opera";
        return OTROS;
    }
    
    private String extraerSistemaOperativo(String userAgent) {
        if (userAgent == null) return DESCONOCIDO;
        
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return OTROS;
    }
    
    private String determinarCategoriaAccion(String accion) {
        if (accion == null) return "Sin categoría";
        
        String accionLower = accion.toLowerCase();
        if (accionLower.contains("login") || accionLower.contains("logout") || accionLower.contains("acceso")) {
            return "Autenticación";
        } else if (accionLower.contains("crear") || accionLower.contains("guardar")) {
            return "Creación";
        } else if (accionLower.contains("modificar") || accionLower.contains("actualizar") || accionLower.contains("editar")) {
            return "Modificación";
        } else if (accionLower.contains("eliminar") || accionLower.contains("borrar")) {
            return "Eliminación";        } else if (accionLower.contains("consultar") || accionLower.contains("buscar") || accionLower.contains("ver")) {
            return "Consulta";
        } else {
            return OTROS;
        }
    }
    
    private Boolean determinarSiEsCritica(String accion) {
        if (accion == null) return false;
        
        String accionLower = accion.toLowerCase();
        return accionLower.contains("eliminar") || 
               accionLower.contains("borrar") || 
               accionLower.contains("cambiar_contrasena") ||
               accionLower.contains("cambiar_estado") ||
               accionLower.contains("admin");
    }
}