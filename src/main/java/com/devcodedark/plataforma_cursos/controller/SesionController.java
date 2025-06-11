package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Sesion;
import com.devcodedark.plataforma_cursos.service.ISesionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {

    @Autowired
    private ISesionService sesionService;

    // Listar todas las sesiones
    @GetMapping
    public ResponseEntity<List<Sesion>> listarTodos() {
        try {
            List<Sesion> sesiones = sesionService.buscarTodos();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesión por ID
    @GetMapping("/{id}")
    public ResponseEntity<Sesion> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Sesion> sesion = sesionService.buscarId(id);
            if (sesion.isPresent()) {
                return ResponseEntity.ok(sesion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nueva sesión
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody Sesion sesion) {
        try {
            sesionService.guardar(sesion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sesión creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear la sesión: " + e.getMessage());
        }
    }

    // Crear nueva sesión para usuario
    @PostMapping("/crear")
    public ResponseEntity<Object> crearSesion(@RequestParam Integer usuarioId, HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            
            Sesion sesion = sesionService.crearSesion(usuarioId, ipAddress, userAgent);
            return ResponseEntity.status(HttpStatus.CREATED).body(sesion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear la sesión: " + e.getMessage());
        }
    }

    // Actualizar sesión
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Sesion sesion) {
        try {
            Optional<Sesion> sesionExistente = sesionService.buscarId(id);
            if (!sesionExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            sesion.setId(id);
            sesionService.modificar(sesion);
            return ResponseEntity.ok("Sesión actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar la sesión: " + e.getMessage());
        }
    }

    // Eliminar sesión
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Sesion> sesion = sesionService.buscarId(id);
            if (!sesion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            sesionService.eliminar(id);
            return ResponseEntity.ok("Sesión eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar la sesión: " + e.getMessage());
        }
    }

    // Buscar sesión por token
    @GetMapping("/token/{token}")
    public ResponseEntity<Sesion> buscarPorToken(@PathVariable String token) {
        try {
            Optional<Sesion> sesion = sesionService.buscarPorToken(token);
            if (sesion.isPresent()) {
                return ResponseEntity.ok(sesion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones activas por usuario
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<List<Sesion>> buscarActivasPorUsuario(@PathVariable Integer usuarioId) {
        try {
            List<Sesion> sesiones = sesionService.buscarSesionesActivasPorUsuario(usuarioId);
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Sesion>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        try {
            List<Sesion> sesiones = sesionService.buscarPorUsuario(usuarioId);
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones activas
    @GetMapping("/activas")
    public ResponseEntity<List<Sesion>> listarActivas() {
        try {
            List<Sesion> sesiones = sesionService.buscarSesionesActivas();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Validar sesión
    @GetMapping("/validar/{token}")
    public ResponseEntity<Boolean> validarSesion(@PathVariable String token) {
        try {
            boolean esValida = sesionService.validarSesion(token);
            return ResponseEntity.ok(esValida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cerrar sesión
    @PutMapping("/cerrar/{token}")
    public ResponseEntity<String> cerrarSesion(@PathVariable String token) {
        try {
            sesionService.cerrarSesion(token);
            return ResponseEntity.ok("Sesión cerrada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cerrar la sesión: " + e.getMessage());
        }
    }

    // Cerrar todas las sesiones del usuario
    @PutMapping("/usuario/{usuarioId}/cerrar-todas")
    public ResponseEntity<String> cerrarTodasLasSesiones(@PathVariable Integer usuarioId) {
        try {
            sesionService.cerrarTodasLasSesionesDelUsuario(usuarioId);
            return ResponseEntity.ok("Todas las sesiones del usuario han sido cerradas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cerrar las sesiones: " + e.getMessage());
        }
    }

    // Extender sesión
    @PutMapping("/extender/{token}")
    public ResponseEntity<String> extenderSesion(@PathVariable String token, @RequestParam int horas) {
        try {
            sesionService.extenderSesion(token, horas);
            return ResponseEntity.ok("Sesión extendida exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al extender la sesión: " + e.getMessage());
        }
    }

    // Limpiar sesiones expiradas
    @DeleteMapping("/limpiar-expiradas")
    public ResponseEntity<String> limpiarSesionesExpiradas() {
        try {
            sesionService.limpiarSesionesExpiradas();
            return ResponseEntity.ok("Sesiones expiradas limpiadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al limpiar sesiones expiradas: " + e.getMessage());
        }
    }

    // Obtener información de sesión
    @GetMapping("/info/{token}")
    public ResponseEntity<Object> obtenerInfoSesion(@PathVariable String token) {
        try {
            Object info = sesionService.obtenerInfoSesion(token);
            if (info != null) {
                return ResponseEntity.ok(info);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar sesiones activas por usuario
    @GetMapping("/usuario/{usuarioId}/count")
    public ResponseEntity<Long> contarSesionesActivas(@PathVariable Integer usuarioId) {
        try {
            Long count = sesionService.contarSesionesActivasPorUsuario(usuarioId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}