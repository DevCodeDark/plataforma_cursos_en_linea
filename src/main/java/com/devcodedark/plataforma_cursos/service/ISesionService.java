package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.devcodedark.plataforma_cursos.dto.SesionDTO;

public interface ISesionService {
    // Métodos CRUD básicos con DTO
    List<SesionDTO> buscarTodos();

    void guardar(SesionDTO sesionDTO);

    void modificar(SesionDTO sesionDTO);

    Optional<SesionDTO> buscarId(Integer id);

    void eliminar(Integer id);

    // Búsquedas específicas
    Optional<SesionDTO> buscarPorToken(String tokenSesion);

    List<SesionDTO> buscarSesionesActivasPorUsuario(Integer usuarioId);

    List<SesionDTO> buscarPorUsuario(Integer usuarioId);

    List<SesionDTO> buscarSesionesExpiradas();

    List<SesionDTO> buscarPorIP(String ip);

    List<SesionDTO> buscarSesionesActivas();

    Optional<SesionDTO> buscarUltimaSesionPorUsuario(Integer usuarioId);

    // Operaciones de negocio
    SesionDTO crearSesion(Integer usuarioId, String ipAddress, String userAgent);

    boolean validarSesion(String tokenSesion);

    void cerrarSesion(String tokenSesion);

    void cerrarTodasLasSesionesDelUsuario(Integer usuarioId);

    void extenderSesion(String tokenSesion, int horas);

    void limpiarSesionesExpiradas();

    // Operaciones de consulta
    Long contarSesionesActivasPorUsuario(Integer usuarioId);

    boolean existeToken(String tokenSesion);

    String generarTokenUnico();

    SesionDTO obtenerInfoSesion(String tokenSesion);

    // Métodos adicionales para estadísticas y gestión
    List<SesionDTO> buscarSesionesPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<SesionDTO> buscarSesionesMoviles();

    Long contarSesionesPorNavegador(String navegador);

    List<SesionDTO> buscarSesionesLargas();

    void cerrarSesionesInactivas(int minutosInactividad);

    boolean necesitaRenovacion(String tokenSesion);
}