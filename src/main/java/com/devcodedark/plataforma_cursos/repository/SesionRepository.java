package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    
    // Buscar sesión por token
    Optional<Sesion> findByTokenSesion(String tokenSesion);
    
    // Buscar sesiones activas por usuario
    @Query("SELECT s FROM Sesion s WHERE s.usuario.id = :usuarioId AND s.activa = true")
    List<Sesion> findSesionesActivasByUsuario(@Param("usuarioId") Integer usuarioId);
    
    // Buscar sesiones por usuario
    @Query("SELECT s FROM Sesion s WHERE s.usuario.id = :usuarioId")
    List<Sesion> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
    
    // Buscar sesiones expiradas
    @Query("SELECT s FROM Sesion s WHERE s.fechaExpiracion < :now AND s.activa = true")
    List<Sesion> findSesionesExpiradas(@Param("now") LocalDateTime now);
    
    // Buscar sesiones por IP
    @Query("SELECT s FROM Sesion s WHERE s.ipAddress = :ip")
    List<Sesion> findByIpAddress(@Param("ip") String ip);
    
    // Contar sesiones activas por usuario
    @Query("SELECT COUNT(s) FROM Sesion s WHERE s.usuario.id = :usuarioId AND s.activa = true")
    Long countSesionesActivasByUsuario(@Param("usuarioId") Integer usuarioId);
    
    // Buscar sesiones activas en general
    @Query("SELECT s FROM Sesion s WHERE s.activa = true")
    List<Sesion> findSesionesActivas();
    
    // Verificar si token existe
    boolean existsByTokenSesion(String tokenSesion);
    
    // Buscar última sesión del usuario
    @Query("SELECT s FROM Sesion s WHERE s.usuario.id = :usuarioId ORDER BY s.fechaInicio DESC")
    List<Sesion> findUltimaSesionByUsuario(@Param("usuarioId") Integer usuarioId);
    
    // Limpiar sesiones expiradas (para uso en servicios)
    @Query("SELECT s FROM Sesion s WHERE s.fechaExpiracion < :fecha")
    List<Sesion> findSesionesParaLimpiar(@Param("fecha") LocalDateTime fecha);
}