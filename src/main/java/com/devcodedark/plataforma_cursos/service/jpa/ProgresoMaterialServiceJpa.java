package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.ProgresoMaterial;
import com.devcodedark.plataforma_cursos.model.Inscripcion;
import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.repository.ProgresoMaterialRepository;
import com.devcodedark.plataforma_cursos.repository.InscripcionRepository;
import com.devcodedark.plataforma_cursos.repository.MaterialRepository;
import com.devcodedark.plataforma_cursos.service.IProgresoMaterialService;

@Service
@Transactional
public class ProgresoMaterialServiceJpa implements IProgresoMaterialService {

    @Autowired
    private ProgresoMaterialRepository progresoMaterialRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterial> buscarTodos() {
        return progresoMaterialRepository.findAll();
    }

    @Override
    public void guardar(ProgresoMaterial progresoMaterial) {
        progresoMaterialRepository.save(progresoMaterial);
    }

    @Override
    public void modificar(ProgresoMaterial progresoMaterial) {
        progresoMaterialRepository.save(progresoMaterial);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterial> buscarId(Integer id) {
        return progresoMaterialRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        progresoMaterialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterial> buscarPorInscripcion(Integer inscripcionId) {
        return progresoMaterialRepository.findByInscripcionId(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterial> buscarPorInscripcionYMaterial(Integer inscripcionId, Integer materialId) {
        return progresoMaterialRepository.findByInscripcionIdAndMaterialId(inscripcionId, materialId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterial> buscarMaterialesVisualizadosPorInscripcion(Integer inscripcionId) {
        return progresoMaterialRepository.findMaterialesVisualizadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterial> buscarMaterialesCompletadosPorInscripcion(Integer inscripcionId) {
        return progresoMaterialRepository.findMaterialesCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMaterialesCompletadosPorInscripcion(Integer inscripcionId) {
        return progresoMaterialRepository.countMaterialesCompletadosByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoMaterial> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId) {
        return progresoMaterialRepository.findByInscripcionIdAndModuloId(inscripcionId, moduloId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calcularTiempoTotalReproducidoPorInscripcion(Integer inscripcionId) {
        return progresoMaterialRepository.sumTiempoReproducidoByInscripcion(inscripcionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProgreso(Integer inscripcionId, Integer materialId) {
        return progresoMaterialRepository.existsByInscripcionIdAndMaterialId(inscripcionId, materialId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoMaterial> buscarUltimoMaterialAccedido(Integer inscripcionId) {
        List<ProgresoMaterial> progresos = progresoMaterialRepository.findUltimoMaterialAccedidoByInscripcion(inscripcionId);
        return progresos.isEmpty() ? Optional.empty() : Optional.of(progresos.get(0));
    }

    @Override
    public ProgresoMaterial marcarComoVisualizado(Integer inscripcionId, Integer materialId) {
        // Verificar si ya existe progreso para este material
        Optional<ProgresoMaterial> progresoExistente = buscarPorInscripcionYMaterial(inscripcionId, materialId);
        if (progresoExistente.isPresent()) {
            ProgresoMaterial progreso = progresoExistente.get();
            progreso.setVisualizado(true);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            return progresoMaterialRepository.save(progreso);
        }
        
        // Crear nuevo progreso
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(inscripcionId);
        Optional<Material> materialOpt = materialRepository.findById(materialId);
        
        if (inscripcionOpt.isPresent() && materialOpt.isPresent()) {
            ProgresoMaterial progreso = new ProgresoMaterial();
            progreso.setInscripcion(inscripcionOpt.get());
            progreso.setMaterial(materialOpt.get());
            progreso.setVisualizado(true);
            progreso.setCompletado(false);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progreso.setTiempoReproducido(0);
            
            return progresoMaterialRepository.save(progreso);
        }
        
        throw new RuntimeException("Inscripción o material no encontrado");
    }

    @Override
    public void completarMaterial(Integer progresoMaterialId) {
        Optional<ProgresoMaterial> progresoOpt = progresoMaterialRepository.findById(progresoMaterialId);
        if (progresoOpt.isPresent()) {
            ProgresoMaterial progreso = progresoOpt.get();
            progreso.setCompletado(true);
            progreso.setVisualizado(true);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progresoMaterialRepository.save(progreso);
        }
    }

    @Override
    public void actualizarTiempoReproducido(Integer progresoMaterialId, Integer tiempoReproducido) {
        Optional<ProgresoMaterial> progresoOpt = progresoMaterialRepository.findById(progresoMaterialId);
        if (progresoOpt.isPresent()) {
            ProgresoMaterial progreso = progresoOpt.get();
            progreso.setTiempoReproducido(tiempoReproducido);
            progreso.setFechaUltimoAcceso(LocalDateTime.now());
            progresoMaterialRepository.save(progreso);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPorcentajeProgresoModulo(Integer inscripcionId, Integer moduloId) {
        // Contar total de materiales del módulo
        Long totalMateriales = materialRepository.countMaterialesByModulo(moduloId);
        
        // Obtener materiales completados en este módulo
        List<ProgresoMaterial> materialesCompletados = buscarPorInscripcionYModulo(inscripcionId, moduloId)
            .stream()
            .filter(ProgresoMaterial::getCompletado)
            .toList();
        
        if (totalMateriales == 0) {
            return BigDecimal.ZERO;
        }
        
        // Calcular porcentaje
        BigDecimal porcentaje = new BigDecimal(materialesCompletados.size())
            .multiply(new BigDecimal("100"))
            .divide(new BigDecimal(totalMateriales), 2, BigDecimal.ROUND_HALF_UP);
            
        return porcentaje;
    }
}