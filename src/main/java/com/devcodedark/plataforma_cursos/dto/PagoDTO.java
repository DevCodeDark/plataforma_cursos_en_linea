package com.devcodedark.plataforma_cursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PagoDTO {
    private Integer id;
    
    @NotNull(message = "La inscripción ID es obligatoria")
    private Integer inscripcionId;
    
    // Información adicional de la inscripción
    private String estudianteNombre;
    private String estudianteEmail;
    private String cursoTitulo;
    private String docenteNombre;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal monto;
    
    @Size(max = 3, message = "La moneda no puede exceder 3 caracteres")
    private String moneda = "USD";
    
    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;
    
    private String estado = "pendiente";
    
    private String transaccionExternaId;
    
    private String datosPago; // JSON como texto
    
    private LocalDateTime fechaPago;
    
    private LocalDateTime fechaActualizacion;
    
    // Campos calculados
    private String tiempoTranscurrido;
    private Boolean esReembolsable;
    private String estadoDescripcion;
    private String metodoPagoDescripcion;

    // Constructores
    public PagoDTO() {}

    public PagoDTO(Integer id) {
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

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTransaccionExternaId() {
        return transaccionExternaId;
    }

    public void setTransaccionExternaId(String transaccionExternaId) {
        this.transaccionExternaId = transaccionExternaId;
    }

    public String getDatosPago() {
        return datosPago;
    }

    public void setDatosPago(String datosPago) {
        this.datosPago = datosPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public void setTiempoTranscurrido(String tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }

    public Boolean getEsReembolsable() {
        return esReembolsable;
    }

    public void setEsReembolsable(Boolean esReembolsable) {
        this.esReembolsable = esReembolsable;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public String getMetodoPagoDescripcion() {
        return metodoPagoDescripcion;
    }

    public void setMetodoPagoDescripcion(String metodoPagoDescripcion) {
        this.metodoPagoDescripcion = metodoPagoDescripcion;
    }

    @Override
    public String toString() {
        return "PagoDTO [id=" + id + ", inscripcionId=" + inscripcionId + ", monto=" + monto + 
               ", metodoPago=" + metodoPago + ", estado=" + estado + ", fechaPago=" + fechaPago + "]";
    }
}
