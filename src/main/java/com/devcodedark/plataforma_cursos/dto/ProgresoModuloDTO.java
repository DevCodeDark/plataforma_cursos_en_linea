package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class ProgresoModuloDTO {
    private Integer id;
    
    @NotNull(message = "La inscripción ID es obligatoria")
    private Integer inscripcionId;
    
    @NotNull(message = "El módulo ID es obligatorio")
    private Integer moduloId;
    
    // Información enriquecida de la inscripción
    private String estudianteNombre;
    private String estudianteEmail;
    private String cursoTitulo;
    
    // Información enriquecida del módulo
    private String moduloTitulo;
    private String moduloDescripcion;
    private Integer moduloOrden;
    private Integer moduloDuracionEstimada;
    
    private Boolean completado = false;
    
    private LocalDateTime fechaInicio;
    
    private LocalDateTime fechaCompletado;
    
    @Min(value = 0, message = "El tiempo invertido no puede ser negativo")
    private Integer tiempoInvertido = 0; // En segundos
    
    // Campos calculados
    private Integer porcentajeCompletado; // 0-100
    private String tiempoInvertidoFormateado; // Formato legible
    private String estadoProgreso; // "No iniciado", "En progreso", "Completado"
    private Long diasTranscurridos; // Días desde el inicio
    private Boolean esUltimoModulo;
    private Integer totalMateriales;
    private Integer materialesCompletados;
    private Double progresoMateriales; // Porcentaje de materiales completados
    
    // Constructores
    public ProgresoModuloDTO() {}

    public ProgresoModuloDTO(Integer id) {
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

    public Integer getModuloId() {
        return moduloId;
    }

    public void setModuloId(Integer moduloId) {
        this.moduloId = moduloId;
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

    public String getModuloTitulo() {
        return moduloTitulo;
    }

    public void setModuloTitulo(String moduloTitulo) {
        this.moduloTitulo = moduloTitulo;
    }

    public String getModuloDescripcion() {
        return moduloDescripcion;
    }

    public void setModuloDescripcion(String moduloDescripcion) {
        this.moduloDescripcion = moduloDescripcion;
    }

    public Integer getModuloOrden() {
        return moduloOrden;
    }

    public void setModuloOrden(Integer moduloOrden) {
        this.moduloOrden = moduloOrden;
    }

    public Integer getModuloDuracionEstimada() {
        return moduloDuracionEstimada;
    }

    public void setModuloDuracionEstimada(Integer moduloDuracionEstimada) {
        this.moduloDuracionEstimada = moduloDuracionEstimada;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
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

    public Integer getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(Integer porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    public String getTiempoInvertidoFormateado() {
        return tiempoInvertidoFormateado;
    }

    public void setTiempoInvertidoFormateado(String tiempoInvertidoFormateado) {
        this.tiempoInvertidoFormateado = tiempoInvertidoFormateado;
    }

    public String getEstadoProgreso() {
        return estadoProgreso;
    }

    public void setEstadoProgreso(String estadoProgreso) {
        this.estadoProgreso = estadoProgreso;
    }

    public Long getDiasTranscurridos() {
        return diasTranscurridos;
    }

    public void setDiasTranscurridos(Long diasTranscurridos) {
        this.diasTranscurridos = diasTranscurridos;
    }

    public Boolean getEsUltimoModulo() {
        return esUltimoModulo;
    }

    public void setEsUltimoModulo(Boolean esUltimoModulo) {
        this.esUltimoModulo = esUltimoModulo;
    }

    public Integer getTotalMateriales() {
        return totalMateriales;
    }

    public void setTotalMateriales(Integer totalMateriales) {
        this.totalMateriales = totalMateriales;
    }

    public Integer getMaterialesCompletados() {
        return materialesCompletados;
    }

    public void setMaterialesCompletados(Integer materialesCompletados) {
        this.materialesCompletados = materialesCompletados;
    }

    public Double getProgresoMateriales() {
        return progresoMateriales;
    }

    public void setProgresoMateriales(Double progresoMateriales) {
        this.progresoMateriales = progresoMateriales;
    }

    // Métodos helper para cálculos
    public void calcularPorcentajeCompletado() {
        if (completado != null && completado) {
            this.porcentajeCompletado = 100;
        } else if (materialesCompletados != null && totalMateriales != null && totalMateriales > 0) {
            this.porcentajeCompletado = (int) Math.round((materialesCompletados.doubleValue() / totalMateriales.doubleValue()) * 100);
        } else {
            this.porcentajeCompletado = 0;
        }
    }

    public void formatearTiempoInvertido() {
        if (tiempoInvertido == null || tiempoInvertido == 0) {
            this.tiempoInvertidoFormateado = "0 min";
        } else {
            int horas = tiempoInvertido / 3600;
            int minutos = (tiempoInvertido % 3600) / 60;
            
            if (horas > 0) {
                this.tiempoInvertidoFormateado = String.format("%d h %d min", horas, minutos);
            } else {
                this.tiempoInvertidoFormateado = String.format("%d min", minutos);
            }
        }
    }

    public void calcularEstadoProgreso() {
        if (completado != null && completado) {
            this.estadoProgreso = "Completado";
        } else if (fechaInicio != null) {
            this.estadoProgreso = "En progreso";
        } else {
            this.estadoProgreso = "No iniciado";
        }
    }

    public void calcularDiasTranscurridos() {
        if (fechaInicio != null) {
            LocalDateTime ahora = LocalDateTime.now();
            this.diasTranscurridos = java.time.Duration.between(fechaInicio, ahora).toDays();
        } else {
            this.diasTranscurridos = 0L;
        }
    }

    public void calcularProgresoMateriales() {
        if (materialesCompletados != null && totalMateriales != null && totalMateriales > 0) {
            this.progresoMateriales = (materialesCompletados.doubleValue() / totalMateriales.doubleValue()) * 100.0;
        } else {
            this.progresoMateriales = 0.0;
        }
    }

    @Override
    public String toString() {
        return "ProgresoModuloDTO [id=" + id + ", inscripcionId=" + inscripcionId + 
               ", moduloId=" + moduloId + ", completado=" + completado + 
               ", tiempoInvertido=" + tiempoInvertido + "]";
    }
}
