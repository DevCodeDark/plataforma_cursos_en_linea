package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;

public interface IUsuarioService {
    // Listar todos los usuarios
    List<Usuario> buscarTodos();
    
    // Guardar usuario
    void guardar(Usuario usuario);
    
    // Modificar usuario
    void modificar(Usuario usuario);
    
    // Buscar usuario por ID
    Optional<Usuario> buscarId(Integer id);
    
    // Eliminar usuario
    void eliminar(Integer id);
    
    // Buscar usuario por email
    Optional<Usuario> buscarPorEmail(String email);
    
    // Buscar usuario por nombre de usuario
    Optional<Usuario> buscarPorUsuario(String usuario);
    
    // Buscar usuarios por rol
    List<Usuario> buscarPorRol(Integer rolId);
    
    // Buscar usuarios por estado
    List<Usuario> buscarPorEstado(EstadoUsuario estado);
    
    // Buscar docentes activos
    List<Usuario> buscarDocentesActivos();
    
    // Buscar estudiantes activos
    List<Usuario> buscarEstudiantesActivos();
    
    // Verificar si existe email
    boolean existePorEmail(String email);
    
    // Verificar si existe usuario
    boolean existePorUsuario(String usuario);
    
    // Buscar por token de recuperación
    Optional<Usuario> buscarPorTokenRecuperacion(String token);
    
    // Cambiar contraseña
    void cambiarContrasena(Integer usuarioId, String nuevaContrasena);
    
    // Activar/desactivar usuario
    void cambiarEstado(Integer usuarioId, EstadoUsuario estado);
}