package com.devcodedark.plataforma_cursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class InscripcionDTO {
    private Integer id;
    
    @NotNull(message = "El estudiante es obligatorio")
    private Integer estudianteId;
    private String estudianteNombre;
    private String estudianteEmail;
    
    @NotNull(message = "El curso es obligatorio")
    private Integer cursoId;
    private String cursoTitulo;
    private String cursoImagenPortada;
    
    @NotNull(message = "El estado es obligatorio")
    private String estado; // activo, finalizado, cancelado, suspendido
    
    private LocalDateTime fechaInscripcion;
    private LocalDateTime fechaFinalizacion;
    
    @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
    private BigDecimal precioPagado = BigDecimal.ZERO;
    
    private String metodoPago; // gratuito, stripe, paypal, transferencia
    private String transaccionId;
    
    @DecimalMin(value = "0.00", message = "El progreso no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El progreso no puede exceder 100%")
    private BigDecimal progresoPorcentaje = BigDecimal.ZERO;
    
    private Boolean certificadoGenerado = false;
    private LocalDateTime fechaCertificado;
    
    // Campos adicionales para DTO
    private String docenteNombre;
    private String categoriaNombre;
    private Integer totalModulos = 0;
    private Integer modulosCompletados = 0;
    private Long tiempoTotalInvertido = 0L; // en minutos
    private Boolean puedeGenerarCertificado = false;
    private Integer diasTranscurridos = 0;
    private String nivelCurso;
    private BigDecimal precioCurso = BigDecimal.ZERO;
    
    // Constructores
    public InscripcionDTO() {}
    
    public InscripcionDTO(Integer id, String estado, BigDecimal progresoPorcentaje) {
        this.id = id;
        this.estado = estado;
        this.progresoPorcentaje = progresoPorcentaje;
    }
    
    // Constructor para listados con información básica
    public InscripcionDTO(Integer id, String estudianteNombre, String cursoTitulo, 
                         String estado, BigDecimal progresoPorcentaje, 
                         LocalDateTime fechaInscripcion, Boolean certificadoGenerado) {
        this.id = id;
        this.estudianteNombre = estudianteNombre;
        this.cursoTitulo = cursoTitulo;
        this.estado = estado;
        this.progresoPorcentaje = progresoPorcentaje;
        this.fechaInscripcion = fechaInscripcion;
        this.certificadoGenerado = certificadoGenerado;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Integer estudianteId) {
        this.estudianteId = estudianteId;
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

    public String getCursoImagenPortada() {
        return cursoImagenPortada;
    }

    public void setCursoImagenPortada(String cursoImagenPortada) {
        this.cursoImagenPortada = cursoImagenPortada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
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

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Integer getTotalModulos() {
        return totalModulos;
    }

    public void setTotalModulos(Integer totalModulos) {
        this.totalModulos = totalModulos;
    }

    public Integer getModulosCompletados() {
        return modulosCompletados;
    }

    public void setModulosCompletados(Integer modulosCompletados) {
        this.modulosCompletados = modulosCompletados;
    }

    public Long getTiempoTotalInvertido() {
        return tiempoTotalInvertido;
    }

    public void setTiempoTotalInvertido(Long tiempoTotalInvertido) {
        this.tiempoTotalInvertido = tiempoTotalInvertido;
    }

    public Boolean getPuedeGenerarCertificado() {
        return puedeGenerarCertificado;
    }

    public void setPuedeGenerarCertificado(Boolean puedeGenerarCertificado) {
        this.puedeGenerarCertificado = puedeGenerarCertificado;
    }

    public Integer getDiasTranscurridos() {
        return diasTranscurridos;
    }

    public void setDiasTranscurridos(Integer diasTranscurridos) {
        this.diasTranscurridos = diasTranscurridos;
    }

    public String getNivelCurso() {
        return nivelCurso;
    }

    public void setNivelCurso(String nivelCurso) {
        this.nivelCurso = nivelCurso;
    }

    public BigDecimal getPrecioCurso() {
        return precioCurso;
    }

    public void setPrecioCurso(BigDecimal precioCurso) {
        this.precioCurso = precioCurso;
    }

    @Override
    public String toString() {
        return "InscripcionDTO [id=" + id + ", estudianteNombre=" + estudianteNombre + 
               ", cursoTitulo=" + cursoTitulo + ", estado=" + estado + 
               ", progresoPorcentaje=" + progresoPorcentaje + "]";
    }
}
