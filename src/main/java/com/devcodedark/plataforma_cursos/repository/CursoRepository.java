package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Curso.EstadoCurso;
import com.devcodedark.plataforma_cursos.model.Curso.NivelCurso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
    // Buscar cursos por categoría
    @Query("SELECT c FROM Curso c WHERE c.categoria.id = :categoriaId")
    List<Curso> findByCategoriaId(@Param("categoriaId") Integer categoriaId);
    
    // Buscar cursos por docente
    @Query("SELECT c FROM Curso c WHERE c.docente.id = :docenteId")
    List<Curso> findByDocenteId(@Param("docenteId") Integer docenteId);
    
    // Buscar cursos por estado
    List<Curso> findByEstado(EstadoCurso estado);
    
    // Buscar cursos publicados
    @Query("SELECT c FROM Curso c WHERE c.estado = 'publicado'")
    List<Curso> findCursosPublicados();
    
    // Buscar cursos por nivel
    List<Curso> findByNivel(NivelCurso nivel);
    
    // Buscar cursos gratuitos
    @Query("SELECT c FROM Curso c WHERE c.esGratuito = true AND c.estado = 'publicado'")
    List<Curso> findCursosGratuitos();
    
    // Buscar cursos por rango de precio
    @Query("SELECT c FROM Curso c WHERE c.precio BETWEEN :precioMin AND :precioMax AND c.estado = 'publicado'")
    List<Curso> findByPrecioBetween(@Param("precioMin") BigDecimal precioMin, 
                                   @Param("precioMax") BigDecimal precioMax);
    
    // Buscar cursos por título (like)
    @Query("SELECT c FROM Curso c WHERE c.titulo LIKE %:titulo% AND c.estado = 'publicado'")
    List<Curso> findByTituloContaining(@Param("titulo") String titulo);
    
    // Contar inscripciones por curso
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id = :cursoId")
    Long countInscripcionesByCurso(@Param("cursoId") Integer cursoId);
    
    // Cursos más populares (por inscripciones)
    @Query("SELECT c FROM Curso c JOIN c.inscripciones i WHERE c.estado = 'publicado' " +
           "GROUP BY c ORDER BY COUNT(i) DESC")
    List<Curso> findCursosMasPopulares();
}