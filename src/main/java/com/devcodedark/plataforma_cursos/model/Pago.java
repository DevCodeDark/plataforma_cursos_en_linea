package com.devcodedark.plataforma_cursos.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@SQLDelete(sql = "UPDATE pagos SET estado = 'cancelado' WHERE id = ?")
@Where(clause = "estado != 'cancelado'")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La inscripción es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;
    
    @Size(max = 3, message = "La moneda no puede exceder 3 caracteres")
    private String moneda = "USD";
    
    @NotNull(message = "El método de pago es obligatorio")
    @Column(name = "metodo_pago")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.pendiente;
    
    @Column(name = "transaccion_externa_id")
    private String transaccionExternaId;
    
    @Column(name = "datos_pago", columnDefinition = "TEXT")
    private String datosPago; // JSON como texto
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Enums
    public enum MetodoPago {
        stripe, paypal, transferencia
    }
    
    public enum EstadoPago {
        pendiente, completado, fallido, reembolsado
    }

    // Constructores
    public Pago() {}
    
    public Pago(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getTransaccionExternaId() {
        return transaccionExternaId;
    }

    public void setTransaccionExternaId(String transaccionExternaId) {
        this.transaccionExternaId = transaccionExternaId;
    }

    public String getDatosPago() {
        return datosPago;
    }

    public void setDatosPago(String datosPago) {
        this.datosPago = datosPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @PrePersist
    protected void onCreate() {
        fechaPago = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Pago [id=" + id + ", monto=" + monto + ", metodoPago=" + metodoPago + 
               ", estado=" + estado + ", fechaPago=" + fechaPago + "]";
    }
}