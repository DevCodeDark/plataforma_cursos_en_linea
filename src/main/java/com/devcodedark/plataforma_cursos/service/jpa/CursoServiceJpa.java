package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.CursoDTO;
import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Categoria;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Curso.EstadoCurso;
import com.devcodedark.plataforma_cursos.model.Curso.NivelCurso;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.repository.CategoriaRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.repository.CalificacionRepository;
import com.devcodedark.plataforma_cursos.service.ICursoService;

@Service
@Transactional
public class CursoServiceJpa implements ICursoService {

    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    private final CalificacionRepository calificacionRepository;
    
    public CursoServiceJpa(CursoRepository cursoRepository, 
                          CategoriaRepository categoriaRepository,
                          UsuarioRepository usuarioRepository,
                          ModuloRepository moduloRepository,
                          CalificacionRepository calificacionRepository) {
        this.cursoRepository = cursoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.calificacionRepository = calificacionRepository;
    }
    
    // Métodos de conversión entre Entity y DTO
    private CursoDTO convertToDTO(Curso curso) {
        if (curso == null) {
            return null;
        }
        
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setTitulo(curso.getTitulo());
        dto.setDescripcionCorta(curso.getDescripcionCorta());
        dto.setDescripcionCompleta(curso.getDescripcionCompleta());
        dto.setImagenPortada(curso.getImagenPortada());
        dto.setNivel(curso.getNivel() != null ? curso.getNivel().name() : null);
        dto.setPrecio(curso.getPrecio());
        dto.setEsGratuito(curso.getEsGratuito());
        dto.setDuracionEstimada(curso.getDuracionEstimada());
        dto.setEstado(curso.getEstado() != null ? curso.getEstado().name() : null);
        dto.setFechaPublicacion(curso.getFechaPublicacion());
        dto.setFechaCreacion(curso.getFechaCreacion());
        dto.setFechaActualizacion(curso.getFechaActualizacion());
        
        // Información de categoría
        if (curso.getCategoria() != null) {
            dto.setCategoriaId(curso.getCategoria().getId());
            dto.setCategoriaNombre(curso.getCategoria().getNombre());
        }
        
        // Información de docente
        if (curso.getDocente() != null) {
            dto.setDocenteId(curso.getDocente().getId());
            dto.setDocenteNombre(curso.getDocente().getNombre() + " " + curso.getDocente().getApellido());
            dto.setDocenteEmail(curso.getDocente().getEmail());
        }        
        
        // Campos calculados
        dto.setTotalModulos(moduloRepository.countModulosByCurso(curso.getId()).intValue());
        dto.setTotalInscripciones(cursoRepository.countInscripcionesByCurso(curso.getId()).intValue());
        
        Double promedio = calificacionRepository.calcularPromedioCalificacionesByCurso(curso.getId());
        dto.setPromedioCalificaciones(promedio != null ? BigDecimal.valueOf(promedio) : BigDecimal.ZERO);
        dto.setTotalCalificaciones(calificacionRepository.countCalificacionesByCurso(curso.getId()).intValue());
        dto.setPuedeInscribirse(curso.getEstado() == EstadoCurso.publicado);
        
        return dto;
    }
    
    private Curso convertToEntity(CursoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Curso curso = new Curso();
        curso.setId(dto.getId());
        curso.setTitulo(dto.getTitulo());
        curso.setDescripcionCorta(dto.getDescripcionCorta());
        curso.setDescripcionCompleta(dto.getDescripcionCompleta());
        curso.setImagenPortada(dto.getImagenPortada());
        curso.setPrecio(dto.getPrecio());
        curso.setEsGratuito(dto.getEsGratuito());
        curso.setDuracionEstimada(dto.getDuracionEstimada());
        
        // Convertir enums
        if (dto.getNivel() != null) {
            curso.setNivel(NivelCurso.valueOf(dto.getNivel()));
        }
        
        if (dto.getEstado() != null) {
            curso.setEstado(EstadoCurso.valueOf(dto.getEstado()));
        }
        
        // Cargar categoría
        if (dto.getCategoriaId() != null) {
            Optional<Categoria> categoria = categoriaRepository.findById(dto.getCategoriaId());
            if (categoria.isPresent()) {
                curso.setCategoria(categoria.get());
            } else {
                throw new RuntimeException("Categoría con ID " + dto.getCategoriaId() + " no encontrada");
            }
        }
        
        // Cargar docente
        if (dto.getDocenteId() != null) {
            Optional<Usuario> docente = usuarioRepository.findById(dto.getDocenteId());
            if (docente.isPresent()) {
                curso.setDocente(docente.get());
            } else {
                throw new RuntimeException("Docente con ID " + dto.getDocenteId() + " no encontrado");
            }
        }
        
        return curso;
    }    
    
    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarTodos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(CursoDTO cursoDTO) {
        Curso curso = convertToEntity(cursoDTO);
        cursoRepository.save(curso);
    }

    @Override
    public void modificar(CursoDTO cursoDTO) {
        Curso curso = convertToEntity(cursoDTO);
        cursoRepository.save(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CursoDTO> buscarId(Integer id) {
        return cursoRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorCategoria(Integer categoriaId) {
        return cursoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorDocente(Integer docenteId) {
        return cursoRepository.findByDocenteId(docenteId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorEstado(String estado) {
        EstadoCurso estadoCurso = EstadoCurso.valueOf(estado);
        return cursoRepository.findByEstado(estadoCurso)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarCursosPublicados() {
        return cursoRepository.findCursosPublicados()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorNivel(String nivel) {
        NivelCurso nivelCurso = NivelCurso.valueOf(nivel);
        return cursoRepository.findByNivel(nivelCurso)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarCursosGratuitos() {
        return cursoRepository.findCursosGratuitos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return cursoRepository.findByPrecioBetween(precioMin, precioMax)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorTitulo(String titulo) {
        return cursoRepository.findByTituloContaining(titulo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInscripcionesPorCurso(Integer cursoId) {
        return cursoRepository.countInscripcionesByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarCursosMasPopulares() {
        return cursoRepository.findCursosMasPopulares()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void publicarCurso(Integer cursoId) {
        cambiarEstado(cursoId, "publicado");
    }

    @Override
    public void pausarCurso(Integer cursoId) {
        cambiarEstado(cursoId, "pausado");
    }

    @Override
    public void cambiarEstado(Integer cursoId, String estado) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setEstado(EstadoCurso.valueOf(estado));
            cursoRepository.save(curso);
        }
    }
      
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPromedioCalificaciones(Integer cursoId) {
        Double promedio = calificacionRepository.calcularPromedioCalificacionesByCurso(cursoId);
        return promedio != null ? BigDecimal.valueOf(promedio) : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer contarModulosPorCurso(Integer cursoId) {
        return moduloRepository.countModulosByCurso(cursoId).intValue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Boolean puedeInscribirseCurso(Integer cursoId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            return curso.getEstado() == EstadoCurso.publicado;
        }
        return false;
    }
}