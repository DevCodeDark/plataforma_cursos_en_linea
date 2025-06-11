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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "materiales")
@SQLDelete(sql = "UPDATE materiales SET estado = 0 WHERE id = ?")
@Where(clause = "estado = 1")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El módulo es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
    
    @NotBlank(message = "El título del material es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    @NotNull(message = "El tipo de material es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipo;
    
    @Size(max = 1000, message = "La URL no puede exceder 1000 caracteres")
    private String url;
    
    @Column(name = "archivo_local")
    private String archivoLocal;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private Integer duracion; // En segundos para videos
    
    @Column(name = "tamaño_archivo")
    private Long tamañoArchivo; // En bytes
    
    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser mayor a 0")
    private Integer orden;
    
    @Column(name = "es_gratuito")
    private Boolean esGratuito = false;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    private Integer estado = 1;
    
    // Relaciones
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgresoMaterial> progresosMaterial;

    // Enum
    public enum TipoMaterial {
        video, pdf, enlace, texto
    }

    // Constructores
    public Material() {}
    
    public Material(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArchivoLocal() {
        return archivoLocal;
    }

    public void setArchivoLocal(String archivoLocal) {
        this.archivoLocal = archivoLocal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Long getTamañoArchivo() {
        return tamañoArchivo;
    }

    public void setTamañoArchivo(Long tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getEsGratuito() {
        return esGratuito;
    }

    public void setEsGratuito(Boolean esGratuito) {
        this.esGratuito = esGratuito;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<ProgresoMaterial> getProgresosMaterial() {
        return progresosMaterial;
    }

    public void setProgresosMaterial(List<ProgresoMaterial> progresosMaterial) {
        this.progresosMaterial = progresosMaterial;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Material [id=" + id + ", titulo=" + titulo + ", tipo=" + tipo + 
               ", orden=" + orden + ", estado=" + estado + "]";
    }
}