package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.MaterialDTO;
import com.devcodedark.plataforma_cursos.service.IMaterialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "*")
public class MaterialController {

    @Autowired
    private IMaterialService materialService;

    // Listar todos los materiales
    @GetMapping
    public ResponseEntity<List<MaterialDTO>> listarTodos() {
        try {
            List<MaterialDTO> materiales = materialService.buscarTodos();
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar material por ID
    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<MaterialDTO> material = materialService.buscarId(id);
            if (material.isPresent()) {
                return ResponseEntity.ok(material.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo material
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody MaterialDTO materialDTO) {
        try {
            materialService.guardar(materialDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Material creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear material: " + e.getMessage());
        }
    }

    // Actualizar material
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody MaterialDTO materialDTO) {
        try {
            Optional<MaterialDTO> materialExistente = materialService.buscarId(id);
            if (materialExistente.isPresent()) {
                materialDTO.setId(id);
                materialService.modificar(materialDTO);
                return ResponseEntity.ok("Material actualizado exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar material: " + e.getMessage());
        }
    }

    // Eliminar material
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            Optional<MaterialDTO> material = materialService.buscarId(id);
            if (material.isPresent()) {
                materialService.eliminar(id);
                return ResponseEntity.ok("Material eliminado exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar material: " + e.getMessage());
        }
    }

    // Obtener materiales por módulo (ordenados)
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<MaterialDTO>> obtenerPorModulo(@PathVariable Integer moduloId) {
        try {
            List<MaterialDTO> materiales = materialService.buscarPorModuloOrdenado(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener materiales activos por módulo
    @GetMapping("/modulo/{moduloId}/activos")
    public ResponseEntity<List<MaterialDTO>> obtenerActivosPorModulo(@PathVariable Integer moduloId) {
        try {
            List<MaterialDTO> materiales = materialService.buscarMaterialesActivosPorModulo(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener materiales gratuitos por módulo
    @GetMapping("/modulo/{moduloId}/gratuitos")
    public ResponseEntity<List<MaterialDTO>> obtenerGratuitosPorModulo(@PathVariable Integer moduloId) {
        try {
            List<MaterialDTO> materiales = materialService.buscarMaterialesGratuitosPorModulo(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener materiales por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MaterialDTO>> obtenerPorTipo(@PathVariable String tipo) {
        try {
            List<MaterialDTO> materiales = materialService.buscarPorTipo(tipo.toUpperCase());
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener materiales por curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<MaterialDTO>> obtenerPorCurso(@PathVariable Integer cursoId) {
        try {
            List<MaterialDTO> materiales = materialService.buscarMaterialesPorCurso(cursoId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar materiales por módulo
    @GetMapping("/modulo/{moduloId}/count")
    public ResponseEntity<Long> contarPorModulo(@PathVariable Integer moduloId) {
        try {
            Long count = materialService.contarMaterialesPorModulo(moduloId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Reordenar materiales de un módulo
    @PutMapping("/modulo/{moduloId}/reordenar")
    public ResponseEntity<?> reordenarMateriales(@PathVariable Integer moduloId,
            @RequestBody List<Integer> nuevoOrden) {
        try {
            materialService.reordenarMateriales(moduloId, nuevoOrden);
            return ResponseEntity.ok("Materiales reordenados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al reordenar materiales: " + e.getMessage());
        }
    }

    // Cambiar disponibilidad gratuita
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(@PathVariable Integer id, @RequestParam Boolean esGratuito) {
        try {
            materialService.cambiarDisponibilidadGratuita(id, esGratuito);
            return ResponseEntity.ok("Disponibilidad actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cambiar disponibilidad: " + e.getMessage());
        }
    }

    // Obtener duración total de videos por módulo
    @GetMapping("/modulo/{moduloId}/duracion-videos")
    public ResponseEntity<Integer> obtenerDuracionVideosPorModulo(@PathVariable Integer moduloId) {
        try {
            Integer duracion = materialService.calcularDuracionVideosPorModulo(moduloId);
            return ResponseEntity.ok(duracion != null ? duracion : 0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas del módulo
    @GetMapping("/modulo/{moduloId}/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticasModulo(@PathVariable Integer moduloId) {
        try {
            Object estadisticas = materialService.obtenerEstadisticasPorModulo(moduloId);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales por título
    @GetMapping("/buscar")
    public ResponseEntity<List<MaterialDTO>> buscarPorTitulo(@RequestParam String titulo) {
        try {
            List<MaterialDTO> materiales = materialService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener materiales recientes
    @GetMapping("/recientes")
    public ResponseEntity<List<MaterialDTO>> obtenerMaterialesRecientes(@RequestParam(defaultValue = "10") int limite) {
        try {
            List<MaterialDTO> materiales = materialService.obtenerMaterialesRecientes(limite);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener siguiente orden disponible
    @GetMapping("/modulo/{moduloId}/siguiente-orden")
    public ResponseEntity<Integer> obtenerSiguienteOrden(@PathVariable Integer moduloId) {
        try {
            Integer siguienteOrden = materialService.obtenerSiguienteOrden(moduloId);
            return ResponseEntity.ok(siguienteOrden);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}