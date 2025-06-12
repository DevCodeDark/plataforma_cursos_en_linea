package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.UsuarioDTO;
import com.devcodedark.plataforma_cursos.service.IUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    
    
    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verificar que no exista un usuario con el mismo email
            if (usuarioService.existePorEmail(usuarioDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Ya existe un usuario con este email");
            }
            
            // Verificar que no exista un usuario con el mismo nombre de usuario
            if (usuarioService.existePorUsuario(usuarioDTO.getUsuario())) {
                return ResponseEntity.badRequest().body("Ya existe un usuario con este nombre de usuario");
            }
            
            usuarioService.guardar(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el usuario: " + e.getMessage());
        }
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Optional<UsuarioDTO> usuarioExistente = usuarioService.buscarId(id);
            if (!usuarioExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Verificar email único
            Optional<UsuarioDTO> usuarioConMismoEmail = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
            if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otro usuario con este email");
            }
            
            // Verificar nombre de usuario único
            Optional<UsuarioDTO> usuarioConMismoNombre = usuarioService.buscarPorUsuario(usuarioDTO.getUsuario());
            if (usuarioConMismoNombre.isPresent() && !usuarioConMismoNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otro usuario con este nombre de usuario");
            }
            
            usuarioDTO.setId(id);
            usuarioService.modificar(usuarioDTO);
            return ResponseEntity.ok("Usuario actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarId(id);
            if (!usuario.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    // Buscar usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarPorEmail(email);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar usuarios por rol
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorRol(@PathVariable Integer rolId) {
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarPorRol(rolId);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar docentes activos
    @GetMapping("/docentes/activos")
    public ResponseEntity<List<UsuarioDTO>> listarDocentesActivos() {
        try {
            List<UsuarioDTO> docentes = usuarioService.buscarDocentesActivos();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar estudiantes activos
    @GetMapping("/estudiantes/activos")
    public ResponseEntity<List<UsuarioDTO>> listarEstudiantesActivos() {
        try {
            List<UsuarioDTO> estudiantes = usuarioService.buscarEstudiantesActivos();
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cambiar contraseña
    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(@PathVariable Integer id, @RequestBody String nuevaContrasena) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarId(id);
            if (!usuario.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            usuarioService.cambiarContrasena(id, nuevaContrasena);
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }

    // Cambiar estado del usuario
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer id, @RequestBody String estado) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarId(id);
            if (!usuario.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            usuarioService.cambiarEstado(id, estado);
            return ResponseEntity.ok("Estado del usuario cambiado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cambiar el estado: " + e.getMessage());
        }
    }

    // Buscar usuarios por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorEstado(@PathVariable String estado) {
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarPorEstado(estado);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas del usuario
    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas(@PathVariable Integer id) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.buscarId(id);
            if (!usuario.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Crear objeto con estadísticas adicionales
            var estadisticas = new Object() {
                public final UsuarioDTO usuario = usuarioService.buscarId(id).get();
                public final Integer cursosCreados = usuarioService.contarCursosCreados(id);
                public final Integer inscripciones = usuarioService.contarInscripciones(id);
                public final Double promedioCalificaciones = usuarioService.calcularPromedioCalificaciones(id);
                public final Integer diasSinAcceso = usuarioService.calcularDiasSinAcceso(id);
                public final Boolean puedeCrearCursos = usuarioService.puedeCrearCursos(id);
            };
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}