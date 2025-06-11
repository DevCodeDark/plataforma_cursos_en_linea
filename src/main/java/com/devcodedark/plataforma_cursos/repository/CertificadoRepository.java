package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Certificado;

public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {
    
    // Buscar certificado por código de verificación
    Optional<Certificado> findByCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificado por inscripción
    @Query("SELECT c FROM Certificado c WHERE c.inscripcion.id = :inscripcionId")
    Optional<Certificado> findByInscripcionId(@Param("inscripcionId") Integer inscripcionId);
    
    // Buscar certificados por estudiante
    @Query("SELECT c FROM Certificado c WHERE c.inscripcion.estudiante.id = :estudianteId AND c.valido = true")
    List<Certificado> findByEstudianteId(@Param("estudianteId") Integer estudianteId);
    
    // Buscar certificados por curso
    @Query("SELECT c FROM Certificado c WHERE c.inscripcion.curso.id = :cursoId AND c.valido = true")
    List<Certificado> findByCursoId(@Param("cursoId") Integer cursoId);
    
    // Buscar certificados válidos
    @Query("SELECT c FROM Certificado c WHERE c.valido = true")
    List<Certificado> findCertificadosValidos();
    
    // Contar certificados por curso
    @Query("SELECT COUNT(c) FROM Certificado c WHERE c.inscripcion.curso.id = :cursoId AND c.valido = true")
    Long countCertificadosByCurso(@Param("cursoId") Integer cursoId);
    
    // Verificar si existe certificado para inscripción
    @Query("SELECT COUNT(c) > 0 FROM Certificado c WHERE c.inscripcion.id = :inscripcionId")
    boolean existsByInscripcionId(@Param("inscripcionId") Integer inscripcionId);
    
    // Verificar si código de verificación existe
    boolean existsByCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificados por estudiante y curso específico
    @Query("SELECT c FROM Certificado c WHERE c.inscripcion.estudiante.id = :estudianteId AND c.inscripcion.curso.id = :cursoId AND c.valido = true")
    Optional<Certificado> findByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                                      @Param("cursoId") Integer cursoId);
}