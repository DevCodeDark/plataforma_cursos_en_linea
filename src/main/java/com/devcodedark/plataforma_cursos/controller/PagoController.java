package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.dto.PagoDTO;
import com.devcodedark.plataforma_cursos.model.Pago.EstadoPago;
import com.devcodedark.plataforma_cursos.model.Pago.MetodoPago;
import com.devcodedark.plataforma_cursos.service.IPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private static final String ERROR_VALIDACION = "Error de validación: ";

    @Autowired
    private IPagoService pagoService;// Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        try {
            List<PagoDTO> pagos = pagoService.buscarTodos();
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<PagoDTO> pago = pagoService.buscarId(id);
            if (pago.isPresent()) {
                return ResponseEntity.ok(pago.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo pago
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody PagoDTO pagoDTO) {
        try {
            pagoService.guardar(pagoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago creado exitosamente");        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
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
            PagoDTO pagoDTO = pagoService.procesarPago(inscripcionId, monto, metodoPago, transaccionExternaId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Pago procesado exitosamente. ID: " + pagoDTO.getId());
        } catch (IllegalArgumentException e) {            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar el pago: " + e.getMessage());
        }
    }

    // Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody PagoDTO pagoDTO) {
        try {
            Optional<PagoDTO> pagoExistente = pagoService.buscarId(id);
            if (!pagoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoDTO.setId(id);
            pagoService.modificar(pagoDTO);
            return ResponseEntity.ok("Pago actualizado exitosamente");        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el pago: " + e.getMessage());
        }
    }    
    
    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<PagoDTO> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.eliminar(id);
            return ResponseEntity.ok("Pago eliminado exitosamente");        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el pago: " + e.getMessage());
        }
    }

    // Buscar pagos por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<List<PagoDTO>> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            List<PagoDTO> pagos = pagoService.buscarPorInscripcion(inscripcionId);
            return ResponseEntity.ok(pagos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PagoDTO>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<PagoDTO> pagos = pagoService.buscarPorEstudiante(estudianteId);
            return ResponseEntity.ok(pagos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> buscarPorEstado(@PathVariable EstadoPago estado) {
        try {
            List<PagoDTO> pagos = pagoService.buscarPorEstado(estado);
            return ResponseEntity.ok(pagos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar pagos pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<PagoDTO>> listarPendientes() {
        try {
            List<PagoDTO> pagos = pagoService.buscarPagosPendientes();
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    
    
    // Confirmar pago
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<String> confirmarPago(@PathVariable Integer id, @RequestParam String transaccionExternaId) {
        try {
            Optional<PagoDTO> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.confirmarPago(id, transaccionExternaId);
            return ResponseEntity.ok("Pago confirmado exitosamente");
        } catch (IllegalArgumentException e) {            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al confirmar el pago: " + e.getMessage());
        }
    }

    // Marcar pago como fallido
    @PutMapping("/{id}/fallar")
    public ResponseEntity<String> marcarComoFallido(@PathVariable Integer id, @RequestBody String razon) {
        try {
            Optional<PagoDTO> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.marcarComoFallido(id, razon);
            return ResponseEntity.ok("Pago marcado como fallido");        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al marcar el pago como fallido: " + e.getMessage());
        }
    }

    // Procesar reembolso
    @PutMapping("/{id}/reembolsar")
    public ResponseEntity<String> procesarReembolso(@PathVariable Integer id, @RequestBody String razon) {
        try {
            Optional<PagoDTO> pago = pagoService.buscarId(id);
            if (!pago.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            pagoService.procesarReembolso(id, razon);
            return ResponseEntity.ok("Reembolso procesado exitosamente");
        } catch (IllegalArgumentException e) {            return ResponseEntity.badRequest().body(ERROR_VALIDACION + e.getMessage());
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