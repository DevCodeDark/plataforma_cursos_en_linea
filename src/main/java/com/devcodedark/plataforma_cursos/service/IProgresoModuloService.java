package com.devcodedark.plataforma_cursos.service;

import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.dto.ProgresoModuloDTO;

public interface IProgresoModuloService {
    // Listar todos los progresos
    List<ProgresoModuloDTO> buscarTodos();
    
    // Guardar progreso
    void guardar(ProgresoModuloDTO progresoModuloDTO);
    
    // Modificar progreso
    void modificar(ProgresoModuloDTO progresoModuloDTO);
    
    // Buscar progreso por ID
    Optional<ProgresoModuloDTO> buscarId(Integer id);
    
    // Eliminar progreso
    void eliminar(Integer id);
    
    // Buscar progreso por inscripción
    List<ProgresoModuloDTO> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso específico
    Optional<ProgresoModuloDTO> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId);
    
    // Buscar módulos completados por inscripción
    List<ProgresoModuloDTO> buscarModulosCompletadosPorInscripcion(Integer inscripcionId);
    
    // Contar módulos completados por inscripción
    Long contarModulosCompletadosPorInscripcion(Integer inscripcionId);
    
    // Calcular tiempo total invertido por inscripción
    Long calcularTiempoTotalInvertidoPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso por estudiante y curso
    List<ProgresoModuloDTO> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Verificar si existe progreso
    boolean existeProgreso(Integer inscripcionId, Integer moduloId);
    
    // Buscar último módulo accedido
    Optional<ProgresoModuloDTO> buscarUltimoModuloAccedido(Integer inscripcionId);
    
    // Iniciar módulo
    ProgresoModuloDTO iniciarModulo(Integer inscripcionId, Integer moduloId);
    
    // Completar módulo
    void completarModulo(Integer inscripcionId, Integer moduloId);
    
    // Actualizar tiempo invertido
    void actualizarTiempoInvertido(Integer inscripcionId, Integer moduloId, Integer tiempoAdicional);
    
    // Calcular porcentaje de progreso del curso
    Double calcularPorcentajeProgresoCurso(Integer inscripcionId);
    
    // Crear progreso si no existe
    ProgresoModuloDTO crearProgresoSiNoExiste(Integer inscripcionId, Integer moduloId);
}