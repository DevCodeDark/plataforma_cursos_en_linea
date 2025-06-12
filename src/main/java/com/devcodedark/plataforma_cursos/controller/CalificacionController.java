package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.CalificacionDTO;
import com.devcodedark.plataforma_cursos.service.ICalificacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private ICalificacionService calificacionService;    
    
    // Listar todas las calificaciones
    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listarTodos() {
        try {
            List<CalificacionDTO> calificaciones = calificacionService.buscarTodos();
            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar calificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<CalificacionDTO> calificacion = calificacionService.buscarId(id);
            if (calificacion.isPresent()) {
                return ResponseEntity.ok(calificacion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nueva calificación
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody CalificacionDTO calificacionDTO) {
        try {
            calificacionService.guardar(calificacionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Calificación creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear la calificación: " + e.getMessage());
        }
    }

    // Calificar curso
    @PostMapping("/calificar")
    public ResponseEntity<String> calificarCurso(
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId,
            @RequestParam Integer puntuacion,
            @RequestParam(required = false) String comentario) {
        try {
            calificacionService.calificarCurso(estudianteId, cursoId, puntuacion, comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Curso calificado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al calificar el curso: " + e.getMessage());
        }
    }    
    
    // Actualizar calificación
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody CalificacionDTO calificacionDTO) {
        try {
            Optional<CalificacionDTO> calificacionExistente = calificacionService.buscarId(id);
            if (!calificacionExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            calificacionDTO.setId(id);
            calificacionService.modificar(calificacionDTO);
            return ResponseEntity.ok("Calificación actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar la calificación: " + e.getMessage());
        }
    }    // Eliminar calificación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<CalificacionDTO> calificacion = calificacionService.buscarId(id);
            if (!calificacion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            calificacionService.eliminar(id);
            return ResponseEntity.ok("Calificación eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar la calificación: " + e.getMessage());
        }
    }

    // Buscar calificaciones por curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorCurso(@PathVariable Integer cursoId) {
        try {
            List<CalificacionDTO> calificaciones = calificacionService.buscarPorCurso(cursoId);
            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar calificaciones por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<CalificacionDTO> calificaciones = calificacionService.buscarPorEstudiante(estudianteId);
            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular promedio de calificaciones por curso
    @GetMapping("/curso/{cursoId}/promedio")
    public ResponseEntity<Double> calcularPromedio(@PathVariable Integer cursoId) {
        try {
            Double promedio = calificacionService.calcularPromedioCalificacionesPorCurso(cursoId);
            return ResponseEntity.ok(promedio != null ? promedio : 0.0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas por curso
    @GetMapping("/curso/{cursoId}/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas(@PathVariable Integer cursoId) {
        try {
            Object estadisticas = calificacionService.obtenerEstadisticasPorCurso(cursoId);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    
    
    // Buscar calificaciones por rango de puntuación
    @GetMapping("/rango")
    public ResponseEntity<List<CalificacionDTO>> buscarPorRango(@RequestParam Integer min, @RequestParam Integer max) {
        try {
            List<CalificacionDTO> calificaciones = calificacionService.buscarPorRangoPuntuacion(min, max);
            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si ya calificó
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarCalificacion(@RequestParam Integer estudianteId, @RequestParam Integer cursoId) {
        try {
            boolean yaCalifico = calificacionService.yaCalificoElCurso(estudianteId, cursoId);
            return ResponseEntity.ok(yaCalifico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar calificaciones por curso
    @GetMapping("/curso/{cursoId}/count")
    public ResponseEntity<Long> contarPorCurso(@PathVariable Integer cursoId) {
        try {
            Long count = calificacionService.contarCalificacionesPorCurso(cursoId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}