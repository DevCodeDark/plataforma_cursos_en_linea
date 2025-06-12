package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.PagoDTO;
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

    // Métodos de conversión entre Entity y DTO
    private PagoDTO convertToDTO(Pago pago) {
        if (pago == null) {
            return null;
        }
        
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setInscripcionId(pago.getInscripcion() != null ? pago.getInscripcion().getId() : null);
        
        // Información adicional de la inscripción
        if (pago.getInscripcion() != null) {
            if (pago.getInscripcion().getEstudiante() != null) {
                dto.setEstudianteNombre(pago.getInscripcion().getEstudiante().getNombre() + " " + 
                                      pago.getInscripcion().getEstudiante().getApellido());
                dto.setEstudianteEmail(pago.getInscripcion().getEstudiante().getEmail());
            }
            
            if (pago.getInscripcion().getCurso() != null) {
                dto.setCursoTitulo(pago.getInscripcion().getCurso().getTitulo());
                
                if (pago.getInscripcion().getCurso().getDocente() != null) {
                    dto.setDocenteNombre(pago.getInscripcion().getCurso().getDocente().getNombre() + " " + 
                                       pago.getInscripcion().getCurso().getDocente().getApellido());
                }
            }
        }
        
        dto.setMonto(pago.getMonto());
        dto.setMoneda(pago.getMoneda());
        dto.setMetodoPago(pago.getMetodoPago() != null ? pago.getMetodoPago().name() : null);
        dto.setEstado(pago.getEstado() != null ? pago.getEstado().name() : null);
        dto.setTransaccionExternaId(pago.getTransaccionExternaId());
        dto.setDatosPago(pago.getDatosPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setFechaActualizacion(pago.getFechaActualizacion());
        
        // Campos calculados
        if (pago.getFechaPago() != null) {
            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(pago.getFechaPago()));
        }
        
        dto.setEsReembolsable(pago.getEstado() == EstadoPago.completado);
        dto.setEstadoDescripcion(obtenerDescripcionEstado(pago.getEstado()));
        dto.setMetodoPagoDescripcion(obtenerDescripcionMetodoPago(pago.getMetodoPago()));
        
        return dto;
    }
    
    private Pago convertToEntity(PagoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Pago pago = new Pago();
        pago.setId(dto.getId());
        pago.setMonto(dto.getMonto());
        pago.setMoneda(dto.getMoneda());
        
        if (dto.getMetodoPago() != null) {
            pago.setMetodoPago(MetodoPago.valueOf(dto.getMetodoPago()));
        }
        
        if (dto.getEstado() != null) {
            pago.setEstado(EstadoPago.valueOf(dto.getEstado()));
        }
        
        pago.setTransaccionExternaId(dto.getTransaccionExternaId());
        pago.setDatosPago(dto.getDatosPago());
        pago.setFechaPago(dto.getFechaPago());
        pago.setFechaActualizacion(dto.getFechaActualizacion());
        
        // Cargar inscripción
        if (dto.getInscripcionId() != null) {
            Optional<Inscripcion> inscripcion = inscripcionRepository.findById(dto.getInscripcionId());
            if (inscripcion.isPresent()) {
                pago.setInscripcion(inscripcion.get());
            } else {
                throw new IllegalArgumentException("Inscripción con ID " + dto.getInscripcionId() + " no encontrada");
            }
        }
        
        return pago;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void guardar(PagoDTO pagoDTO) {
        Pago pago = convertToEntity(pagoDTO);
        pagoRepository.save(pago);
    }

    @Override
    public void modificar(PagoDTO pagoDTO) {
        Pago pago = convertToEntity(pagoDTO);
        pagoRepository.save(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoDTO> buscarId(Integer id) {
        return pagoRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorInscripcion(Integer inscripcionId) {
        return pagoRepository.findByInscripcionId(inscripcionId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorEstudiante(Integer estudianteId) {
        return pagoRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorEstado(EstadoPago estado) {
        return pagoRepository.findByEstado(estado)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorMetodoPago(MetodoPago metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoDTO> buscarPorTransaccionExterna(String transaccionExternaId) {
        return pagoRepository.findByTransaccionExternaId(transaccionExternaId)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPagosCompletadosPorEstudiante(Integer estudianteId) {
        return pagoRepository.findPagosCompletadosByEstudiante(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .toList();
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
    public List<PagoDTO> buscarPagosPendientes() {
        return pagoRepository.findPagosPendientes()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeTransaccionExterna(String transaccionExternaId) {
        return pagoRepository.existsByTransaccionExternaId(transaccionExternaId);
    }

    @Override
    public PagoDTO procesarPago(Integer inscripcionId, BigDecimal monto, MetodoPago metodoPago, String transaccionExternaId) {
        // Verificar que la inscripción existe
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new IllegalArgumentException("Inscripción no encontrada");
        }
        
        // Verificar que no exista ya una transacción con el mismo ID externo
        if (transaccionExternaId != null && existeTransaccionExterna(transaccionExternaId)) {
            throw new IllegalArgumentException("Ya existe una transacción con este ID externo");
        }
        
        Pago pago = new Pago();
        pago.setInscripcion(inscripcionOpt.get());
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado(EstadoPago.pendiente);
        pago.setTransaccionExternaId(transaccionExternaId);
        pago.setFechaPago(LocalDateTime.now());
        
        Pago pagoGuardado = pagoRepository.save(pago);
        return convertToDTO(pagoGuardado);
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
        } else {
            throw new IllegalArgumentException("Pago con ID " + pagoId + " no encontrado");
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
        } else {
            throw new IllegalArgumentException("Pago con ID " + pagoId + " no encontrado");
        }
    }

    @Override
    public void procesarReembolso(Integer pagoId, String razon) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            if (pago.getEstado() != EstadoPago.completado) {
                throw new IllegalArgumentException("Solo se pueden reembolsar pagos completados");
            }
            pago.setEstado(EstadoPago.reembolsado);
            pago.setDatosPago(razon);
            pagoRepository.save(pago);
        } else {
            throw new IllegalArgumentException("Pago con ID " + pagoId + " no encontrado");
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
    
    // Métodos helper
    private String calcularTiempoTranscurrido(LocalDateTime fechaPago) {
        if (fechaPago == null) {
            return "N/A";
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        long dias = ChronoUnit.DAYS.between(fechaPago, ahora);
        
        if (dias == 0) {
            long horas = ChronoUnit.HOURS.between(fechaPago, ahora);
            if (horas == 0) {
                long minutos = ChronoUnit.MINUTES.between(fechaPago, ahora);
                return minutos + " minuto(s)";
            }
            return horas + " hora(s)";
        } else if (dias < 30) {
            return dias + " día(s)";
        } else if (dias < 365) {
            return (dias / 30) + " mes(es)";
        } else {
            return (dias / 365) + " año(s)";
        }
    }
    
    private String obtenerDescripcionEstado(EstadoPago estado) {
        if (estado == null) return "N/A";
        
        return switch (estado) {
            case pendiente -> "Pendiente de Procesamiento";
            case completado -> "Pago Completado";
            case fallido -> "Pago Fallido";
            case reembolsado -> "Pago Reembolsado";
        };
    }
    
    private String obtenerDescripcionMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) return "N/A";
        
        return switch (metodoPago) {
            case stripe -> "Stripe (Tarjeta de Crédito)";
            case paypal -> "PayPal";
            case transferencia -> "Transferencia Bancaria";
        };
    }
}