package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.service.IRolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {

    @Autowired
    private IRolService rolService;

    // Listar todos los roles
    @GetMapping
    public ResponseEntity<List<Rol>> listarTodos() {
        try {
            List<Rol> roles = rolService.buscarTodos();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<Rol> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Rol> rol = rolService.buscarId(id);
            if (rol.isPresent()) {
                return ResponseEntity.ok(rol.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody Rol rol) {
        try {
            // Verificar que no exista un rol con el mismo nombre
            if (rolService.existePorNombre(rol.getNombre())) {
                return ResponseEntity.badRequest().body("Ya existe un rol con este nombre");
            }
            
            rolService.guardar(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el rol: " + e.getMessage());
        }
    }

    // Actualizar rol
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Rol rol) {
        try {
            Optional<Rol> rolExistente = rolService.buscarId(id);
            if (!rolExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Verificar que no exista otro rol con el mismo nombre
            Optional<Rol> rolConMismoNombre = rolService.buscarPorNombre(rol.getNombre());
            if (rolConMismoNombre.isPresent() && !rolConMismoNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otro rol con este nombre");
            }
            
            rol.setId(id);
            rolService.modificar(rol);
            return ResponseEntity.ok("Rol actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el rol: " + e.getMessage());
        }
    }

    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Rol> rol = rolService.buscarId(id);
            if (!rol.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            rolService.eliminar(id);
            return ResponseEntity.ok("Rol eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el rol: " + e.getMessage());
        }
    }

    // Buscar roles activos
    @GetMapping("/activos")
    public ResponseEntity<List<Rol>> listarActivos() {
        try {
            List<Rol> roles = rolService.buscarRolesActivos();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar rol por nombre
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<Rol> buscarPorNombre(@PathVariable String nombre) {
        try {
            Optional<Rol> rol = rolService.buscarPorNombre(nombre);
            if (rol.isPresent()) {
                return ResponseEntity.ok(rol.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar roles por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Rol>> buscarPorEstado(@PathVariable Integer estado) {
        try {
            List<Rol> roles = rolService.buscarPorEstado(estado);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}