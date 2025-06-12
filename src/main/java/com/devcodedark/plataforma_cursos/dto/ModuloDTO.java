package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public class ModuloDTO {
    private Integer id;
    
    @NotNull(message = "El curso ID es obligatorio")
    private Integer cursoId;
    
    // Información adicional del curso
    private String cursoTitulo;
    private String cursoCategoria;
    
    @NotBlank(message = "El título del módulo es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    private String descripcion;
    
    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser mayor a 0")
    private Integer orden;
    
    private Integer duracion; // En minutos
    
    private Boolean esObligatorio = true;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
    
    private Integer estado = 1;
    
    // Campos calculados
    private Integer totalMateriales;
    private Integer totalMaterialesGratuitos;
    private Integer duracionTotalVideos;
    private Boolean tieneProgresos;

    // Constructores
    public ModuloDTO() {}

    public ModuloDTO(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }

    public String getCursoTitulo() {
        return cursoTitulo;
    }

    public void setCursoTitulo(String cursoTitulo) {
        this.cursoTitulo = cursoTitulo;
    }

    public String getCursoCategoria() {
        return cursoCategoria;
    }

    public void setCursoCategoria(String cursoCategoria) {
        this.cursoCategoria = cursoCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Boolean getEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(Boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
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

    public Integer getTotalMateriales() {
        return totalMateriales;
    }

    public void setTotalMateriales(Integer totalMateriales) {
        this.totalMateriales = totalMateriales;
    }

    public Integer getTotalMaterialesGratuitos() {
        return totalMaterialesGratuitos;
    }

    public void setTotalMaterialesGratuitos(Integer totalMaterialesGratuitos) {
        this.totalMaterialesGratuitos = totalMaterialesGratuitos;
    }

    public Integer getDuracionTotalVideos() {
        return duracionTotalVideos;
    }

    public void setDuracionTotalVideos(Integer duracionTotalVideos) {
        this.duracionTotalVideos = duracionTotalVideos;
    }

    public Boolean getTieneProgresos() {
        return tieneProgresos;
    }

    public void setTieneProgresos(Boolean tieneProgresos) {
        this.tieneProgresos = tieneProgresos;
    }

    @Override
    public String toString() {
        return "ModuloDTO [id=" + id + ", cursoId=" + cursoId + ", titulo=" + titulo + 
               ", orden=" + orden + ", duracion=" + duracion + ", estado=" + estado + "]";
    }
}
