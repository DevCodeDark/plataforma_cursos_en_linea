package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.ProgresoMaterialDTO;
import com.devcodedark.plataforma_cursos.service.IProgresoMaterialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progreso-materiales")
@CrossOrigin(origins = "*")
public class ProgresoMaterialController {

    private static final String ERROR_VALIDACION = "Error de validación: ";

    @Autowired
    private IProgresoMaterialService progresoMaterialService;

    // Listar todos los progresos
    @GetMapping
    public ResponseEntity<List<ProgresoMaterialDTO>> listarTodos() {
        try {
            List<ProgresoMaterialDTO> progresos = progresoMaterialService.buscarTodos();
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProgresoMaterialDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<ProgresoMaterialDTO> progreso = progresoMaterialService.buscarId(id);
            if (progreso.isPresent()) {
                return ResponseEntity.ok(progreso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo progreso
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody ProgresoMaterialDTO progresoMaterialDTO) {
        try {
            progresoMaterialService.guardar(progresoMaterialDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Progreso creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al marcar como visualizado: " + e.getMessage());
        }
    }

    // Actualizar progreso
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody ProgresoMaterialDTO progresoMaterialDTO) {
        try {
            Optional<ProgresoMaterialDTO> progresoExistente = progresoMaterialService.buscarId(id);
            if (!progresoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterialDTO.setId(id);
            progresoMaterialService.modificar(progresoMaterialDTO);
            return ResponseEntity.ok("Progreso actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el progreso: " + e.getMessage());
        }
    }

    // Eliminar progreso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<ProgresoMaterialDTO> progreso = progresoMaterialService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            progresoMaterialService.eliminar(id);
            return ResponseEntity.ok("Progreso eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el progreso: " + e.getMessage());
        }
    }

    // Buscar progreso por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<ProgresoMaterialDTO>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterialDTO> progresos = progresoMaterialService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales visualizados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/visualizados")
    public ResponseEntity<List<ProgresoMaterialDTO>> buscarVisualizados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterialDTO> progresos = progresoMaterialService.buscarMaterialesVisualizadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados")
    public ResponseEntity<List<ProgresoMaterialDTO>> buscarCompletados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoMaterialDTO> progresos = progresoMaterialService.buscarMaterialesCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por inscripción y módulo
    @GetMapping("/inscripcion/{inscripcionId}/modulo/{moduloId}")
    public ResponseEntity<List<ProgresoMaterialDTO>> buscarPorInscripcionYModulo(@PathVariable Integer inscripcionId, @PathVariable Integer moduloId) {
        try {
            List<ProgresoMaterialDTO> progresos = progresoMaterialService.buscarPorInscripcionYModulo(inscripcionId, moduloId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular porcentaje de progreso por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/porcentaje")
    public ResponseEntity<Double> calcularPorcentajeProgreso(@PathVariable Integer inscripcionId) {
        try {
            Double porcentaje = progresoMaterialService.calcularPorcentajeProgresoPorInscripcion(inscripcionId);
            return ResponseEntity.ok(porcentaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Marcar material como completado
    @PutMapping("/completar")
    public ResponseEntity<String> marcarComoCompletado(@RequestParam Integer inscripcionId, @RequestParam Integer materialId) {
        try {
            progresoMaterialService.marcarComoCompletado(inscripcionId, materialId);
            return ResponseEntity.ok("Material marcado como completado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al marcar como completado: " + e.getMessage());
        }
    }

    // Actualizar tiempo reproducido
    @PutMapping("/tiempo")
    public ResponseEntity<String> actualizarTiempo(@RequestParam Integer inscripcionId, @RequestParam Integer materialId, @RequestParam Integer tiempoReproducido) {
        try {
            progresoMaterialService.actualizarTiempoReproducido(inscripcionId, materialId, tiempoReproducido);
            return ResponseEntity.ok("Tiempo de reproducción actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el tiempo: " + e.getMessage());
        }
    }

    // Buscar último material accedido
    @GetMapping("/inscripcion/{inscripcionId}/ultimo-accedido")
    public ResponseEntity<ProgresoMaterialDTO> buscarUltimoAccedido(@PathVariable Integer inscripcionId) {
        try {
            Optional<ProgresoMaterialDTO> progreso = progresoMaterialService.buscarUltimoMaterialAccedido(inscripcionId);
            if (progreso.isPresent()) {
                return ResponseEntity.ok(progreso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear progreso si no existe
    @PostMapping("/crear-si-no-existe")
    public ResponseEntity<ProgresoMaterialDTO> crearProgresoSiNoExiste(@RequestParam Integer inscripcionId, @RequestParam Integer materialId) {
        try {
            ProgresoMaterialDTO progreso = progresoMaterialService.crearProgresoSiNoExiste(inscripcionId, materialId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si existe progreso
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeProgreso(@RequestParam Integer inscripcionId, @RequestParam Integer materialId) {
        try {
            boolean existe = progresoMaterialService.existeProgreso(inscripcionId, materialId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // Contar materiales completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados/contar")
    public ResponseEntity<Long> contarMaterialesCompletados(@PathVariable Integer inscripcionId) {
        try {
            Long count = progresoMaterialService.contarMaterialesCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}