package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Categoria;
import com.devcodedark.plataforma_cursos.model.Categoria.EstadoCategoria;
import com.devcodedark.plataforma_cursos.repository.CategoriaRepository;
import com.devcodedark.plataforma_cursos.service.ICategoriaService;

@Service
@Transactional
public class CategoriaServiceJpa implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public void modificar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarId(Integer id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorEstado(EstadoCategoria estado) {
        return categoriaRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasActivas() {
        return categoriaRepository.findAllActivas();
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
    public List<Categoria> buscarCategoriasConCursos() {
        return categoriaRepository.findCategoriasConCursos();
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