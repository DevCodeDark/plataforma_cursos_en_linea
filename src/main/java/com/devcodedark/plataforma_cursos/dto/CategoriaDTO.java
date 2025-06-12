package com.devcodedark.plataforma_cursos.dto;

import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class CategoriaDTO {
    private Integer id;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    private String descripcion;
    
    private String icono;
    
    private String color;
    
    private EstadoCategoria estado;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
    
    private Integer totalCursos;
    
    // Constructores
    public CategoriaDTO() {}
    
    public CategoriaDTO(Integer id, String nombre, String descripcion, String icono, 
                       String color, EstadoCategoria estado, LocalDateTime fechaCreacion,
                       LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.color = color;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Constructor para vista de catálogo con conteo de cursos
    public CategoriaDTO(Integer id, String nombre, String descripcion, String icono, 
                       String color, EstadoCategoria estado, Integer totalCursos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.color = color;
        this.estado = estado;
        this.totalCursos = totalCursos;
    }
    
    // Constructor para crear nueva categoría
    public CategoriaDTO(String nombre, String descripcion, String icono, String color) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.color = color;
        this.estado = EstadoCategoria.activo;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EstadoCategoria getEstado() {
        return estado;
    }

    public void setEstado(EstadoCategoria estado) {
        this.estado = estado;
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

    public Integer getTotalCursos() {
        return totalCursos;
    }

    public void setTotalCursos(Integer totalCursos) {
        this.totalCursos = totalCursos;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", icono='" + icono + '\'' +
                ", color='" + color + '\'' +
                ", estado=" + estado +
                ", totalCursos=" + totalCursos +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
