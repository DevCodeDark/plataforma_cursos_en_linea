package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.ProgresoModulo;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Modulo;
import com.devcodedark.plataforma_cursos.repository.ProgresoModuloRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.service.IProgresoModuloService;

@Service
@Transactional
public class ProgresoModuloServiceJpa implements IProgresoModuloService {

    @Autowired
    private ProgresoModuloRepository progresoModuloRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private ModuloRepository moduloRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModulo> buscarTodos() {
        return progresoModuloRepository.findAll();
    }

    @Override
    public void guardar(ProgresoModulo progresoModulo) {
        progresoModuloRepository.save(progresoModulo);
    }

    @Override
    public void modificar(ProgresoModulo progresoModulo) {
        progresoModuloRepository.save(progresoModulo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModulo> buscarId(Integer id) {
        return progresoModuloRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        progresoModuloRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModulo> buscarPorInscripcion(Integer inscripcionId) {
        return progresoModuloRepository.findByInscripcionId(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModulo> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId) {
        return progresoModuloRepository.findByInscripcionIdAndModuloId(inscripcionId, moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModulo> buscarModulosCompletadosPorInscripcion(Integer inscripcionId) {
        return progresoModuloRepository.findModulosCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarModulosCompletadosPorInscripcion(Integer inscripcionId) {
        return progresoModuloRepository.countModulosCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calcularTiempoTotalInvertidoPorInscripcion(Integer inscripcionId) {
        return progresoModuloRepository.sumTiempoInvertidoByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModulo> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        return progresoModuloRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProgreso(Integer inscripcionId, Integer moduloId) {
        return progresoModuloRepository.existsByInscripcionIdAndModuloId(inscripcionId, moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModulo> buscarUltimoModuloAccedido(Integer inscripcionId) {
        List<ProgresoModulo> progresos = progresoModuloRepository.findUltimoModuloAccedidoByInscripcion(inscripcionId);
        return progresos.isEmpty() ? Optional.empty() : Optional.of(progresos.get(0));
    }

    @Override
    public ProgresoModulo iniciarModulo(Integer inscripcionId, Integer moduloId) {
        // Verificar que no exista ya un progreso para este módulo
        if (existeProgreso(inscripcionId, moduloId)) {
            return buscarPorInscripcionYModulo(inscripcionId, moduloId).get();
        }
        
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        
        if (inscripcionOpt.isPresent() && moduloOpt.isPresent()) {
            ProgresoModulo progreso = new ProgresoModulo();
            progreso.setInscripcion(inscripcionOpt.get());
            progreso.setModulo(moduloOpt.get());
            progreso.setFechaInicio(LocalDateTime.now());
            progreso.setCompletado(false);
            progreso.setTiempoInvertido(0);
            
            return progresoModuloRepository.save(progreso);
        }
        
        throw new RuntimeException("Inscripción o módulo no encontrado");
    }

    @Override
    public void completarModulo(Integer progresoModuloId) {
        Optional<ProgresoModulo> progresoOpt = progresoModuloRepository.findById(progresoModuloId);
        if (progresoOpt.isPresent()) {
            ProgresoModulo progreso = progresoOpt.get();
            progreso.setCompletado(true);
            progreso.setFechaCompletado(LocalDateTime.now());
            progresoModuloRepository.save(progreso);
        }
    }

    @Override
    public void actualizarTiempoInvertido(Integer progresoModuloId, Integer tiempoAdicional) {
        Optional<ProgresoModulo> progresoOpt = progresoModuloRepository.findById(progresoModuloId);
        if (progresoOpt.isPresent()) {
            ProgresoModulo progreso = progresoOpt.get();
            Integer tiempoActual = progreso.getTiempoInvertido() != null ? progreso.getTiempoInvertido() : 0;
            progreso.setTiempoInvertido(tiempoActual + tiempoAdicional);
            progresoModuloRepository.save(progreso);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPorcentajeProgresoCurso(Integer inscripcionId) {
        // Obtener la inscripción para conocer el curso
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            return BigDecimal.ZERO;
        }
        
        Integer cursoId = inscripcionOpt.get().getCurso().getId();
        
        // Contar total de módulos del curso
        Long totalModulos = moduloRepository.countModulosByCurso(cursoId);
        
        // Contar módulos completados
        Long modulosCompletados = contarModulosCompletadosPorInscripcion(inscripcionId);
        
        if (totalModulos == 0) {
            return BigDecimal.ZERO;
        }
        
        // Calcular porcentaje
        BigDecimal porcentaje = new BigDecimal(modulosCompletados)
            .multiply(new BigDecimal("100"))
            .divide(new BigDecimal(totalModulos), 2, BigDecimal.ROUND_HALF_UP);
            
        return porcentaje;
    }
}