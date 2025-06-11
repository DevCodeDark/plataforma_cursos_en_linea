package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.model.LogActividad;

public interface LogActividadRepository extends JpaRepository<LogActividad, Integer> {
    
    // Buscar logs por usuario
    @Query("SELECT l FROM LogActividad l WHERE l.usuario.id = :usuarioId ORDER BY l.fechaAccion DESC")
    List<LogActividad> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
    
    // Buscar logs por acción
    List<LogActividad> findByAccion(String accion);
    
    // Buscar logs por entidad
    List<LogActividad> findByEntidad(String entidad);
    
    // Buscar logs por entidad y entidadId
    @Query("SELECT l FROM LogActividad l WHERE l.entidad = :entidad AND l.entidadId = :entidadId ORDER BY l.fechaAccion DESC")
    List<LogActividad> findByEntidadAndEntidadId(@Param("entidad") String entidad, @Param("entidadId") Integer entidadId);
    
    // Buscar logs por IP
    @Query("SELECT l FROM LogActividad l WHERE l.ipAddress = :ip")
    List<LogActividad> findByIpAddress(@Param("ip") String ip);
    
    // Buscar logs por rango de fechas
    @Query("SELECT l FROM LogActividad l WHERE l.fechaAccion BETWEEN :fechaInicio AND :fechaFin ORDER BY l.fechaAccion DESC")
    List<LogActividad> findByFechaAccionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar logs recientes (últimas 24 horas)
    @Query("SELECT l FROM LogActividad l WHERE l.fechaAccion >= :fecha ORDER BY l.fechaAccion DESC")
    List<LogActividad> findLogsRecientes(@Param("fecha") LocalDateTime fecha);
    
    // Buscar logs por usuario y acción
    @Query("SELECT l FROM LogActividad l WHERE l.usuario.id = :usuarioId AND l.accion = :accion ORDER BY l.fechaAccion DESC")
    List<LogActividad> findByUsuarioIdAndAccion(@Param("usuarioId") Integer usuarioId, @Param("accion") String accion);
    
    // Contar logs por acción
    @Query("SELECT COUNT(l) FROM LogActividad l WHERE l.accion = :accion")
    Long countByAccion(@Param("accion") String accion);
    
    // Contar logs por usuario en un período
    @Query("SELECT COUNT(l) FROM LogActividad l WHERE l.usuario.id = :usuarioId AND l.fechaAccion BETWEEN :fechaInicio AND :fechaFin")
    Long countByUsuarioIdAndFechaAccionBetween(@Param("usuarioId") Integer usuarioId, 
                                              @Param("fechaInicio") LocalDateTime fechaInicio, 
                                              @Param("fechaFin") LocalDateTime fechaFin);
}