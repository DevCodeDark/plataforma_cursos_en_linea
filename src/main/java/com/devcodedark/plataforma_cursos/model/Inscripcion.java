package com.devcodedark.plataforma_cursos.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inscripciones")
@SQLDelete(sql = "UPDATE inscripciones SET estado = 'cancelado' WHERE id = ?")
@Where(clause = "estado != 'cancelado'")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;
    
    @NotNull(message = "El curso es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.activo;
    
    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;
    
    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;
    
    @Column(name = "precio_pagado", precision = 10, scale = 2)
    private BigDecimal precioPagado = BigDecimal.ZERO;
    
    @Column(name = "metodo_pago")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago = MetodoPago.gratuito;
    
    @Column(name = "transaccion_id")
    private String transaccionId;
    
    @DecimalMin(value = "0.00", message = "El progreso no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El progreso no puede exceder 100%")
    @Column(name = "progreso_porcentaje", precision = 5, scale = 2)
    private BigDecimal progresoPorcentaje = BigDecimal.ZERO;
    
    @Column(name = "certificado_generado")
    private Boolean certificadoGenerado = false;
    
    @Column(name = "fecha_certificado")
    private LocalDateTime fechaCertificado;
    
    // Relaciones
    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgresoModulo> progresosModulo;
    
    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgresoMaterial> progresosMaterial;
    
    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos;
    
    @OneToOne(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Certificado certificado;

    // Enums
    public enum EstadoInscripcion {
        activo, finalizado, cancelado, suspendido
    }
    
    public enum MetodoPago {
        gratuito, stripe, paypal, transferencia
    }

    // Constructores
    public Inscripcion() {}
    
    public Inscripcion(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public BigDecimal getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(BigDecimal precioPagado) {
        this.precioPagado = precioPagado;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public BigDecimal getProgresoPorcentaje() {
        return progresoPorcentaje;
    }

    public void setProgresoPorcentaje(BigDecimal progresoPorcentaje) {
        this.progresoPorcentaje = progresoPorcentaje;
    }

    public Boolean getCertificadoGenerado() {
        return certificadoGenerado;
    }

    public void setCertificadoGenerado(Boolean certificadoGenerado) {
        this.certificadoGenerado = certificadoGenerado;
    }

    public LocalDateTime getFechaCertificado() {
        return fechaCertificado;
    }

    public void setFechaCertificado(LocalDateTime fechaCertificado) {
        this.fechaCertificado = fechaCertificado;
    }

    public List<ProgresoModulo> getProgresosModulo() {
        return progresosModulo;
    }

    public void setProgresosModulo(List<ProgresoModulo> progresosModulo) {
        this.progresosModulo = progresosModulo;
    }

    public List<ProgresoMaterial> getProgresosMaterial() {
        return progresosMaterial;
    }

    public void setProgresosMaterial(List<ProgresoMaterial> progresosMaterial) {
        this.progresosMaterial = progresosMaterial;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    @PrePersist
    protected void onCreate() {
        fechaInscripcion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Inscripcion [id=" + id + ", estado=" + estado + ", progresoPorcentaje=" + 
               progresoPorcentaje + ", certificadoGenerado=" + certificadoGenerado + "]";
    }
}