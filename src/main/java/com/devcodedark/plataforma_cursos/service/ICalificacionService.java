package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.CalificacionDTO;

public interface ICalificacionService {
    // Listar todas las calificaciones
    List<CalificacionDTO> buscarTodos();
    
    // Guardar calificación
    void guardar(CalificacionDTO calificacionDTO);
    
    // Modificar calificación
    void modificar(CalificacionDTO calificacionDTO);
    
    // Buscar calificación por ID
    Optional<CalificacionDTO> buscarId(Integer id);
    
    // Eliminar calificación
    void eliminar(Integer id);
    
    // Buscar calificaciones por curso
    List<CalificacionDTO> buscarPorCurso(Integer cursoId);
    
    // Buscar calificaciones por estudiante
    List<CalificacionDTO> buscarPorEstudiante(Integer estudianteId);
    
    // Buscar calificación específica
    Optional<CalificacionDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Calcular promedio de calificaciones por curso
    Double calcularPromedioCalificacionesPorCurso(Integer cursoId);
    
    // Contar calificaciones por puntuación y curso
    Long contarPorPuntuacionYCurso(Integer cursoId, Integer puntuacion);
    
    // Buscar calificaciones por rango de puntuación
    List<CalificacionDTO> buscarPorRangoPuntuacion(Integer min, Integer max);
    
    // Verificar si estudiante ya calificó el curso
    boolean yaCalificoElCurso(Integer estudianteId, Integer cursoId);
    
    // Contar total de calificaciones por curso
    Long contarCalificacionesPorCurso(Integer cursoId);
    
    // Calificar curso
    void calificarCurso(Integer estudianteId, Integer cursoId, Integer puntuacion, String comentario);
    
    // Obtener estadísticas de calificaciones por curso
    Object obtenerEstadisticasPorCurso(Integer cursoId);
}