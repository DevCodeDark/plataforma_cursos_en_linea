package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.model.LogActividad;

public interface ILogActividadService {
    // Listar todos los logs
    List<LogActividad> buscarTodos();
    
    // Guardar log
    void guardar(LogActividad logActividad);
    
    // Modificar log
    void modificar(LogActividad logActividad);
    
    // Buscar log por ID
    Optional<LogActividad> buscarId(Integer id);
    
    // Eliminar log
    void eliminar(Integer id);
    
    // Buscar logs por usuario
    List<LogActividad> buscarPorUsuario(Integer usuarioId);
    
    // Buscar logs por acción
    List<LogActividad> buscarPorAccion(String accion);
    
    // Buscar logs por entidad
    List<LogActividad> buscarPorEntidad(String entidad);
    
    // Buscar logs por entidad y entidadId
    List<LogActividad> buscarPorEntidadYEntidadId(String entidad, Integer entidadId);
    
    // Buscar logs por IP
    List<LogActividad> buscarPorIP(String ip);
    
    // Buscar logs por rango de fechas
    List<LogActividad> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar logs recientes (últimas 24 horas)
    List<LogActividad> buscarLogsRecientes();
    
    // Buscar logs por usuario y acción
    List<LogActividad> buscarPorUsuarioYAccion(Integer usuarioId, String accion);
    
    // Contar logs por acción
    Long contarPorAccion(String accion);
    
    // Contar logs por usuario en un período
    Long contarPorUsuarioEnPeriodo(Integer usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Registrar actividad
    void registrarActividad(Integer usuarioId, String accion, String entidad, Integer entidadId, String detalles);
    
    // Registrar actividad con IP y User Agent
    void registrarActividadCompleta(Integer usuarioId, String accion, String entidad, Integer entidadId, 
                                   String detalles, String ipAddress, String userAgent);
    
    // Limpiar logs antiguos
    void limpiarLogsAntiguos(int diasAntiguedad);
    
    // Obtener estadísticas de actividad
    Object obtenerEstadisticasActividad();
    
    // Obtener actividad reciente por usuario
    List<LogActividad> obtenerActividadRecientePorUsuario(Integer usuarioId, int limite);
    
    // Obtener logs de acceso
    List<LogActividad> obtenerLogsAcceso();
    
    // Obtener logs de errores
    List<LogActividad> obtenerLogsErrores();
}