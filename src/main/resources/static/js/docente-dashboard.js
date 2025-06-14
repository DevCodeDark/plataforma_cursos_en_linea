/**
 * Dashboard Docente - Funcionalidades JavaScript
 */

// Variables globales para datos del servidor
let cursosPublicados = 0;
let cursosBorrador = 0;

document.addEventListener('DOMContentLoaded', function() {
    // Obtener datos del servidor desde atributos del DOM
    const dashboardSection = document.getElementById('dashboard-section');
    if (dashboardSection) {
        cursosPublicados = parseInt(dashboardSection.dataset.cursosPublicados) || 0;
        cursosBorrador = parseInt(dashboardSection.dataset.cursosBorrador) || 0;
    }
    
    // Crear efectos de fondo
    createStars();
    createCosmicRays();
    
    // Inicializar navegación
    initSidebarNavigation();
    
    // Inicializar toggle del sidebar
    initSidebarToggle();
    
    // Inicializar modal de curso
    initCursoModal();
    
    // Inicializar gráficos
    initCharts();
});

/**
 * Crear efectos de estrellas en el fondo
 */
function createStars() {
    const starsContainer = document.querySelector('.stars');
    if (!starsContainer) return;
    
    for (let i = 0; i < 100; i++) {
        const star = document.createElement('div');
        star.className = 'star';
        star.style.left = Math.random() * 100 + '%';
        star.style.top = Math.random() * 100 + '%';
        star.style.animationDelay = Math.random() * 2 + 's';
        starsContainer.appendChild(star);
    }
}

/**
 * Crear efectos de rayos cósmicos
 */
function createCosmicRays() {
    const raysContainer = document.querySelector('.cosmic-rays');
    if (!raysContainer) return;
    
    for (let i = 0; i < 5; i++) {
        const ray = document.createElement('div');
        ray.className = 'cosmic-ray';
        ray.style.left = Math.random() * 100 + '%';
        ray.style.animationDelay = Math.random() * 10 + 's';
        raysContainer.appendChild(ray);
    }
}

/**
 * Inicializar navegación del sidebar
 */
function initSidebarNavigation() {
    const menuItems = document.querySelectorAll('.menu-item[data-section]');
    
    menuItems.forEach(item => {
        item.addEventListener('click', function() {
            const targetSection = this.getAttribute('data-section');
            showSection(targetSection);
        });
    });
}

/**
 * Mostrar sección específica
 */
function showSection(sectionName) {
    const menuItems = document.querySelectorAll('.menu-item[data-section]');
    const sections = document.querySelectorAll('.content-section');
    
    // Remover active de todos los items
    menuItems.forEach(i => i.classList.remove('active'));
    
    // Agregar active al item seleccionado
    const selectedItem = document.querySelector(`[data-section="${sectionName}"]`);
    if (selectedItem) {
        selectedItem.classList.add('active');
    }
    
    // Ocultar todas las secciones
    sections.forEach(section => {
        section.classList.add('hidden');
    });
    
    // Mostrar la sección seleccionada
    const target = document.getElementById(sectionName + '-section');
    if (target) {
        target.classList.remove('hidden');
    }
}

/**
 * Inicializar toggle del sidebar
 */
function initSidebarToggle() {
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');
    
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('sidebar-collapsed');
        });
    }
}

/**
 * Inicializar funcionalidad del modal de curso
 */
function initCursoModal() {
    const nuevoCursoModal = document.getElementById('nuevoCursoModal');
    if (!nuevoCursoModal) return;
    
    // Limpiar formulario al cerrar modal
    nuevoCursoModal.addEventListener('hidden.bs.modal', function () {
        const form = document.getElementById('nuevoCursoForm');
        if (form) {
            form.reset();
            const inputs = form.querySelectorAll('.form-control, .form-select');
            inputs.forEach(input => {
                input.classList.remove('is-invalid', 'is-valid');
            });
        }
    });
    
    // Validación del checkbox de curso gratuito
    initPrecioValidation();
}

/**
 * Inicializar validación de precio gratuito
 */
function initPrecioValidation() {
    const esGratuitoCheckbox = document.getElementById('esGratuito');
    const precioInput = document.getElementById('precio');
    
    if (!esGratuitoCheckbox || !precioInput) return;
    
    function actualizarPrecio() {
        if (esGratuitoCheckbox.checked) {
            precioInput.value = '0.00';
            precioInput.disabled = true;
        } else {
            precioInput.disabled = false;
            if (precioInput.value === '0.00') {
                precioInput.value = '';
            }
        }
    }
    
    esGratuitoCheckbox.addEventListener('change', actualizarPrecio);
    actualizarPrecio(); // Ejecutar al cargar
}

/**
 * Inicializar gráficos
 */
function initCharts() {
    initProgresoChart();
    initEstadoCursosChart();
}

/**
 * Inicializar gráfico de progreso de estudiantes
 */
function initProgresoChart() {
    const progresoCtx = document.getElementById('progresoChart');
    if (!progresoCtx) return;
    
    new Chart(progresoCtx, {
        type: 'line',
        data: {
            labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
            datasets: [{
                label: 'Progreso Promedio (%)',
                data: [20, 35, 50, 65, 75, 85],
                borderColor: '#00d4ff',
                backgroundColor: 'rgba(0, 212, 255, 0.1)',
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    labels: {
                        color: '#ffffff'
                    }
                }
            },
            scales: {
                y: {
                    ticks: {
                        color: '#ffffff'
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    }
                },
                x: {
                    ticks: {
                        color: '#ffffff'
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    }
                }
            }
        }
    });
}

/**
 * Inicializar gráfico de estado de cursos
 */
function initEstadoCursosChart() {
    const estadoCursosCtx = document.getElementById('estadoCursosChart');
    if (!estadoCursosCtx) return;
    
    new Chart(estadoCursosCtx, {
        type: 'doughnut',
        data: {
            labels: ['Publicados', 'Borradores', 'Pausados'],
            datasets: [{
                data: [cursosPublicados, cursosBorrador, 0],
                backgroundColor: ['#00ff88', '#ff6b6b', '#ffa726']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    labels: {
                        color: '#ffffff'
                    }
                }
            }
        }
    });
}
