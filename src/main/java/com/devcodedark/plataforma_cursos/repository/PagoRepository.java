package com.devcodedark.plataforma_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.devcodedark.plataforma_cursos.model.Pago;
import com.devcodedark.plataforma_cursos.model.Pago.EstadoPago;
import com.devcodedark.plataforma_cursos.model.Pago.MetodoPago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    // Buscar pagos por inscripción
    @Query("SELECT p FROM Pago p WHERE p.inscripcion.id = :inscripcionId")
    List<Pago> findByInscripcionId(@Param("inscripcionId") Integer inscripcionId);
    
    // Buscar pagos por estudiante
    @Query("SELECT p FROM Pago p WHERE p.inscripcion.estudiante.id = :estudianteId")
    List<Pago> findByEstudianteId(@Param("estudianteId") Integer estudianteId);
    
    // Buscar pagos por estado
    List<Pago> findByEstado(EstadoPago estado);
    
    // Buscar pagos por método de pago
    List<Pago> findByMetodoPago(MetodoPago metodoPago);
    
    // Buscar pago por transacción externa
    Optional<Pago> findByTransaccionExternaId(String transaccionExternaId);
    
    // Buscar pagos completados por estudiante
    @Query("SELECT p FROM Pago p WHERE p.inscripcion.estudiante.id = :estudianteId AND p.estado = 'completado'")
    List<Pago> findPagosCompletadosByEstudiante(@Param("estudianteId") Integer estudianteId);
    
    // Calcular total de ingresos por curso
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.inscripcion.curso.id = :cursoId AND p.estado = 'completado'")
    BigDecimal sumIngresosByCurso(@Param("cursoId") Integer cursoId);
    
    // Calcular total de ingresos por docente
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.inscripcion.curso.docente.id = :docenteId AND p.estado = 'completado'")
    BigDecimal sumIngresosByDocente(@Param("docenteId") Integer docenteId);
    
    // Contar pagos por estado y curso
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.inscripcion.curso.id = :cursoId AND p.estado = :estado")
    Long countByEstadoAndCurso(@Param("cursoId") Integer cursoId, @Param("estado") EstadoPago estado);
    
    // Buscar pagos pendientes
    @Query("SELECT p FROM Pago p WHERE p.estado = 'pendiente'")
    List<Pago> findPagosPendientes();
    
    // Verificar si existe transacción externa
    boolean existsByTransaccionExternaId(String transaccionExternaId);
}