package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Inscripcion.EstadoInscripcion;

public interface IInscripcionService {
    // Listar todas las inscripciones
    List<Inscripcion> buscarTodos();
    
    // Guardar inscripción
    void guardar(Inscripcion inscripcion);
    
    // Modificar inscripción
    void modificar(Inscripcion inscripcion);
    
    // Buscar inscripción por ID
    Optional<Inscripcion> buscarId(Integer id);
    
    // Eliminar inscripción
    void eliminar(Integer id);
    
    // Buscar inscripciones por estudiante
    List<Inscripcion> buscarPorEstudiante(Integer estudianteId);
    
    // Buscar inscripciones por curso
    List<Inscripcion> buscarPorCurso(Integer cursoId);
    
    // Buscar inscripción específica
    Optional<Inscripcion> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Buscar inscripciones por estado
    List<Inscripcion> buscarPorEstado(EstadoInscripcion estado);
    
    // Buscar inscripciones activas por estudiante
    List<Inscripcion> buscarInscripcionesActivasPorEstudiante(Integer estudianteId);
    
    // Buscar inscripciones completadas por estudiante
    List<Inscripcion> buscarInscripcionesCompletadasPorEstudiante(Integer estudianteId);
    
    // Contar inscripciones por curso
    Long contarInscripcionesPorCurso(Integer cursoId);
    
    // Verificar si estudiante está inscrito
    boolean estaInscrito(Integer estudianteId, Integer cursoId);
    
    // Buscar inscripciones con certificado
    List<Inscripcion> buscarInscripcionesConCertificado();
    
    // Inscribir estudiante
    Inscripcion inscribirEstudiante(Integer estudianteId, Integer cursoId);
    
    // Finalizar inscripción
    void finalizarInscripcion(Integer inscripcionId);
    
    // Cancelar inscripción
    void cancelarInscripcion(Integer inscripcionId);
    
    // Actualizar progreso
    void actualizarProgreso(Integer inscripcionId, BigDecimal progreso);
    
    // Generar certificado
    void generarCertificado(Integer inscripcionId);
}