package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.model.Sesion;

public interface ISesionService {
    // Listar todas las sesiones
    List<Sesion> buscarTodos();
    
    // Guardar sesión
    void guardar(Sesion sesion);
    
    // Modificar sesión
    void modificar(Sesion sesion);
    
    // Buscar sesión por ID
    Optional<Sesion> buscarId(Integer id);
    
    // Eliminar sesión
    void eliminar(Integer id);
    
    // Buscar sesión por token
    Optional<Sesion> buscarPorToken(String tokenSesion);
    
    // Buscar sesiones activas por usuario
    List<Sesion> buscarSesionesActivasPorUsuario(Integer usuarioId);
    
    // Buscar sesiones por usuario
    List<Sesion> buscarPorUsuario(Integer usuarioId);
    
    // Buscar sesiones expiradas
    List<Sesion> buscarSesionesExpiradas();
    
    // Buscar sesiones por IP
    List<Sesion> buscarPorIP(String ip);
    
    // Contar sesiones activas por usuario
    Long contarSesionesActivasPorUsuario(Integer usuarioId);
    
    // Buscar sesiones activas
    List<Sesion> buscarSesionesActivas();
    
    // Verificar si token existe
    boolean existeToken(String tokenSesion);
    
    // Buscar última sesión del usuario
    Optional<Sesion> buscarUltimaSesionPorUsuario(Integer usuarioId);
    
    // Crear nueva sesión
    Sesion crearSesion(Integer usuarioId, String ipAddress, String userAgent);
    
    // Validar sesión
    boolean validarSesion(String tokenSesion);
    
    // Cerrar sesión
    void cerrarSesion(String tokenSesion);
    
    // Cerrar todas las sesiones del usuario
    void cerrarTodasLasSesionesDelUsuario(Integer usuarioId);
    
    // Extender sesión
    void extenderSesion(String tokenSesion, int horas);
    
    // Limpiar sesiones expiradas
    void limpiarSesionesExpiradas();
    
    // Generar token único
    String generarTokenUnico();
    
    // Obtener información de la sesión
    Object obtenerInfoSesion(String tokenSesion);
}