package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.dto.InscripcionDTO;

public interface IInscripcionService {
    // Métodos CRUD básicos con DTO
    List<InscripcionDTO> buscarTodos();
    void guardar(InscripcionDTO inscripcionDTO);
    void modificar(InscripcionDTO inscripcionDTO);
    Optional<InscripcionDTO> buscarId(Integer id);
    void eliminar(Integer id);
    
    // Búsquedas específicas
    List<InscripcionDTO> buscarPorEstudiante(Integer estudianteId);
    List<InscripcionDTO> buscarPorCurso(Integer cursoId);
    Optional<InscripcionDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    List<InscripcionDTO> buscarPorEstado(String estado);
    List<InscripcionDTO> buscarInscripcionesActivasPorEstudiante(Integer estudianteId);
    List<InscripcionDTO> buscarInscripcionesCompletadasPorEstudiante(Integer estudianteId);
    List<InscripcionDTO> buscarInscripcionesConCertificado();
    
    // Operaciones de negocio
    Long contarInscripcionesPorCurso(Integer cursoId);
    boolean estaInscrito(Integer estudianteId, Integer cursoId);
    InscripcionDTO inscribirEstudiante(Integer estudianteId, Integer cursoId);
    void finalizarInscripcion(Integer inscripcionId);
    void cancelarInscripcion(Integer inscripcionId);
    void actualizarProgreso(Integer inscripcionId, BigDecimal progreso);
    void generarCertificado(Integer inscripcionId);
    
    // Métodos adicionales para estadísticas
    Integer calcularDiasTranscurridos(Integer inscripcionId);
    Long calcularTiempoTotalInvertido(Integer inscripcionId);
    Integer contarModulosCompletados(Integer inscripcionId);
    Boolean puedeGenerarCertificado(Integer inscripcionId);
}