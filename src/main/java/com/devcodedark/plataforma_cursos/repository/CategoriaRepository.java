package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Categoria;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    // Buscar categoría por nombre
    Optional<Categoria> findByNombre(String nombre);
    
    // Buscar categorías por estado
    List<Categoria> findByEstado(EstadoCategoria estado);
    
    // Buscar categorías activas
    @Query("SELECT c FROM Categoria c WHERE c.estado = 'activo'")
    List<Categoria> findAllActivas();
    
    // Verificar si existe categoría por nombre
    boolean existsByNombre(String nombre);
    
    // Contar cursos por categoría
    @Query("SELECT COUNT(cur) FROM Curso cur WHERE cur.categoria.id = :categoriaId")
    Long countCursosByCategoria(@Param("categoriaId") Integer categoriaId);
    
    // Buscar categorías con cursos
    @Query("SELECT DISTINCT c FROM Categoria c JOIN c.cursos cur WHERE cur.estado = 'publicado'")
    List<Categoria> findCategoriasConCursos();
}