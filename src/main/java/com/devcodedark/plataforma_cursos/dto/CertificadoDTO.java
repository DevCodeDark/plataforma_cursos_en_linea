package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class CertificadoDTO {
    private Integer id;
    
    @NotNull(message = "El ID de la inscripción es obligatorio")
    private Integer inscripcionId;
    
    // Datos del estudiante para mostrar en el certificado
    private String estudianteNombre;
    private String estudianteEmail;
    
    // Datos del curso para mostrar en el certificado
    private Integer cursoId;
    private String cursoTitulo;
    private String docenteNombre;
    
    @NotBlank(message = "El código de verificación es obligatorio")
    @Size(max = 100, message = "El código no puede exceder 100 caracteres")
    private String codigoVerificacion;
    
    private String urlPdf;
    
    private LocalDateTime fechaGeneracion;
    
    private LocalDateTime fechaCompletadoCurso;
    
    private Boolean valido;
    
    // Constructores
    public CertificadoDTO() {}
    
    public CertificadoDTO(Integer id, Integer inscripcionId, String estudianteNombre, String estudianteEmail,
                         Integer cursoId, String cursoTitulo, String docenteNombre, String codigoVerificacion,
                         String urlPdf, LocalDateTime fechaGeneracion, LocalDateTime fechaCompletadoCurso, Boolean valido) {
        this.id = id;
        this.inscripcionId = inscripcionId;
        this.estudianteNombre = estudianteNombre;
        this.estudianteEmail = estudianteEmail;
        this.cursoId = cursoId;
        this.cursoTitulo = cursoTitulo;
        this.docenteNombre = docenteNombre;
        this.codigoVerificacion = codigoVerificacion;
        this.urlPdf = urlPdf;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaCompletadoCurso = fechaCompletadoCurso;
        this.valido = valido;
    }
    
    // Constructor para crear nuevo certificado
    public CertificadoDTO(Integer inscripcionId, String codigoVerificacion) {
        this.inscripcionId = inscripcionId;
        this.codigoVerificacion = codigoVerificacion;
        this.valido = true;
    }
    
    // Constructor para verificación pública (sin datos sensibles)
    public CertificadoDTO(String codigoVerificacion, String estudianteNombre, String cursoTitulo,
                         String docenteNombre, LocalDateTime fechaGeneracion, Boolean valido) {
        this.codigoVerificacion = codigoVerificacion;
        this.estudianteNombre = estudianteNombre;
        this.cursoTitulo = cursoTitulo;
        this.docenteNombre = docenteNombre;
        this.fechaGeneracion = fechaGeneracion;
        this.valido = valido;
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

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDateTime getFechaCompletadoCurso() {
        return fechaCompletadoCurso;
    }

    public void setFechaCompletadoCurso(LocalDateTime fechaCompletadoCurso) {
        this.fechaCompletadoCurso = fechaCompletadoCurso;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    @Override
    public String toString() {
        return "CertificadoDTO{" +
                "id=" + id +
                ", inscripcionId=" + inscripcionId +
                ", estudianteNombre='" + estudianteNombre + '\'' +
                ", cursoTitulo='" + cursoTitulo + '\'' +
                ", docenteNombre='" + docenteNombre + '\'' +
                ", codigoVerificacion='" + codigoVerificacion + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                ", valido=" + valido +
                '}';
    }
}
