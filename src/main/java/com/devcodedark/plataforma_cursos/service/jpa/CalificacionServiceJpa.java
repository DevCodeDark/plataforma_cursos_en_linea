package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.CalificacionDTO;
import com.devcodedark.plataforma_cursos.model.Calificacion;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.repository.CalificacionRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.service.ICalificacionService;

@Service
@Transactional
public class CalificacionServiceJpa implements ICalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;

    // Métodos de conversión entre Entity y DTO
    private CalificacionDTO convertToDTO(Calificacion calificacion) {
        if (calificacion == null) {
            return null;
        }
        
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setEstudianteId(calificacion.getEstudiante().getId());
        dto.setEstudianteNombre(calificacion.getEstudiante().getNombre());
        dto.setCursoId(calificacion.getCurso().getId());
        dto.setCursoTitulo(calificacion.getCurso().getTitulo());
        dto.setPuntuacion(calificacion.getPuntuacion());
        dto.setComentario(calificacion.getComentario());
        dto.setFechaCalificacion(calificacion.getFechaCalificacion());
        
        return dto;
    }
    
    private Calificacion convertToEntity(CalificacionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Calificacion calificacion = new Calificacion();
        calificacion.setId(dto.getId());
        calificacion.setPuntuacion(dto.getPuntuacion());
        calificacion.setComentario(dto.getComentario());
        calificacion.setFechaCalificacion(dto.getFechaCalificacion());
        
        // Cargar entidades relacionadas
        if (dto.getEstudianteId() != null) {
            Optional<Usuario> estudiante = usuarioRepository.findById(dto.getEstudianteId());
            if (estudiante.isPresent()) {
                calificacion.setEstudiante(estudiante.get());
            } else {
                throw new RuntimeException("Estudiante con ID " + dto.getEstudianteId() + " no encontrado");
            }
        }
        
        if (dto.getCursoId() != null) {
            Optional<Curso> curso = cursoRepository.findById(dto.getCursoId());
            if (curso.isPresent()) {
                calificacion.setCurso(curso.get());
            } else {
                throw new RuntimeException("Curso con ID " + dto.getCursoId() + " no encontrado");
            }
        }
        
        return calificacion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> buscarTodos() {
        return calificacionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(CalificacionDTO calificacionDTO) {
        Calificacion calificacion = convertToEntity(calificacionDTO);
        calificacionRepository.save(calificacion);
    }

    @Override
    public void modificar(CalificacionDTO calificacionDTO) {
        Calificacion calificacion = convertToEntity(calificacionDTO);
        calificacionRepository.save(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalificacionDTO> buscarId(Integer id) {
        return calificacionRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        calificacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> buscarPorCurso(Integer cursoId) {
        return calificacionRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> buscarPorEstudiante(Integer estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalificacionDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return calificacionRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioCalificacionesPorCurso(Integer cursoId) {
        return calificacionRepository.calcularPromedioCalificacionesByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPorPuntuacionYCurso(Integer cursoId, Integer puntuacion) {
        return calificacionRepository.countByPuntuacionAndCurso(cursoId, puntuacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> buscarPorRangoPuntuacion(Integer min, Integer max) {
        return calificacionRepository.findByPuntuacionBetween(min, max)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean yaCalificoElCurso(Integer estudianteId, Integer cursoId) {
        return calificacionRepository.existsByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCalificacionesPorCurso(Integer cursoId) {
        return calificacionRepository.countCalificacionesByCurso(cursoId);
    }

    @Override
    public void calificarCurso(Integer estudianteId, Integer cursoId, Integer puntuacion, String comentario) {
        // Verificar que el estudiante no haya calificado ya el curso
        if (yaCalificoElCurso(estudianteId, cursoId)) {
            throw new RuntimeException("El estudiante ya calificó este curso");
        }
        
        Optional<Usuario> estudianteOpt = usuarioRepository.findById(estudianteId);
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        
        if (estudianteOpt.isPresent() && cursoOpt.isPresent()) {
            Calificacion calificacion = new Calificacion();
            calificacion.setEstudiante(estudianteOpt.get());
            calificacion.setCurso(cursoOpt.get());
            calificacion.setPuntuacion(puntuacion);
            calificacion.setComentario(comentario);
            
            calificacionRepository.save(calificacion);
        } else {
            throw new RuntimeException("Estudiante o curso no encontrado");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerEstadisticasPorCurso(Integer cursoId) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Promedio de calificaciones
        Double promedio = calcularPromedioCalificacionesPorCurso(cursoId);
        estadisticas.put("promedio", promedio != null ? promedio : 0.0);
        
        // Total de calificaciones
        Long total = contarCalificacionesPorCurso(cursoId);
        estadisticas.put("total", total);
        
        // Distribución por puntuación
        Map<Integer, Long> distribucion = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Long count = contarPorPuntuacionYCurso(cursoId, i);
            distribucion.put(i, count);
        }
        estadisticas.put("distribucion", distribucion);
        
        return estadisticas;
    }
}