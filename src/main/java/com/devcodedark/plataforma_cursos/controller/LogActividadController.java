package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.LogActividadDTO;
import com.devcodedark.plataforma_cursos.service.ILogActividadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/logs-actividad")
@CrossOrigin(origins = "*")
public class LogActividadController {

    @Autowired
    private ILogActividadService logActividadService;

    // Listar todos los logs
    @GetMapping
    public ResponseEntity<List<LogActividadDTO>> listarTodos() {
        try {
            List<LogActividadDTO> logs = logActividadService.buscarTodos();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar log por ID
    @GetMapping("/{id}")
    public ResponseEntity<LogActividadDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<LogActividadDTO> log = logActividadService.buscarId(id);
            if (log.isPresent()) {
                return ResponseEntity.ok(log.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo log
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody LogActividadDTO logActividadDTO) {
        try {
            logActividadService.guardar(logActividadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Log creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el log: " + e.getMessage());
        }
    }

    // Registrar actividad
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarActividad(
            @RequestParam Integer usuarioId,
            @RequestParam String accion,
            @RequestParam String entidad,
            @RequestParam(required = false) Integer entidadId,
            @RequestParam(required = false) String detalles,
            HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

            logActividadService.registrarActividadCompleta(usuarioId, accion, entidad, entidadId, detalles, ipAddress,
                    userAgent);
            return ResponseEntity.status(HttpStatus.CREATED).body("Actividad registrada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar la actividad: " + e.getMessage());
        }
    }

    // Actualizar log
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id,
            @Valid @RequestBody LogActividadDTO logActividadDTO) {
        try {
            Optional<LogActividadDTO> logExistente = logActividadService.buscarId(id);
            if (!logExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            logActividadDTO.setId(id);
            logActividadService.modificar(logActividadDTO);
            return ResponseEntity.ok("Log actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el log: " + e.getMessage());
        }
    }

    // Eliminar log
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<LogActividadDTO> log = logActividadService.buscarId(id);
            if (!log.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            logActividadService.eliminar(id);
            return ResponseEntity.ok("Log eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el log: " + e.getMessage());
        }
    }

    // Buscar logs por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<LogActividadDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        try {
            List<LogActividadDTO> logs = logActividadService.buscarPorUsuario(usuarioId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar logs por acción
    @GetMapping("/accion/{accion}")
    public ResponseEntity<List<LogActividadDTO>> buscarPorAccion(@PathVariable String accion) {
        try {
            List<LogActividadDTO> logs = logActividadService.buscarPorAccion(accion);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar logs recientes
    @GetMapping("/recientes")
    public ResponseEntity<List<LogActividadDTO>> buscarRecientes() {
        try {
            List<LogActividadDTO> logs = logActividadService.buscarLogsRecientes();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar logs por rango de fechas
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<LogActividadDTO>> buscarPorRangoFechas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio + " 00:00:00", formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin + " 23:59:59", formatter);

            List<LogActividadDTO> logs = logActividadService.buscarPorRangoFechas(inicio, fin);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas de actividad
    @GetMapping("/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas() {
        try {
            Object estadisticas = logActividadService.obtenerEstadisticasActividad();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener actividad reciente por usuario
    @GetMapping("/usuario/{usuarioId}/reciente")
    public ResponseEntity<List<LogActividadDTO>> obtenerActividadReciente(@PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<LogActividadDTO> logs = logActividadService.obtenerActividadRecientePorUsuario(usuarioId, limite);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener logs de acceso
    @GetMapping("/acceso")
    public ResponseEntity<List<LogActividadDTO>> obtenerLogsAcceso() {
        try {
            List<LogActividadDTO> logs = logActividadService.obtenerLogsAcceso();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener logs de errores
    @GetMapping("/errores")
    public ResponseEntity<List<LogActividadDTO>> obtenerLogsErrores() {
        try {
            List<LogActividadDTO> logs = logActividadService.obtenerLogsErrores();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Limpiar logs antiguos
    @DeleteMapping("/limpiar-antiguos")
    public ResponseEntity<String> limpiarLogsAntiguos(@RequestParam(defaultValue = "90") int diasAntiguedad) {
        try {
            logActividadService.limpiarLogsAntiguos(diasAntiguedad);
            return ResponseEntity.ok("Logs antiguos limpiados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al limpiar logs antiguos: " + e.getMessage());
        }
    }

    // Contar por acción
    @GetMapping("/accion/{accion}/count")
    public ResponseEntity<Long> contarPorAccion(@PathVariable String accion) {
        try {
            Long count = logActividadService.contarPorAccion(accion);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}