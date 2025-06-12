package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.ModuloDTO;
import com.devcodedark.plataforma_cursos.service.IModuloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/modulos")
@CrossOrigin(origins = "*")
public class ModuloController {

    @Autowired
    private IModuloService moduloService;

    // Listar todos los módulos
    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarTodos() {
        try {
            List<ModuloDTO> modulos = moduloService.buscarTodos();
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar módulo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<ModuloDTO> modulo = moduloService.buscarId(id);
            if (modulo.isPresent()) {
                return ResponseEntity.ok(modulo.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo módulo
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody ModuloDTO moduloDTO) {
        try {
            moduloService.guardar(moduloDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Módulo creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el módulo: " + e.getMessage());
        }
    }

    // Actualizar módulo
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody ModuloDTO moduloDTO) {
        try {
            Optional<ModuloDTO> moduloExistente = moduloService.buscarId(id);
            if (!moduloExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            moduloDTO.setId(id);
            moduloService.modificar(moduloDTO);
            return ResponseEntity.ok("Módulo actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el módulo: " + e.getMessage());
        }
    }

    // Eliminar módulo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<ModuloDTO> modulo = moduloService.buscarId(id);
            if (!modulo.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            moduloService.eliminar(id);
            return ResponseEntity.ok("Módulo eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el módulo: " + e.getMessage());
        }
    }

    // Buscar módulos por curso ordenados
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ModuloDTO>> buscarPorCursoOrdenado(@PathVariable Integer cursoId) {
        try {
            List<ModuloDTO> modulos = moduloService.buscarPorCursoOrdenado(cursoId);
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar módulos activos por curso
    @GetMapping("/curso/{cursoId}/activos")
    public ResponseEntity<List<ModuloDTO>> buscarActivosPorCurso(@PathVariable Integer cursoId) {
        try {
            List<ModuloDTO> modulos = moduloService.buscarModulosActivosPorCurso(cursoId);
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar módulos obligatorios por curso
    @GetMapping("/curso/{cursoId}/obligatorios")
    public ResponseEntity<List<ModuloDTO>> buscarObligatoriosPorCurso(@PathVariable Integer cursoId) {
        try {
            List<ModuloDTO> modulos = moduloService.buscarModulosObligatoriosPorCurso(cursoId);
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar módulos por curso
    @GetMapping("/curso/{cursoId}/count")
    public ResponseEntity<Long> contarPorCurso(@PathVariable Integer cursoId) {
        try {
            Long count = moduloService.contarModulosPorCurso(cursoId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular duración total por curso
    @GetMapping("/curso/{cursoId}/duracion-total")
    public ResponseEntity<Integer> calcularDuracionTotal(@PathVariable Integer cursoId) {
        try {
            Integer duracion = moduloService.calcularDuracionTotalPorCurso(cursoId);
            return ResponseEntity.ok(duracion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Reordenar módulos
    @PutMapping("/curso/{cursoId}/reordenar")
    public ResponseEntity<String> reordenarModulos(@PathVariable Integer cursoId, @RequestBody List<Integer> nuevoOrden) {
        try {
            moduloService.reordenarModulos(cursoId, nuevoOrden);
            return ResponseEntity.ok("Módulos reordenados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al reordenar los módulos: " + e.getMessage());
        }
    }    
    
    // Cambiar si es obligatorio
    @PutMapping("/{id}/obligatorio")
    public ResponseEntity<String> cambiarObligatorio(@PathVariable Integer id, @RequestBody Boolean esObligatorio) {
        try {
            Optional<ModuloDTO> modulo = moduloService.buscarId(id);
            if (!modulo.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            moduloService.cambiarObligatorio(id, esObligatorio);
            return ResponseEntity.ok("Configuración de obligatorio cambiada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cambiar la configuración: " + e.getMessage());
        }
    }

    // Obtener siguiente orden disponible
    @GetMapping("/curso/{cursoId}/siguiente-orden")
    public ResponseEntity<Integer> obtenerSiguienteOrden(@PathVariable Integer cursoId) {
        try {
            Integer siguienteOrden = moduloService.obtenerSiguienteOrden(cursoId);
            return ResponseEntity.ok(siguienteOrden);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}