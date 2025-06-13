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

    // Verificar si existe usuario por email o nombre de usuario
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email OR u.usuario = :usuario")
    boolean existsByEmailOrUsuario(@Param("email") String email, @Param("usuario") String usuario);

    // Buscar por token de recuperación
    Optional<Usuario> findByTokenRecuperacion(String token);    // Buscar usuarios recientes (últimos 30 días)
    @Query(value = "SELECT * FROM usuarios u WHERE u.fecha_creacion >= DATE_SUB(NOW(), INTERVAL 30 DAY)", nativeQuery = true)
    List<Usuario> findUsuariosRecientes();
    
    // Buscar usuarios con último acceso en los últimos N días
    @Query(value = "SELECT * FROM usuarios u WHERE u.ultimo_acceso >= DATE_SUB(NOW(), INTERVAL :dias DAY)", nativeQuery = true)
    List<Usuario> findUsuariosConAccesoReciente(@Param("dias") int dias);
    
    // Buscar usuarios sin acceso por más de N días
    @Query(value = "SELECT * FROM usuarios u WHERE u.ultimo_acceso < DATE_SUB(NOW(), INTERVAL :dias DAY) OR u.ultimo_acceso IS NULL", nativeQuery = true)
    List<Usuario> findUsuariosSinAcceso(@Param("dias") int dias);    // Buscar usuario por email (sin filtro de estado para DataInitializer) - Consulta nativa
    @Query(value = "SELECT * FROM usuarios WHERE email = :email", nativeQuery = true)
    Optional<Usuario> findUserByEmail(@Param("email") String email);

    // Buscar usuario por nombre de usuario (sin filtro de estado para DataInitializer) - Consulta nativa
    @Query(value = "SELECT * FROM usuarios WHERE usuario = :usuario", nativeQuery = true)
    Optional<Usuario> findUserByUsuario(@Param("usuario") String usuario);
}