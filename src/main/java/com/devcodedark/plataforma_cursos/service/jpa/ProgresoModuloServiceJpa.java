package com.devcodedark.plataforma_cursos.service.jpa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.ProgresoModuloDTO;
import com.devcodedark.plataforma_cursos.model.ProgresoModulo;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Modulo;
import com.devcodedark.plataforma_cursos.repository.ProgresoModuloRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.repository.ProgresoMaterialRepository;
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

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProgresoMaterialRepository progresoMaterialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModuloDTO> buscarTodos() {
        return progresoModuloRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public void guardar(ProgresoModuloDTO progresoModuloDTO) {
        if (progresoModuloDTO == null) {
            throw new IllegalArgumentException("El progreso de módulo no puede ser nulo");
        }
        ProgresoModulo progreso = convertirAEntidad(progresoModuloDTO);
        progresoModuloRepository.save(progreso);
    }

    @Override
    public void modificar(ProgresoModuloDTO progresoModuloDTO) {
        if (progresoModuloDTO == null || progresoModuloDTO.getId() == null) {
            throw new IllegalArgumentException("El progreso de módulo y su ID no pueden ser nulos");
        }
        ProgresoModulo progreso = convertirAEntidad(progresoModuloDTO);
        progresoModuloRepository.save(progreso);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        progresoModuloRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModuloDTO> buscarId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return progresoModuloRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModuloDTO> buscarPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        return progresoModuloRepository.findByInscripcionId(inscripcionId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModuloDTO> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }
        return progresoModuloRepository.findByInscripcionIdAndModuloId(inscripcionId, moduloId)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModuloDTO> buscarModulosCompletadosPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        return progresoModuloRepository.findModulosCompletadosByInscripcion(inscripcionId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarModulosCompletadosPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        return progresoModuloRepository.countModulosCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calcularTiempoTotalInvertidoPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        return progresoModuloRepository.sumTiempoInvertidoByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoModuloDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId) {
        if (estudianteId == null || cursoId == null) {
            throw new IllegalArgumentException("Los IDs de estudiante y curso no pueden ser nulos");
        }
        return progresoModuloRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProgreso(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }
        return progresoModuloRepository.existsByInscripcionIdAndModuloId(inscripcionId, moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoModuloDTO> buscarUltimoModuloAccedido(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        List<ProgresoModulo> progresos = progresoModuloRepository.findUltimoModuloAccedidoByInscripcion(inscripcionId);
        return progresos.isEmpty() ? Optional.empty() : Optional.of(convertirADTO(progresos.get(0)));
    }

    @Override
    public ProgresoModuloDTO iniciarModulo(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }
        // Verificar que no exista ya un progreso para este módulo
        if (existeProgreso(inscripcionId, moduloId)) {
            Optional<ProgresoModuloDTO> progresoExistente = buscarPorInscripcionYModulo(inscripcionId, moduloId);
            if (progresoExistente.isPresent()) {
                return progresoExistente.get();
            }
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

            ProgresoModulo savedProgreso = progresoModuloRepository.save(progreso);
            return convertirADTO(savedProgreso);
        }

        throw new IllegalArgumentException("Inscripción o módulo no encontrado");
    }

    @Override
    public ProgresoModuloDTO crearProgresoSiNoExiste(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }

        Optional<ProgresoModuloDTO> progresoExistente = buscarPorInscripcionYModulo(inscripcionId, moduloId);
        if (progresoExistente.isPresent()) {
            return progresoExistente.get();
        }

        return iniciarModulo(inscripcionId, moduloId);
    }

    @Override
    public void completarModulo(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }

        Optional<ProgresoModulo> progresoOpt = progresoModuloRepository.findByInscripcionIdAndModuloId(inscripcionId,
                moduloId);
        if (progresoOpt.isPresent()) {
            ProgresoModulo progreso = progresoOpt.get();
            progreso.setCompletado(true);
            progreso.setFechaCompletado(LocalDateTime.now());
            progresoModuloRepository.save(progreso);
        } else {
            throw new IllegalArgumentException("Progreso de módulo no encontrado");
        }
    }

    @Override
    public void actualizarTiempoInvertido(Integer inscripcionId, Integer moduloId, Integer tiempoAdicional) {
        if (inscripcionId == null || moduloId == null || tiempoAdicional == null) {
            throw new IllegalArgumentException("Los IDs de inscripción, módulo y tiempo adicional no pueden ser nulos");
        }

        Optional<ProgresoModulo> progresoOpt = progresoModuloRepository.findByInscripcionIdAndModuloId(inscripcionId,
                moduloId);
        if (progresoOpt.isPresent()) {
            ProgresoModulo progreso = progresoOpt.get();
            Integer tiempoActual = progreso.getTiempoInvertido() != null ? progreso.getTiempoInvertido() : 0;
            progreso.setTiempoInvertido(tiempoActual + tiempoAdicional);
            progresoModuloRepository.save(progreso);
        } else {
            throw new IllegalArgumentException("Progreso de módulo no encontrado");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPorcentajeProgresoCurso(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }

        // Obtener la inscripción para conocer el curso
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            return 0.0;
        }

        Integer cursoId = inscripcionOpt.get().getCurso().getId();

        // Contar total de módulos del curso
        Long totalModulos = moduloRepository.countByCursoId(cursoId);

        // Contar módulos completados
        Long modulosCompletados = contarModulosCompletadosPorInscripcion(inscripcionId);

        if (totalModulos == 0) {
            return 0.0;
        }

        // Calcular porcentaje
        return (modulosCompletados.doubleValue() / totalModulos.doubleValue()) * 100.0;
    }

    // Métodos de conversión
    private ProgresoModuloDTO convertirADTO(ProgresoModulo progreso) {
        ProgresoModuloDTO dto = new ProgresoModuloDTO();
        dto.setId(progreso.getId());
        dto.setInscripcionId(progreso.getInscripcion().getId());
        dto.setModuloId(progreso.getModulo().getId());
        dto.setFechaInicio(progreso.getFechaInicio());
        dto.setFechaCompletado(progreso.getFechaCompletado());
        dto.setCompletado(progreso.getCompletado());
        dto.setTiempoInvertido(progreso.getTiempoInvertido());

        // Información enriquecida del estudiante
        dto.setEstudianteNombre(progreso.getInscripcion().getEstudiante().getNombre());
        dto.setEstudianteEmail(progreso.getInscripcion().getEstudiante().getEmail());

        // Información enriquecida del curso
        dto.setCursoTitulo(progreso.getInscripcion().getCurso().getTitulo());

        // Información enriquecida del módulo
        dto.setModuloTitulo(progreso.getModulo().getTitulo());
        dto.setModuloDescripcion(progreso.getModulo().getDescripcion());
        // Campos calculados
        dto.setPorcentajeCompletado(calcularPorcentajeCompletado(progreso).intValue());
        dto.setTiempoInvertidoFormateado(formatearTiempoInvertido(progreso.getTiempoInvertido()));
        dto.setEstadoProgreso(calcularEstadoProgreso(progreso));
        dto.setDiasTranscurridos(calcularDiasTranscurridos(progreso.getFechaInicio()));
        // Información de materiales del módulo
        Long totalMateriales = materialRepository.countMaterialesByModulo(progreso.getModulo().getId());
        dto.setTotalMateriales(totalMateriales != null ? totalMateriales.intValue() : 0);

        Long materialesCompletados = progresoMaterialRepository.countMaterialesCompletadosByInscripcionAndModulo(
                progreso.getInscripcion().getId(), progreso.getModulo().getId());
        dto.setMaterialesCompletados(materialesCompletados != null ? materialesCompletados.intValue() : 0);

        // Calcular progreso de materiales
        if (dto.getTotalMateriales() > 0) {
            double progresoMateriales = (dto.getMaterialesCompletados().doubleValue()
                    / dto.getTotalMateriales().doubleValue()) * 100.0;
            dto.setProgresoMateriales(progresoMateriales);
        } else {
            dto.setProgresoMateriales(0.0);
        }

        return dto;
    }

    private ProgresoModulo convertirAEntidad(ProgresoModuloDTO dto) {
        ProgresoModulo progreso = new ProgresoModulo();
        progreso.setId(dto.getId());
        progreso.setFechaInicio(dto.getFechaInicio());
        progreso.setFechaCompletado(dto.getFechaCompletado());
        progreso.setCompletado(dto.getCompletado());
        progreso.setTiempoInvertido(dto.getTiempoInvertido());

        // Cargar entidades relacionadas
        if (dto.getInscripcionId() != null) {
            Optional<Inscripcion> inscripcion = inscripcionRepository.findById(dto.getInscripcionId());
            if (inscripcion.isPresent()) {
                progreso.setInscripcion(inscripcion.get());
            } else {
                throw new IllegalArgumentException("Inscripción no encontrada con ID: " + dto.getInscripcionId());
            }
        }

        if (dto.getModuloId() != null) {
            Optional<Modulo> modulo = moduloRepository.findById(dto.getModuloId());
            if (modulo.isPresent()) {
                progreso.setModulo(modulo.get());
            } else {
                throw new IllegalArgumentException("Módulo no encontrado con ID: " + dto.getModuloId());
            }
        }

        return progreso;
    }

    // Métodos helper para cálculos
    private Double calcularPorcentajeCompletado(ProgresoModulo progreso) {
        if (progreso.getCompletado()) {
            return 100.0;
        }
        // Calcular basado en materiales completados
        Long totalMateriales = materialRepository.countMaterialesByModulo(progreso.getModulo().getId());
        if (totalMateriales == null || totalMateriales == 0) {
            return 0.0;
        }

        Long materialesCompletados = progresoMaterialRepository.countMaterialesCompletadosByInscripcionAndModulo(
                progreso.getInscripcion().getId(), progreso.getModulo().getId());
        if (materialesCompletados == null) {
            materialesCompletados = 0L;
        }

        return (materialesCompletados.doubleValue() / totalMateriales.doubleValue()) * 100.0;
    }

    private String formatearTiempoInvertido(Integer tiempoEnMinutos) {
        if (tiempoEnMinutos == null || tiempoEnMinutos == 0) {
            return "0 minutos";
        }

        if (tiempoEnMinutos < 60) {
            return tiempoEnMinutos + " minutos";
        }

        int horas = tiempoEnMinutos / 60;
        int minutosRestantes = tiempoEnMinutos % 60;

        if (minutosRestantes == 0) {
            return horas + " horas";
        }

        return horas + " horas " + minutosRestantes + " minutos";
    }

    private String calcularEstadoProgreso(ProgresoModulo progreso) {
        if (progreso.getCompletado()) {
            return "Completado";
        }

        Double porcentaje = calcularPorcentajeCompletado(progreso);

        if (porcentaje == 0.0) {
            return "No iniciado";
        } else if (porcentaje < 50.0) {
            return "En progreso (inicial)";
        } else if (porcentaje < 100.0) {
            return "En progreso (avanzado)";
        } else {
            return "Listo para completar";
        }
    }

    private Long calcularDiasTranscurridos(LocalDateTime fechaInicio) {
        if (fechaInicio == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(fechaInicio.toLocalDate(), LocalDateTime.now().toLocalDate());
    }
}