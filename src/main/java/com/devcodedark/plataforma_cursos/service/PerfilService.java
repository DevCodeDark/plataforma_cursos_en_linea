package com.devcodedark.plataforma_cursos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devcodedark.plataforma_cursos.dto.CambioPasswordDTO;
import com.devcodedark.plataforma_cursos.dto.PerfilUpdateDTO;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para gestión de perfiles de usuario
 */
@Service
@Transactional
public class PerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String UPLOAD_DIR = "uploads/profiles/";
      /**
     * Actualiza el perfil de un usuario por email
     */
    public boolean actualizarPerfil(String email, PerfilUpdateDTO perfilDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Verificar si el email ya existe en otro usuario
        if (!usuario.getEmail().equals(perfilDTO.getEmail())) {
            if (usuarioRepository.existsByEmail(perfilDTO.getEmail())) {
                throw new RuntimeException("El email ya está en uso por otro usuario");
            }
        }
        
        // Verificar si el nombre de usuario ya existe en otro usuario
        if (!usuario.getUsuario().equals(perfilDTO.getUsuario())) {
            if (usuarioRepository.existsByUsuario(perfilDTO.getUsuario())) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
        }
        
        // Actualizar datos
        usuario.setNombre(perfilDTO.getNombre());
        usuario.setApellido(perfilDTO.getApellido());
        usuario.setEmail(perfilDTO.getEmail());
        usuario.setUsuario(perfilDTO.getUsuario());
        usuario.setTelefono(perfilDTO.getTelefono());
        usuario.setFechaNacimiento(perfilDTO.getFechaNacimiento());
        usuario.setGenero(perfilDTO.getGenero());        usuario.setFechaActualizacion(LocalDateTime.now());
        
        usuarioRepository.save(usuario);
        return true;
    }
      /**
     * Cambia la contraseña de un usuario por email
     */
    public boolean cambiarPassword(String email, CambioPasswordDTO cambioDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Verificar contraseña actual
        if (!passwordEncoder.matches(cambioDTO.getPasswordActual(), usuario.getPasswordHash())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Verificar que las nuevas contraseñas coincidan
        if (!cambioDTO.passwordsCoinciden()) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }
        
        // Actualizar contraseña
        usuario.setPasswordHash(passwordEncoder.encode(cambioDTO.getPasswordNueva()));
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        usuarioRepository.save(usuario);
        return true;
    }
      /**
     * Sube una foto de perfil por email
     */
    public String subirFotoPerfil(String email, MultipartFile archivo) throws IOException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Validar archivo
        if (archivo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        
        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Solo se permiten archivos de imagen");
        }
          // Generar nombre único para el archivo
        String extension = getFileExtension(archivo.getOriginalFilename());
        String nombreArchivo = "perfil_" + usuario.getId() + "_" + UUID.randomUUID().toString() + extension;
        
        // Crear directorio si no existe
        Path directorioUpload = Paths.get(UPLOAD_DIR);
        if (!Files.exists(directorioUpload)) {
            Files.createDirectories(directorioUpload);
        }
        
        // Guardar archivo
        Path rutaArchivo = directorioUpload.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaArchivo);
        
        // Actualizar usuario
        String rutaRelativa = "/uploads/profiles/" + nombreArchivo;
        usuario.setFotoPerfil(rutaRelativa);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        return rutaRelativa;
    }
      /**
     * Obtiene un usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }
    
    /**
     * Obtiene un usuario por email
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    /**
     * Convierte un Usuario a PerfilUpdateDTO
     */
    public PerfilUpdateDTO usuarioToDTO(Usuario usuario) {
        PerfilUpdateDTO dto = new PerfilUpdateDTO();
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setUsuario(usuario.getUsuario());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setGenero(usuario.getGenero());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        return dto;
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex >= 0 ? filename.substring(lastDotIndex) : "";
    }
}
