package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Certificado;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.repository.CertificadoRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.service.ICertificadoService;

@Service
@Transactional
public class CertificadoServiceJpa implements ICertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Certificado> buscarTodos() {
        return certificadoRepository.findAll();
    }

    @Override
    public void guardar(Certificado certificado) {
        certificadoRepository.save(certificado);
    }

    @Override
    public void modificar(Certificado certificado) {
        certificadoRepository.save(certificado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Certificado> buscarId(Integer id) {
        return certificadoRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        certificadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Certificado> buscarPorCodigoVerificacion(String codigoVerificacion) {
        return certificadoRepository.findByCodigoVerificacion(codigoVerificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Certificado> buscarPorInscripcion(Integer inscripcionId) {
        return certificadoRepository.findByInscripcionId(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificado> buscarPorEstudiante(Integer estudianteId) {
        return certificadoRepository.findByEstudianteId(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificado> buscarPorCurso(Integer cursoId) {
        return certificadoRepository.findByCursoId(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificado> buscarCertificadosValidos() {
        return certificadoRepository.findCertificadosValidos();
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
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Certificado> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return certificadoRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    @Override
    public Certificado generarCertificado(Integer inscripcionId) {
        // Verificar que no exista ya un certificado para esta inscripción
        if (existeCertificadoParaInscripcion(inscripcionId)) {
            throw new RuntimeException("Ya existe un certificado para esta inscripción");
        }
        
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        
        Inscripcion inscripcion = inscripcionOpt.get();
        
        // Verificar que la inscripción esté finalizada
        if (!inscripcion.getEstado().equals(Inscripcion.EstadoInscripcion.finalizado)) {
            throw new RuntimeException("La inscripción debe estar finalizada para generar el certificado");
        }
        
        Certificado certificado = new Certificado();
        certificado.setInscripcion(inscripcion);
        certificado.setCodigoVerificacion(generarCodigoUnico());
        certificado.setFechaGeneracion(LocalDateTime.now());
        certificado.setValido(true);
        
        // Marcar la inscripción como que tiene certificado generado
        inscripcion.setCertificadoGenerado(true);
        inscripcionRepository.save(inscripcion);
        
        return certificadoRepository.save(certificado);
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
        Optional<Certificado> certificadoOpt = buscarPorCodigoVerificacion(codigoVerificacion);
        return certificadoOpt.isPresent() && certificadoOpt.get().getValido();
    }

    @Override
    public String generarCodigoUnico() {
        String codigo;
        do {
            // Generar código basado en UUID y timestamp
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            codigo = "CERT-" + uuid + "-" + timestamp;
        } while (existeCodigoVerificacion(codigo));
        
        return codigo;
    }

    @Override
    public String generarPdfCertificado(Integer certificadoId) {
        Optional<Certificado> certificadoOpt = buscarId(certificadoId);
        if (!certificadoOpt.isPresent()) {
            throw new RuntimeException("Certificado no encontrado");
        }
        
        Certificado certificado = certificadoOpt.get();
        
        // Aquí iría la lógica para generar el PDF
        // Por ahora retornamos la URL donde se almacenaría el PDF
        String nombreArchivo = "certificado_" + certificado.getCodigoVerificacion() + ".pdf";
        String rutaArchivo = "/certificados/" + nombreArchivo;
        
        // TODO: Implementar generación real de PDF con librerías como iText o similar
        
        return rutaArchivo;
    }
}