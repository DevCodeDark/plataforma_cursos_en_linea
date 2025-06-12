package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.MaterialDTO;

public interface IMaterialService {
    // Listar todos los materiales
    List<MaterialDTO> buscarTodos();
    
    // Guardar material
    void guardar(MaterialDTO materialDTO);
    
    // Modificar material
    void modificar(MaterialDTO materialDTO);
    
    // Buscar material por ID
    Optional<MaterialDTO> buscarId(Integer id);
    
    // Eliminar material
    void eliminar(Integer id);
    
    // Buscar materiales por módulo ordenados
    List<MaterialDTO> buscarPorModuloOrdenado(Integer moduloId);
    
    // Buscar materiales activos por módulo
    List<MaterialDTO> buscarMaterialesActivosPorModulo(Integer moduloId);
    
    // Buscar materiales por tipo
    List<MaterialDTO> buscarPorTipo(String tipo);
    
    // Buscar materiales gratuitos por módulo
    List<MaterialDTO> buscarMaterialesGratuitosPorModulo(Integer moduloId);
    
    // Buscar materiales por curso
    List<MaterialDTO> buscarMaterialesPorCurso(Integer cursoId);
    
    // Contar materiales por módulo
    Long contarMaterialesPorModulo(Integer moduloId);
    
    // Obtener siguiente orden disponible
    Integer obtenerSiguienteOrden(Integer moduloId);
    
    // Calcular duración total de videos por módulo
    Integer calcularDuracionVideosPorModulo(Integer moduloId);
    
    // Reordenar materiales
    void reordenarMateriales(Integer moduloId, List<Integer> nuevoOrden);
    
    // Cambiar disponibilidad gratuita
    void cambiarDisponibilidadGratuita(Integer materialId, Boolean esGratuito);
    
    // Obtener estadísticas de materiales por módulo
    Object obtenerEstadisticasPorModulo(Integer moduloId);
    
    // Buscar materiales por título
    List<MaterialDTO> buscarPorTitulo(String titulo);
      
    // Obtener materiales recientes
    List<MaterialDTO> obtenerMaterialesRecientes(int limite);
}