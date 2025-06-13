package com.devcodedark.plataforma_cursos.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO para encapsular estadísticas del dashboard
 */
public class EstadisticasDTO {
    
    // Estadísticas básicas
    private Long totalUsuarios;
    private Long totalCursos;
    private Long totalDocentes;
    private Long totalEstudiantes;
    private Long totalInscripciones;
    private Double tasaCrecimientoUsuarios;
    private Double tasaCrecimientoCursos;
    
    // Estadísticas por período
    private Map<String, Long> usuariosPorMes;
    private Map<String, Long> cursosPorMes;
    private Map<String, Long> inscripcionesPorMes;
    
    // Estadísticas por categoría
    private Map<String, Long> usuariosPorRol;
    private Map<String, Long> cursosPorCategoria;
    private Map<String, Long> usuariosPorEstado;
    
    // Actividad reciente
    private List<Map<String, Object>> actividadReciente;
    private List<Map<String, Object>> cursosPopulares;
    private List<Map<String, Object>> docentesActivos;
    
    // Métricas de rendimiento
    private Double promedioCalificaciones;
    private Long totalEvaluaciones;
    private Double tasaCompletacion;
    private Double tiempoPromedioSesion;
    
    // Datos para gráficos
    private List<String> etiquetasMeses;
    private List<Long> datosUsuarios;
    private List<Long> datosCursos;
    private List<Long> datosInscripciones;
    
    // Constructores
    public EstadisticasDTO() {
    }
    
    // Getters y Setters
    public Long getTotalUsuarios() {
        return totalUsuarios;
    }
    
    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }
    
    public Long getTotalCursos() {
        return totalCursos;
    }
    
    public void setTotalCursos(Long totalCursos) {
        this.totalCursos = totalCursos;
    }
    
    public Long getTotalDocentes() {
        return totalDocentes;
    }
    
    public void setTotalDocentes(Long totalDocentes) {
        this.totalDocentes = totalDocentes;
    }
    
    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }
    
    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }
    
    public Long getTotalInscripciones() {
        return totalInscripciones;
    }
    
    public void setTotalInscripciones(Long totalInscripciones) {
        this.totalInscripciones = totalInscripciones;
    }
    
    public Double getTasaCrecimientoUsuarios() {
        return tasaCrecimientoUsuarios;
    }
    
    public void setTasaCrecimientoUsuarios(Double tasaCrecimientoUsuarios) {
        this.tasaCrecimientoUsuarios = tasaCrecimientoUsuarios;
    }
    
    public Double getTasaCrecimientoCursos() {
        return tasaCrecimientoCursos;
    }
    
    public void setTasaCrecimientoCursos(Double tasaCrecimientoCursos) {
        this.tasaCrecimientoCursos = tasaCrecimientoCursos;
    }
    
    public Map<String, Long> getUsuariosPorMes() {
        return usuariosPorMes;
    }
    
    public void setUsuariosPorMes(Map<String, Long> usuariosPorMes) {
        this.usuariosPorMes = usuariosPorMes;
    }
    
    public Map<String, Long> getCursosPorMes() {
        return cursosPorMes;
    }
    
    public void setCursosPorMes(Map<String, Long> cursosPorMes) {
        this.cursosPorMes = cursosPorMes;
    }
    
    public Map<String, Long> getInscripcionesPorMes() {
        return inscripcionesPorMes;
    }
    
    public void setInscripcionesPorMes(Map<String, Long> inscripcionesPorMes) {
        this.inscripcionesPorMes = inscripcionesPorMes;
    }
    
    public Map<String, Long> getUsuariosPorRol() {
        return usuariosPorRol;
    }
    
    public void setUsuariosPorRol(Map<String, Long> usuariosPorRol) {
        this.usuariosPorRol = usuariosPorRol;
    }
    
    public Map<String, Long> getCursosPorCategoria() {
        return cursosPorCategoria;
    }
    
    public void setCursosPorCategoria(Map<String, Long> cursosPorCategoria) {
        this.cursosPorCategoria = cursosPorCategoria;
    }
    
    public Map<String, Long> getUsuariosPorEstado() {
        return usuariosPorEstado;
    }
    
    public void setUsuariosPorEstado(Map<String, Long> usuariosPorEstado) {
        this.usuariosPorEstado = usuariosPorEstado;
    }
    
    public List<Map<String, Object>> getActividadReciente() {
        return actividadReciente;
    }
    
    public void setActividadReciente(List<Map<String, Object>> actividadReciente) {
        this.actividadReciente = actividadReciente;
    }
    
    public List<Map<String, Object>> getCursosPopulares() {
        return cursosPopulares;
    }
    
    public void setCursosPopulares(List<Map<String, Object>> cursosPopulares) {
        this.cursosPopulares = cursosPopulares;
    }
    
    public List<Map<String, Object>> getDocentesActivos() {
        return docentesActivos;
    }
    
    public void setDocentesActivos(List<Map<String, Object>> docentesActivos) {
        this.docentesActivos = docentesActivos;
    }
    
    public Double getPromedioCalificaciones() {
        return promedioCalificaciones;
    }
    
    public void setPromedioCalificaciones(Double promedioCalificaciones) {
        this.promedioCalificaciones = promedioCalificaciones;
    }
    
    public Long getTotalEvaluaciones() {
        return totalEvaluaciones;
    }
    
    public void setTotalEvaluaciones(Long totalEvaluaciones) {
        this.totalEvaluaciones = totalEvaluaciones;
    }
    
    public Double getTasaCompletacion() {
        return tasaCompletacion;
    }
    
    public void setTasaCompletacion(Double tasaCompletacion) {
        this.tasaCompletacion = tasaCompletacion;
    }
    
    public Double getTiempoPromedioSesion() {
        return tiempoPromedioSesion;
    }
    
    public void setTiempoPromedioSesion(Double tiempoPromedioSesion) {
        this.tiempoPromedioSesion = tiempoPromedioSesion;
    }
    
    public List<String> getEtiquetasMeses() {
        return etiquetasMeses;
    }
    
    public void setEtiquetasMeses(List<String> etiquetasMeses) {
        this.etiquetasMeses = etiquetasMeses;
    }
    
    public List<Long> getDatosUsuarios() {
        return datosUsuarios;
    }
    
    public void setDatosUsuarios(List<Long> datosUsuarios) {
        this.datosUsuarios = datosUsuarios;
    }
    
    public List<Long> getDatosCursos() {
        return datosCursos;
    }
    
    public void setDatosCursos(List<Long> datosCursos) {
        this.datosCursos = datosCursos;
    }
    
    public List<Long> getDatosInscripciones() {
        return datosInscripciones;
    }
    
    public void setDatosInscripciones(List<Long> datosInscripciones) {
        this.datosInscripciones = datosInscripciones;
    }
}
