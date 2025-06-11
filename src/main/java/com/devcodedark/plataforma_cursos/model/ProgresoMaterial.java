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
@Table(name = "progreso_materiales")
@SQLDelete(sql = "UPDATE progreso_materiales SET estado = 0 WHERE id = ?")
@Where(clause = "estado = 1")
public class ProgresoMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La inscripción es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;
    
    @NotNull(message = "El material es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;
    
    private Boolean visualizado = false;
    
    @Min(value = 0, message = "El tiempo reproducido no puede ser negativo")
    @Column(name = "tiempo_reproducido")
    private Integer tiempoReproducido = 0; // En segundos para videos
    
    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;
    
    private Boolean completado = false;
    
    private Integer estado = 1;

    // Constructores
    public ProgresoMaterial() {}
    
    public ProgresoMaterial(Integer id) {
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Boolean getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(Boolean visualizado) {
        this.visualizado = visualizado;
        if (visualizado) {
            fechaUltimoAcceso = LocalDateTime.now();
        }
    }

    public Integer getTiempoReproducido() {
        return tiempoReproducido;
    }

    public void setTiempoReproducido(Integer tiempoReproducido) {
        this.tiempoReproducido = tiempoReproducido;
        fechaUltimoAcceso = LocalDateTime.now();
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ProgresoMaterial [id=" + id + ", visualizado=" + visualizado + 
               ", tiempoReproducido=" + tiempoReproducido + ", completado=" + completado + "]";
    }
}