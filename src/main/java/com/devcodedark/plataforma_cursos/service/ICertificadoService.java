package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Certificado;

public interface ICertificadoService {
    // Listar todos los certificados
    List<Certificado> buscarTodos();
    
    // Guardar certificado
    void guardar(Certificado certificado);
    
    // Modificar certificado
    void modificar(Certificado certificado);
    
    // Buscar certificado por ID
    Optional<Certificado> buscarId(Integer id);
    
    // Eliminar certificado
    void eliminar(Integer id);
    
    // Buscar certificado por código de verificación
    Optional<Certificado> buscarPorCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificado por inscripción
    Optional<Certificado> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar certificados por estudiante
    List<Certificado> buscarPorEstudiante(Integer estudianteId);
    
    // Buscar certificados por curso
    List<Certificado> buscarPorCurso(Integer cursoId);
    
    // Buscar certificados válidos
    List<Certificado> buscarCertificadosValidos();
    
    // Contar certificados por curso
    Long contarCertificadosPorCurso(Integer cursoId);
    
    // Verificar si existe certificado para inscripción
    boolean existeCertificadoParaInscripcion(Integer inscripcionId);
    
    // Verificar código de verificación
    boolean existeCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificado por estudiante y curso
    Optional<Certificado> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Generar certificado
    Certificado generarCertificado(Integer inscripcionId);
    
    // Invalidar certificado
    void invalidarCertificado(Integer certificadoId);
    
    // Verificar certificado
    boolean verificarCertificado(String codigoVerificacion);
    
    // Generar código único
    String generarCodigoUnico();
    
    // Generar PDF del certificado
    String generarPdfCertificado(Integer certificadoId);
}