// JavaScript específico para la sección de configuración del dashboard

/**
 * Inicialización de la sección de configuración
 */
document.addEventListener('DOMContentLoaded', function() {
    // Escuchar cuando se cambie a la sección de configuración
    window.addEventListener('sectionChanged', function(event) {
        if (event.detail.section === 'configuracion') {
            inicializarConfiguracion();
        }
    });

    // Inicializar si la sección de configuración es la inicial
    const currentSection = localStorage.getItem('currentSection');
    if (currentSection === 'configuracion') {
        setTimeout(inicializarConfiguracion, 100);
    }
});

/**
 * Inicializa la funcionalidad de configuración
 */
function inicializarConfiguracion() {
    configurarNavegacionCategorias();
    configurarBuscadorConfiguracion();
    cargarConfiguracionesIniciales();
}

/**
 * Configura la navegación entre categorías de configuración
 */
function configurarNavegacionCategorias() {
    const enlaces = document.querySelectorAll('[data-categoria]');
    
    enlaces.forEach(enlace => {
        enlace.addEventListener('click', function(e) {
            e.preventDefault();
            
            const categoria = this.getAttribute('data-categoria');
            mostrarCategoriaConfiguracion(categoria, this);
        });
    });
}

/**
 * Muestra una categoría específica de configuración
 */
function mostrarCategoriaConfiguracion(categoria, enlaceActivo) {
    // Actualizar navegación activa
    document.querySelectorAll('[data-categoria]').forEach(enlace => {
        enlace.classList.remove('active');
    });
    
    if (enlaceActivo) {
        enlaceActivo.classList.add('active');
    }
    
    // Ocultar todas las categorías
    document.querySelectorAll('.categoria-content').forEach(contenido => {
        contenido.style.display = 'none';
    });
    
    // Mostrar la categoría seleccionada
    if (categoria === 'sistema') {
        const sistemaContent = document.getElementById('categoria-sistema');
        if (sistemaContent) {
            sistemaContent.style.display = 'block';
        }
    } else {
        cargarCategoriaEspecifica(categoria);
    }
}

/**
 * Carga una categoría específica dinámicamente
 */
function cargarCategoriaEspecifica(categoria) {
    const contenedorOtras = document.getElementById('otras-categorias');
    const tituloCategoria = document.getElementById('titulo-categoria');
    const contenidoCategoria = document.getElementById('contenido-categoria');
    
    if (!contenedorOtras || !tituloCategoria || !contenidoCategoria) {
        return;
    }
    
    // Mostrar loading
    mostrarLoadingConfiguracion(contenidoCategoria);
    
    // Actualizar título
    const configuracionesCategorias = {
        'limites': {
            icono: 'fas fa-chart-line',
            nombre: 'Límites del Sistema',
            contenido: generarFormularioLimites()
        },
        'archivos': {
            icono: 'fas fa-file',
            nombre: 'Configuración de Archivos',
            contenido: generarFormularioArchivos()
        },
        'apariencia': {
            icono: 'fas fa-palette',
            nombre: 'Apariencia y Tema',
            contenido: generarFormularioApariencia()
        },
        'notificaciones': {
            icono: 'fas fa-bell',
            nombre: 'Configuración de Notificaciones',
            contenido: generarFormularioNotificaciones()
        }
    };
    
    const config = configuracionesCategorias[categoria];
    if (config) {
        tituloCategoria.innerHTML = `<i class="${config.icono} me-2"></i>${config.nombre}`;
        
        setTimeout(() => {
            contenidoCategoria.innerHTML = config.contenido;
            contenedorOtras.style.display = 'block';
        }, 500);
    }
}

/**
 * Genera el formulario para límites del sistema
 */
function generarFormularioLimites() {
    return `
        <form onsubmit="guardarConfiguracionLimites(event)">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Límite máximo de usuarios simultáneos</label>
                        <input type="number" class="form-control" value="500" min="1">
                        <small class="form-text text-muted">Número máximo de usuarios conectados al mismo tiempo</small>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Límite de sesiones por usuario</label>
                        <input type="number" class="form-control" value="3" min="1">
                        <small class="form-text text-muted">Número máximo de sesiones activas por usuario</small>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Tiempo de expiración de sesión (minutos)</label>
                        <input type="number" class="form-control" value="30" min="1">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Límite de intentos de login</label>
                        <input type="number" class="form-control" value="5" min="1">
                    </div>
                </div>
            </div>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save me-2"></i>Guardar Límites
                </button>
            </div>
        </form>
    `;
}

/**
 * Genera el formulario para configuración de archivos
 */
function generarFormularioArchivos() {
    return `
        <form onsubmit="guardarConfiguracionArchivos(event)">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Tamaño máximo de archivo (MB)</label>
                        <input type="number" class="form-control" value="10" min="1">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Tipos de archivo permitidos</label>
                        <input type="text" class="form-control" value="jpg,jpeg,png,pdf,docx">
                        <small class="form-text text-muted">Separados por comas</small>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Directorio de almacenamiento</label>
                <input type="text" class="form-control" value="/uploads/" readonly>
                <small class="form-text text-muted">Directorio donde se almacenan los archivos subidos</small>
            </div>
            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" checked>
                <label class="form-check-label">Habilitar compresión automática de imágenes</label>
            </div>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save me-2"></i>Guardar Configuración de Archivos
                </button>
            </div>
        </form>
    `;
}

/**
 * Genera el formulario para configuración de apariencia
 */
function generarFormularioApariencia() {
    return `
        <form onsubmit="guardarConfiguracionApariencia(event)">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Tema predeterminado</label>
                        <select class="form-select">
                            <option value="light" selected>Claro</option>
                            <option value="dark">Oscuro</option>
                            <option value="auto">Automático</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label class="form-label">Color primario</label>
                        <div class="input-group">
                            <input type="text" class="form-control" value="#007bff">
                            <button class="btn btn-outline-secondary" type="button" onclick="abrirSelectorColor('colorPrimario')">
                                <i class="fas fa-palette"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Logo del sitio</label>
                <input type="file" class="form-control" accept="image/*">
                <small class="form-text text-muted">Tamaño recomendado: 200x50 píxeles</small>
            </div>
            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" checked>
                <label class="form-check-label">Permitir a usuarios cambiar tema</label>
            </div>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save me-2"></i>Guardar Apariencia
                </button>
            </div>
        </form>
    `;
}

/**
 * Genera el formulario para configuración de notificaciones
 */
function generarFormularioNotificaciones() {
    return `
        <form onsubmit="guardarConfiguracionNotificaciones(event)">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-check form-switch mb-3">
                        <input class="form-check-input" type="checkbox" checked>
                        <label class="form-check-label">Notificaciones por email</label>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-check form-switch mb-3">
                        <input class="form-check-input" type="checkbox" checked>
                        <label class="form-check-label">Notificaciones del navegador</label>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Email para notificaciones del sistema</label>
                <input type="email" class="form-control" value="admin@plataforma.com">
            </div>
            <div class="mb-3">
                <label class="form-label">Frecuencia de notificaciones de actividad</label>
                <select class="form-select">
                    <option value="inmediata">Inmediata</option>
                    <option value="diaria" selected>Diaria</option>
                    <option value="semanal">Semanal</option>
                    <option value="mensual">Mensual</option>
                </select>
            </div>
            <div class="text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save me-2"></i>Guardar Notificaciones
                </button>
            </div>
        </form>
    `;
}

/**
 * Configura el buscador de configuraciones
 */
function configurarBuscadorConfiguracion() {
    const buscador = document.getElementById('busquedaConfig');
    if (!buscador) return;
    
    buscador.addEventListener('input', function() {
        const termino = this.value.toLowerCase();
        buscarEnConfiguraciones(termino);
    });
}

/**
 * Busca configuraciones por término
 */
function buscarEnConfiguraciones(termino) {
    const elementos = document.querySelectorAll('.form-label, .form-check-label, input[placeholder]');
    elementos.forEach(elemento => {
        const texto = elemento.textContent || elemento.placeholder || '';
        const contenedor = elemento.closest('.mb-3') || elemento.closest('.form-check');
        
        if (contenedor) {
            if (texto.toLowerCase().includes(termino) || termino === '') {
                contenedor.style.display = '';
            } else {
                contenedor.style.display = 'none';
            }
        }
    });
}

/**
 * Funciones para guardar configuraciones
 */
function guardarConfiguracionSistema(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const datos = Object.fromEntries(formData);
    
    mostrarNotificacionConfiguracion('Configuración del sistema guardada correctamente', 'success');
    console.log('Configuración del sistema:', datos);
}

function guardarConfiguracionLimites(event) {
    event.preventDefault();
    mostrarNotificacionConfiguracion('Límites del sistema actualizados', 'success');
}

function guardarConfiguracionArchivos(event) {
    event.preventDefault();
    mostrarNotificacionConfiguracion('Configuración de archivos guardada', 'success');
}

function guardarConfiguracionApariencia(event) {
    event.preventDefault();
    mostrarNotificacionConfiguracion('Configuración de apariencia guardada', 'success');
}

function guardarConfiguracionNotificaciones(event) {
    event.preventDefault();
    mostrarNotificacionConfiguracion('Configuración de notificaciones guardada', 'success');
}

/**
 * Funciones de utilidad para configuración
 */
function exportarConfiguracion() {
    const boton = document.querySelector('[onclick="exportarConfiguracion()"]');
    const icono = boton.querySelector('i');
    const iconoOriginal = icono.className;
    
    icono.className = 'fas fa-spinner fa-spin me-1';
    boton.disabled = true;
    
    setTimeout(() => {
        icono.className = iconoOriginal;
        boton.disabled = false;
        mostrarNotificacionConfiguracion('Configuración exportada correctamente', 'success');
    }, 1500);
}

function importarConfiguracion() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    
    input.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            mostrarNotificacionConfiguracion('Configuración importada correctamente', 'success');
        }
    });
    
    input.click();
}

function reiniciarConfiguracion() {
    if (confirm('¿Está seguro de que desea reiniciar toda la configuración a los valores predeterminados?')) {
        mostrarNotificacionConfiguracion('Configuración reiniciada a valores predeterminados', 'warning');
    }
}

function resetearConfiguracion() {
    if (confirm('¿Está seguro de que desea resetear esta configuración?')) {
        // Resetear formulario
        const form = document.getElementById('form-sistema');
        if (form) {
            form.reset();
        }
        mostrarNotificacionConfiguracion('Configuración reseteada', 'info');
    }
}

function cargarConfiguracionesIniciales() {
    // Simular carga de configuraciones desde el servidor
    console.log('Configuraciones iniciales cargadas');
}

function mostrarLoadingConfiguracion(contenedor) {
    contenedor.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="mt-2 text-muted">Cargando configuración...</p>
        </div>
    `;
}

function mostrarNotificacionConfiguracion(mensaje, tipo = 'info') {
    // Usar la función de notificaciones del dashboard principal
    if (typeof mostrarNotificacion === 'function') {
        mostrarNotificacion(mensaje, tipo);
    } else {
        alert(mensaje);
    }
}

function abrirSelectorColor(inputId) {
    const colorInput = document.createElement('input');
    colorInput.type = 'color';
    colorInput.value = '#007bff';
    
    colorInput.addEventListener('change', function() {
        const input = document.querySelector(`#${inputId}, [value="#007bff"]`);
        if (input) {
            input.value = this.value;
        }
    });
    
    colorInput.click();
}
