package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Pago;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Pago.EstadoPago;
import com.devcodedark.plataforma_cursos.model.Pago.MetodoPago;
import com.devcodedark.plataforma_cursos.repository.PagoRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.service.IPagoService;

@Service
@Transactional
public class PagoServiceJpa implements IPagoService {

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public void guardar(Pago pago) {
        pagoRepository.save(pago);
    }

    @Override
    public void modificar(Pago pago) {
        pagoRepository.save(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> buscarId(Integer id) {
        return pagoRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPorInscripcion(Integer inscripcionId) {
        return pagoRepository.findByInscripcionId(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPorEstudiante(Integer estudianteId) {
        return pagoRepository.findByEstudianteId(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPorEstado(EstadoPago estado) {
        return pagoRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPorMetodoPago(MetodoPago metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> buscarPorTransaccionExterna(String transaccionExternaId) {
        return pagoRepository.findByTransaccionExternaId(transaccionExternaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPagosCompletadosPorEstudiante(Integer estudianteId) {
        return pagoRepository.findPagosCompletadosByEstudiante(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularIngresosPorCurso(Integer cursoId) {
        BigDecimal ingresos = pagoRepository.sumIngresosByCurso(cursoId);
        return ingresos != null ? ingresos : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularIngresosPorDocente(Integer docenteId) {
        BigDecimal ingresos = pagoRepository.sumIngresosByDocente(docenteId);
        return ingresos != null ? ingresos : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPorEstadoYCurso(Integer cursoId, EstadoPago estado) {
        return pagoRepository.countByEstadoAndCurso(cursoId, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> buscarPagosPendientes() {
        return pagoRepository.findPagosPendientes();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeTransaccionExterna(String transaccionExternaId) {
        return pagoRepository.existsByTransaccionExternaId(transaccionExternaId);
    }

    @Override
    public Pago procesarPago(Integer inscripcionId, BigDecimal monto, MetodoPago metodoPago, String transaccionExternaId) {
        // Verificar que la inscripción existe
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        
        // Verificar que no exista ya una transacción con el mismo ID externo
        if (transaccionExternaId != null && existeTransaccionExterna(transaccionExternaId)) {
            throw new RuntimeException("Ya existe una transacción con este ID externo");
        }
        
        Pago pago = new Pago();
        pago.setInscripcion(inscripcionOpt.get());
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado(EstadoPago.pendiente);
        pago.setTransaccionExternaId(transaccionExternaId);
        pago.setFechaPago(LocalDateTime.now());
        
        return pagoRepository.save(pago);
    }

    @Override
    public void confirmarPago(Integer pagoId, String transaccionExternaId) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setEstado(EstadoPago.completado);
            pago.setTransaccionExternaId(transaccionExternaId);
            pago.setFechaPago(LocalDateTime.now());
            pagoRepository.save(pago);
        }
    }

    @Override
    public void marcarComoFallido(Integer pagoId, String razon) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setEstado(EstadoPago.fallido);
            pago.setDatosPago(razon);
            pagoRepository.save(pago);
        }
    }

    @Override
    public void procesarReembolso(Integer pagoId, String razon) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            if (pago.getEstado() != EstadoPago.completado) {
                throw new RuntimeException("Solo se pueden reembolsar pagos completados");
            }
            pago.setEstado(EstadoPago.reembolsado);
            pago.setDatosPago(razon);
            pagoRepository.save(pago);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularIngresosEnPeriodo(String fechaInicio, String fechaFin) {
        // Convertir strings a LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio + " 00:00:00", formatter);
        LocalDateTime fin = LocalDateTime.parse(fechaFin + " 23:59:59", formatter);
        
        // Buscar todos los pagos completados en el período
        List<Pago> pagos = pagoRepository.findAll().stream()
            .filter(p -> p.getEstado() == EstadoPago.completado)
            .filter(p -> p.getFechaPago().isAfter(inicio) && p.getFechaPago().isBefore(fin))
            .toList();
        
        return pagos.stream()
            .map(Pago::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerEstadisticasPagos() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Total de pagos por estado
        Map<String, Long> pagosPorEstado = new HashMap<>();
        for (EstadoPago estado : EstadoPago.values()) {
            Long count = Integer.valueOf(pagoRepository.findByEstado(estado).size()).longValue();
            pagosPorEstado.put(estado.name(), count);
        }
        estadisticas.put("pagosPorEstado", pagosPorEstado);
        
        // Total de ingresos
        BigDecimal totalIngresos = pagoRepository.findByEstado(EstadoPago.completado).stream()
            .map(Pago::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        estadisticas.put("totalIngresos", totalIngresos);
        
        // Método de pago más usado
        Map<String, Long> pagosPorMetodo = new HashMap<>();
        for (MetodoPago metodo : MetodoPago.values()) {
            Long count = Integer.valueOf(pagoRepository.findByMetodoPago(metodo).size()).longValue();
            pagosPorMetodo.put(metodo.name(), count);
        }
        estadisticas.put("pagosPorMetodo", pagosPorMetodo);
        
        return estadisticas;
    }
}