package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.ProgresoModulo;

public interface ProgresoModuloRepository extends JpaRepository<ProgresoModulo, Integer> {
    
    // Buscar progreso por inscripción
    @Query("SELECT pm FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId")
    List<ProgresoModulo> findByInscripcionId(@Param("inscripcionId") Integer inscripcionId);
    
    // Buscar progreso específico de inscripción y módulo
    @Query("SELECT pm FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId AND pm.modulo.id = :moduloId")
    Optional<ProgresoModulo> findByInscripcionIdAndModuloId(@Param("inscripcionId") Integer inscripcionId, 
                                                           @Param("moduloId") Integer moduloId);
    
    // Buscar módulos completados por inscripción
    @Query("SELECT pm FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId AND pm.completado = true")
    List<ProgresoModulo> findModulosCompletadosByInscripcion(@Param("inscripcionId") Integer inscripcionId);
    
    // Contar módulos completados por inscripción
    @Query("SELECT COUNT(pm) FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId AND pm.completado = true")
    Long countModulosCompletadosByInscripcion(@Param("inscripcionId") Integer inscripcionId);
    
    // Calcular tiempo total invertido por inscripción
    @Query("SELECT COALESCE(SUM(pm.tiempoInvertido), 0) FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId")
    Long sumTiempoInvertidoByInscripcion(@Param("inscripcionId") Integer inscripcionId);
    
    // Buscar progreso por estudiante y curso
    @Query("SELECT pm FROM ProgresoModulo pm WHERE pm.inscripcion.estudiante.id = :estudianteId AND pm.modulo.curso.id = :cursoId")
    List<ProgresoModulo> findByEstudianteIdAndCursoId(@Param("estudianteId") Integer estudianteId, 
                                                     @Param("cursoId") Integer cursoId);
    
    // Verificar si existe progreso para inscripción y módulo
    @Query("SELECT COUNT(pm) > 0 FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId AND pm.modulo.id = :moduloId")
    boolean existsByInscripcionIdAndModuloId(@Param("inscripcionId") Integer inscripcionId, 
                                            @Param("moduloId") Integer moduloId);
    
    // Buscar último módulo accedido por inscripción
    @Query("SELECT pm FROM ProgresoModulo pm WHERE pm.inscripcion.id = :inscripcionId AND pm.fechaInicio IS NOT NULL ORDER BY pm.fechaInicio DESC")
    List<ProgresoModulo> findUltimoModuloAccedidoByInscripcion(@Param("inscripcionId") Integer inscripcionId);
}