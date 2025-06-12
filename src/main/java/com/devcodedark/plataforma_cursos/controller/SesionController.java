package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.SesionDTO;
import com.devcodedark.plataforma_cursos.service.ISesionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {

    private static final String ERROR_VALIDACION = "Error de validación: ";

    @Autowired
    private ISesionService sesionService;

    // Listar todas las sesiones
    @GetMapping
    public ResponseEntity<List<SesionDTO>> listarTodos() {
        try {
            List<SesionDTO> sesiones = sesionService.buscarTodos();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesión por ID
    @GetMapping("/{id}")
    public ResponseEntity<SesionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<SesionDTO> sesion = sesionService.buscarId(id);
            if (sesion.isPresent()) {
                return ResponseEntity.ok(sesion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nueva sesión
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody SesionDTO sesionDTO) {
        try {
            sesionService.guardar(sesionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sesión creada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
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

            SesionDTO sesion = sesionService.crearSesion(usuarioId, ipAddress, userAgent);
            return ResponseEntity.status(HttpStatus.CREATED).body(sesion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la sesión: " + e.getMessage());
        }
    }

    // Actualizar sesión
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody SesionDTO sesionDTO) {
        try {
            Optional<SesionDTO> sesionExistente = sesionService.buscarId(id);
            if (!sesionExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            sesionDTO.setId(id);
            sesionService.modificar(sesionDTO);
            return ResponseEntity.ok("Sesión actualizada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la sesión: " + e.getMessage());
        }
    }

    // Eliminar sesión
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<SesionDTO> sesion = sesionService.buscarId(id);
            if (!sesion.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            sesionService.eliminar(id);
            return ResponseEntity.ok("Sesión eliminada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la sesión: " + e.getMessage());
        }
    }

    // Buscar sesión por token
    @GetMapping("/token/{token}")
    public ResponseEntity<SesionDTO> buscarPorToken(@PathVariable String token) {
        try {
            Optional<SesionDTO> sesion = sesionService.buscarPorToken(token);
            if (sesion.isPresent()) {
                return ResponseEntity.ok(sesion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones activas por usuario
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<List<SesionDTO>> buscarActivasPorUsuario(@PathVariable Integer usuarioId) {
        try {
            List<SesionDTO> sesiones = sesionService.buscarSesionesActivasPorUsuario(usuarioId);
            return ResponseEntity.ok(sesiones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SesionDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        try {
            List<SesionDTO> sesiones = sesionService.buscarPorUsuario(usuarioId);
            return ResponseEntity.ok(sesiones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones activas
    @GetMapping("/activas")
    public ResponseEntity<List<SesionDTO>> listarActivas() {
        try {
            List<SesionDTO> sesiones = sesionService.buscarSesionesActivas();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones expiradas
    @GetMapping("/expiradas")
    public ResponseEntity<List<SesionDTO>> listarExpiradas() {
        try {
            List<SesionDTO> sesiones = sesionService.buscarSesionesExpiradas();
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
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
    public ResponseEntity<SesionDTO> obtenerInfoSesion(@PathVariable String token) {
        try {
            SesionDTO info = sesionService.obtenerInfoSesion(token);
            return ResponseEntity.ok(info);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones por IP
    @GetMapping("/ip/{ip}")
    public ResponseEntity<List<SesionDTO>> buscarPorIP(@PathVariable String ip) {
        try {
            List<SesionDTO> sesiones = sesionService.buscarPorIP(ip);
            return ResponseEntity.ok(sesiones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar última sesión por usuario
    @GetMapping("/usuario/{usuarioId}/ultima")
    public ResponseEntity<SesionDTO> buscarUltimaSesion(@PathVariable Integer usuarioId) {
        try {
            Optional<SesionDTO> sesion = sesionService.buscarUltimaSesionPorUsuario(usuarioId);
            if (sesion.isPresent()) {
                return ResponseEntity.ok(sesion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si token existe
    @GetMapping("/existe/{token}")
    public ResponseEntity<Boolean> existeToken(@PathVariable String token) {
        try {
            boolean existe = sesionService.existeToken(token);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones móviles
    @GetMapping("/moviles")
    public ResponseEntity<List<SesionDTO>> buscarSesionesMoviles() {
        try {
            List<SesionDTO> sesiones = sesionService.buscarSesionesMoviles();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar sesiones largas
    @GetMapping("/largas")
    public ResponseEntity<List<SesionDTO>> buscarSesionesLargas() {
        try {
            List<SesionDTO> sesiones = sesionService.buscarSesionesLargas();
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar sesiones por navegador
    @GetMapping("/navegador/{navegador}/count")
    public ResponseEntity<Long> contarPorNavegador(@PathVariable String navegador) {
        try {
            Long count = sesionService.contarSesionesPorNavegador(navegador);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si necesita renovación
    @GetMapping("/renovacion/{token}")
    public ResponseEntity<Boolean> necesitaRenovacion(@PathVariable String token) {
        try {
            boolean necesita = sesionService.necesitaRenovacion(token);
            return ResponseEntity.ok(necesita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cerrar sesiones inactivas
    @DeleteMapping("/cerrar-inactivas")
    public ResponseEntity<String> cerrarSesionesInactivas(@RequestParam(defaultValue = "30") int minutosInactividad) {
        try {
            sesionService.cerrarSesionesInactivas(minutosInactividad);
            return ResponseEntity.ok("Sesiones inactivas cerradas exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cerrar sesiones inactivas: " + e.getMessage());
        }
    } // Buscar sesiones por rango de fechas

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<SesionDTO>> buscarPorRangoFechas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);

            List<SesionDTO> sesiones = sesionService.buscarSesionesPorRangoFechas(inicio, fin);
            return ResponseEntity.ok(sesiones);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}