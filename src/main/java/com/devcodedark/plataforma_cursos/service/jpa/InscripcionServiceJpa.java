package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.InscripcionDTO;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Inscripcion.EstadoInscripcion;
import com.devcodedark.plataforma_cursos.model.Inscripcion.MetodoPago;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.repository.ProgresoModuloRepository;
import com.devcodedark.plataforma_cursos.service.IInscripcionService;

@Service
@Transactional
public class InscripcionServiceJpa implements IInscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final ProgresoModuloRepository progresoModuloRepository;
    
    public InscripcionServiceJpa(InscripcionRepository inscripcionRepository,
                                UsuarioRepository usuarioRepository,
                                CursoRepository cursoRepository,
                                ModuloRepository moduloRepository,
                                ProgresoModuloRepository progresoModuloRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.moduloRepository = moduloRepository;
        this.progresoModuloRepository = progresoModuloRepository;
    }
    
    // Métodos de conversión entre Entity y DTO
    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }
        
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setEstado(inscripcion.getEstado() != null ? inscripcion.getEstado().name() : null);
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setFechaFinalizacion(inscripcion.getFechaFinalizacion());
        dto.setPrecioPagado(inscripcion.getPrecioPagado());
        dto.setMetodoPago(inscripcion.getMetodoPago() != null ? inscripcion.getMetodoPago().name() : null);
        dto.setTransaccionId(inscripcion.getTransaccionId());
        dto.setProgresoPorcentaje(inscripcion.getProgresoPorcentaje());
        dto.setCertificadoGenerado(inscripcion.getCertificadoGenerado());
        dto.setFechaCertificado(inscripcion.getFechaCertificado());
        
        // Información del estudiante
        if (inscripcion.getEstudiante() != null) {
            dto.setEstudianteId(inscripcion.getEstudiante().getId());
            dto.setEstudianteNombre(inscripcion.getEstudiante().getNombre() + " " + inscripcion.getEstudiante().getApellido());
            dto.setEstudianteEmail(inscripcion.getEstudiante().getEmail());
        }
        
        // Información del curso
        if (inscripcion.getCurso() != null) {
            dto.setCursoId(inscripcion.getCurso().getId());
            dto.setCursoTitulo(inscripcion.getCurso().getTitulo());
            dto.setCursoImagenPortada(inscripcion.getCurso().getImagenPortada());
            dto.setNivelCurso(inscripcion.getCurso().getNivel() != null ? inscripcion.getCurso().getNivel().name() : null);
            dto.setPrecioCurso(inscripcion.getCurso().getPrecio());
            
            // Información del docente
            if (inscripcion.getCurso().getDocente() != null) {
                dto.setDocenteNombre(inscripcion.getCurso().getDocente().getNombre() + " " + inscripcion.getCurso().getDocente().getApellido());
            }
            
            // Información de la categoría
            if (inscripcion.getCurso().getCategoria() != null) {
                dto.setCategoriaNombre(inscripcion.getCurso().getCategoria().getNombre());
            }
        }
        
        // Campos calculados
        dto.setTotalModulos(moduloRepository.countModulosByCurso(inscripcion.getCurso().getId()).intValue());
        dto.setModulosCompletados(progresoModuloRepository.countModulosCompletadosByInscripcion(inscripcion.getId()).intValue());
        dto.setTiempoTotalInvertido(progresoModuloRepository.sumTiempoInvertidoByInscripcion(inscripcion.getId()));
        dto.setDiasTranscurridos(calcularDiasTranscurridos(inscripcion.getId()));
        dto.setPuedeGenerarCertificado(puedeGenerarCertificado(inscripcion.getId()));
        
        return dto;
    }
    
    private Inscripcion convertToEntity(InscripcionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(dto.getId());
        inscripcion.setPrecioPagado(dto.getPrecioPagado());
        inscripcion.setTransaccionId(dto.getTransaccionId());
        inscripcion.setProgresoPorcentaje(dto.getProgresoPorcentaje());
        inscripcion.setCertificadoGenerado(dto.getCertificadoGenerado());
        inscripcion.setFechaCertificado(dto.getFechaCertificado());
        
        // Convertir enums
        if (dto.getEstado() != null) {
            inscripcion.setEstado(EstadoInscripcion.valueOf(dto.getEstado()));
        }
        
        if (dto.getMetodoPago() != null) {
            inscripcion.setMetodoPago(MetodoPago.valueOf(dto.getMetodoPago()));
        }
        
        // Cargar estudiante
        if (dto.getEstudianteId() != null) {
            Optional<Usuario> estudiante = usuarioRepository.findById(dto.getEstudianteId());
            if (estudiante.isPresent()) {
                inscripcion.setEstudiante(estudiante.get());
            } else {
                throw new RuntimeException("Estudiante con ID " + dto.getEstudianteId() + " no encontrado");
            }
        }
        
        // Cargar curso
        if (dto.getCursoId() != null) {
            Optional<Curso> curso = cursoRepository.findById(dto.getCursoId());
            if (curso.isPresent()) {
                inscripcion.setCurso(curso.get());
            } else {
                throw new RuntimeException("Curso con ID " + dto.getCursoId() + " no encontrado");
            }
        }
        
        return inscripcion;
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarTodos() {
        return inscripcionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = convertToEntity(inscripcionDTO);
        inscripcionRepository.save(inscripcion);
    }

    @Override
    public void modificar(InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = convertToEntity(inscripcionDTO);
        inscripcionRepository.save(inscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscripcionDTO> buscarId(Integer id) {
        return inscripcionRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarPorCurso(Integer cursoId) {
        return inscripcionRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscripcionDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return inscripcionRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarPorEstado(String estado) {
        EstadoInscripcion estadoInscripcion = EstadoInscripcion.valueOf(estado);
        return inscripcionRepository.findByEstado(estadoInscripcion)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarInscripcionesActivasPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findInscripcionesActivasByEstudiante(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarInscripcionesCompletadasPorEstudiante(Integer estudianteId) {
        return inscripcionRepository.findInscripcionesCompletadasByEstudiante(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> buscarInscripcionesConCertificado() {
        return inscripcionRepository.findInscripcionesConCertificado()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
    public InscripcionDTO inscribirEstudiante(Integer estudianteId, Integer cursoId) {
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
            
            Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);
            return convertToDTO(inscripcionGuardada);
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
            inscripcion.setFechaCertificado(LocalDateTime.now());
            inscripcionRepository.save(inscripcion);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer calcularDiasTranscurridos(Integer inscripcionId) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            if (inscripcion.getFechaInscripcion() != null) {
                return (int) ChronoUnit.DAYS.between(inscripcion.getFechaInscripcion(), LocalDateTime.now());
            }
        }
        return 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long calcularTiempoTotalInvertido(Integer inscripcionId) {
        return progresoModuloRepository.sumTiempoInvertidoByInscripcion(inscripcionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer contarModulosCompletados(Integer inscripcionId) {
        return progresoModuloRepository.countModulosCompletadosByInscripcion(inscripcionId).intValue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Boolean puedeGenerarCertificado(Integer inscripcionId) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (inscripcionOpt.isPresent()) {
            Inscripcion inscripcion = inscripcionOpt.get();
            return inscripcion.getEstado() == EstadoInscripcion.finalizado && 
                   inscripcion.getProgresoPorcentaje().compareTo(new BigDecimal("100")) >= 0 &&
                   !inscripcion.getCertificadoGenerado();
        }
        return false;
    }
}