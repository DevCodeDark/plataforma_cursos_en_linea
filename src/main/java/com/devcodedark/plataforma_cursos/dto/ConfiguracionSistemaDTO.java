package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;

/**
 * DTO para la configuración del sistema
 */
public class ConfiguracionSistemaDTO {
    
    @NotBlank(message = "El nombre del sitio es obligatorio")
    private String nombreSitio;
    
    @NotBlank(message = "La descripción del sitio es obligatoria")
    private String descripcionSitio;
    
    @Email(message = "El email de contacto debe ser válido")
    @NotBlank(message = "El email de contacto es obligatorio")
    private String emailContacto;
    
    @NotBlank(message = "El teléfono de contacto es obligatorio")
    private String telefonoContacto;
    
    private String direccion;
    private String urlLogo;
    private String urlFavicon;
    
    @NotNull(message = "El límite de usuarios es obligatorio")
    @Min(value = 1, message = "El límite de usuarios debe ser mayor a 0")
    private Integer limiteUsuarios;
    
    @NotNull(message = "El límite de cursos es obligatorio")
    @Min(value = 1, message = "El límite de cursos debe ser mayor a 0")
    private Integer limiteCursos;
    
    @NotNull(message = "El tamaño máximo de archivo es obligatorio")
    @Min(value = 1, message = "El tamaño máximo de archivo debe ser mayor a 0")
    private Integer tamajoMaximoArchivo; // En MB
    
    private Boolean registroPublico;
    private Boolean notificacionesEmail;
    private Boolean modoMantenimiento;
    private Boolean logActividades;
    
    private String colorPrimario;
    private String colorSecundario;
    private String colorAccento;
    
    private String idiomaPorDefecto;
    private String zonaHoraria;
    private String formatoFecha;
    
    private String politicasPrivacidad;
    private String terminosCondiciones;
    
    // Constructores
    public ConfiguracionSistemaDTO() {}

    // Getters y Setters
    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getDescripcionSitio() {
        return descripcionSitio;
    }

    public void setDescripcionSitio(String descripcionSitio) {
        this.descripcionSitio = descripcionSitio;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlFavicon() {
        return urlFavicon;
    }

    public void setUrlFavicon(String urlFavicon) {
        this.urlFavicon = urlFavicon;
    }

    public Integer getLimiteUsuarios() {
        return limiteUsuarios;
    }

    public void setLimiteUsuarios(Integer limiteUsuarios) {
        this.limiteUsuarios = limiteUsuarios;
    }

    public Integer getLimiteCursos() {
        return limiteCursos;
    }

    public void setLimiteCursos(Integer limiteCursos) {
        this.limiteCursos = limiteCursos;
    }

    public Integer getTamajoMaximoArchivo() {
        return tamajoMaximoArchivo;
    }

    public void setTamajoMaximoArchivo(Integer tamajoMaximoArchivo) {
        this.tamajoMaximoArchivo = tamajoMaximoArchivo;
    }

    public Boolean getRegistroPublico() {
        return registroPublico;
    }

    public void setRegistroPublico(Boolean registroPublico) {
        this.registroPublico = registroPublico;
    }

    public Boolean getNotificacionesEmail() {
        return notificacionesEmail;
    }

    public void setNotificacionesEmail(Boolean notificacionesEmail) {
        this.notificacionesEmail = notificacionesEmail;
    }

    public Boolean getModoMantenimiento() {
        return modoMantenimiento;
    }

    public void setModoMantenimiento(Boolean modoMantenimiento) {
        this.modoMantenimiento = modoMantenimiento;
    }

    public Boolean getLogActividades() {
        return logActividades;
    }

    public void setLogActividades(Boolean logActividades) {
        this.logActividades = logActividades;
    }

    public String getColorPrimario() {
        return colorPrimario;
    }

    public void setColorPrimario(String colorPrimario) {
        this.colorPrimario = colorPrimario;
    }

    public String getColorSecundario() {
        return colorSecundario;
    }

    public void setColorSecundario(String colorSecundario) {
        this.colorSecundario = colorSecundario;
    }

    public String getColorAccento() {
        return colorAccento;
    }

    public void setColorAccento(String colorAccento) {
        this.colorAccento = colorAccento;
    }

    public String getIdiomaPorDefecto() {
        return idiomaPorDefecto;
    }

    public void setIdiomaPorDefecto(String idiomaPorDefecto) {
        this.idiomaPorDefecto = idiomaPorDefecto;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    public String getPoliticasPrivacidad() {
        return politicasPrivacidad;
    }

    public void setPoliticasPrivacidad(String politicasPrivacidad) {
        this.politicasPrivacidad = politicasPrivacidad;
    }

    public String getTerminosCondiciones() {
        return terminosCondiciones;
    }

    public void setTerminosCondiciones(String terminosCondiciones) {
        this.terminosCondiciones = terminosCondiciones;
    }
}
