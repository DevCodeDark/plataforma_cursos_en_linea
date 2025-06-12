package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RolDTO {
    private Integer id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    private String descripcion;

    private String permisos;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private Integer estado = 1;

    // Campos calculados y enriquecidos
    private String fechaCreacionFormateada;
    private String fechaActualizacionFormateada;
    private Long diasDesdeCreacion;
    private Long diasDesdeActualizacion;
    private Boolean esRolActivo;
    private Integer cantidadUsuarios;
    private List<String> listaPermisos;
    private String tiempoVidaFormateado;
    private String estadoDescriptivo;

    // Constructores
    public RolDTO() {
    }

    public RolDTO(Integer id) {
        this.id = id;
    }

    public RolDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters básicos
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

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
        // Actualizar lista de permisos cuando se establecen los permisos
        this.listaPermisos = calcularListaPermisos();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        // Actualizar campos calculados
        this.fechaCreacionFormateada = formatearFecha(fechaCreacion);
        this.diasDesdeCreacion = calcularDiasDesde(fechaCreacion);
        this.tiempoVidaFormateado = calcularTiempoVida();
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
        // Actualizar campos calculados
        this.fechaActualizacionFormateada = formatearFecha(fechaActualizacion);
        this.diasDesdeActualizacion = calcularDiasDesde(fechaActualizacion);
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
        // Actualizar campos calculados
        this.esRolActivo = determinarEsActivo();
        this.estadoDescriptivo = calcularEstadoDescriptivo();
    }

    // Getters para campos calculados
    public String getFechaCreacionFormateada() {
        return fechaCreacionFormateada;
    }

    public void setFechaCreacionFormateada(String fechaCreacionFormateada) {
        this.fechaCreacionFormateada = fechaCreacionFormateada;
    }

    public String getFechaActualizacionFormateada() {
        return fechaActualizacionFormateada;
    }

    public void setFechaActualizacionFormateada(String fechaActualizacionFormateada) {
        this.fechaActualizacionFormateada = fechaActualizacionFormateada;
    }

    public Long getDiasDesdeCreacion() {
        return diasDesdeCreacion;
    }

    public void setDiasDesdeCreacion(Long diasDesdeCreacion) {
        this.diasDesdeCreacion = diasDesdeCreacion;
    }

    public Long getDiasDesdeActualizacion() {
        return diasDesdeActualizacion;
    }

    public void setDiasDesdeActualizacion(Long diasDesdeActualizacion) {
        this.diasDesdeActualizacion = diasDesdeActualizacion;
    }

    public Boolean getEsRolActivo() {
        return esRolActivo;
    }

    public void setEsRolActivo(Boolean esRolActivo) {
        this.esRolActivo = esRolActivo;
    }

    public Integer getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    public void setCantidadUsuarios(Integer cantidadUsuarios) {
        this.cantidadUsuarios = cantidadUsuarios;
    }

    public List<String> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<String> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public String getTiempoVidaFormateado() {
        return tiempoVidaFormateado;
    }

    public void setTiempoVidaFormateado(String tiempoVidaFormateado) {
        this.tiempoVidaFormateado = tiempoVidaFormateado;
    }

    public String getEstadoDescriptivo() {
        return estadoDescriptivo;
    }

    public void setEstadoDescriptivo(String estadoDescriptivo) {
        this.estadoDescriptivo = estadoDescriptivo;
    }

    // Métodos helper para cálculos
    private String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fecha.format(formatter);
    }

    private Long calcularDiasDesde(LocalDateTime fecha) {
        if (fecha == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(fecha.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    private Boolean determinarEsActivo() {
        return estado != null && estado == 1;
    }

    private List<String> calcularListaPermisos() {
        if (permisos == null || permisos.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(permisos.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private String calcularTiempoVida() {
        if (fechaCreacion == null) {
            return "0 días";
        }

        long dias = calcularDiasDesde(fechaCreacion);

        if (dias == 0) {
            return "Hoy";
        } else if (dias == 1) {
            return "1 día";
        } else if (dias < 30) {
            return dias + " días";
        } else if (dias < 365) {
            long meses = dias / 30;
            return meses + (meses == 1 ? " mes" : " meses");
        } else {
            long anios = dias / 365;
            long mesesRestantes = (dias % 365) / 30;
            String resultado = anios + (anios == 1 ? " año" : " años");
            if (mesesRestantes > 0) {
                resultado += " " + mesesRestantes + (mesesRestantes == 1 ? " mes" : " meses");
            }
            return resultado;
        }
    }

    private String calcularEstadoDescriptivo() {
        if (estado == null) {
            return "Indefinido";
        }

        switch (estado) {
            case 1:
                return "Activo";
            case 0:
                return "Inactivo";
            default:
                return "Estado desconocido";
        }
    }

    // Métodos de utilidad adicionales
    public boolean tienePermiso(String permiso) {
        return listaPermisos != null && listaPermisos.contains(permiso.trim());
    }

    public int contarPermisos() {
        return listaPermisos != null ? listaPermisos.size() : 0;
    }

    public boolean esRolReciente() {
        return diasDesdeCreacion != null && diasDesdeCreacion <= 7;
    }

    public boolean fueActualizadoRecientemente() {
        return diasDesdeActualizacion != null && diasDesdeActualizacion <= 3;
    }

    @Override
    public String toString() {
        return "RolDTO [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion +
                ", estado=" + estadoDescriptivo + ", cantidadUsuarios=" + cantidadUsuarios +
                ", permisos=" + contarPermisos() + "]";
    }
}
