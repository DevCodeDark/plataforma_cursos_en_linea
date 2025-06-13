package com.devcodedark.plataforma_cursos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar configuraciones del sistema
 */
@Entity
@Table(name = "configuraciones")
public class Configuracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String clave;
    
    @Column(columnDefinition = "TEXT")
    private String valor;
    
    @Column(name = "valor_por_defecto", columnDefinition = "TEXT")
    private String valorPorDefecto;
    
    @Column(nullable = false)
    private String categoria;
    
    @Column(nullable = false)
    private String tipo; // STRING, INTEGER, BOOLEAN, DECIMAL, JSON
    
    private String descripcion;
    
    @Column(name = "es_modificable")
    private Boolean esModificable = true;
    
    @Column(name = "es_visible")
    private Boolean esVisible = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Configuracion() {}
    
    public Configuracion(String clave, String valor, String categoria, String tipo) {
        this.clave = clave;
        this.valor = valor;
        this.categoria = categoria;
        this.tipo = tipo;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Configuracion(String clave, String valor, String valorPorDefecto, String categoria, String tipo, String descripcion) {
        this.clave = clave;
        this.valor = valor;
        this.valorPorDefecto = valorPorDefecto;
        this.categoria = categoria;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Métodos de utilidad
    public Object getValorTipado() {
        if (valor == null) return null;
        
        switch (tipo.toUpperCase()) {
            case "INTEGER":
                try {
                    return Integer.parseInt(valor);
                } catch (NumberFormatException e) {
                    return null;
                }
            case "BOOLEAN":
                return Boolean.parseBoolean(valor);
            case "DECIMAL":
                try {
                    return Double.parseDouble(valor);
                } catch (NumberFormatException e) {
                    return null;
                }            case "JSON", "STRING":
            default:
                return valor;
        }
    }
    
    public void setValorDesdeTipo(Object valorTipado) {
        if (valorTipado == null) {
            this.valor = null;
        } else {
            this.valor = valorTipado.toString();
        }
        this.fechaActualizacion = LocalDateTime.now();
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
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getValorPorDefecto() {
        return valorPorDefecto;
    }

    public void setValorPorDefecto(String valorPorDefecto) {
        this.valorPorDefecto = valorPorDefecto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsModificable() {
        return esModificable;
    }

    public void setEsModificable(Boolean esModificable) {
        this.esModificable = esModificable;
    }

    public Boolean getEsVisible() {
        return esVisible;
    }

    public void setEsVisible(Boolean esVisible) {
        this.esVisible = esVisible;
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
}
