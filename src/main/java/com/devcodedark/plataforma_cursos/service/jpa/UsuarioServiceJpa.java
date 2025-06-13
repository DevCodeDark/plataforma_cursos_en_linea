package com.devcodedark.plataforma_cursos.service.jpa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.UsuarioDTO;
import com.devcodedark.plataforma_cursos.dto.UsuarioRegistroDTO;
import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;
import com.devcodedark.plataforma_cursos.model.Usuario.Genero;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;

@Service
@Transactional
public class UsuarioServiceJpa implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UsuarioServiceJpa(UsuarioRepository usuarioRepository,
                            RolRepository rolRepository,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    // Métodos de conversión entre Entity y DTO
    private UsuarioDTO convertToDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setUsuario(usuario.getUsuario());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setGenero(usuario.getGenero() != null ? usuario.getGenero().name() : null);
        dto.setEstado(usuario.getEstado() != null ? usuario.getEstado().name() : null);
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());
        
        // Información del rol
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getId());
            dto.setRolNombre(usuario.getRol().getNombre());
            dto.setRolDescripcion(usuario.getRol().getDescripcion());
        }
        
        // Campos calculados
        dto.setNombreCompleto(usuario.getNombre() + " " + usuario.getApellido());
        dto.setEstaActivo("activo".equalsIgnoreCase(dto.getEstado()));
        
        if (dto.getRolNombre() != null) {
            dto.setEsDocente("docente".equalsIgnoreCase(dto.getRolNombre()));
            dto.setEsEstudiante("estudiante".equalsIgnoreCase(dto.getRolNombre()));
            dto.setEsAdministrador("administrador".equalsIgnoreCase(dto.getRolNombre()));
        }
        
        // Calcular días sin acceso
        if (usuario.getUltimoAcceso() != null) {
            dto.setDiasSinAcceso((int) ChronoUnit.DAYS.between(usuario.getUltimoAcceso(), LocalDateTime.now()));
        }
        
        // Estadísticas (se calculan bajo demanda para evitar N+1 queries)
        if (usuario.getCursosCreados() != null) {
            dto.setTotalCursosCreados(usuario.getCursosCreados().size());
        }
        
        if (usuario.getInscripciones() != null) {
            dto.setTotalInscripciones(usuario.getInscripciones().size());
        }
        
        if (usuario.getCalificaciones() != null) {
            dto.setTotalCalificaciones(usuario.getCalificaciones().size());
            if (!usuario.getCalificaciones().isEmpty()) {
                double promedio = usuario.getCalificaciones().stream()
                    .mapToInt(c -> c.getPuntuacion())
                    .average()
                    .orElse(0.0);
                dto.setPromedioCalificaciones(promedio);
            }
        }
        
        return dto;
    }
    
    private Usuario convertToEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setUsuario(dto.getUsuario());
        usuario.setFotoPerfil(dto.getFotoPerfil());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        
        // Convertir enums
        if (dto.getGenero() != null) {
            usuario.setGenero(Genero.valueOf(dto.getGenero()));
        }
        
        if (dto.getEstado() != null) {
            usuario.setEstado(EstadoUsuario.valueOf(dto.getEstado()));
        }
        
        usuario.setUltimoAcceso(dto.getUltimoAcceso());
        usuario.setFechaCreacion(dto.getFechaCreacion());
        usuario.setFechaActualizacion(dto.getFechaActualizacion());
        
        // Cargar rol
        if (dto.getRolId() != null) {
            Optional<Rol> rol = rolRepository.findById(dto.getRolId());
            if (rol.isPresent()) {
                usuario.setRol(rol.get());
            } else {
                throw new RuntimeException("Rol con ID " + dto.getRolId() + " no encontrado");
            }
        }
        
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        
        // Encriptar contraseña si es nueva (en caso de creación)
        if (usuarioDTO.getId() == null && usuario.getPasswordHash() != null && !usuario.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        
        usuarioRepository.save(usuario);
    }

    @Override
    public void modificar(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorEstado(String estado) {
        EstadoUsuario estadoUsuario = EstadoUsuario.valueOf(estado);
        return usuarioRepository.findByEstado(estadoUsuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarDocentesActivos() {
        return usuarioRepository.findDocentesActivos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarEstudiantesActivos() {
        return usuarioRepository.findEstudiantesActivos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorTokenRecuperacion(String token) {
        return usuarioRepository.findByTokenRecuperacion(token)
                .map(this::convertToDTO);
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
    public void cambiarContrasena(Integer usuarioId, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswordHash(passwordEncoder.encode(nuevaContrasena));
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void cambiarEstado(Integer usuarioId, String estado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(EstadoUsuario.valueOf(estado));
            usuarioRepository.save(usuario);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer contarCursosCreados(Integer usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        return usuario.map(u -> u.getCursosCreados() != null ? u.getCursosCreados().size() : 0)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer contarInscripciones(Integer usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        return usuario.map(u -> u.getInscripciones() != null ? u.getInscripciones().size() : 0)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioCalificaciones(Integer usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        return usuario.map(u -> {
            if (u.getCalificaciones() != null && !u.getCalificaciones().isEmpty()) {
                return u.getCalificaciones().stream()
                    .mapToInt(c -> c.getPuntuacion())
                    .average()
                    .orElse(0.0);
            }
            return 0.0;
        }).orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calcularDiasSinAcceso(Integer usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        return usuario.map(u -> {
            if (u.getUltimoAcceso() != null) {
                return (int) ChronoUnit.DAYS.between(u.getUltimoAcceso(), LocalDateTime.now());
            }
            return 0;
        }).orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean puedeCrearCursos(Integer usuarioId) {
        Optional<UsuarioDTO> usuario = buscarId(usuarioId);
        return usuario.map(u -> u.getEsDocente() || u.getEsAdministrador())
                .orElse(false);
    }

    // Métodos adicionales para autenticación
    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuario(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    @Override
    public void registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        // Buscar rol de estudiante por defecto
        Rol rolEstudiante = rolRepository.findByNombre("Estudiante")
                .orElseThrow(() -> new RuntimeException("Rol Estudiante no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRegistroDTO.getNombre());
        usuario.setApellido(usuarioRegistroDTO.getApellido());
        usuario.setEmail(usuarioRegistroDTO.getEmail());
        usuario.setUsuario(usuarioRegistroDTO.getUsuario());
        usuario.setPasswordHash(passwordEncoder.encode(usuarioRegistroDTO.getPassword()));
        usuario.setTelefono(usuarioRegistroDTO.getTelefono());
        usuario.setFechaNacimiento(usuarioRegistroDTO.getFechaNacimiento());
        
        // Convertir género si está presente
        if (usuarioRegistroDTO.getGenero() != null && !usuarioRegistroDTO.getGenero().isEmpty()) {
            usuario.setGenero(Genero.valueOf(usuarioRegistroDTO.getGenero()));
        }
        
        usuario.setRol(rolEstudiante);
        usuario.setEstado(EstadoUsuario.activo);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());

        usuarioRepository.save(usuario);
    }
    
    // Métodos de estadísticas
    @Override
    @Transactional(readOnly = true)
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuariosPorRol(String rolNombre) {
        Long count = usuarioRepository.countByRolNombre(rolNombre);
        return count != null ? count : 0;
    }
}