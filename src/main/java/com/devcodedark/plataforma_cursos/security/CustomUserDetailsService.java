package com.devcodedark.plataforma_cursos.security;

import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio personalizado para cargar detalles del usuario durante la autenticación
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar usuario por email o username
        Usuario usuario = usuarioRepository.findByEmailOrUsuario(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si el usuario está activo
        if (usuario.getEstado() != Usuario.EstadoUsuario.activo) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        // Crear lista de authorities basada en el rol
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().toUpperCase()));
            
            // Agregar permisos específicos del rol si existen
            if (usuario.getRol().getPermisos() != null && !usuario.getRol().getPermisos().isEmpty()) {
                String[] permisos = usuario.getRol().getPermisos().split(",");
                for (String permiso : permisos) {
                    authorities.add(new SimpleGrantedAuthority(permiso.trim()));
                }
            }
        }

        // Actualizar último acceso (esto se puede hacer en un listener de autenticación)
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Retornar UserDetails
        return User.builder()
                .username(usuario.getEmail()) // Usar email como username principal
                .password(usuario.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Obtener usuario por email
     */
    public Usuario getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
}
