package com.devcodedark.plataforma_cursos.repository;

import com.devcodedark.plataforma_cursos.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar configuraciones del sistema
 */
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {

    /**
     * Busca una configuración por su clave
     */
    Optional<Configuracion> findByClave(String clave);

    /**
     * Busca configuraciones por categoría
     */
    List<Configuracion> findByCategoria(String categoria);

    /**
     * Busca configuraciones visibles por categoría
     */
    @Query("SELECT c FROM Configuracion c WHERE c.categoria = :categoria AND c.esVisible = true ORDER BY c.clave")
    List<Configuracion> findByCategoriaAndEsVisibleTrue(@Param("categoria") String categoria);

    /**
     * Busca todas las configuraciones visibles ordenadas por categoría y clave
     */
    @Query("SELECT c FROM Configuracion c WHERE c.esVisible = true ORDER BY c.categoria, c.clave")
    List<Configuracion> findAllVisibles();

    /**
     * Busca configuraciones modificables
     */
    @Query("SELECT c FROM Configuracion c WHERE c.esModificable = true AND c.esVisible = true ORDER BY c.categoria, c.clave")
    List<Configuracion> findAllModificables();

    /**
     * Busca configuraciones modificables por categoría
     */
    @Query("SELECT c FROM Configuracion c WHERE c.categoria = :categoria AND c.esModificable = true AND c.esVisible = true ORDER BY c.clave")
    List<Configuracion> findByCategoriaAndEsModificableTrue(@Param("categoria") String categoria);

    /**
     * Obtiene todas las categorías únicas
     */
    @Query("SELECT DISTINCT c.categoria FROM Configuracion c WHERE c.esVisible = true ORDER BY c.categoria")
    List<String> findDistinctCategorias();

    /**
     * Verifica si existe una configuración con la clave dada
     */
    boolean existsByClave(String clave);

    /**
     * Busca configuraciones por tipo
     */
    List<Configuracion> findByTipo(String tipo);

    /**
     * Cuenta configuraciones por categoría
     */
    @Query("SELECT COUNT(c) FROM Configuracion c WHERE c.categoria = :categoria AND c.esVisible = true")
    Long countByCategoria(@Param("categoria") String categoria);

    /**
     * Busca configuraciones que contengan una cadena en la clave o descripción
     */
    @Query("SELECT c FROM Configuracion c WHERE " +
           "(LOWER(c.clave) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))) AND " +
           "c.esVisible = true ORDER BY c.categoria, c.clave")
    List<Configuracion> buscarPorClaveODescripcion(@Param("busqueda") String busqueda);

    /**
     * Obtiene configuraciones del sistema básicas
     */
    @Query("SELECT c FROM Configuracion c WHERE c.categoria = 'SISTEMA' AND c.esVisible = true ORDER BY c.clave")
    List<Configuracion> findConfiguracionesSistema();

    /**
     * Obtiene configuraciones de apariencia
     */
    @Query("SELECT c FROM Configuracion c WHERE c.categoria = 'APARIENCIA' AND c.esVisible = true ORDER BY c.clave")
    List<Configuracion> findConfiguracionesApariencia();

    /**
     * Obtiene configuraciones de notificaciones
     */
    @Query("SELECT c FROM Configuracion c WHERE c.categoria = 'NOTIFICACIONES' AND c.esVisible = true ORDER BY c.clave")
    List<Configuracion> findConfiguracionesNotificaciones();    /**
     * Reinicia todas las configuraciones a sus valores por defecto
     */
    @Modifying
    @Query("UPDATE Configuracion c SET c.valor = c.valorPorDefecto, c.fechaActualizacion = CURRENT_TIMESTAMP " +
           "WHERE c.esModificable = true AND c.valorPorDefecto IS NOT NULL")
    int reiniciarAValoresPorDefecto();
}
