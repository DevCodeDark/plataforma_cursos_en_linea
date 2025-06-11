package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Rol;

public interface IRolService {
    // Listar todos los roles
    List<Rol> buscarTodos();
    
    // Guardar rol
    void guardar(Rol rol);
    
    // Modificar rol
    void modificar(Rol rol);
    
    // Buscar rol por ID
    Optional<Rol> buscarId(Integer id);
    
    // Eliminar rol
    void eliminar(Integer id);
    
    // Buscar rol por nombre
    Optional<Rol> buscarPorNombre(String nombre);
    
    // Buscar roles activos
    List<Rol> buscarRolesActivos();
    
    // Verificar si existe rol por nombre
    boolean existePorNombre(String nombre);
    
    // Buscar roles por estado
    List<Rol> buscarPorEstado(Integer estado);
}