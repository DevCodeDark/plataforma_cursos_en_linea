package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.RolDTO;

public interface IRolService {
    // Listar todos los roles
    List<RolDTO> buscarTodos();
    
    // Guardar rol
    void guardar(RolDTO rolDTO);
    
    // Modificar rol
    void modificar(RolDTO rolDTO);
    
    // Buscar rol por ID
    Optional<RolDTO> buscarId(Integer id);
    
    // Eliminar rol
    void eliminar(Integer id);
    
    // Buscar rol por nombre
    Optional<RolDTO> buscarPorNombre(String nombre);
    
    // Buscar roles activos
    List<RolDTO> buscarRolesActivos();
    
    // Verificar si existe rol por nombre
    boolean existePorNombre(String nombre);
    
    // Buscar roles por estado
    List<RolDTO> buscarPorEstado(Integer estado);
}