// JavaScript específico para la sección de reportes del dashboard

// Variables globales para los gráficos de reportes
let crecimientoChart;
let usuariosPorRolChart;

/**
 * Configura los gráficos de la sección de reportes
 */
function configurarGraficosReportes() {
    configurarGraficoCrecimiento();
    configurarGraficoUsuariosPorRol();
}

/**
 * Configura el gráfico de crecimiento por mes
 */
function configurarGraficoCrecimiento() {
    const ctx = document.getElementById('crecimientoChart');
    if (!ctx) return;
    
    // Destruir gráfico existente si existe
    if (crecimientoChart) {
        crecimientoChart.destroy();
    }
    
    crecimientoChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
            datasets: [
                {
                    label: 'Usuarios',
                    data: [45, 32, 28, 38, 44, 50],
                    borderColor: '#007bff',
                    backgroundColor: 'rgba(0, 123, 255, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                },
                {
                    label: 'Cursos',
                    data: [10, 15, 12, 18, 22, 25],
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4
                },
                {
                    label: 'Inscripciones',
                    data: [85, 120, 95, 150, 180, 210],
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
                }
            },
            scales: {
                x: {
                    grid: {
                        display: false
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            }
        }
    });
}

/**
 * Configura el gráfico de usuarios por rol
 */
function configurarGraficoUsuariosPorRol() {
    const ctx = document.getElementById('usuariosPorRolChart');
    if (!ctx) return;
    
    // Destruir gráfico existente si existe
    if (usuariosPorRolChart) {
        usuariosPorRolChart.destroy();
    }
    
    usuariosPorRolChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Administradores', 'Estudiantes', 'Docentes'],
            datasets: [{
                data: [1, 450, 25],
                backgroundColor: ['#007bff', '#28a745', '#ffc107'],
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
                }
            }
        }
    });
}

/**
 * Funciones para los botones de reportes
 */
function actualizarReportes() {
    const boton = document.querySelector('[onclick="actualizarReportes()"]');
    if (!boton) return;
    
    const icono = boton.querySelector('i');
    const iconoOriginal = icono.className;
    
    icono.className = 'fas fa-spinner fa-spin me-1';
    boton.disabled = true;
    
    setTimeout(() => {
        icono.className = iconoOriginal;
        boton.disabled = false;
        mostrarNotificacion('Reportes actualizados correctamente', 'success');
    }, 2000);
}

function exportarReporte(formato) {
    const boton = document.querySelector(`[onclick="exportarReporte('${formato}')"]`);
    if (!boton) return;
    
    const icono = boton.querySelector('i');
    const iconoOriginal = icono.className;
    
    icono.className = 'fas fa-spinner fa-spin me-1';
    boton.disabled = true;
    
    setTimeout(() => {
        icono.className = iconoOriginal;
        boton.disabled = false;
        mostrarNotificacion(`Reporte exportado en formato ${formato.toUpperCase()}`, 'success');
    }, 1500);
}

function mostrarNotificacion(mensaje, tipo = 'info') {
    const notificacion = document.createElement('div');
    notificacion.className = `alert alert-${tipo} alert-dismissible fade show position-fixed`;
    notificacion.style.cssText = 'top: 20px; right: 20px; z-index: 1050; min-width: 300px;';
    
    const iconos = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-circle',
        'info': 'info-circle'
    };
    
    notificacion.innerHTML = `
        <i class="fas fa-${iconos[tipo] || 'info-circle'} me-2"></i>
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notificacion);
    
    setTimeout(() => {
        if (notificacion.parentNode) {
            notificacion.remove();
        }
    }, 3000);
}

// Inicialización y eventos
document.addEventListener('DOMContentLoaded', function() {
    // Escuchar cuando se cambie a la sección de reportes
    window.addEventListener('sectionChanged', function(event) {
        if (event.detail.section === 'reportes') {
            // Configurar gráficos cuando se muestre la sección
            setTimeout(configurarGraficosReportes, 100);
        }
    });

    // Configurar gráficos si la sección de reportes es la inicial
    const currentSection = localStorage.getItem('currentSection');
    if (currentSection === 'reportes') {
        setTimeout(configurarGraficosReportes, 500);
    }
});
