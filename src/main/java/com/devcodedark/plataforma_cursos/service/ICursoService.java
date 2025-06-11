package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Curso.EstadoCurso;
import com.devcodedark.plataforma_cursos.model.Curso.NivelCurso;

public interface ICursoService {
    // Listar todos los cursos
    List<Curso> buscarTodos();
    
    // Guardar curso
    void guardar(Curso curso);
    
    // Modificar curso
    void modificar(Curso curso);
    
    // Buscar curso por ID
    Optional<Curso> buscarId(Integer id);
    
    // Eliminar curso
    void eliminar(Integer id);
    
    // Buscar cursos por categoría
    List<Curso> buscarPorCategoria(Integer categoriaId);
    
    // Buscar cursos por docente
    List<Curso> buscarPorDocente(Integer docenteId);
    
    // Buscar cursos por estado
    List<Curso> buscarPorEstado(EstadoCurso estado);
    
    // Buscar cursos publicados
    List<Curso> buscarCursosPublicados();
    
    // Buscar cursos por nivel
    List<Curso> buscarPorNivel(NivelCurso nivel);
    
    // Buscar cursos gratuitos
    List<Curso> buscarCursosGratuitos();
    
    // Buscar cursos por rango de precio
    List<Curso> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
    
    // Buscar cursos por título
    List<Curso> buscarPorTitulo(String titulo);
    
    // Contar inscripciones por curso
    Long contarInscripcionesPorCurso(Integer cursoId);
    
    // Buscar cursos más populares
    List<Curso> buscarCursosMasPopulares();
    
    // Publicar curso
    void publicarCurso(Integer cursoId);
    
    // Pausar curso
    void pausarCurso(Integer cursoId);
    
    // Cambiar estado del curso
    void cambiarEstado(Integer cursoId, EstadoCurso estado);
}