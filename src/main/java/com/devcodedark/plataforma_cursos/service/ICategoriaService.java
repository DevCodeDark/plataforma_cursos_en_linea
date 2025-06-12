package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.CategoriaDTO;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;

public interface ICategoriaService {
    // Listar todas las categorías
    List<CategoriaDTO> buscarTodos();
    
    // Guardar categoría
    void guardar(CategoriaDTO categoriaDTO);
    
    // Modificar categoría
    void modificar(CategoriaDTO categoriaDTO);
    
    // Buscar categoría por ID
    Optional<CategoriaDTO> buscarId(Integer id);
    
    // Eliminar categoría
    void eliminar(Integer id);
    
    // Buscar categoría por nombre
    Optional<CategoriaDTO> buscarPorNombre(String nombre);
    
    // Buscar categorías por estado
    List<CategoriaDTO> buscarPorEstado(EstadoCategoria estado);
    
    // Buscar categorías activas
    List<CategoriaDTO> buscarCategoriasActivas();
    
    // Verificar si existe categoría por nombre
    boolean existePorNombre(String nombre);
    
    // Contar cursos por categoría
    Long contarCursosPorCategoria(Integer categoriaId);
    
    // Buscar categorías con cursos
    List<CategoriaDTO> buscarCategoriasConCursos();
    
    // Cambiar estado de categoría
    void cambiarEstado(Integer categoriaId, EstadoCategoria estado);
}