package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {    // Buscar rol por nombre (sin filtro de estado para DataInitializer) - Consulta nativa
    @Query(value = "SELECT * FROM roles WHERE nombre = :nombre", nativeQuery = true)
    Optional<Rol> findRoleByName(@Param("nombre") String nombre);
    
    // Actualizar estado de roles a activo (para corregir datos existentes)
    @Modifying
    @Transactional
    @Query(value = "UPDATE roles SET estado = 1, fecha_actualizacion = NOW() WHERE estado IS NULL OR estado != 1", nativeQuery = true)
    int updateRolesToActive();
    
    // Buscar rol por nombre
    Optional<Rol> findByNombre(String nombre);
    
    // Buscar roles activos
    @Query("SELECT r FROM Rol r WHERE r.estado = 1")
    List<Rol> findAllActivos();
    
    // Verificar si existe un rol por nombre
    boolean existsByNombre(String nombre);
    
    // Buscar roles por estado
    @Query("SELECT r FROM Rol r WHERE r.estado = :estado")
    List<Rol> findByEstado(@Param("estado") Integer estado);
}