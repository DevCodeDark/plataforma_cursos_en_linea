package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.ProgresoMaterial;

public interface ProgresoMaterialRepository extends JpaRepository<ProgresoMaterial, Integer> {

    // Buscar progreso por inscripción
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId")
    List<ProgresoMaterial> findByInscripcionId(@Param("inscripcionId") Integer inscripcionId);

    // Buscar progreso específico de inscripción y material
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.material.id = :materialId")
    Optional<ProgresoMaterial> findByInscripcionIdAndMaterialId(@Param("inscripcionId") Integer inscripcionId,
            @Param("materialId") Integer materialId);

    // Buscar materiales visualizados por inscripción
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.visualizado = true")
    List<ProgresoMaterial> findMaterialesVisualizadosByInscripcion(@Param("inscripcionId") Integer inscripcionId);

    // Buscar materiales completados por inscripción
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.completado = true")
    List<ProgresoMaterial> findMaterialesCompletadosByInscripcion(@Param("inscripcionId") Integer inscripcionId);

    // Contar materiales completados por inscripción
    @Query("SELECT COUNT(pm) FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.completado = true")
    Long countMaterialesCompletadosByInscripcion(@Param("inscripcionId") Integer inscripcionId);

    // Buscar progreso por módulo específico
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.material.modulo.id = :moduloId")
    List<ProgresoMaterial> findByInscripcionIdAndModuloId(@Param("inscripcionId") Integer inscripcionId,
            @Param("moduloId") Integer moduloId);

    // Calcular tiempo total reproducido por inscripción
    @Query("SELECT COALESCE(SUM(pm.tiempoReproducido), 0) FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId")
    Long sumTiempoReproducidoByInscripcion(@Param("inscripcionId") Integer inscripcionId);

    // Verificar si existe progreso para inscripción y material
    @Query("SELECT COUNT(pm) > 0 FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.material.id = :materialId")
    boolean existsByInscripcionIdAndMaterialId(@Param("inscripcionId") Integer inscripcionId,
            @Param("materialId") Integer materialId);

    // Buscar último material accedido por inscripción
    @Query("SELECT pm FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.fechaUltimoAcceso IS NOT NULL ORDER BY pm.fechaUltimoAcceso DESC")
    List<ProgresoMaterial> findUltimoMaterialAccedidoByInscripcion(@Param("inscripcionId") Integer inscripcionId);

    // Contar materiales completados por inscripción y módulo
    @Query("SELECT COUNT(pm) FROM ProgresoMaterial pm WHERE pm.inscripcion.id = :inscripcionId AND pm.material.modulo.id = :moduloId AND pm.completado = true")
    Long countMaterialesCompletadosByInscripcionAndModulo(@Param("inscripcionId") Integer inscripcionId,
            @Param("moduloId") Integer moduloId);
}