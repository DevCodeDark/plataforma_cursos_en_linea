package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.ModuloDTO;
import com.devcodedark.plataforma_cursos.model.Modulo;
import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.service.IModuloService;

@Service
@Transactional
public class ModuloServiceJpa implements IModuloService {

    @Autowired
    private ModuloRepository moduloRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private MaterialRepository materialRepository;

    // Métodos de conversión entre Entity y DTO
    private ModuloDTO convertToDTO(Modulo modulo) {
        if (modulo == null) {
            return null;
        }
        
        ModuloDTO dto = new ModuloDTO();
        dto.setId(modulo.getId());
        dto.setCursoId(modulo.getCurso() != null ? modulo.getCurso().getId() : null);
        dto.setCursoTitulo(modulo.getCurso() != null ? modulo.getCurso().getTitulo() : null);
        dto.setCursoCategoria(modulo.getCurso() != null && modulo.getCurso().getCategoria() != null ? 
                            modulo.getCurso().getCategoria().getNombre() : null);
        dto.setTitulo(modulo.getTitulo());
        dto.setDescripcion(modulo.getDescripcion());
        dto.setOrden(modulo.getOrden());
        dto.setDuracion(modulo.getDuracion());
        dto.setEsObligatorio(modulo.getEsObligatorio());
        dto.setFechaCreacion(modulo.getFechaCreacion());
        dto.setFechaActualizacion(modulo.getFechaActualizacion());
        dto.setEstado(modulo.getEstado());
        
        // Campos calculados
        if (modulo.getId() != null) {
            dto.setTotalMateriales(materialRepository.countMaterialesByModulo(modulo.getId()).intValue());
            dto.setTotalMaterialesGratuitos(materialRepository.countByModuloIdAndEsGratuitoTrue(modulo.getId()).intValue());
            dto.setDuracionTotalVideos(materialRepository.sumDuracionVideosByModulo(modulo.getId()));
            dto.setTieneProgresos(modulo.getProgresosModulo() != null && !modulo.getProgresosModulo().isEmpty());
        }
        
        return dto;
    }
    
    private Modulo convertToEntity(ModuloDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Modulo modulo = new Modulo();
        modulo.setId(dto.getId());
        modulo.setTitulo(dto.getTitulo());
        modulo.setDescripcion(dto.getDescripcion());
        modulo.setOrden(dto.getOrden());
        modulo.setDuracion(dto.getDuracion());
        modulo.setEsObligatorio(dto.getEsObligatorio());
        modulo.setFechaCreacion(dto.getFechaCreacion());
        modulo.setFechaActualizacion(dto.getFechaActualizacion());
        modulo.setEstado(dto.getEstado());
        
        // Cargar curso
        if (dto.getCursoId() != null) {
            Optional<Curso> curso = cursoRepository.findById(dto.getCursoId());
            if (curso.isPresent()) {
                modulo.setCurso(curso.get());
            } else {
                throw new IllegalArgumentException("Curso con ID " + dto.getCursoId() + " no encontrado");
            }
        }
        
        return modulo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> buscarTodos() {
        return moduloRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void guardar(ModuloDTO moduloDTO) {
        // Si no tiene orden asignado, asignar el siguiente disponible
        if (moduloDTO.getOrden() == null && moduloDTO.getCursoId() != null) {
            Integer siguienteOrden = moduloRepository.findNextOrdenByCurso(moduloDTO.getCursoId());
            moduloDTO.setOrden(siguienteOrden);
        }
        Modulo modulo = convertToEntity(moduloDTO);
        moduloRepository.save(modulo);
    }

    @Override
    public void modificar(ModuloDTO moduloDTO) {
        Modulo modulo = convertToEntity(moduloDTO);
        moduloRepository.save(modulo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModuloDTO> buscarId(Integer id) {
        return moduloRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        moduloRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> buscarPorCursoOrdenado(Integer cursoId) {
        return moduloRepository.findByCursoIdOrderByOrden(cursoId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> buscarModulosActivosPorCurso(Integer cursoId) {
        return moduloRepository.findModulosActivosByCurso(cursoId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> buscarModulosObligatoriosPorCurso(Integer cursoId) {
        return moduloRepository.findModulosObligatoriosByCurso(cursoId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarModulosPorCurso(Integer cursoId) {
        return moduloRepository.countModulosByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerSiguienteOrden(Integer cursoId) {
        return moduloRepository.findNextOrdenByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calcularDuracionTotalPorCurso(Integer cursoId) {
        return moduloRepository.sumDuracionByCurso(cursoId);
    }

    @Override
    public void reordenarModulos(Integer cursoId, List<Integer> nuevoOrden) {
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Integer moduloId = nuevoOrden.get(i);
            Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
            if (moduloOpt.isPresent()) {
                Modulo modulo = moduloOpt.get();
                modulo.setOrden(i + 1);
                moduloRepository.save(modulo);
            }
        }
    }

    @Override
    public void cambiarObligatorio(Integer moduloId, Boolean esObligatorio) {
        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        if (moduloOpt.isPresent()) {
            Modulo modulo = moduloOpt.get();
            modulo.setEsObligatorio(esObligatorio);
            moduloRepository.save(modulo);
        }
    }
}