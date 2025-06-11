package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.model.Pago;
import com.devcodedark.plataforma_cursos.model.Pago.EstadoPago;
import com.devcodedark.plataforma_cursos.model.Pago.MetodoPago;

public interface IPagoService {
    // Listar todos los pagos
    List<Pago> buscarTodos();
    
    // Guardar pago
    void guardar(Pago pago);
    
    // Modificar pago
    void modificar(Pago pago);
    
    // Buscar pago por ID
    Optional<Pago> buscarId(Integer id);
    
    // Eliminar pago
    void eliminar(Integer id);
    
    // Buscar pagos por inscripción
    List<Pago> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar pagos por estudiante
    List<Pago> buscarPorEstudiante(Integer estudianteId);
    
    // Buscar pagos por estado
    List<Pago> buscarPorEstado(EstadoPago estado);
    
    // Buscar pagos por método de pago
    List<Pago> buscarPorMetodoPago(MetodoPago metodoPago);
    
    // Buscar pago por transacción externa
    Optional<Pago> buscarPorTransaccionExterna(String transaccionExternaId);
    
    // Buscar pagos completados por estudiante
    List<Pago> buscarPagosCompletadosPorEstudiante(Integer estudianteId);
    
    // Calcular total de ingresos por curso
    BigDecimal calcularIngresosPorCurso(Integer cursoId);
    
    // Calcular total de ingresos por docente
    BigDecimal calcularIngresosPorDocente(Integer docenteId);
    
    // Contar pagos por estado y curso
    Long contarPorEstadoYCurso(Integer cursoId, EstadoPago estado);
    
    // Buscar pagos pendientes
    List<Pago> buscarPagosPendientes();
    
    // Verificar si existe transacción externa
    boolean existeTransaccionExterna(String transaccionExternaId);
    
    // Procesar pago
    Pago procesarPago(Integer inscripcionId, BigDecimal monto, MetodoPago metodoPago, String transaccionExternaId);
    
    // Confirmar pago
    void confirmarPago(Integer pagoId, String transaccionExternaId);
    
    // Marcar pago como fallido
    void marcarComoFallido(Integer pagoId, String razon);
    
    // Procesar reembolso
    void procesarReembolso(Integer pagoId, String razon);
    
    // Calcular total de ingresos en un período
    BigDecimal calcularIngresosEnPeriodo(String fechaInicio, String fechaFin);
    
    // Obtener estadísticas de pagos
    Object obtenerEstadisticasPagos();
}