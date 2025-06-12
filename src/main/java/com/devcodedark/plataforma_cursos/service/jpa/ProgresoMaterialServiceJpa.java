package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.ProgresoMaterialDTO;
import com.devcodedark.plataforma_cursos.model.ProgresoMaterial;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.repository.ProgresoMaterialRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.service.IProgresoMaterialService;

@Service
@Transactional
public class ProgresoMaterialServiceJpa implements IProgresoMaterialService {

    @Autowired
    private ProgresoMaterialRepository progresoMaterialRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterialDTO> buscarTodos() {
        return progresoMaterialRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public void guardar(ProgresoMaterialDTO progresoMaterialDTO) {
        ProgresoMaterial progreso = convertirAEntidad(progresoMaterialDTO);
        progresoMaterialRepository.save(progreso);
    }

    @Override
    public void modificar(ProgresoMaterialDTO progresoMaterialDTO) {
        if (progresoMaterialDTO.getId() == null) {
            throw new IllegalArgumentException("El ID del progreso es requerido para modificar");
        }
        
        Optional<ProgresoMaterial> progresoExistente = progresoMaterialRepository.findById(progresoMaterialDTO.getId());
        if (!progresoExistente.isPresent()) {
            throw new IllegalArgumentException("Progreso no encontrado con ID: " + progresoMaterialDTO.getId());
        }
        
        ProgresoMaterial progreso = progresoExistente.get();
        actualizarEntidadDesdeDTO(progreso, progresoMaterialDTO);
        progresoMaterialRepository.save(progreso);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterialDTO> buscarId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        return progresoMaterialRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!progresoMaterialRepository.existsById(id)) {
            throw new IllegalArgumentException("Progreso no encontrado con ID: " + id);
        }
        
        progresoMaterialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterialDTO> buscarPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        return progresoMaterialRepository.findByInscripcionId(inscripcionId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterialDTO> buscarPorInscripcionYMaterial(Integer inscripcionId, Integer materialId) {
        if (inscripcionId == null || materialId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y material no pueden ser nulos");
        }
        
        return progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterialDTO> buscarMaterialesVisualizadosPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        return progresoMaterialRepository.findMaterialesVisualizadosByInscripcion(inscripcionId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterialDTO> buscarMaterialesCompletadosPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        return progresoMaterialRepository.findMaterialesCompletadosByInscripcion(inscripcionId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMaterialesCompletadosPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        return progresoMaterialRepository.countMaterialesCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterialDTO> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId) {
        if (inscripcionId == null || moduloId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y módulo no pueden ser nulos");
        }
        
        return progresoMaterialRepository.findByInscripcionIdAndModuloId(inscripcionId, moduloId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long calcularTiempoTotalReproducidoPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        return progresoMaterialRepository.sumTiempoReproducidoByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProgreso(Integer inscripcionId, Integer materialId) {
        if (inscripcionId == null || materialId == null) {
            return false;
        }
        
        return progresoMaterialRepository.existsByInscripcionIdAndMaterialId(inscripcionId, materialId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterialDTO> buscarUltimoMaterialAccedido(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        List<ProgresoMaterial> progresos = progresoMaterialRepository.findUltimoMaterialAccedidoByInscripcion(inscripcionId);
        return progresos.isEmpty() ? Optional.empty() : Optional.of(convertirADTO(progresos.get(0)));
    }

    @Override
    public void actualizarTiempoReproducido(Integer inscripcionId, Integer materialId, Integer tiempoReproducido) {
        if (inscripcionId == null || materialId == null || tiempoReproducido == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        Optional<ProgresoMaterial> progresoOpt = progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId);
        
        if (progresoOpt.isPresent()) {
            ProgresoMaterial progreso = progresoOpt.get();
            progreso.setTiempoReproducido(tiempoReproducido);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progresoMaterialRepository.save(progreso);
        } else {
            // Crear progreso si no existe
            crearProgresoSiNoExiste(inscripcionId, materialId);
            actualizarTiempoReproducido(inscripcionId, materialId, tiempoReproducido);
        }
    }

    @Override
    public void marcarComoVisualizado(Integer inscripcionId, Integer materialId) {
        if (inscripcionId == null || materialId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y material no pueden ser nulos");
        }
        
        Optional<ProgresoMaterial> progresoOpt = progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId);
          if (progresoOpt.isPresent()) {
            ProgresoMaterial progreso = progresoOpt.get();
            progreso.setVisualizado(true);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progresoMaterialRepository.save(progreso);
        } else {
            // Crear progreso si no existe
            crearProgresoSiNoExiste(inscripcionId, materialId);
            marcarComoVisualizado(inscripcionId, materialId);
        }
    }

    @Override
    public void marcarComoCompletado(Integer inscripcionId, Integer materialId) {
        if (inscripcionId == null || materialId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y material no pueden ser nulos");
        }
        
        Optional<ProgresoMaterial> progresoOpt = progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId);
          if (progresoOpt.isPresent()) {
            ProgresoMaterial progreso = progresoOpt.get();
            progreso.setCompletado(true);
            progreso.setVisualizado(true);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progresoMaterialRepository.save(progreso);
        } else {
            // Crear progreso si no existe
            crearProgresoSiNoExiste(inscripcionId, materialId);
            marcarComoCompletado(inscripcionId, materialId);
        }
    }

    @Override
    public ProgresoMaterialDTO crearProgresoSiNoExiste(Integer inscripcionId, Integer materialId) {
        if (inscripcionId == null || materialId == null) {
            throw new IllegalArgumentException("Los IDs de inscripción y material no pueden ser nulos");
        }
          if (progresoMaterialRepository.existsByInscripcionIdAndMaterialId(inscripcionId, materialId)) {
            return progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId)
                    .map(this::convertirADTO).orElse(null);
        }
        
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        Optional<Material> materialOpt = materialRepository.findById(materialId);
        
        if (!inscripcionOpt.isPresent()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + inscripcionId);
        }
        
        if (!materialOpt.isPresent()) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + materialId);
        }
        
        ProgresoMaterial progreso = new ProgresoMaterial();
        progreso.setInscripcion(inscripcionOpt.get());
        progreso.setMaterial(materialOpt.get());
        progreso.setVisualizado(false);
        progreso.setCompletado(false);
        progreso.setTiempoReproducido(0);
        progreso.setFechaUltimoAcceso(LocalDateTime.now());
        
        ProgresoMaterial progresoGuardado = progresoMaterialRepository.save(progreso);
        return convertirADTO(progresoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPorcentajeProgresoPorInscripcion(Integer inscripcionId) {
        if (inscripcionId == null) {
            throw new IllegalArgumentException("El ID de inscripción no puede ser nulo");
        }
        
        // Obtener inscripción para acceder al curso
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        if (!inscripcionOpt.isPresent()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + inscripcionId);
        }
        
        // Contar total de materiales del curso
        Long totalMateriales = materialRepository.countByCursoId(inscripcionOpt.get().getCurso().getId());
        if (totalMateriales == 0) {
            return 0.0;
        }
          // Contar materiales completados
        Long materialesCompletados = progresoMaterialRepository.countMaterialesCompletadosByInscripcion(inscripcionId);
        
        return (materialesCompletados.doubleValue() / totalMateriales.doubleValue()) * 100.0;
    }

    // Métodos de conversión entre Entity y DTO
    private ProgresoMaterialDTO convertirADTO(ProgresoMaterial progreso) {
        ProgresoMaterialDTO dto = new ProgresoMaterialDTO();
        
        dto.setId(progreso.getId());
        dto.setInscripcionId(progreso.getInscripcion().getId());
        dto.setMaterialId(progreso.getMaterial().getId());
        dto.setVisualizado(progreso.getVisualizado());
        dto.setTiempoReproducido(progreso.getTiempoReproducido());
        dto.setFechaUltimoAcceso(progreso.getFechaUltimoAcceso());
        dto.setCompletado(progreso.getCompletado());
        
        // Información enriquecida de la inscripción
        Inscripcion inscripcion = progreso.getInscripcion();
        dto.setEstudianteNombre(inscripcion.getEstudiante().getNombre() + " " + inscripcion.getEstudiante().getApellido());
        dto.setEstudianteEmail(inscripcion.getEstudiante().getEmail());
        dto.setCursoTitulo(inscripcion.getCurso().getTitulo());
        
        // Información enriquecida del material
        Material material = progreso.getMaterial();
        dto.setMaterialTitulo(material.getTitulo());
        dto.setMaterialTipo(material.getTipo().toString());
        dto.setMaterialUrl(material.getUrl());
        dto.setDuracionVideo(material.getDuracion());
        dto.setModuloTitulo(material.getModulo().getTitulo());
        
        // Calcular campos derivados
        dto.determinarEsVideo();
        dto.calcularPorcentajeCompletado();
        dto.calcularTiempoRestante();
        dto.calcularEstadoProgreso();
        
        return dto;
    }

    private ProgresoMaterial convertirAEntidad(ProgresoMaterialDTO dto) {
        ProgresoMaterial progreso = new ProgresoMaterial();
        
        if (dto.getId() != null) {
            progreso.setId(dto.getId());
        }
        
        // Obtener entidades relacionadas
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(dto.getInscripcionId());
        Optional<Material> materialOpt = materialRepository.findById(dto.getMaterialId());
        
        if (!inscripcionOpt.isPresent()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + dto.getInscripcionId());
        }
        
        if (!materialOpt.isPresent()) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + dto.getMaterialId());
        }
        
        progreso.setInscripcion(inscripcionOpt.get());
        progreso.setMaterial(materialOpt.get());
        progreso.setVisualizado(dto.getVisualizado());
        progreso.setTiempoReproducido(dto.getTiempoReproducido());
        progreso.setFechaUltimoAcceso(dto.getFechaUltimoAcceso());
        progreso.setCompletado(dto.getCompletado());
        
        return progreso;
    }

    private void actualizarEntidadDesdeDTO(ProgresoMaterial progreso, ProgresoMaterialDTO dto) {
        if (dto.getVisualizado() != null) {
            progreso.setVisualizado(dto.getVisualizado());
        }
        if (dto.getTiempoReproducido() != null) {
            progreso.setTiempoReproducido(dto.getTiempoReproducido());
        }
        if (dto.getFechaUltimoAcceso() != null) {
            progreso.setFechaUltimoAcceso(dto.getFechaUltimoAcceso());
        }
        if (dto.getCompletado() != null) {
            progreso.setCompletado(dto.getCompletado());
        }
        
        // Si se modificó la inscripción o material
        if (dto.getInscripcionId() != null && !dto.getInscripcionId().equals(progreso.getInscripcion().getId())) {
            Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(dto.getInscripcionId());
            if (!inscripcionOpt.isPresent()) {
                throw new IllegalArgumentException("Inscripción no encontrada con ID: " + dto.getInscripcionId());
            }
            progreso.setInscripcion(inscripcionOpt.get());
        }
        
        if (dto.getMaterialId() != null && !dto.getMaterialId().equals(progreso.getMaterial().getId())) {
            Optional<Material> materialOpt = materialRepository.findById(dto.getMaterialId());
            if (!materialOpt.isPresent()) {
                throw new IllegalArgumentException("Material no encontrado con ID: " + dto.getMaterialId());
            }
            progreso.setMaterial(materialOpt.get());
        }
    }
}