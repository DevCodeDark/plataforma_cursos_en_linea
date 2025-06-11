package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.LogActividad;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.repository.LogActividadRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.ILogActividadService;

@Service
@Transactional
public class LogActividadServiceJpa implements ILogActividadService {

    @Autowired
    private LogActividadRepository logActividadRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarTodos() {
        return logActividadRepository.findAll();
    }

    @Override
    public void guardar(LogActividad logActividad) {
        logActividadRepository.save(logActividad);
    }

    @Override
    public void modificar(LogActividad logActividad) {
        logActividadRepository.save(logActividad);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LogActividad> buscarId(Integer id) {
        return logActividadRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        logActividadRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorUsuario(Integer usuarioId) {
        return logActividadRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorAccion(String accion) {
        return logActividadRepository.findByAccion(accion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorEntidad(String entidad) {
        return logActividadRepository.findByEntidad(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorEntidadYEntidadId(String entidad, Integer entidadId) {
        return logActividadRepository.findByEntidadAndEntidadId(entidad, entidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorIP(String ip) {
        return logActividadRepository.findByIpAddress(ip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return logActividadRepository.findByFechaAccionBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarLogsRecientes() {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        return logActividadRepository.findLogsRecientes(hace24Horas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> buscarPorUsuarioYAccion(Integer usuarioId, String accion) {
        return logActividadRepository.findByUsuarioIdAndAccion(usuarioId, accion);
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
    public void registrarActividad(Integer usuarioId, String accion, String entidad, Integer entidadId, String detalles) {
        registrarActividadCompleta(usuarioId, accion, entidad, entidadId, detalles, null, null);
    }

    @Override
    public void registrarActividadCompleta(Integer usuarioId, String accion, String entidad, Integer entidadId, 
                                         String detalles, String ipAddress, String userAgent) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
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
        
        // Actividad de las últimas 24 horas
        List<LogActividad> actividadReciente = buscarLogsRecientes();
        estadisticas.put("actividadUltimas24Horas", actividadReciente.size());
        
        // Top 5 acciones más frecuentes
        Map<String, Long> accionesFrecuentes = new HashMap<>();
        List<String> acciones = List.of("LOGIN", "LOGOUT", "CREAR_CURSO", "INSCRIPCION", "COMPLETAR_MODULO");
        for (String accion : acciones) {
            Long count = contarPorAccion(accion);
            accionesFrecuentes.put(accion, count);
        }
        estadisticas.put("accionesFrecuentes", accionesFrecuentes);
        
        // Usuarios más activos (últimos 7 días)
        LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
        Map<String, Long> usuariosActivos = new HashMap<>();
        
        // Esta sería una consulta más compleja en una implementación real
        estadisticas.put("usuariosActivosUltimos7Dias", usuariosActivos);
        
        return estadisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> obtenerActividadRecientePorUsuario(Integer usuarioId, int limite) {
        List<LogActividad> actividad = buscarPorUsuario(usuarioId);
        return actividad.stream()
            .sorted((a, b) -> b.getFechaAccion().compareTo(a.getFechaAccion()))
            .limit(limite)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> obtenerLogsAcceso() {
        List<String> accionesAcceso = List.of("LOGIN", "LOGOUT", "ACCESO_CURSO", "ACCESO_MATERIAL");
        return logActividadRepository.findAll().stream()
            .filter(log -> accionesAcceso.contains(log.getAccion()))
            .sorted((a, b) -> b.getFechaAccion().compareTo(a.getFechaAccion()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogActividad> obtenerLogsErrores() {
        List<String> accionesError = List.of("ERROR", "EXCEPCION", "FALLO_PAGO", "ERROR_AUTENTICACION");
        return logActividadRepository.findAll().stream()
            .filter(log -> accionesError.contains(log.getAccion()))
            .sorted((a, b) -> b.getFechaAccion().compareTo(a.getFechaAccion()))
            .toList();
    }
}