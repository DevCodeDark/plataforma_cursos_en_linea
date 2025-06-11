package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public List<Calificacion> buscarTodos() {
        return calificacionRepository.findAll();
    }

    @Override
    public void guardar(Calificacion calificacion) {
        calificacionRepository.save(calificacion);
    }

    @Override
    public void modificar(Calificacion calificacion) {
        calificacionRepository.save(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calificacion> buscarId(Integer id) {
        return calificacionRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        calificacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Calificacion> buscarPorCurso(Integer cursoId) {
        return calificacionRepository.findByCursoId(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Calificacion> buscarPorEstudiante(Integer estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calificacion> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return calificacionRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);
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
    public List<Calificacion> buscarPorRangoPuntuacion(Integer min, Integer max) {
        return calificacionRepository.findByPuntuacionBetween(min, max);
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