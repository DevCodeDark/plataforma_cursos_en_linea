package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.ProgresoMaterial;
import com.devcodedark.plataforma_cursos.service.IProgresoMaterialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progreso-materiales")
@CrossOrigin(origins = "*")
public class ProgresoMaterialController {

    @Autowired
    private IProgresoMaterialService progresoMaterialService;

    // Listar todos los progresos
    @GetMapping
    public ResponseEntity<List<ProgresoMaterial>> listarTodos() {
        try {
            List<ProgresoMaterial> progresos = progresoMaterialService.buscarTodos();
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProgresoMaterial> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<ProgresoMaterial> progreso = progresoMaterialService.buscarId(id);
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
    public ResponseEntity<String> crear(@Valid @RequestBody ProgresoMaterial progresoMaterial) {
        try {
            progresoMaterialService.guardar(progresoMaterial);
            return ResponseEntity.status(HttpStatus.CREATED).body("Progreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el progreso: " + e.getMessage());
        }
    }

    // Marcar material como visualizado
    @PostMapping("/visualizar")
    public ResponseEntity<String> marcarComoVisualizado(@RequestParam Integer inscripcionId, @RequestParam Integer materialId) {
        try {
            progresoMaterialService.marcarComoVisualizado(inscripcionId, materialId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Material marcado como visualizado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al marcar como visualizado: " + e.getMessage());
        }
    }

    // Actualizar progreso
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody ProgresoMaterial progresoMaterial) {
        try {
            Optional<ProgresoMaterial> progresoExistente = progresoMaterialService.buscarId(id);
            if (!progresoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterial.setId(id);
            progresoMaterialService.modificar(progresoMaterial);
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
            Optional<ProgresoMaterial> progreso = progresoMaterialService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterialService.eliminar(id);
            return ResponseEntity.ok("Progreso eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el progreso: " + e.getMessage());
        }
    }

    // Buscar progreso por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<ProgresoMaterial>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterial> progresos = progresoMaterialService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales visualizados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/visualizados")
    public ResponseEntity<List<ProgresoMaterial>> buscarVisualizados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterial> progresos = progresoMaterialService.buscarMaterialesVisualizadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados")
    public ResponseEntity<List<ProgresoMaterial>> buscarCompletados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterial> progresos = progresoMaterialService.buscarMaterialesCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por inscripción y módulo
    @GetMapping("/inscripcion/{inscripcionId}/modulo/{moduloId}")
    public ResponseEntity<List<ProgresoMaterial>> buscarPorInscripcionYModulo(@PathVariable Integer inscripcionId, @PathVariable Integer moduloId) {
        try {
            List<ProgresoMaterial> progresos = progresoMaterialService.buscarPorInscripcionYModulo(inscripcionId, moduloId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular tiempo total reproducido
    @GetMapping("/inscripcion/{inscripcionId}/tiempo-total")
    public ResponseEntity<Long> calcularTiempoTotal(@PathVariable Integer inscripcionId) {
        try {
            Long tiempoTotal = progresoMaterialService.calcularTiempoTotalReproducidoPorInscripcion(inscripcionId);
            return ResponseEntity.ok(tiempoTotal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular porcentaje de progreso del módulo
    @GetMapping("/inscripcion/{inscripcionId}/modulo/{moduloId}/porcentaje")
    public ResponseEntity<BigDecimal> calcularPorcentajeModulo(@PathVariable Integer inscripcionId, @PathVariable Integer moduloId) {
        try {
            BigDecimal porcentaje = progresoMaterialService.calcularPorcentajeProgresoModulo(inscripcionId, moduloId);
            return ResponseEntity.ok(porcentaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Completar material
    @PutMapping("/{id}/completar")
    public ResponseEntity<String> completarMaterial(@PathVariable Integer id) {
        try {
            Optional<ProgresoMaterial> progreso = progresoMaterialService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterialService.completarMaterial(id);
            return ResponseEntity.ok("Material completado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al completar el material: " + e.getMessage());
        }
    }

    // Actualizar tiempo reproducido
    @PutMapping("/{id}/tiempo")
    public ResponseEntity<String> actualizarTiempo(@PathVariable Integer id, @RequestBody Integer tiempoReproducido) {
        try {
            Optional<ProgresoMaterial> progreso = progresoMaterialService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterialService.actualizarTiempoReproducido(id, tiempoReproducido);
            return ResponseEntity.ok("Tiempo de reproducción actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el tiempo: " + e.getMessage());
        }
    }

    // Buscar último material accedido
    @GetMapping("/inscripcion/{inscripcionId}/ultimo-accedido")
    public ResponseEntity<ProgresoMaterial> buscarUltimoAccedido(@PathVariable Integer inscripcionId) {
        try {
            Optional<ProgresoMaterial> progreso = progresoMaterialService.buscarUltimoMaterialAccedido(inscripcionId);
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