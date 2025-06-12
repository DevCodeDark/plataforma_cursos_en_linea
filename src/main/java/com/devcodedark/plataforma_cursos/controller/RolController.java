package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.RolDTO;
import com.devcodedark.plataforma_cursos.service.IRolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {

    private static final String ERROR_VALIDACION = "Error de validación: ";

    @Autowired
    private IRolService rolService;

    // Listar todos los roles
    @GetMapping
    public ResponseEntity<List<RolDTO>> listarTodos() {
        try {
            List<RolDTO> roles = rolService.buscarTodos();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<RolDTO> rol = rolService.buscarId(id);
            if (rol.isPresent()) {
                return ResponseEntity.ok(rol.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody RolDTO rolDTO) {
        try {
            // Verificar que no exista un rol con el mismo nombre
            if (rolService.existePorNombre(rolDTO.getNombre())) {
                return ResponseEntity.badRequest().body(ERROR_VALIDACION + "Ya existe un rol con este nombre");
            }

            rolService.guardar(rolDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el rol: " + e.getMessage());
        }
    }

    // Actualizar rol
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody RolDTO rolDTO) {
        try {
            Optional<RolDTO> rolExistente = rolService.buscarId(id);
            if (!rolExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar que no exista otro rol con el mismo nombre
            Optional<RolDTO> rolConMismoNombre = rolService.buscarPorNombre(rolDTO.getNombre());
            if (rolConMismoNombre.isPresent() && !rolConMismoNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(ERROR_VALIDACION + "Ya existe otro rol con este nombre");
            }

            rolDTO.setId(id);
            rolService.modificar(rolDTO);
            return ResponseEntity.ok("Rol actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el rol: " + e.getMessage());
        }
    }

    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<RolDTO> rol = rolService.buscarId(id);
            if (!rol.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            rolService.eliminar(id);
            return ResponseEntity.ok("Rol eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el rol: " + e.getMessage());
        }
    }

    // Buscar roles activos
    @GetMapping("/activos")
    public ResponseEntity<List<RolDTO>> listarActivos() {
        try {
            List<RolDTO> roles = rolService.buscarRolesActivos();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar rol por nombre
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<RolDTO> buscarPorNombre(@PathVariable String nombre) {
        try {
            Optional<RolDTO> rol = rolService.buscarPorNombre(nombre);
            if (rol.isPresent()) {
                return ResponseEntity.ok(rol.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar roles por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RolDTO>> buscarPorEstado(@PathVariable Integer estado) {
        try {
            List<RolDTO> roles = rolService.buscarPorEstado(estado);
            return ResponseEntity.ok(roles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si existe rol por nombre
    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(@PathVariable String nombre) {
        try {
            boolean existe = rolService.existePorNombre(nombre);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // Obtener estadísticas del rol
    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<RolDTO> obtenerEstadisticas(@PathVariable Integer id) {
        try {
            Optional<RolDTO> rol = rolService.buscarId(id);
            if (rol.isPresent()) {
                // El DTO ya incluye información enriquecida como cantidad de usuarios
                return ResponseEntity.ok(rol.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}