package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.Material;
import com.devcodedark.plataforma_cursos.model.Material.TipoMaterial;

public interface IMaterialService {
    // Listar todos los materiales
    List<Material> buscarTodos();
    
    // Guardar material
    void guardar(Material material);
    
    // Modificar material
    void modificar(Material material);
    
    // Buscar material por ID
    Optional<Material> buscarId(Integer id);
    
    // Eliminar material
    void eliminar(Integer id);
    
    // Buscar materiales por módulo ordenados
    List<Material> buscarPorModuloOrdenado(Integer moduloId);
    
    // Buscar materiales activos por módulo
    List<Material> buscarMaterialesActivosPorModulo(Integer moduloId);
    
    // Buscar materiales por tipo
    List<Material> buscarPorTipo(TipoMaterial tipo);
    
    // Buscar materiales gratuitos por módulo
    List<Material> buscarMaterialesGratuitosPorModulo(Integer moduloId);
    
    // Buscar materiales por curso
    List<Material> buscarMaterialesPorCurso(Integer cursoId);
    
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
}