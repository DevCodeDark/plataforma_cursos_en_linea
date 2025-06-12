package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class ProgresoMaterialDTO {
    private Integer id;
    
    @NotNull(message = "La inscripción ID es obligatoria")
    private Integer inscripcionId;
    
    @NotNull(message = "El material ID es obligatorio")
    private Integer materialId;
    
    // Información enriquecida de la inscripción
    private String estudianteNombre;
    private String estudianteEmail;
    private String cursoTitulo;
    
    // Información enriquecida del material
    private String materialTitulo;
    private String materialTipo;
    private String materialUrl;
    private Integer duracionVideo; // En segundos para videos
    private String moduloTitulo;
    
    private Boolean visualizado = false;
    
    @Min(value = 0, message = "El tiempo reproducido no puede ser negativo")
    private Integer tiempoReproducido = 0; // En segundos para videos
    
    private LocalDateTime fechaUltimoAcceso;
    
    private Boolean completado = false;
    
    // Campos calculados
    private Integer porcentajeCompletado; // 0-100
    private String tiempoRestante; // Formato legible
    private String estadoProgreso; // "No iniciado", "En progreso", "Completado"
    private Boolean esVideo;
    
    // Constructores
    public ProgresoMaterialDTO() {}

    public ProgresoMaterialDTO(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Integer inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getEstudianteEmail() {
        return estudianteEmail;
    }

    public void setEstudianteEmail(String estudianteEmail) {
        this.estudianteEmail = estudianteEmail;
    }

    public String getCursoTitulo() {
        return cursoTitulo;
    }

    public void setCursoTitulo(String cursoTitulo) {
        this.cursoTitulo = cursoTitulo;
    }

    public String getMaterialTitulo() {
        return materialTitulo;
    }

    public void setMaterialTitulo(String materialTitulo) {
        this.materialTitulo = materialTitulo;
    }

    public String getMaterialTipo() {
        return materialTipo;
    }

    public void setMaterialTipo(String materialTipo) {
        this.materialTipo = materialTipo;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public Integer getDuracionVideo() {
        return duracionVideo;
    }

    public void setDuracionVideo(Integer duracionVideo) {
        this.duracionVideo = duracionVideo;
    }

    public String getModuloTitulo() {
        return moduloTitulo;
    }

    public void setModuloTitulo(String moduloTitulo) {
        this.moduloTitulo = moduloTitulo;
    }

    public Boolean getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(Boolean visualizado) {
        this.visualizado = visualizado;
    }

    public Integer getTiempoReproducido() {
        return tiempoReproducido;
    }

    public void setTiempoReproducido(Integer tiempoReproducido) {
        this.tiempoReproducido = tiempoReproducido;
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

    public Integer getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(Integer porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    public String getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(String tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public String getEstadoProgreso() {
        return estadoProgreso;
    }

    public void setEstadoProgreso(String estadoProgreso) {
        this.estadoProgreso = estadoProgreso;
    }

    public Boolean getEsVideo() {
        return esVideo;
    }

    public void setEsVideo(Boolean esVideo) {
        this.esVideo = esVideo;
    }    // Métodos helper para cálculos
    public void calcularPorcentajeCompletado() {
        if (Boolean.TRUE.equals(completado)) {
            this.porcentajeCompletado = 100;
        } else if (duracionVideo != null && duracionVideo > 0) {
            this.porcentajeCompletado = Math.min(100, (tiempoReproducido * 100) / duracionVideo);
        } else if (Boolean.TRUE.equals(visualizado)) {
            this.porcentajeCompletado = 50; // Para materiales no video
        } else {
            this.porcentajeCompletado = 0;
        }
    }

    public void calcularTiempoRestante() {
        if (Boolean.TRUE.equals(completado) || duracionVideo == null || duracionVideo <= 0) {
            this.tiempoRestante = "0:00";
        } else {
            int restante = Math.max(0, duracionVideo - tiempoReproducido);
            int minutos = restante / 60;
            int segundos = restante % 60;
            this.tiempoRestante = String.format("%d:%02d", minutos, segundos);
        }
    }

    public void calcularEstadoProgreso() {
        if (Boolean.TRUE.equals(completado)) {
            this.estadoProgreso = "Completado";
        } else if (Boolean.TRUE.equals(visualizado) || tiempoReproducido > 0) {
            this.estadoProgreso = "En progreso";
        } else {
            this.estadoProgreso = "No iniciado";
        }
    }

    public void determinarEsVideo() {
        this.esVideo = materialTipo != null && 
                       (materialTipo.equalsIgnoreCase("video") || materialTipo.equalsIgnoreCase("VIDEO"));
    }

    @Override
    public String toString() {
        return "ProgresoMaterialDTO [id=" + id + ", inscripcionId=" + inscripcionId + 
               ", materialId=" + materialId + ", visualizado=" + visualizado + 
               ", tiempoReproducido=" + tiempoReproducido + ", completado=" + completado + "]";
    }
}
