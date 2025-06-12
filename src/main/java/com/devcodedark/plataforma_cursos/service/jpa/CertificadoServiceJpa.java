package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.CertificadoDTO;
import com.devcodedark.plataforma_cursos.model.Certificado;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.repository.CertificadoRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.service.ICertificadoService;

@Service
@Transactional
public class CertificadoServiceJpa implements ICertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final InscripcionRepository inscripcionRepository;
    
    public CertificadoServiceJpa(CertificadoRepository certificadoRepository, 
                                InscripcionRepository inscripcionRepository) {
        this.certificadoRepository = certificadoRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    // Métodos de conversión entre Entity y DTO
    private CertificadoDTO convertToDTO(Certificado certificado) {
        if (certificado == null) {
            return null;
        }
        
        CertificadoDTO dto = new CertificadoDTO();
        dto.setId(certificado.getId());
        dto.setInscripcionId(certificado.getInscripcion().getId());
        dto.setCodigoVerificacion(certificado.getCodigoVerificacion());
        dto.setUrlPdf(certificado.getUrlPdf());
        dto.setFechaGeneracion(certificado.getFechaGeneracion());
        dto.setValido(certificado.getValido());
        
        // Agregar información del estudiante y curso desde la inscripción
        if (certificado.getInscripcion() != null) {
            Inscripcion inscripcion = certificado.getInscripcion();
            
            if (inscripcion.getEstudiante() != null) {
                dto.setEstudianteNombre(inscripcion.getEstudiante().getNombre());
                dto.setEstudianteEmail(inscripcion.getEstudiante().getEmail());
            }
            
            if (inscripcion.getCurso() != null) {
                dto.setCursoId(inscripcion.getCurso().getId());
                dto.setCursoTitulo(inscripcion.getCurso().getTitulo());
                
                if (inscripcion.getCurso().getDocente() != null) {
                    dto.setDocenteNombre(inscripcion.getCurso().getDocente().getNombre());
                }
            }
            
            dto.setFechaCompletadoCurso(inscripcion.getFechaFinalizacion());
        }
        
        return dto;
    }
    
    private Certificado convertToEntity(CertificadoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Certificado certificado = new Certificado();
        certificado.setId(dto.getId());
        certificado.setCodigoVerificacion(dto.getCodigoVerificacion());
        certificado.setUrlPdf(dto.getUrlPdf());
        certificado.setFechaGeneracion(dto.getFechaGeneracion());
        certificado.setValido(dto.getValido());
        
        // Cargar inscripción
        if (dto.getInscripcionId() != null) {
            Optional<Inscripcion> inscripcion = inscripcionRepository.findById(dto.getInscripcionId());
            if (inscripcion.isPresent()) {
                certificado.setInscripcion(inscripcion.get());
            } else {
                throw new RuntimeException("Inscripción con ID " + dto.getInscripcionId() + " no encontrada");
            }
        }
        
        return certificado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> buscarTodos() {
        return certificadoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(CertificadoDTO certificadoDTO) {
        Certificado certificado = convertToEntity(certificadoDTO);
        certificadoRepository.save(certificado);
    }

    @Override
    public void modificar(CertificadoDTO certificadoDTO) {
        Certificado certificado = convertToEntity(certificadoDTO);
        certificadoRepository.save(certificado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarId(Integer id) {
        return certificadoRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        certificadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarPorCodigoVerificacion(String codigoVerificacion) {
        return certificadoRepository.findByCodigoVerificacion(codigoVerificacion)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarPorInscripcion(Integer inscripcionId) {
        return certificadoRepository.findByInscripcionId(inscripcionId)
                .map(this::convertToDTO);
    }    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> buscarPorEstudiante(Integer estudianteId) {
        return certificadoRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> buscarPorCurso(Integer cursoId) {
        return certificadoRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> buscarCertificadosValidos() {
        return certificadoRepository.findCertificadosValidos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCertificadosPorCurso(Integer cursoId) {
        return certificadoRepository.countCertificadosByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCertificadoParaInscripcion(Integer inscripcionId) {
        return certificadoRepository.existsByInscripcionId(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoVerificacion(String codigoVerificacion) {
        return certificadoRepository.existsByCodigoVerificacion(codigoVerificacion);
    }    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return certificadoRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId)
                .map(this::convertToDTO);
    }

    @Override
    public CertificadoDTO generarCertificado(Integer inscripcionId) {
        // Verificar que la inscripción existe
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        
        Inscripcion inscripcion = inscripcionOpt.get();
          // Verificar que el curso está completado
        if (inscripcion.getFechaFinalizacion() == null) {
            throw new RuntimeException("El curso no ha sido completado");
        }
        
        // Verificar que no existe ya un certificado para esta inscripción
        if (existeCertificadoParaInscripcion(inscripcionId)) {
            throw new RuntimeException("Ya existe un certificado para esta inscripción");
        }
        
        // Crear nuevo certificado
        Certificado certificado = new Certificado();
        certificado.setInscripcion(inscripcion);
        certificado.setCodigoVerificacion(generarCodigoUnico());
        certificado.setFechaGeneracion(LocalDateTime.now());
        certificado.setValido(true);
        
        certificadoRepository.save(certificado);
        
        return convertToDTO(certificado);
    }

    @Override
    public void invalidarCertificado(Integer certificadoId) {
        Optional<Certificado> certificadoOpt = certificadoRepository.findById(certificadoId);
        if (certificadoOpt.isPresent()) {
            Certificado certificado = certificadoOpt.get();
            certificado.setValido(false);
            certificadoRepository.save(certificado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarCertificado(String codigoVerificacion) {
        Optional<Certificado> certificado = certificadoRepository.findByCodigoVerificacion(codigoVerificacion);
        return certificado.isPresent() && certificado.get().getValido();
    }

    @Override
    public String generarCodigoUnico() {
        String codigo;
        do {
            codigo = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (existeCodigoVerificacion(codigo));
        
        return codigo;
    }

    @Override
    public String generarPdfCertificado(Integer certificadoId) {
        Optional<Certificado> certificadoOpt = certificadoRepository.findById(certificadoId);
        if (!certificadoOpt.isPresent()) {
            throw new RuntimeException("Certificado no encontrado");
        }
        
        // Aquí implementarías la lógica para generar el PDF
        // Por ahora retornamos una URL simulada
        String rutaPdf = "/certificados/pdf/" + certificadoId + ".pdf";
        
        // Actualizar la URL del PDF en el certificado
        Certificado certificado = certificadoOpt.get();
        certificado.setUrlPdf(rutaPdf);
        certificadoRepository.save(certificado);
        
        return rutaPdf;
    }
}