package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.CategoriaDTO;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;
import com.devcodedark.plataforma_cursos.service.ICategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    // Listar todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodos() {
        try {
            List<CategoriaDTO> categorias = categoriaService.buscarTodos();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<CategoriaDTO> categoria = categoriaService.buscarId(id);
            if (categoria.isPresent()) {
                return ResponseEntity.ok(categoria.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nueva categoría
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            // Verificar que no exista una categoría con el mismo nombre
            if (categoriaService.existePorNombre(categoriaDTO.getNombre())) {
                return ResponseEntity.badRequest().body("Ya existe una categoría con este nombre");
            }

            categoriaService.guardar(categoriaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Categoría creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la categoría: " + e.getMessage());
        }
    }

    // Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            Optional<CategoriaDTO> categoriaExistente = categoriaService.buscarId(id);
            if (!categoriaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar que no exista otra categoría con el mismo nombre
            Optional<CategoriaDTO> categoriaConMismoNombre = categoriaService.buscarPorNombre(categoriaDTO.getNombre());
            if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Ya existe otra categoría con este nombre");
            }

            categoriaDTO.setId(id);
            categoriaService.modificar(categoriaDTO);
            return ResponseEntity.ok("Categoría actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    // Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<CategoriaDTO> categoria = categoriaService.buscarId(id);
            if (!categoria.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar que no tenga cursos asociados
            Long cursosAsociados = categoriaService.contarCursosPorCategoria(id);
            if (cursosAsociados > 0) {
                return ResponseEntity.badRequest()
                        .body("No se puede eliminar la categoría porque tiene " + cursosAsociados
                                + " curso(s) asociado(s)");
            }

            categoriaService.eliminar(id);
            return ResponseEntity.ok("Categoría eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    // Buscar categorías activas
    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaDTO>> listarActivas() {
        try {
            List<CategoriaDTO> categorias = categoriaService.buscarCategoriasActivas();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar categoría por nombre
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<CategoriaDTO> buscarPorNombre(@PathVariable String nombre) {
        try {
            Optional<CategoriaDTO> categoria = categoriaService.buscarPorNombre(nombre);
            if (categoria.isPresent()) {
                return ResponseEntity.ok(categoria.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar categorías con cursos
    @GetMapping("/con-cursos")
    public ResponseEntity<List<CategoriaDTO>> listarConCursos() {
        try {
            List<CategoriaDTO> categorias = categoriaService.buscarCategoriasConCursos();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Contar cursos por categoría
    @GetMapping("/{id}/cursos/count")
    public ResponseEntity<Long> contarCursos(@PathVariable Integer id) {
        try {
            Optional<CategoriaDTO> categoria = categoriaService.buscarId(id);
            if (!categoria.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Long count = categoriaService.contarCursosPorCategoria(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cambiar estado de la categoría
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer id, @RequestBody EstadoCategoria estado) {
        try {
            Optional<CategoriaDTO> categoria = categoriaService.buscarId(id);
            if (!categoria.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            categoriaService.cambiarEstado(id, estado);
            return ResponseEntity.ok("Estado de la categoría cambiado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar el estado: " + e.getMessage());
        }
    }
}