package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.model.Material.TipoMaterial;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    
    // Buscar materiales por módulo ordenados por orden
    @Query("SELECT m FROM Material m WHERE m.modulo.id = :moduloId ORDER BY m.orden ASC")
    List<Material> findByModuloIdOrderByOrden(@Param("moduloId") Integer moduloId);
    
    // Buscar materiales activos por módulo
    @Query("SELECT m FROM Material m WHERE m.modulo.id = :moduloId AND m.estado = 1 ORDER BY m.orden ASC")
    List<Material> findMaterialesActivosByModulo(@Param("moduloId") Integer moduloId);
    
    // Buscar materiales por tipo
    List<Material> findByTipo(TipoMaterial tipo);
    
    // Buscar materiales gratuitos por módulo
    @Query("SELECT m FROM Material m WHERE m.modulo.id = :moduloId AND m.esGratuito = true AND m.estado = 1 ORDER BY m.orden ASC")
    List<Material> findMaterialesGratuitosByModulo(@Param("moduloId") Integer moduloId);
    
    // Buscar materiales por curso (a través del módulo)
    @Query("SELECT m FROM Material m WHERE m.modulo.curso.id = :cursoId AND m.estado = 1 ORDER BY m.modulo.orden, m.orden")
    List<Material> findMaterialesByCurso(@Param("cursoId") Integer cursoId);
    
    // Contar materiales por módulo
    @Query("SELECT COUNT(m) FROM Material m WHERE m.modulo.id = :moduloId AND m.estado = 1")
    Long countMaterialesByModulo(@Param("moduloId") Integer moduloId);
    
    // Buscar siguiente orden disponible para un módulo
    @Query("SELECT COALESCE(MAX(m.orden), 0) + 1 FROM Material m WHERE m.modulo.id = :moduloId")
    Integer findNextOrdenByModulo(@Param("moduloId") Integer moduloId);
    
    // Duración total de videos por módulo
    @Query("SELECT COALESCE(SUM(m.duracion), 0) FROM Material m WHERE m.modulo.id = :moduloId AND m.tipo = 'video' AND m.estado = 1")
    Integer sumDuracionVideosByModulo(@Param("moduloId") Integer moduloId);
}