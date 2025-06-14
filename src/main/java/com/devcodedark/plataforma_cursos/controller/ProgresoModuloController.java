package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.ProgresoModuloDTO;
import com.devcodedark.plataforma_cursos.service.IProgresoModuloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progreso-modulos")
@CrossOrigin(origins = "*")
public class ProgresoModuloController {

    private static final String ERROR_VALIDACION = "Error de validación: ";

    @Autowired
    private IProgresoModuloService progresoModuloService;

    // Listar todos los progresos
    @GetMapping
    public ResponseEntity<List<ProgresoModuloDTO>> listarTodos() {
        try {
            List<ProgresoModuloDTO> progresos = progresoModuloService.buscarTodos();
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProgresoModuloDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<ProgresoModuloDTO> progreso = progresoModuloService.buscarId(id);
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

    // Crear progreso
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody ProgresoModuloDTO progresoModuloDTO) {
        try {
            progresoModuloService.guardar(progresoModuloDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Progreso creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el progreso: " + e.getMessage());
        }
    }

    // Actualizar progreso
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id,
            @Valid @RequestBody ProgresoModuloDTO progresoModuloDTO) {
        try {
            Optional<ProgresoModuloDTO> progresoExistente = progresoModuloService.buscarId(id);
            if (!progresoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            progresoModuloDTO.setId(id);
            progresoModuloService.modificar(progresoModuloDTO);
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
            Optional<ProgresoModuloDTO> progreso = progresoModuloService.buscarId(id);
            if (!progreso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            progresoModuloService.eliminar(id);
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
    public ResponseEntity<List<ProgresoModuloDTO>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoModuloDTO> progresos = progresoModuloService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar módulos completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados")
    public ResponseEntity<List<ProgresoModuloDTO>> buscarCompletados(@PathVariable Integer inscripcionId) {
        try {
            List<ProgresoModuloDTO> progresos = progresoModuloService
                    .buscarModulosCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular porcentaje de progreso del curso
    @GetMapping("/inscripcion/{inscripcionId}/porcentaje-progreso")
    public ResponseEntity<Double> calcularPorcentajeProgreso(@PathVariable Integer inscripcionId) {
        try {
            Double porcentaje = progresoModuloService.calcularPorcentajeProgresoCurso(inscripcionId);
            return ResponseEntity.ok(porcentaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Completar módulo
    @PutMapping("/completar")
    public ResponseEntity<String> completarModulo(@RequestParam Integer inscripcionId, @RequestParam Integer moduloId) {
        try {
            progresoModuloService.completarModulo(inscripcionId, moduloId);
            return ResponseEntity.ok("Módulo completado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al completar el módulo: " + e.getMessage());
        }
    }

    // Actualizar tiempo invertido
    @PutMapping("/tiempo")
    public ResponseEntity<String> actualizarTiempo(@RequestParam Integer inscripcionId, @RequestParam Integer moduloId,
            @RequestParam Integer tiempoAdicional) {
        try {
            progresoModuloService.actualizarTiempoInvertido(inscripcionId, moduloId, tiempoAdicional);
            return ResponseEntity.ok("Tiempo invertido actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el tiempo invertido: " + e.getMessage());
        }
    }

    // Buscar último módulo accedido
    @GetMapping("/inscripcion/{inscripcionId}/ultimo-accedido")
    public ResponseEntity<ProgresoModuloDTO> buscarUltimoAccedido(@PathVariable Integer inscripcionId) {
        try {
            Optional<ProgresoModuloDTO> progreso = progresoModuloService.buscarUltimoModuloAccedido(inscripcionId);
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

    // Iniciar módulo
    @PostMapping("/iniciar")
    public ResponseEntity<ProgresoModuloDTO> iniciarModulo(@RequestParam Integer inscripcionId,
            @RequestParam Integer moduloId) {
        try {
            ProgresoModuloDTO progreso = progresoModuloService.iniciarModulo(inscripcionId, moduloId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear progreso si no existe
    @PostMapping("/crear-si-no-existe")
    public ResponseEntity<ProgresoModuloDTO> crearProgresoSiNoExiste(@RequestParam Integer inscripcionId,
            @RequestParam Integer moduloId) {
        try {
            ProgresoModuloDTO progreso = progresoModuloService.crearProgresoSiNoExiste(inscripcionId, moduloId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si existe progreso
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeProgreso(@RequestParam Integer inscripcionId, @RequestParam Integer moduloId) {
        try {
            boolean existe = progresoModuloService.existeProgreso(inscripcionId, moduloId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // Contar módulos completados por inscripción
    @GetMapping("/inscripcion/{inscripcionId}/completados/contar")
    public ResponseEntity<Long> contarModulosCompletados(@PathVariable Integer inscripcionId) {
        try {
            Long count = progresoModuloService.contarModulosCompletadosPorInscripcion(inscripcionId);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar progreso específico por inscripción y módulo
    @GetMapping("/inscripcion/{inscripcionId}/modulo/{moduloId}")
    public ResponseEntity<ProgresoModuloDTO> buscarPorInscripcionYModulo(@PathVariable Integer inscripcionId,
            @PathVariable Integer moduloId) {
        try {
            Optional<ProgresoModuloDTO> progreso = progresoModuloService.buscarPorInscripcionYModulo(inscripcionId,
                    moduloId);
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

    // Buscar progreso por estudiante y curso
    @GetMapping("/estudiante/{estudianteId}/curso/{cursoId}")
    public ResponseEntity<List<ProgresoModuloDTO>> buscarPorEstudianteYCurso(@PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            List<ProgresoModuloDTO> progresos = progresoModuloService.buscarPorEstudianteYCurso(estudianteId, cursoId);
            return ResponseEntity.ok(progresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}