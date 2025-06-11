package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Inscripcion.EstadoInscripcion;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.service.IInscripcionService;

@Service
@Transactional
public class InscripcionServiceJpa implements IInscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarTodos() {
        return inscripcionRepository.findAll();
    }

    @Override
    public void guardar(Inscripcion inscripcion) {
        inscripcionRepository.save(inscripcion);
    }

    @Override
    public void modificar(Inscripcion inscripcion) {
        inscripcionRepository.save(inscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscripcion> buscarId(Integer id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarPorCurso(Integer cursoId) {
        return inscripcionRepository.findByCursoId(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscripcion> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return inscripcionRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarPorEstado(EstadoInscripcion estado) {
        return inscripcionRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarInscripcionesActivasPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findInscripcionesActivasByEstudiante(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarInscripcionesCompletadasPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findInscripcionesCompletadasByEstudiante(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInscripcionesPorCurso(Integer cursoId) {
        return inscripcionRepository.countInscripcionesByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaInscrito(Integer estudianteId, Integer cursoId) {
        return inscripcionRepository.existsByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> buscarInscripcionesConCertificado() {
        return inscripcionRepository.findInscripcionesConCertificado();
    }

    @Override
    public Inscripcion inscribirEstudiante(Integer estudianteId, Integer cursoId) {
        // Verificar que el estudiante no esté ya inscrito
        if (estaInscrito(estudianteId, cursoId)) {
            throw new RuntimeException("El estudiante ya está inscrito en este curso");
        }
        
        Optional<Usuario> estudianteOpt = usuarioRepository.findById(estudianteId);
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        
        if (estudianteOpt.isPresent() && cursoOpt.isPresent()) {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setEstudiante(estudianteOpt.get());
            inscripcion.setCurso(cursoOpt.get());
            inscripcion.setEstado(EstadoInscripcion.activo);
            inscripcion.setProgresoPorcentaje(BigDecimal.ZERO);
            
            return inscripcionRepository.save(inscripcion);
        }
        
        throw new RuntimeException("Estudiante o curso no encontrado");
    }

    @Override
    public void finalizarInscripcion(Integer inscripcionId) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            inscripcion.setEstado(EstadoInscripcion.finalizado);
            inscripcion.setFechaFinalizacion(LocalDateTime.now());
            inscripcionRepository.save(inscripcion);
        }
    }

    @Override
    public void cancelarInscripcion(Integer inscripcionId) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            inscripcion.setEstado(EstadoInscripcion.cancelado);
            inscripcionRepository.save(inscripcion);
        }
    }

    @Override
    public void actualizarProgreso(Integer inscripcionId, BigDecimal progreso) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            inscripcion.setProgresoPorcentaje(progreso);
            
            // Si el progreso es 100%, finalizar automáticamente
            if (progreso.compareTo(new BigDecimal("100")) >= 0) {
                inscripcion.setEstado(EstadoInscripcion.finalizado);
                inscripcion.setFechaFinalizacion(LocalDateTime.now());
            }
            
            inscripcionRepository.save(inscripcion);
        }
    }

    @Override
    public void generarCertificado(Integer inscripcionId) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            inscripcion.setCertificadoGenerado(true);
            inscripcionRepository.save(inscripcion);
        }
    }
}