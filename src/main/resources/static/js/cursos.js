// Cursos Page JavaScript
// Variables globales
let cursosData = [];
let cursosOriginales = [];

// Cargar cursos al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    cargarCursos();
});

// Función para cargar cursos desde la API
async function cargarCursos() {
    try {
        const response = await fetch('/api/cursos');
        if (response.ok) {
            cursosData = await response.json();
            cursosOriginales = [...cursosData];
            mostrarCursos(cursosData);
            actualizarEstadisticas();
        } else {
            mostrarError('Error al cargar los cursos');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarCursosEjemplo(); // Mostrar cursos de ejemplo si hay error
    } finally {
        document.getElementById('loading').style.display = 'none';
    }
}

// Función para mostrar cursos en el grid
function mostrarCursos(cursos) {
    const grid = document.getElementById('cursos-grid');
    const noResults = document.getElementById('no-results');
    
    if (cursos.length === 0) {
        grid.style.display = 'none';
        noResults.style.display = 'block';
        return;
    }

    noResults.style.display = 'none';
    grid.style.display = 'flex';
    
    grid.innerHTML = cursos.map(curso => `
        <div class="col-lg-4 col-md-6">
            <div class="card course-card h-100 shadow-sm">
                <div class="card-img-top bg-gradient-primary d-flex align-items-center justify-content-center" style="height: 200px;">
                    <i class="fas ${getCursoIcon(curso.categoria)} fa-3x text-white"></i>
                </div>
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <span class="badge bg-primary">${curso.categoria || 'General'}</span>
                        <span class="badge bg-secondary">${curso.nivel || 'Intermedio'}</span>
                    </div>
                    <h5 class="card-title">${curso.nombre}</h5>
                    <p class="card-text flex-grow-1">${curso.descripcion || 'Curso completo de desarrollo.'}</p>
                    <div class="course-meta">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="text-muted">
                                <i class="fas fa-clock me-1"></i>
                                ${curso.duracion || '40'} horas
                            </span>
                            <span class="text-warning">
                                <i class="fas fa-star me-1"></i>
                                ${curso.calificacion || '4.8'}
                            </span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="h5 mb-0 text-primary">
                                ${curso.precio ? `$${curso.precio}` : 'Gratuito'}
                            </span>
                            <button class="btn btn-primary btn-sm" onclick="verDetalleCurso(${curso.id})">
                                <i class="fas fa-arrow-right me-1"></i>Ver Curso
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

// Función para obtener el ícono según la categoría
function getCursoIcon(categoria) {
    const iconos = {
        'frontend': 'fa-code',
        'backend': 'fa-server',
        'fullstack': 'fa-layers',
        'movil': 'fa-mobile-alt',
        'datos': 'fa-chart-bar',
        'devops': 'fa-cogs',
        'ia': 'fa-brain',
        'default': 'fa-graduation-cap'
    };
    return iconos[categoria] || iconos.default;
}

// Función para aplicar filtros
function aplicarFiltros() {
    const busqueda = document.getElementById('busqueda').value.toLowerCase();
    const categoria = document.getElementById('categoria').value;
    const nivel = document.getElementById('nivel').value;
    const precio = document.getElementById('precio').value;

    let cursosFiltrados = cursosOriginales.filter(curso => {
        const matchBusqueda = !busqueda || 
            curso.nombre.toLowerCase().includes(busqueda) ||
            (curso.descripcion && curso.descripcion.toLowerCase().includes(busqueda));
        
        const matchCategoria = !categoria || curso.categoria === categoria;
        const matchNivel = !nivel || curso.nivel === nivel;
        
        const esGratis = !curso.precio || curso.precio === 0;
        const matchPrecio = !precio || 
            (precio === 'gratis' && esGratis) ||
            (precio === 'pago' && !esGratis);

        return matchBusqueda && matchCategoria && matchNivel && matchPrecio;
    });

    mostrarCursos(cursosFiltrados);
}

// Función para limpiar filtros
function limpiarFiltros() {
    document.getElementById('busqueda').value = '';
    document.getElementById('categoria').value = '';
    document.getElementById('nivel').value = '';
    document.getElementById('precio').value = '';
    mostrarCursos(cursosOriginales);
}

// Función para ver detalle del curso
function verDetalleCurso(cursoId) {
    // Por ahora solo mostrar alerta, luego se puede implementar modal o página detalle
    alert(`Ver detalle del curso ID: ${cursoId}`);
    // TODO: Implementar navegación al detalle del curso
    // window.location.href = `/astrodev/cursos/${cursoId}`;
}

// Función para actualizar estadísticas
function actualizarEstadisticas() {
    const totalCursos = cursosOriginales.length;
    document.getElementById('total-cursos').textContent = totalCursos + '+';
}

// Función para mostrar error
function mostrarError(mensaje) {
    const grid = document.getElementById('cursos-grid');
    grid.innerHTML = `
        <div class="col-12">
            <div class="alert alert-danger text-center">
                <i class="fas fa-exclamation-triangle me-2"></i>
                ${mensaje}
            </div>
        </div>
    `;
    grid.style.display = 'flex';
}

// Función para mostrar cursos de ejemplo si no hay conexión a la API
function mostrarCursosEjemplo() {
    const cursosEjemplo = [
        {
            id: 1,
            nombre: "Desarrollo Frontend con React",
            descripcion: "Aprende a crear interfaces modernas con React, hooks y context.",
            categoria: "frontend",
            nivel: "intermedio",
            duracion: 45,
            precio: 299,
            calificacion: 4.9
        },
        {
            id: 2,
            nombre: "Backend con Spring Boot",
            descripcion: "Domina el desarrollo backend con Java y Spring Boot.",
            categoria: "backend",
            nivel: "intermedio",
            duracion: 60,
            precio: 399,
            calificacion: 4.8
        },
        {
            id: 3,
            nombre: "Introducción a Python",
            descripcion: "Aprende los fundamentos de la programación con Python.",
            categoria: "backend",
            nivel: "principiante",
            duracion: 30,
            precio: 0,
            calificacion: 4.7
        }
    ];
    
    cursosData = cursosEjemplo;
    cursosOriginales = [...cursosEjemplo];
    mostrarCursos(cursosEjemplo);
    actualizarEstadisticas();
}

// Event listeners para filtros en tiempo real
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('busqueda').addEventListener('input', aplicarFiltros);
    document.getElementById('categoria').addEventListener('change', aplicarFiltros);
    document.getElementById('nivel').addEventListener('change', aplicarFiltros);
    document.getElementById('precio').addEventListener('change', aplicarFiltros);
});
