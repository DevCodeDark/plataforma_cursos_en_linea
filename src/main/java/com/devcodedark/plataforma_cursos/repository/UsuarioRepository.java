package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsuario(String usuario);
    
    // Buscar usuario por email o username (para login)
    @Query("SELECT u FROM Usuario u WHERE u.email = :identifier OR u.usuario = :identifier")
    Optional<Usuario> findByEmailOrUsuario(@Param("identifier") String email, @Param("identifier") String usuario);

    // Buscar usuarios por rol
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = :rolId")
    List<Usuario> findByRolId(@Param("rolId") Integer rolId);
    
    // Buscar usuarios por nombre de rol
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rolNombre")
    List<Usuario> findByRolNombre(@Param("rolNombre") String rolNombre);

    // Contar usuarios por rol
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.id = :rolId")
    Long countByRolId(@Param("rolId") Integer rolId);
    
    // Contar usuarios por nombre de rol
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.nombre = :rolNombre")
    Long countByRolNombre(@Param("rolNombre") String rolNombre);

    // Buscar usuarios por estado
    List<Usuario> findByEstado(EstadoUsuario estado);

    // Buscar docentes activos
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'Docente' AND u.estado = 'activo'")
    List<Usuario> findDocentesActivos();

    // Buscar estudiantes activos
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'Estudiante' AND u.estado = 'activo'")
    List<Usuario> findEstudiantesActivos();

    // Verificar si existe email
    boolean existsByEmail(String email);

    // Verificar si existe usuario
    boolean existsByUsuario(String usuario);

    // Buscar por token de recuperación
    Optional<Usuario> findByTokenRecuperacion(String token);
    
    // Buscar usuarios recientes (últimos 30 días)
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= CURRENT_DATE - 30")
    List<Usuario> findUsuariosRecientes();
    
    // Buscar usuarios con último acceso en los últimos N días
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcceso >= CURRENT_DATE - :dias")
    List<Usuario> findUsuariosConAccesoReciente(@Param("dias") int dias);
    
    // Buscar usuarios sin acceso por más de N días
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcceso < CURRENT_DATE - :dias OR u.ultimoAcceso IS NULL")
    List<Usuario> findUsuariosSinAcceso(@Param("dias") int dias);
}