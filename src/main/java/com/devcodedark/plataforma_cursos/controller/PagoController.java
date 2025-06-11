package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Pago;
import com.devcodedark.plataforma_cursos.model.Pago.EstadoPago;
import com.devcodedark.plataforma_cursos.model.Pago.MetodoPago;
import com.devcodedark.plataforma_cursos.service.IPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private IPagoService pagoService;

    // Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        try {
            List<Pago> pagos = pagoService.buscarTodos();
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Pago> pago = pagoService.buscarId(id);
            if (pago.isPresent()) {
                return ResponseEntity.ok(pago.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo pago
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody Pago pago) {
        try {
            pagoService.guardar(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el pago: " + e.getMessage());
        }
    }

    // Procesar pago
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(
            @RequestParam Integer inscripcionId,
            @RequestParam BigDecimal monto,
            @RequestParam MetodoPago metodoPago,
            @RequestParam(required = false) String transaccionExternaId) {
        try {
            pagoService.procesarPago(inscripcionId, monto, metodoPago, transaccionExternaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago procesado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar el pago: " + e.getMessage());
        }
    }

    // Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Pago pago) {
        try {
            Optional<Pago> pagoExistente = pagoService.buscarId(id);
            if (!pagoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pago.setId(id);
            pagoService.modificar(pago);
            return ResponseEntity.ok("Pago actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el pago: " + e.getMessage());
        }
    }

    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Pago> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.eliminar(id);
            return ResponseEntity.ok("Pago eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el pago: " + e.getMessage());
        }
    }

    // Buscar pagos por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<Pago>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<Pago> pagos = pagoService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Pago>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<Pago> pagos = pagoService.buscarPorEstudiante(estudianteId);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> buscarPorEstado(@PathVariable EstadoPago estado) {
        try {
            List<Pago> pagos = pagoService.buscarPorEstado(estado);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<Pago>> listarPendientes() {
        try {
            List<Pago> pagos = pagoService.buscarPagosPendientes();
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Confirmar pago
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmarPago(@PathVariable Integer id, @RequestParam String transaccionExternaId) {
        try {
            Optional<Pago> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.confirmarPago(id, transaccionExternaId);
            return ResponseEntity.ok("Pago confirmado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al confirmar el pago: " + e.getMessage());
        }
    }

    // Marcar pago como fallido
    @PutMapping("/{id}/fallar")
    public ResponseEntity<String> marcarComoFallido(@PathVariable Integer id, @RequestBody String razon) {
        try {
            Optional<Pago> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.marcarComoFallido(id, razon);
            return ResponseEntity.ok("Pago marcado como fallido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al marcar el pago como fallido: " + e.getMessage());
        }
    }

    // Procesar reembolso
    @PutMapping("/{id}/reembolsar")
    public ResponseEntity<String> procesarReembolso(@PathVariable Integer id, @RequestBody String razon) {
        try {
            Optional<Pago> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.procesarReembolso(id, razon);
            return ResponseEntity.ok("Reembolso procesado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar el reembolso: " + e.getMessage());
        }
    }

    // Calcular ingresos por curso
    @GetMapping("/curso/{cursoId}/ingresos")
    public ResponseEntity<BigDecimal> calcularIngresosPorCurso(@PathVariable Integer cursoId) {
        try {
            BigDecimal ingresos = pagoService.calcularIngresosPorCurso(cursoId);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular ingresos por docente
    @GetMapping("/docente/{docenteId}/ingresos")
    public ResponseEntity<BigDecimal> calcularIngresosPorDocente(@PathVariable Integer docenteId) {
        try {
            BigDecimal ingresos = pagoService.calcularIngresosPorDocente(docenteId);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas de pagos
    @GetMapping("/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas() {
        try {
            Object estadisticas = pagoService.obtenerEstadisticasPagos();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Calcular ingresos en período
    @GetMapping("/ingresos-periodo")
    public ResponseEntity<BigDecimal> calcularIngresosEnPeriodo(@RequestParam String fechaInicio, @RequestParam String fechaFin) {
        try {
            BigDecimal ingresos = pagoService.calcularIngresosEnPeriodo(fechaInicio, fechaFin);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}