package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarTodos() {
        return sesionRepository.findAll();
    }

    @Override
    public void guardar(Sesion sesion) {
        sesionRepository.save(sesion);
    }

    @Override
    public void modificar(Sesion sesion) {
        sesionRepository.save(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sesion> buscarId(Integer id) {
        return sesionRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        sesionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sesion> buscarPorToken(String tokenSesion) {
        return sesionRepository.findByTokenSesion(tokenSesion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarSesionesActivasPorUsuario(Integer usuarioId) {
        return sesionRepository.findSesionesActivasByUsuario(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarPorUsuario(Integer usuarioId) {
        return sesionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarSesionesExpiradas() {
        return sesionRepository.findSesionesExpiradas(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarPorIP(String ip) {
        return sesionRepository.findByIpAddress(ip);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarSesionesActivasPorUsuario(Integer usuarioId) {
        return sesionRepository.countSesionesActivasByUsuario(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sesion> buscarSesionesActivas() {
        return sesionRepository.findSesionesActivas();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeToken(String tokenSesion) {
        return sesionRepository.existsByTokenSesion(tokenSesion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sesion> buscarUltimaSesionPorUsuario(Integer usuarioId) {
        List<Sesion> sesiones = sesionRepository.findUltimaSesionByUsuario(usuarioId);
        return sesiones.isEmpty() ? Optional.empty() : Optional.of(sesiones.get(0));
    }

    @Override
    public Sesion crearSesion(Integer usuarioId, String ipAddress, String userAgent) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        // Cerrar sesiones activas del usuario (opcional, según política de la aplicación)
        cerrarTodasLasSesionesDelUsuario(usuarioId);
        
        Sesion sesion = new Sesion();
        sesion.setUsuario(usuarioOpt.get());
        sesion.setTokenSesion(generarTokenUnico());
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setFechaExpiracion(LocalDateTime.now().plusHours(24)); // 24 horas por defecto
        sesion.setActiva(true);
        sesion.setIpAddress(ipAddress);
        sesion.setUserAgent(userAgent);
        
        return sesionRepository.save(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarSesion(String tokenSesion) {
        Optional<Sesion> sesionOpt = buscarPorToken(tokenSesion);
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
        Optional<Sesion> sesionOpt = buscarPorToken(tokenSesion);
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setActiva(false);
            sesion.setFechaExpiracion(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void cerrarTodasLasSesionesDelUsuario(Integer usuarioId) {
        List<Sesion> sesionesActivas = buscarSesionesActivasPorUsuario(usuarioId);
        for (Sesion sesion : sesionesActivas) {
            sesion.setActiva(false);
            sesion.setFechaExpiracion(LocalDateTime.now());
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void extenderSesion(String tokenSesion, int horas) {
        Optional<Sesion> sesionOpt = buscarPorToken(tokenSesion);
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setFechaExpiracion(sesion.getFechaExpiracion().plusHours(horas));
            sesionRepository.save(sesion);
        }
    }

    @Override
    public void limpiarSesionesExpiradas() {
        List<Sesion> sesionesExpiradas = buscarSesionesExpiradas();
        for (Sesion sesion : sesionesExpiradas) {
            sesion.setActiva(false);
            if (sesion.getFechaExpiracion() == null) {
                sesion.setFechaExpiracion(LocalDateTime.now());
            }
            sesionRepository.save(sesion);
        }
    }

    @Override
    public String generarTokenUnico() {
        String token;
        do {
            token = UUID.randomUUID().toString().replaceAll("-", "");
        } while (existeToken(token));
        
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerInfoSesion(String tokenSesion) {
        Optional<Sesion> sesionOpt = buscarPorToken(tokenSesion);
        if (!sesionOpt.isPresent()) {
            return null;
        }
        
        Sesion sesion = sesionOpt.get();
        Map<String, Object> info = new HashMap<>();
        
        info.put("tokenSesion", sesion.getTokenSesion());
        info.put("usuario", sesion.getUsuario().getUsuario());
        info.put("fechaInicio", sesion.getFechaInicio());
        info.put("fechaExpiracion", sesion.getFechaExpiracion());
        info.put("activa", sesion.getActiva());
        info.put("ipAddress", sesion.getIpAddress());
        info.put("userAgent", sesion.getUserAgent());
        
        return info;
    }
}