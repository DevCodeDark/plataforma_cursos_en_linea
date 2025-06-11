package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.model.Material.TipoMaterial;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.service.IMaterialService;

@Service
@Transactional
public class MaterialServiceJpa implements IMaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarTodos() {
        return materialRepository.findAll();
    }

    @Override
    public void guardar(Material material) {
        // Si no tiene orden asignado, asignar el siguiente disponible
        if (material.getOrden() == null) {
            material.setOrden(obtenerSiguienteOrden(material.getModulo().getId()));
        }
        materialRepository.save(material);
    }

    @Override
    public void modificar(Material material) {
        materialRepository.save(material);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Material> buscarId(Integer id) {
        return materialRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarPorModuloOrdenado(Integer moduloId) {
        return materialRepository.findByModuloIdOrderByOrden(moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarMaterialesActivosPorModulo(Integer moduloId) {
        return materialRepository.findMaterialesActivosByModulo(moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarPorTipo(TipoMaterial tipo) {
        return materialRepository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarMaterialesGratuitosPorModulo(Integer moduloId) {
        return materialRepository.findMaterialesGratuitosByModulo(moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> buscarMaterialesPorCurso(Integer cursoId) {
        return materialRepository.findMaterialesByCurso(cursoId);
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
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Integer materialId = nuevoOrden.get(i);
            Optional<Material> materialOpt = materialRepository.findById(materialId);
            if (materialOpt.isPresent()) {
                Material material = materialOpt.get();
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
        }
    }
}