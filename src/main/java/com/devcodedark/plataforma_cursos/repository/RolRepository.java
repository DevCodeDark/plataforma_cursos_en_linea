package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    
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