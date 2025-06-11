package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Inscripcion.EstadoInscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    // Buscar inscripciones por estudiante
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId")
    List<Inscripcion> findByEstudianteId(@Param("estudianteId") Integer estudianteId);
    
    // Buscar inscripciones por curso
    @Query("SELECT i FROM Inscripcion i WHERE i.curso.id = :cursoId")
    List<Inscripcion> findByCursoId(@Param("cursoId") Integer cursoId);
    
    // Buscar inscripción específica de estudiante en curso
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.curso.id = :cursoId")
    Optional<Inscripcion> findByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                                      @Param("cursoId") Integer cursoId);
    
    // Buscar inscripciones por estado
    List<Inscripcion> findByEstado(EstadoInscripcion estado);
    
    // Buscar inscripciones activas por estudiante
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.estado = 'activo'")
    List<Inscripcion> findInscripcionesActivasByEstudiante(@Param("estudianteId") Integer estudianteId);
    
    // Buscar inscripciones completadas por estudiante
    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.estado = 'finalizado'")
    List<Inscripcion> findInscripcionesCompletadasByEstudiante(@Param("estudianteId") Integer estudianteId);
    
    // Contar inscripciones por curso
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id = :cursoId")
    Long countInscripcionesByCurso(@Param("cursoId") Integer cursoId);
    
    // Verificar si estudiante está inscrito en curso
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.curso.id = :cursoId")
    boolean existsByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                          @Param("cursoId") Integer cursoId);
    
    // Inscripciones con certificado generado
    @Query("SELECT i FROM Inscripcion i WHERE i.certificadoGenerado = true")
    List<Inscripcion> findInscripcionesConCertificado();
}