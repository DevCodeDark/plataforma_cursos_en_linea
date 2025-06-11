package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Categoria;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;

public interface ICategoriaService {
    // Listar todas las categorías
    List<Categoria> buscarTodos();
    
    // Guardar categoría
    void guardar(Categoria categoria);
    
    // Modificar categoría
    void modificar(Categoria categoria);
    
    // Buscar categoría por ID
    Optional<Categoria> buscarId(Integer id);
    
    // Eliminar categoría
    void eliminar(Integer id);
    
    // Buscar categoría por nombre
    Optional<Categoria> buscarPorNombre(String nombre);
    
    // Buscar categorías por estado
    List<Categoria> buscarPorEstado(EstadoCategoria estado);
    
    // Buscar categorías activas
    List<Categoria> buscarCategoriasActivas();
    
    // Verificar si existe categoría por nombre
    boolean existePorNombre(String nombre);
    
    // Contar cursos por categoría
    Long contarCursosPorCategoria(Integer categoriaId);
    
    // Buscar categorías con cursos
    List<Categoria> buscarCategoriasConCursos();
    
    // Cambiar estado de categoría
    void cambiarEstado(Integer categoriaId, EstadoCategoria estado);
}