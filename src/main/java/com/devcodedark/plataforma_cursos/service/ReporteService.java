package com.devcodedark.plataforma_cursos.service;

import com.devcodedark.plataforma_cursos.dto.EstadisticasDTO;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes y estadísticas del sistema
 */
@Service
@Transactional(readOnly = true)
public class ReporteService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private ICursoService cursoService;
    
    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Genera estadísticas completas del dashboard
     */
    public EstadisticasDTO generarEstadisticasCompletas() {
        EstadisticasDTO estadisticas = new EstadisticasDTO();
        
        try {
            // Estadísticas básicas
            long totalUsuarios = usuarioService.contarUsuarios();
            long totalCursos = cursoService.contarCursos();
            long totalDocentes = usuarioService.contarUsuariosPorRol("Docente");
            long totalEstudiantes = usuarioService.contarUsuariosPorRol("Estudiante");
            
            estadisticas.setTotalUsuarios(totalUsuarios);
            estadisticas.setTotalCursos(totalCursos);
            estadisticas.setTotalDocentes(totalDocentes);
            estadisticas.setTotalEstudiantes(totalEstudiantes);
            estadisticas.setTotalInscripciones(0L); // TODO: Implementar cuando exista el modelo
            
            // Calcular tasas de crecimiento
            estadisticas.setTasaCrecimientoUsuarios(calcularTasaCrecimientoUsuarios());
            estadisticas.setTasaCrecimientoCursos(calcularTasaCrecimientoCursos());
            
            // Estadísticas por período
            estadisticas.setUsuariosPorMes(obtenerUsuariosPorMes());
            estadisticas.setCursosPorMes(obtenerCursosPorMes());
            estadisticas.setInscripcionesPorMes(obtenerInscripcionesPorMes());
            
            // Estadísticas por categoría
            estadisticas.setUsuariosPorRol(obtenerUsuariosPorRol());
            estadisticas.setCursosPorCategoria(obtenerCursosPorCategoria());
            estadisticas.setUsuariosPorEstado(obtenerUsuariosPorEstado());
            
            // Actividad reciente
            estadisticas.setActividadReciente(obtenerActividadReciente());
            estadisticas.setCursosPopulares(obtenerCursosPopulares());
            estadisticas.setDocentesActivos(obtenerDocentesActivos());
            
            // Métricas de rendimiento (valores simulados por ahora)
            estadisticas.setPromedioCalificaciones(4.2);
            estadisticas.setTotalEvaluaciones(0L);
            estadisticas.setTasaCompletacion(78.5);
            estadisticas.setTiempoPromedioSesion(45.3);
            
            // Datos para gráficos
            estadisticas.setEtiquetasMeses(obtenerEtiquetasMeses());
            estadisticas.setDatosUsuarios(obtenerDatosUsuariosPorMes());
            estadisticas.setDatosCursos(obtenerDatosCursosPorMes());
            estadisticas.setDatosInscripciones(obtenerDatosInscripcionesPorMes());
            
        } catch (Exception e) {
            // En caso de error, devolver estadísticas básicas
            estadisticas.setTotalUsuarios(0L);
            estadisticas.setTotalCursos(0L);
            estadisticas.setTotalDocentes(0L);
            estadisticas.setTotalEstudiantes(0L);
        }
        
        return estadisticas;
    }

    /**
     * Calcula la tasa de crecimiento de usuarios en el último mes
     */
    private Double calcularTasaCrecimientoUsuarios() {
        try {
            LocalDateTime inicioMesActual = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            LocalDateTime inicioMesAnterior = inicioMesActual.minusMonths(1);
            
            // Simular datos por ahora
            return 12.5; // 12.5% de crecimiento
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Calcula la tasa de crecimiento de cursos en el último mes
     */
    private Double calcularTasaCrecimientoCursos() {
        try {
            // Simular datos por ahora
            return 8.3; // 8.3% de crecimiento
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Obtiene usuarios registrados por mes en los últimos 6 meses
     */
    private Map<String, Long> obtenerUsuariosPorMes() {
        Map<String, Long> datos = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.of("es", "ES"));
        
        // Simular datos para los últimos 6 meses
        LocalDate fecha = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        for (int i = 0; i < 6; i++) {
            String mes = fecha.format(formatter);
            datos.put(mes, (long)(Math.random() * 50 + 10)); // Datos simulados
            fecha = fecha.plusMonths(1);
        }
        
        return datos;
    }

    /**
     * Obtiene cursos creados por mes en los últimos 6 meses
     */
    private Map<String, Long> obtenerCursosPorMes() {
        Map<String, Long> datos = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.of("es", "ES"));
        
        // Simular datos para los últimos 6 meses
        LocalDate fecha = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        for (int i = 0; i < 6; i++) {
            String mes = fecha.format(formatter);
            datos.put(mes, (long)(Math.random() * 15 + 3)); // Datos simulados
            fecha = fecha.plusMonths(1);
        }
        
        return datos;
    }

    /**
     * Obtiene inscripciones por mes en los últimos 6 meses
     */
    private Map<String, Long> obtenerInscripcionesPorMes() {
        Map<String, Long> datos = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.of("es", "ES"));
        
        // Simular datos para los últimos 6 meses
        LocalDate fecha = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        for (int i = 0; i < 6; i++) {
            String mes = fecha.format(formatter);
            datos.put(mes, (long)(Math.random() * 200 + 50)); // Datos simulados
            fecha = fecha.plusMonths(1);
        }
        
        return datos;
    }

    /**
     * Obtiene distribución de usuarios por rol
     */
    private Map<String, Long> obtenerUsuariosPorRol() {
        try {
            Map<String, Long> datos = new HashMap<>();
            datos.put("Estudiantes", usuarioService.contarUsuariosPorRol("Estudiante"));
            datos.put("Docentes", usuarioService.contarUsuariosPorRol("Docente"));
            datos.put("Administradores", usuarioService.contarUsuariosPorRol("Administrador"));
            return datos;
        } catch (Exception e) {
            return Map.of("Estudiantes", 0L, "Docentes", 0L, "Administradores", 0L);
        }
    }

    /**
     * Obtiene distribución de cursos por categoría
     */
    private Map<String, Long> obtenerCursosPorCategoria() {
        // Simular datos por categorías
        Map<String, Long> datos = new LinkedHashMap<>();
        datos.put("Programación", 15L);
        datos.put("Diseño", 8L);
        datos.put("Marketing", 5L);
        datos.put("Idiomas", 12L);
        datos.put("Ciencias", 7L);
        return datos;
    }

    /**
     * Obtiene distribución de usuarios por estado
     */
    private Map<String, Long> obtenerUsuariosPorEstado() {
        // Simular datos por estado
        Map<String, Long> datos = new LinkedHashMap<>();
        datos.put("Activos", 450L);
        datos.put("Inactivos", 45L);
        datos.put("Suspendidos", 5L);
        return datos;
    }

    /**
     * Obtiene actividad reciente del sistema
     */
    private List<Map<String, Object>> obtenerActividadReciente() {
        List<Map<String, Object>> actividades = new ArrayList<>();
        
        // Simular actividades recientes
        String[] tipos = {"Registro", "Inscripción", "Completación", "Nuevo Curso"};
        String[] usuarios = {"Ana García", "Carlos López", "María Rodríguez", "Juan Pérez"};
        
        for (int i = 0; i < 10; i++) {
            Map<String, Object> actividad = new HashMap<>();
            actividad.put("tipo", tipos[(int)(Math.random() * tipos.length)]);
            actividad.put("usuario", usuarios[(int)(Math.random() * usuarios.length)]);
            actividad.put("descripcion", "Actividad de ejemplo #" + (i + 1));
            actividad.put("fecha", LocalDateTime.now().minusHours(i * 2));
            actividades.add(actividad);
        }
        
        return actividades;
    }

    /**
     * Obtiene cursos más populares
     */
    private List<Map<String, Object>> obtenerCursosPopulares() {
        List<Map<String, Object>> cursos = new ArrayList<>();
        
        // Simular cursos populares
        String[] nombres = {
            "Java desde Cero", "React Avanzado", "Python para Data Science",
            "Diseño UX/UI", "JavaScript Moderno"
        };
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> curso = new HashMap<>();
            curso.put("nombre", nombres[i]);
            curso.put("inscripciones", (int)(Math.random() * 200 + 50));
            curso.put("calificacion", 4.0 + Math.random() * 1.0);
            cursos.add(curso);
        }
        
        return cursos;
    }

    /**
     * Obtiene docentes más activos
     */
    private List<Map<String, Object>> obtenerDocentesActivos() {
        List<Map<String, Object>> docentes = new ArrayList<>();
        
        // Simular docentes activos
        String[] nombres = {
            "Dr. Roberto Silva", "Ing. Laura Martínez", "Prof. David González",
            "Dra. Carmen Ruiz", "Lic. Fernando Torres"
        };
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> docente = new HashMap<>();
            docente.put("nombre", nombres[i]);
            docente.put("cursos", (int)(Math.random() * 8 + 2));
            docente.put("estudiantes", (int)(Math.random() * 150 + 20));
            docente.put("calificacion", 4.0 + Math.random() * 1.0);
            docentes.add(docente);
        }
        
        return docentes;
    }

    /**
     * Obtiene etiquetas de meses para gráficos
     */
    private List<String> obtenerEtiquetasMeses() {
        List<String> etiquetas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", Locale.of("es", "ES"));
        
        LocalDate fecha = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        for (int i = 0; i < 6; i++) {
            etiquetas.add(fecha.format(formatter));
            fecha = fecha.plusMonths(1);
        }
        
        return etiquetas;
    }

    /**
     * Obtiene datos de usuarios por mes para gráficos
     */
    private List<Long> obtenerDatosUsuariosPorMes() {
        return obtenerUsuariosPorMes().values().stream().collect(Collectors.toList());
    }

    /**
     * Obtiene datos de cursos por mes para gráficos
     */
    private List<Long> obtenerDatosCursosPorMes() {
        return obtenerCursosPorMes().values().stream().collect(Collectors.toList());
    }

    /**
     * Obtiene datos de inscripciones por mes para gráficos
     */
    private List<Long> obtenerDatosInscripcionesPorMes() {
        return obtenerInscripcionesPorMes().values().stream().collect(Collectors.toList());
    }

    /**
     * Genera reporte específico por período
     */
    public EstadisticasDTO generarReportePorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        // TODO: Implementar cuando sea necesario
        return generarEstadisticasCompletas();
    }

    /**
     * Exporta estadísticas a formato específico
     */
    public Map<String, Object> exportarEstadisticas(String formato) {
        EstadisticasDTO estadisticas = generarEstadisticasCompletas();
        Map<String, Object> export = new HashMap<>();
        
        export.put("fecha", LocalDateTime.now());
        export.put("formato", formato);
        export.put("datos", estadisticas);
        
        return export;
    }
}
