package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;

@Service
@Transactional
public class UsuarioServiceJpa implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public void guardar(Usuario usuario) {
        // Encriptar contraseña si es nueva
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        usuarioRepository.save(usuario);
    }

    @Override
    public void modificar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorEstado(EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarDocentesActivos() {
        return usuarioRepository.findDocentesActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarEstudiantesActivos() {
        return usuarioRepository.findEstudiantesActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorUsuario(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorTokenRecuperacion(String token) {
        return usuarioRepository.findByTokenRecuperacion(token);
    }

    @Override
    public void cambiarContrasena(Integer usuarioId, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswordHash(passwordEncoder.encode(nuevaContrasena));
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void cambiarEstado(Integer usuarioId, EstadoUsuario estado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            usuarioRepository.save(usuario);
        }
    }
}