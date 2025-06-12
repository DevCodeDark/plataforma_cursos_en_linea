package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.devcodedark.plataforma_cursos.model.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Integer> {
    
    // Buscar módulos por curso ordenados por orden
    @Query("SELECT m FROM Modulo m WHERE m.curso.id = :cursoId ORDER BY m.orden ASC")
    List<Modulo> findByCursoIdOrderByOrden(@Param("cursoId") Integer cursoId);
    
    // Buscar módulos activos por curso
    @Query("SELECT m FROM Modulo m WHERE m.curso.id = :cursoId AND m.estado = 1 ORDER BY m.orden ASC")
    List<Modulo> findModulosActivosByCurso(@Param("cursoId") Integer cursoId);
    
    // Buscar módulos obligatorios por curso
    @Query("SELECT m FROM Modulo m WHERE m.curso.id = :cursoId AND m.esObligatorio = true ORDER BY m.orden ASC")
    List<Modulo> findModulosObligatoriosByCurso(@Param("cursoId") Integer cursoId);
    
    // Contar módulos por curso
    @Query("SELECT COUNT(m) FROM Modulo m WHERE m.curso.id = :cursoId AND m.estado = 1")
    Long countModulosByCurso(@Param("cursoId") Integer cursoId);
    
    // Buscar siguiente orden disponible para un curso
    @Query("SELECT COALESCE(MAX(m.orden), 0) + 1 FROM Modulo m WHERE m.curso.id = :cursoId")
    Integer findNextOrdenByCurso(@Param("cursoId") Integer cursoId);
      
    // Duración total de módulos por curso
    @Query("SELECT COALESCE(SUM(m.duracion), 0) FROM Modulo m WHERE m.curso.id = :cursoId AND m.estado = 1")
    Integer sumDuracionByCurso(@Param("cursoId") Integer cursoId);
    
    // Contar módulos por curso (alias para compatibilidad)
    @Query("SELECT COUNT(m) FROM Modulo m WHERE m.curso.id = :cursoId AND m.estado = 1")
    Long countByCursoId(@Param("cursoId") Integer cursoId);
}