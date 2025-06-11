package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Modulo;
import com.devcodedark.plataforma_cursos.repository.ModuloRepository;
import com.devcodedark.plataforma_cursos.service.IModuloService;

@Service
@Transactional
public class ModuloServiceJpa implements IModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Modulo> buscarTodos() {
        return moduloRepository.findAll();
    }

    @Override
    public void guardar(Modulo modulo) {
        // Si no tiene orden asignado, asignar el siguiente disponible
        if (modulo.getOrden() == null) {
            modulo.setOrden(obtenerSiguienteOrden(modulo.getCurso().getId()));
        }
        moduloRepository.save(modulo);
    }

    @Override
    public void modificar(Modulo modulo) {
        moduloRepository.save(modulo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Modulo> buscarId(Integer id) {
        return moduloRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        moduloRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modulo> buscarPorCursoOrdenado(Integer cursoId) {
        return moduloRepository.findByCursoIdOrderByOrden(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modulo> buscarModulosActivosPorCurso(Integer cursoId) {
        return moduloRepository.findModulosActivosByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modulo> buscarModulosObligatoriosPorCurso(Integer cursoId) {
        return moduloRepository.findModulosObligatoriosByCurso(cursoId);
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