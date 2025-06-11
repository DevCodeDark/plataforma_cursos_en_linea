package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Curso;
import com.devcodedark.plataforma_cursos.model.Curso.EstadoCurso;
import com.devcodedark.plataforma_cursos.model.Curso.NivelCurso;
import com.devcodedark.plataforma_cursos.repository.CursoRepository;
import com.devcodedark.plataforma_cursos.service.ICursoService;

@Service
@Transactional
public class CursoServiceJpa implements ICursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarTodos() {
        return cursoRepository.findAll();
    }

    @Override
    public void guardar(Curso curso) {
        cursoRepository.save(curso);
    }

    @Override
    public void modificar(Curso curso) {
        cursoRepository.save(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> buscarId(Integer id) {
        return cursoRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorCategoria(Integer categoriaId) {
        return cursoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorDocente(Integer docenteId) {
        return cursoRepository.findByDocenteId(docenteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorEstado(EstadoCurso estado) {
        return cursoRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarCursosPublicados() {
        return cursoRepository.findCursosPublicados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorNivel(NivelCurso nivel) {
        return cursoRepository.findByNivel(nivel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarCursosGratuitos() {
        return cursoRepository.findCursosGratuitos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return cursoRepository.findByPrecioBetween(precioMin, precioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorTitulo(String titulo) {
        return cursoRepository.findByTituloContaining(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInscripcionesPorCurso(Integer cursoId) {
        return cursoRepository.countInscripcionesByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarCursosMasPopulares() {
        return cursoRepository.findCursosMasPopulares();
    }

    @Override
    public void publicarCurso(Integer cursoId) {
        cambiarEstado(cursoId, EstadoCurso.publicado);
    }

    @Override
    public void pausarCurso(Integer cursoId) {
        cambiarEstado(cursoId, EstadoCurso.pausado);
    }

    @Override
    public void cambiarEstado(Integer cursoId, EstadoCurso estado) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setEstado(estado);
            cursoRepository.save(curso);
        }
    }
}