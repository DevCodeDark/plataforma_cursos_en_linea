package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.UsuarioDTO;

public interface IUsuarioService {
    // Métodos CRUD básicos con DTO
    List<UsuarioDTO> buscarTodos();
    void guardar(UsuarioDTO usuarioDTO);
    void modificar(UsuarioDTO usuarioDTO);
    Optional<UsuarioDTO> buscarId(Integer id);
    void eliminar(Integer id);
    
    // Búsquedas específicas
    Optional<UsuarioDTO> buscarPorEmail(String email);
    Optional<UsuarioDTO> buscarPorUsuario(String usuario);
    List<UsuarioDTO> buscarPorRol(Integer rolId);
    List<UsuarioDTO> buscarPorEstado(String estado);
    List<UsuarioDTO> buscarDocentesActivos();
    List<UsuarioDTO> buscarEstudiantesActivos();
    Optional<UsuarioDTO> buscarPorTokenRecuperacion(String token);
    
    // Validaciones
    boolean existePorEmail(String email);
    boolean existePorUsuario(String usuario);
    
    // Operaciones de negocio
    void cambiarContrasena(Integer usuarioId, String nuevaContrasena);
    void cambiarEstado(Integer usuarioId, String estado);
    
    // Métodos adicionales para estadísticas
    Integer contarCursosCreados(Integer usuarioId);
    Integer contarInscripciones(Integer usuarioId);
    Double calcularPromedioCalificaciones(Integer usuarioId);
    Integer calcularDiasSinAcceso(Integer usuarioId);
    Boolean puedeCrearCursos(Integer usuarioId);
}