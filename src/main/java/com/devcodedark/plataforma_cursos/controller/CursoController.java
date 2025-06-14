package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.CursoDTO;
import com.devcodedark.plataforma_cursos.service.ICursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private ICursoService cursoService;

    // Listar todos los cursos
    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarTodos() {
        try {
            List<CursoDTO> cursos = cursoService.buscarTodos();
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (curso.isPresent()) {
                return ResponseEntity.ok(curso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo curso
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.guardar(cursoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Curso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el curso: " + e.getMessage());
        }
    }

    // Actualizar curso
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody CursoDTO cursoDTO) {
        try {
            Optional<CursoDTO> cursoExistente = cursoService.buscarId(id);
            if (!cursoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            cursoDTO.setId(id);
            cursoService.modificar(cursoDTO);
            return ResponseEntity.ok("Curso actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el curso: " + e.getMessage());
        }
    }

    // Eliminar curso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (!curso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            cursoService.eliminar(id);
            return ResponseEntity.ok("Curso eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el curso: " + e.getMessage());
        }
    }

    // Buscar cursos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<CursoDTO>> buscarPorCategoria(@PathVariable Integer categoriaId) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorCategoria(categoriaId);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos por docente
    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<CursoDTO>> buscarPorDocente(@PathVariable Integer docenteId) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorDocente(docenteId);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos publicados
    @GetMapping("/publicados")
    public ResponseEntity<List<CursoDTO>> listarPublicados() {
        try {
            List<CursoDTO> cursos = cursoService.buscarCursosPublicados();
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos por nivel
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<CursoDTO>> buscarPorNivel(@PathVariable String nivel) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorNivel(nivel);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos gratuitos
    @GetMapping("/gratuitos")
    public ResponseEntity<List<CursoDTO>> listarGratuitos() {
        try {
            List<CursoDTO> cursos = cursoService.buscarCursosGratuitos();
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos por rango de precio
    @GetMapping("/precio")
    public ResponseEntity<List<CursoDTO>> buscarPorRangoPrecio(
            @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorRangoPrecio(min, max);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos por título
    @GetMapping("/buscar")
    public ResponseEntity<List<CursoDTO>> buscarPorTitulo(@RequestParam String titulo) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar cursos más populares
    @GetMapping("/populares")
    public ResponseEntity<List<CursoDTO>> listarMasPopulares() {
        try {
            List<CursoDTO> cursos = cursoService.buscarCursosMasPopulares();
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar inscripciones por curso
    @GetMapping("/{id}/inscripciones/count")
    public ResponseEntity<Long> contarInscripciones(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (!curso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Long count = cursoService.contarInscripcionesPorCurso(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Publicar curso
    @PutMapping("/{id}/publicar")
    public ResponseEntity<String> publicarCurso(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (!curso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            cursoService.publicarCurso(id);
            return ResponseEntity.ok("Curso publicado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al publicar el curso: " + e.getMessage());
        }
    }

    // Pausar curso
    @PutMapping("/{id}/pausar")
    public ResponseEntity<String> pausarCurso(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (!curso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            cursoService.pausarCurso(id);
            return ResponseEntity.ok("Curso pausado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al pausar el curso: " + e.getMessage());
        }
    }

    // Cambiar estado del curso
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (!curso.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            cursoService.cambiarEstado(id, estado);
            return ResponseEntity.ok("Estado del curso cambiado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar el estado: " + e.getMessage());
        }
    }

    // Buscar cursos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CursoDTO>> buscarPorEstado(@PathVariable String estado) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorEstado(estado);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas del curso
    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<CursoDTO> obtenerEstadisticas(@PathVariable Integer id) {
        try {
            Optional<CursoDTO> curso = cursoService.buscarId(id);
            if (curso.isPresent()) {
                return ResponseEntity.ok(curso.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}