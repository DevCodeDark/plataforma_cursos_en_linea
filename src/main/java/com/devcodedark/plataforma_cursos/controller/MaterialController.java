package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.model.Material.TipoMaterial;
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
    public ResponseEntity<List<Material>> listarTodos() {
        try {
            List<Material> materiales = materialService.buscarTodos();
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar material por ID
    @GetMapping("/{id}")
    public ResponseEntity<Material> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Material> material = materialService.buscarId(id);
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
    public ResponseEntity<String> crear(@Valid @RequestBody Material material) {
        try {
            materialService.guardar(material);
            return ResponseEntity.status(HttpStatus.CREATED).body("Material creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el material: " + e.getMessage());
        }
    }

    // Actualizar material
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Material material) {
        try {
            Optional<Material> materialExistente = materialService.buscarId(id);
            if (!materialExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            material.setId(id);
            materialService.modificar(material);
            return ResponseEntity.ok("Material actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el material: " + e.getMessage());
        }
    }

    // Eliminar material
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Material> material = materialService.buscarId(id);
            if (!material.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            materialService.eliminar(id);
            return ResponseEntity.ok("Material eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el material: " + e.getMessage());
        }
    }

    // Buscar materiales por módulo ordenados
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<Material>> buscarPorModuloOrdenado(@PathVariable Integer moduloId) {
        try {
            List<Material> materiales = materialService.buscarPorModuloOrdenado(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales activos por módulo
    @GetMapping("/modulo/{moduloId}/activos")
    public ResponseEntity<List<Material>> buscarActivosPorModulo(@PathVariable Integer moduloId) {
        try {
            List<Material> materiales = materialService.buscarMaterialesActivosPorModulo(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Material>> buscarPorTipo(@PathVariable TipoMaterial tipo) {
        try {
            List<Material> materiales = materialService.buscarPorTipo(tipo);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales gratuitos por módulo
    @GetMapping("/modulo/{moduloId}/gratuitos")
    public ResponseEntity<List<Material>> buscarGratuitosPorModulo(@PathVariable Integer moduloId) {
        try {
            List<Material> materiales = materialService.buscarMaterialesGratuitosPorModulo(moduloId);
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar materiales por curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Material>> buscarPorCurso(@PathVariable Integer cursoId) {
        try {
            List<Material> materiales = materialService.buscarMaterialesPorCurso(cursoId);
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

    // Calcular duración de videos por módulo
    @GetMapping("/modulo/{moduloId}/duracion-videos")
    public ResponseEntity<Integer> calcularDuracionVideos(@PathVariable Integer moduloId) {
        try {
            Integer duracion = materialService.calcularDuracionVideosPorModulo(moduloId);
            return ResponseEntity.ok(duracion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Reordenar materiales
    @PutMapping("/modulo/{moduloId}/reordenar")
    public ResponseEntity<String> reordenarMateriales(@PathVariable Integer moduloId, @RequestBody List<Integer> nuevoOrden) {
        try {
            materialService.reordenarMateriales(moduloId, nuevoOrden);
            return ResponseEntity.ok("Materiales reordenados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al reordenar los materiales: " + e.getMessage());
        }
    }

    // Cambiar disponibilidad gratuita
    @PutMapping("/{id}/disponibilidad-gratuita")
    public ResponseEntity<String> cambiarDisponibilidadGratuita(@PathVariable Integer id, @RequestBody Boolean esGratuito) {
        try {
            Optional<Material> material = materialService.buscarId(id);
            if (!material.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            materialService.cambiarDisponibilidadGratuita(id, esGratuito);
            return ResponseEntity.ok("Disponibilidad gratuita cambiada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cambiar la disponibilidad: " + e.getMessage());
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