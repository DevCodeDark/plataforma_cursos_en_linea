// JavaScript para la sección de Reportes y Estadísticas

// Variables globales para los gráficos
let crecimientoChart;
let usuariosPorRolChart;

/**
 * Configura todos los gráficos de la página
 */
function configurarGraficos(estadisticas) {
    if (!estadisticas) {
        console.warn('No se recibieron datos de estadísticas');
        return;
    }
    
    configurarGraficoCrecimiento(estadisticas);
    configurarGraficoUsuariosPorRol(estadisticas);
}

/**
 * Configura el gráfico de crecimiento por mes
 */
function configurarGraficoCrecimiento(estadisticas) {
    const ctx = document.getElementById('crecimientoChart');
    if (!ctx) {
        console.error('No se encontró el canvas para el gráfico de crecimiento');
        return;
    }
    
    // Destruir gráfico existente si existe
    if (crecimientoChart) {
        crecimientoChart.destroy();
    }
    
    const etiquetas = estadisticas.etiquetasMeses || ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'];
    const datosUsuarios = estadisticas.datosUsuarios || [10, 15, 20, 25, 30, 35];
    const datosCursos = estadisticas.datosCursos || [3, 5, 7, 9, 11, 13];
    const datosInscripciones = estadisticas.datosInscripciones || [50, 75, 100, 125, 150, 175];
    
    crecimientoChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: etiquetas,
            datasets: [
                {
                    label: 'Usuarios',
                    data: datosUsuarios,
                    borderColor: '#007bff',
                    backgroundColor: 'rgba(0, 123, 255, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                },
                {
                    label: 'Cursos',
                    data: datosCursos,
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                },
                {
                    label: 'Inscripciones',
                    data: datosInscripciones,
                    borderColor: '#17a2b8',
                    backgroundColor: 'rgba(23, 162, 184, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    borderColor: '#ddd',
                    borderWidth: 1
                }
            },
            scales: {
                x: {
                    display: true,
                    title: {
                        display: true,
                        text: 'Mes'
                    },
                    grid: {
                        display: false
                    }
                },
                y: {
                    display: true,
                    title: {
                        display: true,
                        text: 'Cantidad'
                    },
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            }
        }
    });
}

/**
 * Configura el gráfico de usuarios por rol
 */
function configurarGraficoUsuariosPorRol(estadisticas) {
    const ctx = document.getElementById('usuariosPorRolChart');
    if (!ctx) {
        console.error('No se encontró el canvas para el gráfico de usuarios por rol');
        return;
    }
    
    // Destruir gráfico existente si existe
    if (usuariosPorRolChart) {
        usuariosPorRolChart.destroy();
    }
    
    const usuariosPorRol = estadisticas.usuariosPorRol || {
        'Estudiantes': 450,
        'Docentes': 25,
        'Administradores': 3
    };
    
    const etiquetas = Object.keys(usuariosPorRol);
    const datos = Object.values(usuariosPorRol);
    const colores = ['#007bff', '#28a745', '#ffc107'];
    
    usuariosPorRolChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: etiquetas,
            datasets: [{
                data: datos,
                backgroundColor: colores,
                borderColor: '#fff',
                borderWidth: 3,
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = ((context.parsed * 100) / total).toFixed(1);
                            return `${context.label}: ${context.parsed} (${percentage}%)`;
                        }
                    }
                }
            },
            animation: {
                animateScale: true,
                animateRotate: true
            }
        }
    });
}

/**
 * Actualiza los reportes cargando nuevos datos
 */
function actualizarReportes() {
    const botonActualizar = document.querySelector('[onclick="actualizarReportes()"]');
    const iconoOriginal = botonActualizar.querySelector('i');
    
    // Cambiar ícono a spinner
    iconoOriginal.className = 'fas fa-spinner fa-spin me-1';
    botonActualizar.disabled = true;
    
    // Simular actualización
    setTimeout(() => {
        // Restaurar ícono original
        iconoOriginal.className = 'fas fa-sync-alt me-1';
        botonActualizar.disabled = false;
        
        // Mostrar notificación
        mostrarNotificacion('Reportes actualizados correctamente', 'success');
        
        // En una implementación real, aquí se cargarían nuevos datos del servidor
        // window.location.reload();
    }, 2000);
}

/**
 * Exporta el reporte en el formato especificado
 */
function exportarReporte(formato) {
    const botonExportar = document.querySelector(`[onclick="exportarReporte('${formato}')"]`);
    const iconoOriginal = botonExportar.querySelector('i');
    
    // Cambiar ícono a spinner
    iconoOriginal.className = 'fas fa-spinner fa-spin me-1';
    botonExportar.disabled = true;
    
    // Simular exportación
    setTimeout(() => {
        // Restaurar ícono original
        if (formato === 'pdf') {
            iconoOriginal.className = 'fas fa-file-pdf me-1';
        } else {
            iconoOriginal.className = 'fas fa-file-excel me-1';
        }
        botonExportar.disabled = false;
        
        // Mostrar notificación
        mostrarNotificacion(`Reporte exportado en formato ${formato.toUpperCase()}`, 'success');
        
        // En una implementación real, aquí se descargaría el archivo
        // window.open(`/admin/reportes/export?formato=${formato}`, '_blank');
    }, 1500);
}

/**
 * Muestra una notificación toast
 */
function mostrarNotificacion(mensaje, tipo = 'info') {
    // Crear elemento de notificación
    const notificacion = document.createElement('div');
    notificacion.className = `alert alert-${tipo} alert-dismissible fade show position-fixed`;
    notificacion.style.cssText = 'top: 20px; right: 20px; z-index: 1050; min-width: 300px;';
    
    notificacion.innerHTML = `
        <i class="fas fa-${getIconoPorTipo(tipo)} me-2"></i>
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Agregar al DOM
    document.body.appendChild(notificacion);
    
    // Remover automáticamente después de 3 segundos
    setTimeout(() => {
        if (notificacion.parentNode) {
            notificacion.remove();
        }
    }, 3000);
}

/**
 * Obtiene el ícono apropiado según el tipo de notificación
 */
function getIconoPorTipo(tipo) {
    const iconos = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-circle',
        'info': 'info-circle'
    };
    return iconos[tipo] || 'info-circle';
}

/**
 * Anima las tarjetas al cargar la página
 */
function animarTarjetas() {
    const tarjetas = document.querySelectorAll('.card');
    tarjetas.forEach((tarjeta, index) => {
        tarjeta.style.opacity = '0';
        tarjeta.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            tarjeta.style.transition = 'all 0.5s ease';
            tarjeta.style.opacity = '1';
            tarjeta.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

/**
 * Actualiza las métricas en tiempo real (simulado)
 */
function actualizarMetricasEnTiempoReal() {
    // Esta función podría usar WebSockets o polling para actualizar métricas
    // Por ahora, solo es una simulación
    setInterval(() => {
        // Simular cambios pequeños en las métricas
        const tarjetasPrincipales = document.querySelectorAll('.card .card-title');
        tarjetasPrincipales.forEach(tarjeta => {
            const valorActual = parseInt(tarjeta.textContent);
            if (!isNaN(valorActual) && Math.random() > 0.9) {
                // 10% de probabilidad de cambio
                const cambio = Math.random() > 0.5 ? 1 : -1;
                const nuevoValor = Math.max(0, valorActual + cambio);
                
                // Animación de cambio
                tarjeta.style.transform = 'scale(1.1)';
                tarjeta.style.transition = 'transform 0.2s ease';
                
                setTimeout(() => {
                    tarjeta.textContent = nuevoValor;
                    tarjeta.style.transform = 'scale(1)';
                }, 100);
            }
        });
    }, 5000); // Cada 5 segundos
}

/**
 * Inicialización cuando el DOM está listo
 */
document.addEventListener('DOMContentLoaded', function() {
    // Animar tarjetas al cargar
    animarTarjetas();
    
    // Inicializar actualización en tiempo real (comentado por ahora)
    // actualizarMetricasEnTiempoReal();
    
    // Configurar tooltips de Bootstrap si están disponibles
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
    
    console.log('Sistema de reportes inicializado correctamente');
});

/**
 * Manejo de errores globales
 */
window.addEventListener('error', function(event) {
    console.error('Error en reportes.js:', event.error);
    mostrarNotificacion('Ha ocurrido un error al cargar los reportes', 'danger');
});

/**
 * Función de utilidad para formatear números
 */
function formatearNumero(numero) {
    return new Intl.NumberFormat('es-ES').format(numero);
}

/**
 * Función de utilidad para formatear porcentajes
 */
function formatearPorcentaje(numero) {
    return new Intl.NumberFormat('es-ES', {
        style: 'percent',
        minimumFractionDigits: 1,
        maximumFractionDigits: 1
    }).format(numero / 100);
}
