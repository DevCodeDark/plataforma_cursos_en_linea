package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;
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
    public ResponseEntity<List<Usuario>> listarTodos() {
        try {
            List<Usuario> usuarios = usuarioService.buscarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarId(id);
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
    public ResponseEntity<String> crear(@Valid @RequestBody Usuario usuario) {
        try {
            // Verificar que no exista un usuario con el mismo email
            if (usuarioService.existePorEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().body("Ya existe un usuario con este email");
            }
            
            // Verificar que no exista un usuario con el mismo nombre de usuario
            if (usuarioService.existePorUsuario(usuario.getUsuario())) {
                return ResponseEntity.badRequest().body("Ya existe un usuario con este nombre de usuario");
            }
            
            usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el usuario: " + e.getMessage());
        }
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.buscarId(id);
            if (!usuarioExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Verificar email único
            Optional<Usuario> usuarioConMismoEmail = usuarioService.buscarPorEmail(usuario.getEmail());
            if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otro usuario con este email");
            }
            
            // Verificar nombre de usuario único
            Optional<Usuario> usuarioConMismoNombre = usuarioService.buscarPorUsuario(usuario.getUsuario());
            if (usuarioConMismoNombre.isPresent() && !usuarioConMismoNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otro usuario con este nombre de usuario");
            }
            
            usuario.setId(id);
            usuarioService.modificar(usuario);
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
            Optional<Usuario> usuario = usuarioService.buscarId(id);
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
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
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
    public ResponseEntity<List<Usuario>> buscarPorRol(@PathVariable Integer rolId) {
        try {
            List<Usuario> usuarios = usuarioService.buscarPorRol(rolId);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar docentes activos
    @GetMapping("/docentes/activos")
    public ResponseEntity<List<Usuario>> listarDocentesActivos() {
        try {
            List<Usuario> docentes = usuarioService.buscarDocentesActivos();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar estudiantes activos
    @GetMapping("/estudiantes/activos")
    public ResponseEntity<List<Usuario>> listarEstudiantesActivos() {
        try {
            List<Usuario> estudiantes = usuarioService.buscarEstudiantesActivos();
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cambiar contraseña
    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(@PathVariable Integer id, @RequestBody String nuevaContrasena) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarId(id);
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
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer id, @RequestBody EstadoUsuario estado) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarId(id);
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
}