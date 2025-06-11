package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.ProgresoModulo;
import com.devcodedark.plataforma_cursos.service.IProgresoModuloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progreso-modulos")
@CrossOrigin(origins = "*")
public class ProgresoModuloController {

    @Autowired
    private IProgresoModuloService progresoModuloService;

    // Listar todos los progresos
    @GetMapping
    public ResponseEntity<List<ProgresoModulo>> listarTodos() {
        try {
            List<ProgresoModulo> progresos = progresoModuloService.buscarTodos();
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProgresoModulo> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<ProgresoModulo> progreso = progresoModuloService.buscarId(id);
            if (progreso.isPresent()) {
                return ResponseEntity.ok(progreso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo progreso
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody ProgresoModulo progresoModulo) {
        try {
            progresoModuloService.guardar(progresoModulo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Progreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el progreso: " + e.getMessage());
        }
    }

    // Iniciar módulo
    @PostMapping("/iniciar")
    public ResponseEntity<String> iniciarModulo(@RequestParam Integer inscripcionId, @RequestParam Integer moduloId) {
        try {
            progresoModuloService.iniciarModulo(inscripcionId, moduloId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Módulo iniciado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al iniciar el módulo: " + e.getMessage());
        }
    }

    // Actualizar progreso
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody ProgresoModulo progresoModulo) {
        try {
            Optional<ProgresoModulo> progresoExistente = progresoModuloService.buscarId(id);
            if (!progresoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoModulo.setId(id);
            progresoModuloService.modificar(progresoModulo);
            return ResponseEntity.ok("Progreso actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el progreso: " + e.getMessage());
        }
    }

    // Eliminar progreso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<ProgresoModulo> progreso = progresoModuloService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoModuloService.eliminar(id);
            return ResponseEntity.ok("Progreso eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el progreso: " + e.getMessage());
        }
    }

    // Buscar progreso por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<ProgresoModulo>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoModulo> progresos = progresoModuloService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar módulos completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados")
    public ResponseEntity<List<ProgresoModulo>> buscarCompletados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoModulo> progresos = progresoModuloService.buscarModulosCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar módulos completados
    @GetMapping("/inscripcion/{inscripcionId}/completados/count")
    public ResponseEntity<Long> contarCompletados(@PathVariable Integer inscripcionId) {
        try {
            Long count = progresoModuloService.contarModulosCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular tiempo total invertido
    @GetMapping("/inscripcion/{inscripcionId}/tiempo-total")
    public ResponseEntity<Long> calcularTiempoTotal(@PathVariable Integer inscripcionId) {
        try {
            Long tiempoTotal = progresoModuloService.calcularTiempoTotalInvertidoPorInscripcion(inscripcionId);
            return ResponseEntity.ok(tiempoTotal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular porcentaje de progreso del curso
    @GetMapping("/inscripcion/{inscripcionId}/porcentaje-progreso")
    public ResponseEntity<BigDecimal> calcularPorcentajeProgreso(@PathVariable Integer inscripcionId) {
        try {
            BigDecimal porcentaje = progresoModuloService.calcularPorcentajeProgresoCurso(inscripcionId);
            return ResponseEntity.ok(porcentaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Completar módulo
    @PutMapping("/{id}/completar")
    public ResponseEntity<String> completarModulo(@PathVariable Integer id) {
        try {
            Optional<ProgresoModulo> progreso = progresoModuloService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoModuloService.completarModulo(id);
            return ResponseEntity.ok("Módulo completado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al completar el módulo: " + e.getMessage());
        }
    }

    // Actualizar tiempo invertido
    @PutMapping("/{id}/tiempo")
    public ResponseEntity<String> actualizarTiempo(@PathVariable Integer id, @RequestBody Integer tiempoAdicional) {
        try {
            Optional<ProgresoModulo> progreso = progresoModuloService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoModuloService.actualizarTiempoInvertido(id, tiempoAdicional);
            return ResponseEntity.ok("Tiempo actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el tiempo: " + e.getMessage());
        }
    }

    // Buscar último módulo accedido
    @GetMapping("/inscripcion/{inscripcionId}/ultimo-accedido")
    public ResponseEntity<ProgresoModulo> buscarUltimoAccedido(@PathVariable Integer inscripcionId) {
        try {
            Optional<ProgresoModulo> progreso = progresoModuloService.buscarUltimoModuloAccedido(inscripcionId);
            if (progreso.isPresent()) {
                return ResponseEntity.ok(progreso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}