package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.CertificadoDTO;

public interface ICertificadoService {
    // Listar todos los certificados
    List<CertificadoDTO> buscarTodos();
    
    // Guardar certificado
    void guardar(CertificadoDTO certificadoDTO);
    
    // Modificar certificado
    void modificar(CertificadoDTO certificadoDTO);
    
    // Buscar certificado por ID
    Optional<CertificadoDTO> buscarId(Integer id);
    
    // Eliminar certificado
    void eliminar(Integer id);
    
    // Buscar certificado por código de verificación
    Optional<CertificadoDTO> buscarPorCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificado por inscripción
    Optional<CertificadoDTO> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar certificados por estudiante
    List<CertificadoDTO> buscarPorEstudiante(Integer estudianteId);
    
    // Buscar certificados por curso
    List<CertificadoDTO> buscarPorCurso(Integer cursoId);
    
    // Buscar certificados válidos
    List<CertificadoDTO> buscarCertificadosValidos();
    
    // Contar certificados por curso
    Long contarCertificadosPorCurso(Integer cursoId);
    
    // Verificar si existe certificado para inscripción
    boolean existeCertificadoParaInscripcion(Integer inscripcionId);
    
    // Verificar código de verificación
    boolean existeCodigoVerificacion(String codigoVerificacion);
    
    // Buscar certificado por estudiante y curso
    Optional<CertificadoDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Generar certificado
    CertificadoDTO generarCertificado(Integer inscripcionId);
    
    // Invalidar certificado
    void invalidarCertificado(Integer certificadoId);
    
    // Verificar certificado
    boolean verificarCertificado(String codigoVerificacion);
    
    // Generar código único
    String generarCodigoUnico();
    
    // Generar PDF del certificado
    String generarPdfCertificado(Integer certificadoId);
}