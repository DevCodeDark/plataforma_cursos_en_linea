package com.devcodedark.plataforma_cursos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.devcodedark.plataforma_cursos.model.ProgresoModulo;

public interface IProgresoModuloService {
    // Listar todos los progresos
    List<ProgresoModulo> buscarTodos();
    
    // Guardar progreso
    void guardar(ProgresoModulo progresoModulo);
    
    // Modificar progreso
    void modificar(ProgresoModulo progresoModulo);
    
    // Buscar progreso por ID
    Optional<ProgresoModulo> buscarId(Integer id);
    
    // Eliminar progreso
    void eliminar(Integer id);
    
    // Buscar progreso por inscripción
    List<ProgresoModulo> buscarPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso específico
    Optional<ProgresoModulo> buscarPorInscripcionYModulo(Integer inscripcionId, Integer moduloId);
    
    // Buscar módulos completados por inscripción
    List<ProgresoModulo> buscarModulosCompletadosPorInscripcion(Integer inscripcionId);
    
    // Contar módulos completados por inscripción
    Long contarModulosCompletadosPorInscripcion(Integer inscripcionId);
    
    // Calcular tiempo total invertido por inscripción
    Long calcularTiempoTotalInvertidoPorInscripcion(Integer inscripcionId);
    
    // Buscar progreso por estudiante y curso
    List<ProgresoModulo> buscarPorEstudianteYCurso(Integer estudianteId, Integer cursoId);
    
    // Verificar si existe progreso
    boolean existeProgreso(Integer inscripcionId, Integer moduloId);
    
    // Buscar último módulo accedido
    Optional<ProgresoModulo> buscarUltimoModuloAccedido(Integer inscripcionId);
    
    // Iniciar módulo
    ProgresoModulo iniciarModulo(Integer inscripcionId, Integer moduloId);
    
    // Completar módulo
    void completarModulo(Integer progresoModuloId);
    
    // Actualizar tiempo invertido
    void actualizarTiempoInvertido(Integer progresoModuloId, Integer tiempoAdicional);
    
    // Calcular porcentaje de progreso del curso
    BigDecimal calcularPorcentajeProgresoCurso(Integer inscripcionId);
}