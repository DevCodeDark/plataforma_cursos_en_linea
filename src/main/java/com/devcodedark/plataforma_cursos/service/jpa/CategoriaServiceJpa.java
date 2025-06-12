package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.CategoriaDTO;
import com.devcodedark.plataforma_cursos.model.Categoria;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;
import com.devcodedark.plataforma_cursos.repository.CategoriaRepository;
import com.devcodedark.plataforma_cursos.service.ICategoriaService;

@Service
@Transactional
public class CategoriaServiceJpa implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Métodos de conversión entre Entity y DTO
    private CategoriaDTO convertToDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setIcono(categoria.getIcono());
        dto.setColor(categoria.getColor());
        dto.setEstado(categoria.getEstado());
        dto.setFechaCreacion(categoria.getFechaCreacion());
        dto.setFechaActualizacion(categoria.getFechaActualizacion());
        
        // Calcular total de cursos si es necesario
        if (categoria.getCursos() != null) {
            dto.setTotalCursos(categoria.getCursos().size());
        }
        
        return dto;
    }
    
    private Categoria convertToEntity(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setIcono(dto.getIcono());
        categoria.setColor(dto.getColor());
        categoria.setEstado(dto.getEstado());
        categoria.setFechaCreacion(dto.getFechaCreacion());
        categoria.setFechaActualizacion(dto.getFechaActualizacion());
        
        return categoria;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertToEntity(categoriaDTO);
        categoriaRepository.save(categoria);
    }

    @Override
    public void modificar(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertToEntity(categoriaDTO);
        categoriaRepository.save(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> buscarId(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarPorEstado(EstadoCategoria estado) {
        return categoriaRepository.findByEstado(estado)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarCategoriasActivas() {
        return categoriaRepository.findAllActivas()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCursosPorCategoria(Integer categoriaId) {
        return categoriaRepository.countCursosByCategoria(categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarCategoriasConCursos() {
        return categoriaRepository.findCategoriasConCursos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cambiarEstado(Integer categoriaId, EstadoCategoria estado) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setEstado(estado);
            categoriaRepository.save(categoria);
        }
    }
}