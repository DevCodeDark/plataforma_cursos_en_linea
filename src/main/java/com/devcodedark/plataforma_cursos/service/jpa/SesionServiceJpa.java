package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.SesionDTO;
import com.devcodedark.plataforma_cursos.model.Sesion;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.repository.SesionRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.ISesionService;

@Service
@Transactional
public class SesionServiceJpa implements ISesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Métodos CRUD básicos con DTO
    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarTodos() {
        return sesionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public void guardar(SesionDTO sesionDTO) {
        if (sesionDTO == null) {
            throw new IllegalArgumentException("El DTO de sesión no puede ser nulo");
        }

        Sesion sesion = convertirAEntidad(sesionDTO);

        // Generar token único si es una nueva sesión
        if (sesion.getId() == null && sesion.getTokenSesion() == null) {
            sesion.setTokenSesion(generarTokenUnico());
        }

        sesionRepository.save(sesion);
    }

    @Override
    public void modificar(SesionDTO sesionDTO) {
        if (sesionDTO == null || sesionDTO.getId() == null) {
            throw new IllegalArgumentException("El DTO de sesión y su ID no pueden ser nulos");
        }

        Sesion sesion = convertirAEntidad(sesionDTO);
        sesionRepository.save(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SesionDTO> buscarId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        return sesionRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        sesionRepository.deleteById(id);
    }

    // Búsquedas específicas
    @Override
    @Transactional(readOnly = true)
    public Optional<SesionDTO> buscarPorToken(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de sesión no puede ser nulo o vacío");
        }

        return sesionRepository.findByTokenSesion(tokenSesion)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesActivasPorUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        return sesionRepository.findSesionesActivasByUsuario(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarPorUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        return sesionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesExpiradas() {
        return sesionRepository.findSesionesExpiradas(LocalDateTime.now())
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarPorIP(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("La IP no puede ser nula o vacía");
        }

        return sesionRepository.findByIpAddress(ip)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesActivas() {
        return sesionRepository.findSesionesActivas()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SesionDTO> buscarUltimaSesionPorUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        List<Sesion> sesiones = sesionRepository.findUltimaSesionByUsuario(usuarioId);
        return sesiones.isEmpty() ? Optional.empty() : Optional.of(convertirADTO(sesiones.get(0)));
    }

    // Operaciones de negocio
    @Override
    public SesionDTO crearSesion(Integer usuarioId, String ipAddress, String userAgent) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
        }

        // Cerrar sesiones activas del usuario (opcional, según política de la
        // aplicación)
        cerrarTodasLasSesionesDelUsuario(usuarioId);

        Sesion sesion = new Sesion();
        sesion.setUsuario(usuarioOpt.get());
        sesion.setTokenSesion(generarTokenUnico());
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setFechaExpiracion(LocalDateTime.now().plusHours(24)); // 24 horas por defecto
        sesion.setActiva(true);
        sesion.setIpAddress(ipAddress);
        sesion.setUserAgent(userAgent);

        Sesion sesionGuardada = sesionRepository.save(sesion);
        return convertirADTO(sesionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarSesion(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            return false;
        }

        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesion(tokenSesion);
        if (!sesionOpt.isPresent()) {
            return false;
        }

        Sesion sesion = sesionOpt.get();

        // Verificar si la sesión está activa y no ha expirado
        return sesion.getActiva() &&
                sesion.getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    @Override
    public void cerrarSesion(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de sesión no puede ser nulo o vacío");
        }

        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesion(tokenSesion);
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setActiva(false);
            sesion.setFechaExpiracion(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void cerrarTodasLasSesionesDelUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        List<Sesion> sesionesActivas = sesionRepository.findSesionesActivasByUsuario(usuarioId);
        for (Sesion sesion : sesionesActivas) {
            sesion.setActiva(false);
            sesion.setFechaExpiracion(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void extenderSesion(String tokenSesion, int horas) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de sesión no puede ser nulo o vacío");
        }

        if (horas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayor a 0");
        }

        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesion(tokenSesion);
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setFechaExpiracion(sesion.getFechaExpiracion().plusHours(horas));
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void limpiarSesionesExpiradas() {
        List<Sesion> sesionesExpiradas = sesionRepository.findSesionesExpiradas(LocalDateTime.now());
        for (Sesion sesion : sesionesExpiradas) {
            sesion.setActiva(false);
            if (sesion.getFechaExpiracion() == null) {
                sesion.setFechaExpiracion(LocalDateTime.now());
            }
            sesionRepository.save(sesion);
        }
    }

    // Operaciones de consulta
    @Override
    @Transactional(readOnly = true)
    public Long contarSesionesActivasPorUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        return sesionRepository.countSesionesActivasByUsuario(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeToken(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            return false;
        }

        return sesionRepository.existsByTokenSesion(tokenSesion);
    }

    @Override
    public String generarTokenUnico() {
        String token;
        do {
            token = UUID.randomUUID().toString().replace("-", "");
        } while (existeToken(token));

        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public SesionDTO obtenerInfoSesion(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de sesión no puede ser nulo o vacío");
        }

        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesion(tokenSesion);
        if (!sesionOpt.isPresent()) {
            throw new IllegalArgumentException("Sesión no encontrada con token: " + tokenSesion);
        }

        return convertirADTO(sesionOpt.get());
    }

    // Métodos adicionales para estadísticas y gestión
    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return sesionRepository.findAll()
                .stream()
                .filter(sesion -> sesion.getFechaInicio().isAfter(fechaInicio.minusSeconds(1)) &&
                        sesion.getFechaInicio().isBefore(fechaFin.plusSeconds(1)))
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesMoviles() {
        return sesionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .filter(sesionDTO -> sesionDTO.getEsMovil() != null && sesionDTO.getEsMovil())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarSesionesPorNavegador(String navegador) {
        if (navegador == null || navegador.trim().isEmpty()) {
            throw new IllegalArgumentException("El navegador no puede ser nulo o vacío");
        }
        return sesionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .filter(sesionDTO -> navegador.equalsIgnoreCase(sesionDTO.getNavegador()))
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> buscarSesionesLargas() {
        return sesionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .filter(SesionDTO::esSesionLarga)
                .toList();
    }

    @Override
    public void cerrarSesionesInactivas(int minutosInactividad) {
        if (minutosInactividad <= 0) {
            throw new IllegalArgumentException("Los minutos de inactividad deben ser mayor a 0");
        }

        LocalDateTime fechaLimite = LocalDateTime.now().minusMinutes(minutosInactividad);
        List<Sesion> sesionesInactivas = sesionRepository.findAll()
                .stream()
                .filter(sesion -> sesion.getActiva() &&
                        sesion.getFechaInicio().isBefore(fechaLimite) &&
                        (sesion.getFechaExpiracion() == null
                                || sesion.getFechaExpiracion().isBefore(LocalDateTime.now())))
                .toList();

        for (Sesion sesion : sesionesInactivas) {
            sesion.setActiva(false);
            sesion.setFechaExpiracion(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean necesitaRenovacion(String tokenSesion) {
        if (tokenSesion == null || tokenSesion.trim().isEmpty()) {
            return false;
        }

        Optional<SesionDTO> sesionOpt = buscarPorToken(tokenSesion);
        return sesionOpt.map(SesionDTO::necesitaRenovacion).orElse(false);
    }

    // Métodos de conversión Entity ↔ DTO
    private SesionDTO convertirADTO(Sesion sesion) {
        if (sesion == null) {
            return null;
        }

        SesionDTO dto = new SesionDTO();
        dto.setId(sesion.getId());
        dto.setUsuarioId(sesion.getUsuario() != null ? sesion.getUsuario().getId() : null);
        dto.setTokenSesion(sesion.getTokenSesion());
        dto.setIpAddress(sesion.getIpAddress());
        dto.setUserAgent(sesion.getUserAgent());
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setFechaExpiracion(sesion.getFechaExpiracion());
        dto.setActiva(sesion.getActiva());

        // Enriquecer DTO con información adicional
        enriquecerDTO(dto, sesion);

        return dto;
    }

    private void enriquecerDTO(SesionDTO dto, Sesion sesion) {
        // Información del usuario
        if (sesion.getUsuario() != null) {
            dto.setUsuarioNombre(sesion.getUsuario().getUsuario());
        }

        // Campos calculados automáticamente cuando se establece el userAgent
        // (ya se calculan en el setter del userAgent en SesionDTO)
    }

    private Sesion convertirAEntidad(SesionDTO dto) {
        if (dto == null) {
            return null;
        }

        Sesion sesion = new Sesion();
        sesion.setId(dto.getId());
        sesion.setTokenSesion(dto.getTokenSesion());
        sesion.setIpAddress(dto.getIpAddress());
        sesion.setUserAgent(dto.getUserAgent());
        sesion.setFechaInicio(dto.getFechaInicio());
        sesion.setFechaExpiracion(dto.getFechaExpiracion());
        sesion.setActiva(dto.getActiva());

        // Establecer usuario
        if (dto.getUsuarioId() != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getUsuarioId());
            if (usuarioOpt.isPresent()) {
                sesion.setUsuario(usuarioOpt.get());
            }
        }

        return sesion;
    }
}