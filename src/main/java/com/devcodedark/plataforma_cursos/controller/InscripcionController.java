package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.service.IInscripcionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    private IInscripcionService inscripcionService;

    // Listar todas las inscripciones
    @GetMapping
    public ResponseEntity<List<Inscripcion>> listarTodos() {
        try {
            List<Inscripcion> inscripciones = inscripcionService.buscarTodos();
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar inscripción por ID
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (inscripcion.isPresent()) {
                return ResponseEntity.ok(inscripcion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nueva inscripción
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody Inscripcion inscripcion) {
        try {
            inscripcionService.guardar(inscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Inscripción creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear la inscripción: " + e.getMessage());
        }
    }

    // Inscribir estudiante a curso
    @PostMapping("/inscribir")
    public ResponseEntity<String> inscribirEstudiante(@RequestParam Integer estudianteId, @RequestParam Integer cursoId) {
        try {
            inscripcionService.inscribirEstudiante(estudianteId, cursoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante inscrito exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al inscribir estudiante: " + e.getMessage());
        }
    }

    // Actualizar inscripción
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Inscripcion inscripcion) {
        try {
            Optional<Inscripcion> inscripcionExistente = inscripcionService.buscarId(id);
            if (!inscripcionExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcion.setId(id);
            inscripcionService.modificar(inscripcion);
            return ResponseEntity.ok("Inscripción actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar la inscripción: " + e.getMessage());
        }
    }

    // Eliminar inscripción
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (!inscripcion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcionService.eliminar(id);
            return ResponseEntity.ok("Inscripción eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar la inscripción: " + e.getMessage());
        }
    }

    // Buscar inscripciones por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Inscripcion>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<Inscripcion> inscripciones = inscripcionService.buscarPorEstudiante(estudianteId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar inscripciones por curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> buscarPorCurso(@PathVariable Integer cursoId) {
        try {
            List<Inscripcion> inscripciones = inscripcionService.buscarPorCurso(cursoId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar inscripciones activas por estudiante
    @GetMapping("/estudiante/{estudianteId}/activas")
    public ResponseEntity<List<Inscripcion>> buscarActivasPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<Inscripcion> inscripciones = inscripcionService.buscarInscripcionesActivasPorEstudiante(estudianteId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar inscripciones completadas por estudiante
    @GetMapping("/estudiante/{estudianteId}/completadas")
    public ResponseEntity<List<Inscripcion>> buscarCompletadasPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<Inscripcion> inscripciones = inscripcionService.buscarInscripcionesCompletadasPorEstudiante(estudianteId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar si estudiante está inscrito
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarInscripcion(@RequestParam Integer estudianteId, @RequestParam Integer cursoId) {
        try {
            boolean estaInscrito = inscripcionService.estaInscrito(estudianteId, cursoId);
            return ResponseEntity.ok(estaInscrito);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Finalizar inscripción
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizarInscripcion(@PathVariable Integer id) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (!inscripcion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcionService.finalizarInscripcion(id);
            return ResponseEntity.ok("Inscripción finalizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al finalizar la inscripción: " + e.getMessage());
        }
    }

    // Cancelar inscripción
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarInscripcion(@PathVariable Integer id) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (!inscripcion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcionService.cancelarInscripcion(id);
            return ResponseEntity.ok("Inscripción cancelada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cancelar la inscripción: " + e.getMessage());
        }
    }

    // Actualizar progreso
    @PutMapping("/{id}/progreso")
    public ResponseEntity<String> actualizarProgreso(@PathVariable Integer id, @RequestBody BigDecimal progreso) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (!inscripcion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcionService.actualizarProgreso(id, progreso);
            return ResponseEntity.ok("Progreso actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el progreso: " + e.getMessage());
        }
    }

    // Generar certificado
    @PostMapping("/{id}/certificado")
    public ResponseEntity<String> generarCertificado(@PathVariable Integer id) {
        try {
            Optional<Inscripcion> inscripcion = inscripcionService.buscarId(id);
            if (!inscripcion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            inscripcionService.generarCertificado(id);
            return ResponseEntity.ok("Certificado generado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al generar el certificado: " + e.getMessage());
        }
    }
}