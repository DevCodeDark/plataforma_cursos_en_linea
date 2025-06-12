package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CalificacionDTO {
    private Integer id;
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Integer estudianteId;
    
    private String estudianteNombre;
    
    @NotNull(message = "El ID del curso es obligatorio")
    private Integer cursoId;
    
    private String cursoTitulo;
    
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;
    
    private String comentario;
    
    private LocalDateTime fechaCalificacion;
    
    // Constructores
    public CalificacionDTO() {}
    
    public CalificacionDTO(Integer id, Integer estudianteId, String estudianteNombre, 
                          Integer cursoId, String cursoTitulo, Integer puntuacion, 
                          String comentario, LocalDateTime fechaCalificacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.cursoId = cursoId;
        this.cursoTitulo = cursoTitulo;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fechaCalificacion = fechaCalificacion;
    }
    
    // Constructor para crear nueva calificación
    public CalificacionDTO(Integer estudianteId, Integer cursoId, Integer puntuacion, String comentario) {
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
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

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    @Override
    public String toString() {
        return "CalificacionDTO{" +
                "id=" + id +
                ", estudianteId=" + estudianteId +
                ", estudianteNombre='" + estudianteNombre + '\'' +
                ", cursoId=" + cursoId +
                ", cursoTitulo='" + cursoTitulo + '\'' +
                ", puntuacion=" + puntuacion +
                ", comentario='" + comentario + '\'' +
                ", fechaCalificacion=" + fechaCalificacion +
                '}';
    }
}
