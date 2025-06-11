package com.devcodedark.plataforma_cursos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.ProgresoMaterial;

public interface IProgresoMaterialService {
    // Listar todos los progresos
    List<ProgresoMaterial> buscarTodos();
    
    // Guardar progreso
    void guardar(ProgresoMaterial progresoMaterial);
    
    // Modificar progreso
    void modificar(ProgresoMaterial progresoMaterial);
    
    // Buscar progreso por ID
    Optional<ProgresoMaterial> buscarId(Integer id);
    
    // Eliminar progreso
    void eliminar(Integer id);
    
    // Buscar progreso por inscripción
    List<ProgresoMaterial> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso específico
    Optional<ProgresoMaterial> buscarPorInscripcionYMaterial(Integer inscripcionId, Integer materialId);
    
    // Buscar materiales visualizados por inscripción
    List<ProgresoMaterial> buscarMaterialesVisualizadosPorInscripcion(Integer inscripcionId);
    
    // Buscar materiales completados por inscripción
    List<ProgresoMaterial> buscarMaterialesCompletadosPorInscripcion(Integer inscripcionId);
    
    // Contar materiales completados por inscripción
    Long contarMaterialesCompletadosPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso por módulo específico
    List<ProgresoMaterial> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId);
    
    // Calcular tiempo total reproducido por inscripción
    Long calcularTiempoTotalReproducidoPorInscripcion(Integer inscripcionId);
    
    // Verificar si existe progreso
    boolean existeProgreso(Integer inscripcionId, Integer materialId);
    
    // Buscar último material accedido
    Optional<ProgresoMaterial> buscarUltimoMaterialAccedido(Integer inscripcionId);
    
    // Marcar material como visualizado
    ProgresoMaterial marcarComoVisualizado(Integer inscripcionId, Integer materialId);
    
    // Completar material
    void completarMaterial(Integer progresoMaterialId);
    
    // Actualizar tiempo reproducido
    void actualizarTiempoReproducido(Integer progresoMaterialId, Integer tiempoReproducido);
    
    // Calcular porcentaje de progreso del módulo
    BigDecimal calcularPorcentajeProgresoModulo(Integer inscripcionId, Integer moduloId);
}