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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
@SQLDelete(sql = "UPDATE certificados SET valido = false WHERE id = ?")
@Where(clause = "valido = true")
public class Certificado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La inscripción es obligatoria")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;
    
    @NotBlank(message = "El código de verificación es obligatorio")
    @Size(max = 100, message = "El código no puede exceder 100 caracteres")
    @Column(name = "codigo_verificacion", unique = true)
    private String codigoVerificacion;
    
    @Column(name = "url_pdf")
    private String urlPdf;
    
    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;
    
    private Boolean valido = true;

    // Constructores
    public Certificado() {}
    
    public Certificado(Integer id) {
        this.id = id;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
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

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
        if (codigoVerificacion == null) {
            // Generar código único
            codigoVerificacion = "CERT-" + System.currentTimeMillis() + "-" + id;
        }
    }

    @Override
    public String toString() {
        return "Certificado [id=" + id + ", codigoVerificacion=" + codigoVerificacion + 
               ", fechaGeneracion=" + fechaGeneracion + ", valido=" + valido + "]";
    }
}