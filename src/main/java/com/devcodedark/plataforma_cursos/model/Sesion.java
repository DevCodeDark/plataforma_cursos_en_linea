package com.devcodedark.plataforma_cursos.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
@SQLDelete(sql = "UPDATE sesiones SET activa = false WHERE id = ?")
@Where(clause = "activa = true")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotBlank(message = "El token de sesión es obligatorio")
    @Size(max = 255, message = "El token no puede exceder 255 caracteres")
    @Column(name = "token_sesion", unique = true)
    private String tokenSesion;
    
    @Size(max = 45, message = "La IP no puede exceder 45 caracteres")
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "La fecha de expiración es obligatoria")
    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;
    
    private Boolean activa = true;

    // Constructores
    public Sesion() {}
    
    public Sesion(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTokenSesion() {
        return tokenSesion;
    }

    public void setTokenSesion(String tokenSesion) {
        this.tokenSesion = tokenSesion;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    @PrePersist
    protected void onCreate() {
        fechaInicio = LocalDateTime.now();
        if (fechaExpiracion == null) {
            // Sesión válida por 24 horas por defecto
            fechaExpiracion = LocalDateTime.now().plusHours(24);
        }
    }

    // Método para verificar si la sesión está expirada
    public boolean isExpirada() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    // Método para extender la sesión
    public void extenderSesion(int horas) {
        this.fechaExpiracion = LocalDateTime.now().plusHours(horas);
    }

    @Override
    public String toString() {
        return "Sesion [id=" + id + ", tokenSesion=" + tokenSesion + ", ipAddress=" + ipAddress + 
               ", fechaInicio=" + fechaInicio + ", fechaExpiracion=" + fechaExpiracion + 
               ", activa=" + activa + "]";
    }
}