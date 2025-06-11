package com.devcodedark.plataforma_cursos.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_modulos")
@SQLDelete(sql = "UPDATE progreso_modulos SET estado = 0 WHERE id = ?")
@Where(clause = "estado = 1")
public class ProgresoModulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La inscripción es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;
    
    @NotNull(message = "El módulo es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
    
    private Boolean completado = false;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;
    
    @Min(value = 0, message = "El tiempo invertido no puede ser negativo")
    @Column(name = "tiempo_invertido")
    private Integer tiempoInvertido = 0; // En segundos
    
    private Integer estado = 1;

    // Constructores
    public ProgresoModulo() {}
    
    public ProgresoModulo(Integer id) {
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

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
        if (completado && fechaCompletado == null) {
            fechaCompletado = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public Integer getTiempoInvertido() {
        return tiempoInvertido;
    }

    public void setTiempoInvertido(Integer tiempoInvertido) {
        this.tiempoInvertido = tiempoInvertido;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ProgresoModulo [id=" + id + ", completado=" + completado + 
               ", tiempoInvertido=" + tiempoInvertido + ", estado=" + estado + "]";
    }
}