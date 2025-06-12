package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.ProgresoMaterialDTO;

public interface IProgresoMaterialService {
    // Listar todos los progresos
    List<ProgresoMaterialDTO> buscarTodos();
    
    // Guardar progreso
    void guardar(ProgresoMaterialDTO progresoMaterialDTO);
    
    // Modificar progreso
    void modificar(ProgresoMaterialDTO progresoMaterialDTO);
    
    // Buscar progreso por ID
    Optional<ProgresoMaterialDTO> buscarId(Integer id);
    
    // Eliminar progreso
    void eliminar(Integer id);
    
    // Buscar progreso por inscripción
    List<ProgresoMaterialDTO> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso específico
    Optional<ProgresoMaterialDTO> buscarPorInscripcionYMaterial(Integer inscripcionId, Integer materialId);
    
    // Buscar materiales visualizados por inscripción
    List<ProgresoMaterialDTO> buscarMaterialesVisualizadosPorInscripcion(Integer inscripcionId);
    
    // Buscar materiales completados por inscripción
    List<ProgresoMaterialDTO> buscarMaterialesCompletadosPorInscripcion(Integer inscripcionId);
    
    // Contar materiales completados por inscripción
    Long contarMaterialesCompletadosPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso por módulo específico
    List<ProgresoMaterialDTO> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId);
      // Calcular tiempo total reproducido por inscripción
    Long calcularTiempoTotalReproducidoPorInscripcion(Integer inscripcionId);
    
    // Verificar si existe progreso
    boolean existeProgreso(Integer inscripcionId, Integer materialId);
    
    // Buscar último material accedido
    Optional<ProgresoMaterialDTO> buscarUltimoMaterialAccedido(Integer inscripcionId);
    
    // Actualizar tiempo reproducido
    void actualizarTiempoReproducido(Integer inscripcionId, Integer materialId, Integer tiempoReproducido);
    
    // Marcar material como visualizado
    void marcarComoVisualizado(Integer inscripcionId, Integer materialId);
    
    // Marcar material como completado
    void marcarComoCompletado(Integer inscripcionId, Integer materialId);
    
    // Crear progreso inicial si no existe
    ProgresoMaterialDTO crearProgresoSiNoExiste(Integer inscripcionId, Integer materialId);
    
    // Calcular porcentaje total de progreso por inscripción
    Double calcularPorcentajeProgresoPorInscripcion(Integer inscripcionId);
}