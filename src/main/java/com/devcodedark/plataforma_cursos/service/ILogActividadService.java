package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.dto.LogActividadDTO;

public interface ILogActividadService {
    // Métodos CRUD básicos con DTO
    List<LogActividadDTO> buscarTodos();
    void guardar(LogActividadDTO logActividadDTO);
    void modificar(LogActividadDTO logActividadDTO);
    Optional<LogActividadDTO> buscarId(Integer id);
    void eliminar(Integer id);
    
    // Búsquedas específicas
    List<LogActividadDTO> buscarPorUsuario(Integer usuarioId);
    List<LogActividadDTO> buscarPorAccion(String accion);
    List<LogActividadDTO> buscarPorEntidad(String entidad);
    List<LogActividadDTO> buscarPorEntidadYEntidadId(String entidad, Integer entidadId);
    List<LogActividadDTO> buscarPorIP(String ip);
    List<LogActividadDTO> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<LogActividadDTO> buscarLogsRecientes();
    List<LogActividadDTO> buscarPorUsuarioYAccion(Integer usuarioId, String accion);
    List<LogActividadDTO> buscarPorCategoriaAccion(String categoria);
    List<LogActividadDTO> buscarAccionesCriticas();
    
    // Operaciones estadísticas
    Long contarPorAccion(String accion);
    Long contarPorUsuarioEnPeriodo(Integer usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Long contarAccionesCriticas();
    Long contarPorCategoria(String categoria);
    
    // Registrar actividad
    void registrarActividad(Integer usuarioId, String accion, String entidad, Integer entidadId, String detalles);
    void registrarActividadCompleta(Integer usuarioId, String accion, String entidad, Integer entidadId, 
                                   String detalles, String ipAddress, String userAgent);
    
    // Operaciones de mantenimiento
    void limpiarLogsAntiguos(int diasAntiguedad);
    Object obtenerEstadisticasActividad();
    List<LogActividadDTO> obtenerActividadRecientePorUsuario(Integer usuarioId, int limite);
    List<LogActividadDTO> obtenerLogsAcceso();
    List<LogActividadDTO> obtenerLogsErrores();
}