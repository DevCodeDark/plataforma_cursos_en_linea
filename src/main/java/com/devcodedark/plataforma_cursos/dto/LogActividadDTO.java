package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class LogActividadDTO {
    
    // Campos básicos de la entidad
    private Integer id;
    
    @NotBlank(message = "La acción es obligatoria")
    @Size(max = 100, message = "La acción no puede exceder 100 caracteres")
    private String accion;
    
    @Size(max = 50, message = "La entidad no puede exceder 50 caracteres")
    private String entidad;
    
    private Integer entidadId;
    private String detalles;
    
    @Size(max = 45, message = "La IP no puede exceder 45 caracteres")
    private String ipAddress;
    
    private String userAgent;
    private LocalDateTime fechaAccion;
    
    // Campos de relación con información adicional
    private Integer usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private String usuarioNombreCompleto;
    
    // Campos calculados adicionales
    private String tiempoTranscurrido;
    private String navegador;
    private String sistemaOperativo;
    private String ubicacionIP;
    private Boolean esAccionCritica;
    private Boolean esAccionReciente;
    private String categoriaAccion;
    private String descripcionAccion;
    
    // Constructores
    public LogActividadDTO() {}
    
    public LogActividadDTO(Integer id, String accion, String entidad, Integer entidadId) {
        this.id = id;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.fechaAccion = LocalDateTime.now();
        this.esAccionReciente = true;
        this.categoriaAccion = determinarCategoriaAccion(accion);
    }
    
    public LogActividadDTO(String accion, String entidad, Integer entidadId, String usuarioNombre, String ipAddress) {
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.usuarioNombre = usuarioNombre;
        this.ipAddress = ipAddress;
        this.fechaAccion = LocalDateTime.now();
        this.esAccionReciente = true;
        this.esAccionCritica = determinarSiEsCritica(accion);
        this.categoriaAccion = determinarCategoriaAccion(accion);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
        this.categoriaAccion = determinarCategoriaAccion(accion);
        this.esAccionCritica = determinarSiEsCritica(accion);
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
        this.navegador = extraerNavegador(userAgent);
        this.sistemaOperativo = extraerSistemaOperativo(userAgent);
    }
    
    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }
    
    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
        this.esAccionReciente = calcularSiEsReciente(fechaAccion);
        this.tiempoTranscurrido = calcularTiempoTranscurrido(fechaAccion);
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public String getUsuarioEmail() {
        return usuarioEmail;
    }
    
    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }
    
    public String getUsuarioNombreCompleto() {
        return usuarioNombreCompleto;
    }
    
    public void setUsuarioNombreCompleto(String usuarioNombreCompleto) {
        this.usuarioNombreCompleto = usuarioNombreCompleto;
    }
    
    public String getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
    
    public void setTiempoTranscurrido(String tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }
    
    public String getNavegador() {
        return navegador;
    }
    
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }
    
    public String getSistemaOperativo() {
        return sistemaOperativo;
    }
    
    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }
    
    public String getUbicacionIP() {
        return ubicacionIP;
    }
    
    public void setUbicacionIP(String ubicacionIP) {
        this.ubicacionIP = ubicacionIP;
    }
    
    public Boolean getEsAccionCritica() {
        return esAccionCritica;
    }
    
    public void setEsAccionCritica(Boolean esAccionCritica) {
        this.esAccionCritica = esAccionCritica;
    }
    
    public Boolean getEsAccionReciente() {
        return esAccionReciente;
    }
    
    public void setEsAccionReciente(Boolean esAccionReciente) {
        this.esAccionReciente = esAccionReciente;
    }
    
    public String getCategoriaAccion() {
        return categoriaAccion;
    }
    
    public void setCategoriaAccion(String categoriaAccion) {
        this.categoriaAccion = categoriaAccion;
    }
    
    public String getDescripcionAccion() {
        return descripcionAccion;
    }
    
    public void setDescripcionAccion(String descripcionAccion) {
        this.descripcionAccion = descripcionAccion;
    }
    
    // Métodos helper
    private String determinarCategoriaAccion(String accion) {
        if (accion == null) return "Sin categoría";
        
        String accionLower = accion.toLowerCase();
        if (accionLower.contains("login") || accionLower.contains("logout") || accionLower.contains("acceso")) {
            return "Autenticación";
        } else if (accionLower.contains("crear") || accionLower.contains("guardar")) {
            return "Creación";
        } else if (accionLower.contains("modificar") || accionLower.contains("actualizar") || accionLower.contains("editar")) {
            return "Modificación";
        } else if (accionLower.contains("eliminar") || accionLower.contains("borrar")) {
            return "Eliminación";
        } else if (accionLower.contains("consultar") || accionLower.contains("buscar") || accionLower.contains("ver")) {
            return "Consulta";
        } else {
            return "Otros";
        }
    }
    
    private Boolean determinarSiEsCritica(String accion) {
        if (accion == null) return false;
        
        String accionLower = accion.toLowerCase();
        return accionLower.contains("eliminar") || 
               accionLower.contains("borrar") || 
               accionLower.contains("cambiar_contrasena") ||
               accionLower.contains("cambiar_estado") ||
               accionLower.contains("admin");
    }
    
    private Boolean calcularSiEsReciente(LocalDateTime fechaAccion) {
        if (fechaAccion == null) return false;
        return fechaAccion.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    private String calcularTiempoTranscurrido(LocalDateTime fechaAccion) {
        if (fechaAccion == null) return "Desconocido";
        
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaAccion, ahora).toMinutes();
        
        if (minutos < 1) {
            return "Hace menos de 1 minuto";
        } else if (minutos < 60) {
            return "Hace " + minutos + " minuto" + (minutos > 1 ? "s" : "");
        } else if (minutos < 1440) { // 24 horas
            long horas = minutos / 60;
            return "Hace " + horas + " hora" + (horas > 1 ? "s" : "");
        } else {
            long dias = minutos / 1440;
            return "Hace " + dias + " día" + (dias > 1 ? "s" : "");
        }
    }
    
    private String extraerNavegador(String userAgent) {
        if (userAgent == null) return "Desconocido";
        
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Opera")) return "Opera";
        return "Otros";
    }
    
    private String extraerSistemaOperativo(String userAgent) {
        if (userAgent == null) return "Desconocido";
        
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return "Otros";
    }
    
    @Override
    public String toString() {
        return "LogActividadDTO{" +
                "id=" + id +
                ", accion='" + accion + '\'' +
                ", entidad='" + entidad + '\'' +
                ", entidadId=" + entidadId +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", fechaAccion=" + fechaAccion +
                ", categoriaAccion='" + categoriaAccion + '\'' +
                ", esAccionCritica=" + esAccionCritica +
                '}';
    }
}
