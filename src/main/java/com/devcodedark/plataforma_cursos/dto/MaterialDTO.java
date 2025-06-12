package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class MaterialDTO {
    private Integer id;
    
    @NotNull(message = "El ID del módulo es obligatorio")
    private Integer moduloId;
    
    private String moduloTitulo;
    
    @NotBlank(message = "El título del material es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;
    
    @NotNull(message = "El tipo de material es obligatorio")
    private String tipo; // video, pdf, enlace, texto
    
    @Size(max = 1000, message = "La URL no puede exceder 1000 caracteres")
    private String url;
    
    private String archivoLocal;
    
    private String descripcion;
    
    private Integer duracion; // En segundos para videos
    
    private Long tamañoArchivo; // En bytes
    
    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser mayor a 0")
    private Integer orden;
    
    private Boolean esGratuito = false;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
    
    private Integer estado = 1;
    
    // Campos adicionales calculados
    private String cursoTitulo;
    private String tamañoFormateado;
    private String duracionFormateada;
    private Boolean esVideo;
    private Boolean esPdf;
    private Boolean esEnlace;
    private Boolean esTexto;
    private Integer totalProgresos;
    private Double porcentajeCompletado;
    
    // Constructores
    public MaterialDTO() {}
    
    public MaterialDTO(Integer id, Integer moduloId, String moduloTitulo, String titulo, 
                      String tipo, String url, String descripcion, Integer orden, 
                      Boolean esGratuito, LocalDateTime fechaCreacion) {
        this.id = id;
        this.moduloId = moduloId;
        this.moduloTitulo = moduloTitulo;
        this.titulo = titulo;
        this.tipo = tipo;
        this.url = url;
        this.descripcion = descripcion;
        this.orden = orden;
        this.esGratuito = esGratuito;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Constructor para crear nuevo material
    public MaterialDTO(Integer moduloId, String titulo, String tipo, String url, 
                      String descripcion, Integer orden, Boolean esGratuito) {
        this.moduloId = moduloId;
        this.titulo = titulo;
        this.tipo = tipo;
        this.url = url;
        this.descripcion = descripcion;
        this.orden = orden;
        this.esGratuito = esGratuito;
    }
    
    // Métodos helper
    public String getDuracionFormateada() {
        if (duracion == null || duracion == 0) {
            return "N/A";
        }
        
        int horas = duracion / 3600;
        int minutos = (duracion % 3600) / 60;
        int segundos = duracion % 60;
        
        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%d:%02d", minutos, segundos);
        }
    }
    
    public String getTamañoFormateado() {
        if (tamañoArchivo == null || tamañoArchivo == 0) {
            return "N/A";
        }
        
        if (tamañoArchivo < 1024) {
            return tamañoArchivo + " B";
        } else if (tamañoArchivo < 1024 * 1024) {
            return String.format("%.1f KB", tamañoArchivo / 1024.0);
        } else if (tamañoArchivo < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", tamañoArchivo / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", tamañoArchivo / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    public Boolean getEsVideo() {
        return "video".equals(this.tipo);
    }
    
    public Boolean getEsPdf() {
        return "pdf".equals(this.tipo);
    }
    
    public Boolean getEsEnlace() {
        return "enlace".equals(this.tipo);
    }
    
    public Boolean getEsTexto() {
        return "texto".equals(this.tipo);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuloId() {
        return moduloId;
    }

    public void setModuloId(Integer moduloId) {
        this.moduloId = moduloId;
    }

    public String getModuloTitulo() {
        return moduloTitulo;
    }

    public void setModuloTitulo(String moduloTitulo) {
        this.moduloTitulo = moduloTitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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

    public String getCursoTitulo() {
        return cursoTitulo;
    }

    public void setCursoTitulo(String cursoTitulo) {
        this.cursoTitulo = cursoTitulo;
    }

    public void setTamañoFormateado(String tamañoFormateado) {
        this.tamañoFormateado = tamañoFormateado;
    }

    public void setDuracionFormateada(String duracionFormateada) {
        this.duracionFormateada = duracionFormateada;
    }

    public void setEsVideo(Boolean esVideo) {
        this.esVideo = esVideo;
    }

    public void setEsPdf(Boolean esPdf) {
        this.esPdf = esPdf;
    }

    public void setEsEnlace(Boolean esEnlace) {
        this.esEnlace = esEnlace;
    }

    public void setEsTexto(Boolean esTexto) {
        this.esTexto = esTexto;
    }

    public Integer getTotalProgresos() {
        return totalProgresos;
    }

    public void setTotalProgresos(Integer totalProgresos) {
        this.totalProgresos = totalProgresos;
    }

    public Double getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(Double porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "id=" + id +
                ", moduloId=" + moduloId +
                ", moduloTitulo='" + moduloTitulo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", orden=" + orden +
                ", esGratuito=" + esGratuito +
                ", estado=" + estado +
                '}';
    }
}
