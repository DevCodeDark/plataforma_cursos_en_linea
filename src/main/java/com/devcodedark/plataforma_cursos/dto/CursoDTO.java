package com.devcodedark.plataforma_cursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CursoDTO {
    private Integer id;
    
    @NotBlank(message = "El título del curso es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    @NotBlank(message = "La descripción corta es obligatoria")
    private String descripcionCorta;
    
    private String descripcionCompleta;
    private String imagenPortada;
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;
    private String categoriaNombre;
    
    @NotNull(message = "El docente es obligatorio")
    private Integer docenteId;
    private String docenteNombre;
    private String docenteEmail;
    
    @NotNull(message = "El nivel es obligatorio")
    private String nivel; // básico, intermedio, avanzado
    
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precio = BigDecimal.ZERO;
    
    private Boolean esGratuito = true;
    private Integer duracionEstimada;
    private String estado; // borrador, publicado, pausado, eliminado
    
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para DTO
    private Integer totalModulos = 0;
    private Integer totalInscripciones = 0;
    private BigDecimal promedioCalificaciones = BigDecimal.ZERO;
    private Integer totalCalificaciones = 0;
    private Boolean puedeInscribirse = true;
    
    // Constructores
    public CursoDTO() {}
    
    public CursoDTO(Integer id, String titulo, String descripcionCorta, String nivel, 
                   BigDecimal precio, Boolean esGratuito, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionCorta = descripcionCorta;
        this.nivel = nivel;
        this.precio = precio;
        this.esGratuito = esGratuito;
        this.estado = estado;
    }
    
    // Constructor con información básica para listados
    public CursoDTO(Integer id, String titulo, String descripcionCorta, String categoriaNombre,
                   String docenteNombre, String nivel, BigDecimal precio, Boolean esGratuito,
                   String estado, Integer totalInscripciones) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionCorta = descripcionCorta;
        this.categoriaNombre = categoriaNombre;
        this.docenteNombre = docenteNombre;
        this.nivel = nivel;
        this.precio = precio;
        this.esGratuito = esGratuito;
        this.estado = estado;
        this.totalInscripciones = totalInscripciones;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcionCompleta() {
        return descripcionCompleta;
    }

    public void setDescripcionCompleta(String descripcionCompleta) {
        this.descripcionCompleta = descripcionCompleta;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Integer getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getDocenteEmail() {
        return docenteEmail;
    }

    public void setDocenteEmail(String docenteEmail) {
        this.docenteEmail = docenteEmail;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getEsGratuito() {
        return esGratuito;
    }

    public void setEsGratuito(Boolean esGratuito) {
        this.esGratuito = esGratuito;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
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

    public Integer getTotalModulos() {
        return totalModulos;
    }

    public void setTotalModulos(Integer totalModulos) {
        this.totalModulos = totalModulos;
    }

    public Integer getTotalInscripciones() {
        return totalInscripciones;
    }

    public void setTotalInscripciones(Integer totalInscripciones) {
        this.totalInscripciones = totalInscripciones;
    }

    public BigDecimal getPromedioCalificaciones() {
        return promedioCalificaciones;
    }

    public void setPromedioCalificaciones(BigDecimal promedioCalificaciones) {
        this.promedioCalificaciones = promedioCalificaciones;
    }

    public Integer getTotalCalificaciones() {
        return totalCalificaciones;
    }

    public void setTotalCalificaciones(Integer totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }

    public Boolean getPuedeInscribirse() {
        return puedeInscribirse;
    }

    public void setPuedeInscribirse(Boolean puedeInscribirse) {
        this.puedeInscribirse = puedeInscribirse;
    }

    @Override
    public String toString() {
        return "CursoDTO [id=" + id + ", titulo=" + titulo + ", nivel=" + nivel + 
               ", precio=" + precio + ", estado=" + estado + ", docenteNombre=" + docenteNombre + "]";
    }
}
