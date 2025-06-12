package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SesionDTO {
    private Integer id;

    @NotNull(message = "El usuario ID es obligatorio")
    private Integer usuarioId;

    @NotBlank(message = "El token de sesión es obligatorio")
    @Size(max = 255, message = "El token no puede exceder 255 caracteres")
    private String tokenSesion;

    @Size(max = 45, message = "La IP no puede exceder 45 caracteres")
    private String ipAddress;

    private String userAgent;

    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de expiración es obligatoria")
    private LocalDateTime fechaExpiracion;

    private Boolean activa = true;

    // Información enriquecida del usuario
    private String usuarioNombre;
    private String usuarioEmail;
    private String usuarioRol;

    // Campos calculados
    private String fechaInicioFormateada;
    private String fechaExpiracionFormateada;
    private Boolean estaExpirada;
    private Long minutosRestantes;
    private String tiempoRestanteFormateado;
    private Long duracionSesionMinutos;
    private String duracionSesionFormateada;
    private String estadoSesion;
    private Boolean esMovil;
    private String navegador;
    private String sistemaOperativo;
    private Long diasActividad;

    // Constructores
    public SesionDTO() {
    }

    public SesionDTO(Integer id) {
        this.id = id;
    }

    public SesionDTO(Integer usuarioId, String tokenSesion) {
        this.usuarioId = usuarioId;
        this.tokenSesion = tokenSesion;
    }

    // Getters y Setters básicos
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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
        // Actualizar campos calculados cuando se establece el user agent
        this.esMovil = determinarEsMovil();
        this.navegador = extraerNavegador();
        this.sistemaOperativo = extraerSistemaOperativo();
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
        // Actualizar campos calculados
        this.fechaInicioFormateada = formatearFecha(fechaInicio);
        this.duracionSesionMinutos = calcularDuracionSesion();
        this.duracionSesionFormateada = formatearDuracion(this.duracionSesionMinutos);
        this.diasActividad = calcularDiasActividad();
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
        // Actualizar campos calculados
        this.fechaExpiracionFormateada = formatearFecha(fechaExpiracion);
        this.estaExpirada = calcularEstaExpirada();
        this.minutosRestantes = calcularMinutosRestantes();
        this.tiempoRestanteFormateado = formatearTiempoRestante();
        this.estadoSesion = calcularEstadoSesion();
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
        // Actualizar estado de sesión
        this.estadoSesion = calcularEstadoSesion();
    }

    // Getters para información enriquecida
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

    public String getUsuarioRol() {
        return usuarioRol;
    }

    public void setUsuarioRol(String usuarioRol) {
        this.usuarioRol = usuarioRol;
    }

    // Getters para campos calculados
    public String getFechaInicioFormateada() {
        return fechaInicioFormateada;
    }

    public void setFechaInicioFormateada(String fechaInicioFormateada) {
        this.fechaInicioFormateada = fechaInicioFormateada;
    }

    public String getFechaExpiracionFormateada() {
        return fechaExpiracionFormateada;
    }

    public void setFechaExpiracionFormateada(String fechaExpiracionFormateada) {
        this.fechaExpiracionFormateada = fechaExpiracionFormateada;
    }

    public Boolean getEstaExpirada() {
        return estaExpirada;
    }

    public void setEstaExpirada(Boolean estaExpirada) {
        this.estaExpirada = estaExpirada;
    }

    public Long getMinutosRestantes() {
        return minutosRestantes;
    }

    public void setMinutosRestantes(Long minutosRestantes) {
        this.minutosRestantes = minutosRestantes;
    }

    public String getTiempoRestanteFormateado() {
        return tiempoRestanteFormateado;
    }

    public void setTiempoRestanteFormateado(String tiempoRestanteFormateado) {
        this.tiempoRestanteFormateado = tiempoRestanteFormateado;
    }

    public Long getDuracionSesionMinutos() {
        return duracionSesionMinutos;
    }

    public void setDuracionSesionMinutos(Long duracionSesionMinutos) {
        this.duracionSesionMinutos = duracionSesionMinutos;
    }

    public String getDuracionSesionFormateada() {
        return duracionSesionFormateada;
    }

    public void setDuracionSesionFormateada(String duracionSesionFormateada) {
        this.duracionSesionFormateada = duracionSesionFormateada;
    }

    public String getEstadoSesion() {
        return estadoSesion;
    }

    public void setEstadoSesion(String estadoSesion) {
        this.estadoSesion = estadoSesion;
    }

    public Boolean getEsMovil() {
        return esMovil;
    }

    public void setEsMovil(Boolean esMovil) {
        this.esMovil = esMovil;
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

    public Long getDiasActividad() {
        return diasActividad;
    }

    public void setDiasActividad(Long diasActividad) {
        this.diasActividad = diasActividad;
    }

    // Métodos helper para cálculos
    private String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fecha.format(formatter);
    }

    private Boolean calcularEstaExpirada() {
        if (fechaExpiracion == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    private Long calcularMinutosRestantes() {
        if (fechaExpiracion == null || calcularEstaExpirada()) {
            return 0L;
        }
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), fechaExpiracion);
    }

    private String formatearTiempoRestante() {
        if (minutosRestantes == null || minutosRestantes <= 0) {
            return "Expirada";
        }

        if (minutosRestantes < 60) {
            return minutosRestantes + " minutos";
        }

        long horas = minutosRestantes / 60;
        long minutosExtra = minutosRestantes % 60;

        if (minutosExtra == 0) {
            return horas + (horas == 1 ? " hora" : " horas");
        }

        return horas + (horas == 1 ? " hora " : " horas ") + minutosExtra + " minutos";
    }

    private Long calcularDuracionSesion() {
        if (fechaInicio == null) {
            return 0L;
        }

        LocalDateTime finSesion = activa != null && activa ? LocalDateTime.now()
                : (fechaExpiracion != null ? fechaExpiracion : LocalDateTime.now());

        return ChronoUnit.MINUTES.between(fechaInicio, finSesion);
    }

    private String formatearDuracion(Long minutos) {
        if (minutos == null || minutos <= 0) {
            return "0 minutos";
        }

        if (minutos < 60) {
            return minutos + " minutos";
        }

        long horas = minutos / 60;
        long minutosExtra = minutos % 60;

        if (horas >= 24) {
            long dias = horas / 24;
            long horasExtra = horas % 24;
            String resultado = dias + (dias == 1 ? " día" : " días");
            if (horasExtra > 0) {
                resultado += " " + horasExtra + (horasExtra == 1 ? " hora" : " horas");
            }
            return resultado;
        }

        if (minutosExtra == 0) {
            return horas + (horas == 1 ? " hora" : " horas");
        }

        return horas + (horas == 1 ? " hora " : " horas ") + minutosExtra + " minutos";
    }

    private String calcularEstadoSesion() {
        if (activa == null || !activa) {
            return "Inactiva";
        }

        if (calcularEstaExpirada()) {
            return "Expirada";
        }

        if (minutosRestantes != null && minutosRestantes < 30) {
            return "Por expirar";
        }

        return "Activa";
    }

    private Boolean determinarEsMovil() {
        if (userAgent == null || userAgent.isEmpty()) {
            return false;
        }

        String ua = userAgent.toLowerCase();
        return ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") ||
                ua.contains("ipad") || ua.contains("tablet");
    }

    private String extraerNavegador() {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Desconocido";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("firefox")) {
            return "Firefox";
        } else if (ua.contains("chrome") && !ua.contains("edg")) {
            return "Chrome";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("edg")) {
            return "Edge";
        } else if (ua.contains("opera")) {
            return "Opera";
        } else if (ua.contains("msie") || ua.contains("trident")) {
            return "Internet Explorer";
        }

        return "Otro";
    }

    private String extraerSistemaOperativo() {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Desconocido";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("mac os")) {
            return "macOS";
        } else if (ua.contains("linux")) {
            return "Linux";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) {
            return "iOS";
        }

        return "Otro";
    }

    private Long calcularDiasActividad() {
        if (fechaInicio == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(fechaInicio.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    // Métodos de utilidad adicionales
    public boolean esSesionActiva() {
        return activa != null && activa && !calcularEstaExpirada();
    }

    public boolean necesitaRenovacion() {
        return minutosRestantes != null && minutosRestantes < 60 && minutosRestantes > 0;
    }

    public boolean esSesionLarga() {
        return duracionSesionMinutos != null && duracionSesionMinutos > 480; // Más de 8 horas
    }

    public boolean esAccesoMovil() {
        return esMovil != null && esMovil;
    }

    @Override
    public String toString() {
        return "SesionDTO [id=" + id + ", usuarioNombre=" + usuarioNombre +
                ", ipAddress=" + ipAddress + ", estadoSesion=" + estadoSesion +
                ", navegador=" + navegador + ", esMovil=" + esMovil + "]";
    }
}
