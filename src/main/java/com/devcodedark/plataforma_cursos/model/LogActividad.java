package com.devcodedark.plataforma_cursos.model;

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
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_actividad")
public class LogActividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @NotBlank(message = "La acción es obligatoria")
    @Size(max = 100, message = "La acción no puede exceder 100 caracteres")
    private String accion;
    
    @Size(max = 50, message = "La entidad no puede exceder 50 caracteres")
    private String entidad; // tabla afectada
    
    @Column(name = "entidad_id")
    private Integer entidadId; // ID del registro afectado
    
    @Column(columnDefinition = "TEXT")
    private String detalles; // JSON como texto con información adicional
    
    @Size(max = 45, message = "La IP no puede exceder 45 caracteres")
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(name = "fecha_accion")
    private LocalDateTime fechaAccion;

    // Constructores
    public LogActividad() {}
    
    public LogActividad(Integer id) {
        this.id = id;
    }
    
    // Constructor para crear logs fácilmente
    public LogActividad(Usuario usuario, String accion, String entidad, Integer entidadId, String detalles) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalles = detalles;
        this.fechaAccion = LocalDateTime.now();
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

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public Integer getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Integer entidadId) {
        this.entidadId = entidadId;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
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

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    @PrePersist
    protected void onCreate() {
        fechaAccion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "LogActividad [id=" + id + ", accion=" + accion + ", entidad=" + entidad + 
               ", entidadId=" + entidadId + ", fechaAccion=" + fechaAccion + "]";
    }
}