package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.ModuloDTO;

public interface IModuloService {
    // Listar todos los módulos
    List<ModuloDTO> buscarTodos();
    
    // Guardar módulo
    void guardar(ModuloDTO moduloDTO);
    
    // Modificar módulo
    void modificar(ModuloDTO moduloDTO);
    
    // Buscar módulo por ID
    Optional<ModuloDTO> buscarId(Integer id);
    
    // Eliminar módulo
    void eliminar(Integer id);
    
    // Buscar módulos por curso ordenados por orden
    List<ModuloDTO> buscarPorCursoOrdenado(Integer cursoId);
    
    // Buscar módulos activos por curso
    List<ModuloDTO> buscarModulosActivosPorCurso(Integer cursoId);
    
    // Buscar módulos obligatorios por curso
    List<ModuloDTO> buscarModulosObligatoriosPorCurso(Integer cursoId);
    
    // Contar módulos por curso
    Long contarModulosPorCurso(Integer cursoId);
    
    // Obtener siguiente orden disponible
    Integer obtenerSiguienteOrden(Integer cursoId);
    
    // Calcular duración total por curso
    Integer calcularDuracionTotalPorCurso(Integer cursoId);
    
    // Reordenar módulos
    void reordenarModulos(Integer cursoId, List<Integer> nuevoOrden);
    
    // Marcar como obligatorio/opcional
    void cambiarObligatorio(Integer moduloId, Boolean esObligatorio);
}