package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.MaterialDTO;
import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.model.Material.TipoMaterial;
import com.devcodedark.plataforma_cursos.model.Modulo;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.service.IMaterialService;

@Service
@Transactional
public class MaterialServiceJpa implements IMaterialService {

    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired
    private ModuloRepository moduloRepository;

    // Métodos de conversión entre Entity y DTO
    private MaterialDTO convertToDTO(Material material) {
        if (material == null) {
            return null;
        }
        
        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setModuloId(material.getModulo() != null ? material.getModulo().getId() : null);
        dto.setModuloTitulo(material.getModulo() != null ? material.getModulo().getTitulo() : null);
        dto.setTitulo(material.getTitulo());
        dto.setTipo(material.getTipo().name());
        dto.setUrl(material.getUrl());
        dto.setArchivoLocal(material.getArchivoLocal());
        dto.setDescripcion(material.getDescripcion());
        dto.setDuracion(material.getDuracion());
        dto.setTamañoArchivo(material.getTamañoArchivo());
        dto.setOrden(material.getOrden());
        dto.setEsGratuito(material.getEsGratuito());
        dto.setFechaCreacion(material.getFechaCreacion());
        dto.setFechaActualizacion(material.getFechaActualizacion());
        dto.setEstado(material.getEstado());
        
        // Información del curso
        if (material.getModulo() != null && material.getModulo().getCurso() != null) {
            dto.setCursoTitulo(material.getModulo().getCurso().getTitulo());
        }
        
        // Estadísticas de progreso
        if (material.getProgresosMaterial() != null) {
            dto.setTotalProgresos(material.getProgresosMaterial().size());
            long completados = material.getProgresosMaterial().stream()
                .filter(p -> p.getCompletado() != null && p.getCompletado())
                .count();
            if (dto.getTotalProgresos() > 0) {
                dto.setPorcentajeCompletado((completados * 100.0) / dto.getTotalProgresos());
            } else {
                dto.setPorcentajeCompletado(0.0);
            }
        }
        
        return dto;
    }
    
    private Material convertToEntity(MaterialDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Material material = new Material();
        material.setId(dto.getId());
        material.setTitulo(dto.getTitulo());
        material.setTipo(TipoMaterial.valueOf(dto.getTipo()));
        material.setUrl(dto.getUrl());
        material.setArchivoLocal(dto.getArchivoLocal());
        material.setDescripcion(dto.getDescripcion());
        material.setDuracion(dto.getDuracion());
        material.setTamañoArchivo(dto.getTamañoArchivo());
        material.setOrden(dto.getOrden());
        material.setEsGratuito(dto.getEsGratuito());
        material.setFechaCreacion(dto.getFechaCreacion());
        material.setFechaActualizacion(dto.getFechaActualizacion());
        material.setEstado(dto.getEstado());
          // Cargar módulo
        if (dto.getModuloId() != null) {
            Optional<Modulo> modulo = moduloRepository.findById(dto.getModuloId());
            if (modulo.isPresent()) {
                material.setModulo(modulo.get());
            } else {
                throw new IllegalArgumentException("Módulo con ID " + dto.getModuloId() + " no encontrado");
            }
        }
        
        return material;
    }    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarTodos() {
        return materialRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void guardar(MaterialDTO materialDTO) {
        // Si no tiene orden asignado, asignar el siguiente disponible
        if (materialDTO.getOrden() == null && materialDTO.getModuloId() != null) {
            Integer siguienteOrden = materialRepository.findNextOrdenByModulo(materialDTO.getModuloId());
            materialDTO.setOrden(siguienteOrden);
        }
        Material material = convertToEntity(materialDTO);
        materialRepository.save(material);
    }

    @Override
    public void modificar(MaterialDTO materialDTO) {
        Material material = convertToEntity(materialDTO);
        materialRepository.save(material);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialDTO> buscarId(Integer id) {
        return materialRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorModuloOrdenado(Integer moduloId) {        return materialRepository.findByModuloIdOrderByOrden(moduloId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarMaterialesActivosPorModulo(Integer moduloId) {        return materialRepository.findMaterialesActivosByModulo(moduloId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorTipo(String tipo) {
        TipoMaterial tipoMaterial = TipoMaterial.valueOf(tipo);        return materialRepository.findByTipo(tipoMaterial)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarMaterialesGratuitosPorModulo(Integer moduloId) {        return materialRepository.findMaterialesGratuitosByModulo(moduloId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarMaterialesPorCurso(Integer cursoId) {        return materialRepository.findMaterialesByCurso(cursoId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMaterialesPorModulo(Integer moduloId) {
        return materialRepository.countMaterialesByModulo(moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerSiguienteOrden(Integer moduloId) {
        return materialRepository.findNextOrdenByModulo(moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calcularDuracionVideosPorModulo(Integer moduloId) {
        return materialRepository.sumDuracionVideosByModulo(moduloId);
    }

    @Override
    public void reordenarMateriales(Integer moduloId, List<Integer> nuevoOrden) {
        List<Material> materiales = materialRepository.findByModuloIdOrderByOrden(moduloId);
        
        for (int i = 0; i < nuevoOrden.size() && i < materiales.size(); i++) {
            Integer materialId = nuevoOrden.get(i);
            Material material = materiales.stream()
                .filter(m -> m.getId().equals(materialId))
                .findFirst()
                .orElse(null);
            
            if (material != null) {
                material.setOrden(i + 1);
                materialRepository.save(material);
            }
        }
    }

    @Override
    public void cambiarDisponibilidadGratuita(Integer materialId, Boolean esGratuito) {
        Optional<Material> materialOpt = materialRepository.findById(materialId);
        if (materialOpt.isPresent()) {
            Material material = materialOpt.get();
            material.setEsGratuito(esGratuito);
            materialRepository.save(material);
        } else {
            throw new IllegalArgumentException("Material con ID " + materialId + " no encontrado");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerEstadisticasPorModulo(Integer moduloId) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        Long totalMateriales = materialRepository.countMaterialesByModulo(moduloId);
        estadisticas.put("totalMateriales", totalMateriales);
        
        // Materiales por tipo
        Map<String, Long> materialesPorTipo = new HashMap<>();
        for (TipoMaterial tipo : TipoMaterial.values()) {
            Long count = materialRepository.countByModuloIdAndTipo(moduloId, tipo);
            materialesPorTipo.put(tipo.name(), count);
        }
        estadisticas.put("materialesPorTipo", materialesPorTipo);
        
        // Duración total de videos
        Integer duracionTotal = materialRepository.sumDuracionVideosByModulo(moduloId);
        estadisticas.put("duracionTotalVideos", duracionTotal);
        
        // Materiales gratuitos vs premium
        Long materialesGratuitos = materialRepository.countByModuloIdAndEsGratuitoTrue(moduloId);
        Long materialesPremium = totalMateriales - materialesGratuitos;
        estadisticas.put("materialesGratuitos", materialesGratuitos);
        estadisticas.put("materialesPremium", materialesPremium);
        
        return estadisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorTitulo(String titulo) {        return materialRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDTO> obtenerMaterialesRecientes(int limite) {        return materialRepository.findTop10ByOrderByFechaCreacionDesc()
                .stream()
                .limit(limite)
                .map(this::convertToDTO)
                .toList();
    }
}