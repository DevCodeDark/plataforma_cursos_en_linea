package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.dto.CursoDTO;

public interface ICursoService {
    // Métodos CRUD básicos con DTO
    List<CursoDTO> buscarTodos();
    void guardar(CursoDTO cursoDTO);
    void modificar(CursoDTO cursoDTO);
    Optional<CursoDTO> buscarId(Integer id);
    void eliminar(Integer id);
    
    // Búsquedas específicas
    List<CursoDTO> buscarPorCategoria(Integer categoriaId);
    List<CursoDTO> buscarPorDocente(Integer docenteId);
    List<CursoDTO> buscarPorEstado(String estado);
    List<CursoDTO> buscarCursosPublicados();
    List<CursoDTO> buscarPorNivel(String nivel);
    List<CursoDTO> buscarCursosGratuitos();
    List<CursoDTO> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
    List<CursoDTO> buscarPorTitulo(String titulo);
    
    // Operaciones de negocio
    Long contarInscripcionesPorCurso(Integer cursoId);
    List<CursoDTO> buscarCursosMasPopulares();
    void publicarCurso(Integer cursoId);
    void pausarCurso(Integer cursoId);
    void cambiarEstado(Integer cursoId, String estado);
      // Métodos adicionales para estadísticas
    long contarCursos();
    BigDecimal calcularPromedioCalificaciones(Integer cursoId);
    Integer contarModulosPorCurso(Integer cursoId);
    Boolean puedeInscribirseCurso(Integer cursoId);
}