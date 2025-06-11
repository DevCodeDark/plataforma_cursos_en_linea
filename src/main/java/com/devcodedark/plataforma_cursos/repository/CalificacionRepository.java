package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    
    // Buscar calificaciones por curso
    @Query("SELECT c FROM Calificacion c WHERE c.curso.id = :cursoId")
    List<Calificacion> findByCursoId(@Param("cursoId") Integer cursoId);
    
    // Buscar calificaciones por estudiante
    @Query("SELECT c FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    List<Calificacion> findByEstudianteId(@Param("estudianteId") Integer estudianteId);
    
    // Buscar calificación específica de estudiante para curso
    @Query("SELECT c FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.curso.id = :cursoId")
    Optional<Calificacion> findByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                                       @Param("cursoId") Integer cursoId);
    
    // Calcular promedio de calificaciones por curso
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.curso.id = :cursoId")
    Double calcularPromedioCalificacionesByCurso(@Param("cursoId") Integer cursoId);
    
    // Contar calificaciones por puntuación y curso
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.curso.id = :cursoId AND c.puntuacion = :puntuacion")
    Long countByPuntuacionAndCurso(@Param("cursoId") Integer cursoId, @Param("puntuacion") Integer puntuacion);
    
    // Buscar calificaciones por rango de puntuación
    @Query("SELECT c FROM Calificacion c WHERE c.puntuacion BETWEEN :min AND :max")
    List<Calificacion> findByPuntuacionBetween(@Param("min") Integer min, @Param("max") Integer max);
    
    // Verificar si estudiante ya calificó el curso
    @Query("SELECT COUNT(c) > 0 FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.curso.id = :cursoId")
    boolean existsByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                          @Param("cursoId") Integer cursoId);
    
    // Contar total de calificaciones por curso
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.curso.id = :cursoId")
    Long countCalificacionesByCurso(@Param("cursoId") Integer cursoId);
}