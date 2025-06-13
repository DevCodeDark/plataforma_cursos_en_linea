/**
 * JavaScript para la página de configuración del sistema
 * Gestiona la interacción del usuario, validaciones y llamadas AJAX
 */

// Variables globales
let configuracionesOriginales = {};
let configuracionesCargadas = false;
let busquedaTimeout = null;

// Inicialización cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    inicializarConfiguracion();
});

/**
 * Inicializa todos los componentes de la página
 */
function inicializarConfiguracion() {
    configurarNavegacionCategorias();
    configurarBuscador();
    configurarFormularios();
    configurarSelectoresColor();
    configurarTooltips();
    configurarValidaciones();
    cargarConfiguracionesIniciales();
}

/**
 * Configura la navegación entre categorías
 */
function configurarNavegacionCategorias() {
    const enlaces = document.querySelectorAll('.list-group-item-action');
    
    enlaces.forEach(enlace => {
        enlace.addEventListener('click', function(e) {
            e.preventDefault();
            const categoria = this.getAttribute('href').substring(1);
            mostrarCategoria(categoria, this);
        });
    });
}

/**
 * Muestra una categoría específica y actualiza la navegación
 */
function mostrarCategoria(categoria, enlaceActivo) {
    // Actualizar navegación activa
    document.querySelectorAll('.list-group-item-action').forEach(enlace => {
        enlace.classList.remove('active');
    });
    
    if (enlaceActivo) {
        enlaceActivo.classList.add('active');
    }
    
    // Ocultar todas las categorías
    document.querySelectorAll('.categoria-content').forEach(contenido => {
        contenido.style.display = 'none';
        contenido.classList.add('hidden');
    });
    
    // Mostrar la categoría seleccionada
    const contenidoCategoria = document.getElementById(`categoria-${categoria}`);
    if (contenidoCategoria) {
        contenidoCategoria.style.display = 'block';
        contenidoCategoria.classList.remove('hidden');
    } else {
        // Si no existe la categoría específica, cargar dinámicamente
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
        console.error('No se encontraron los elementos para cargar la categoría');
        return;
    }
    
    // Mostrar loading
    mostrarLoading(contenidoCategoria);
    
    // Actualizar título
    const iconos = {
        'limites': 'fas fa-chart-line',
        'archivos': 'fas fa-file',
        'apariencia': 'fas fa-palette',
        'notificaciones': 'fas fa-bell'
    };
    
    const nombres = {
        'limites': 'Límites del Sistema',
        'archivos': 'Configuración de Archivos',
        'apariencia': 'Apariencia y Tema',
        'notificaciones': 'Notificaciones'
    };
    
    tituloCategoria.innerHTML = `<i class="${iconos[categoria] || 'fas fa-cog'} me-2"></i>${nombres[categoria] || 'Configuraciones'}`;
    
    // Cargar configuraciones de la categoría
    fetch(`/admin/configuracion/categoria/${categoria}`)
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                throw new Error(data.error);
            }
            generarFormularioCategoria(contenidoCategoria, data, categoria);
        })
        .catch(error => {
            console.error('Error al cargar categoría:', error);
            contenidoCategoria.innerHTML = `
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    Error al cargar las configuraciones: ${error.message}
                </div>
            `;
        })
        .finally(() => {
            contenedorOtras.style.display = 'block';
            contenedorOtras.classList.remove('hidden');
        });
}

/**
 * Genera el formulario para una categoría específica
 */
function generarFormularioCategoria(contenedor, configuraciones, categoria) {
    let html = `<form id="form-${categoria}" onsubmit="guardarConfiguracionCategoria(event, '${categoria}')">`;
    
    for (const [clave, valor] of Object.entries(configuraciones)) {
        const config = obtenerMetadataConfiguracion(clave);
        html += generarCampoConfiguracion(clave, valor, config);
    }
    
    html += `
        <div class="text-end mt-4">
            <button type="button" class="btn btn-outline-secondary me-2" onclick="resetearCategoria('${categoria}')">
                <i class="fas fa-undo me-2"></i>Resetear
            </button>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save me-2"></i>Guardar Cambios
            </button>
        </div>
    </form>`;
    
    contenedor.innerHTML = html;
    configurarValidacionesFormulario(`form-${categoria}`);
}

/**
 * Genera un campo de configuración individual
 */
function generarCampoConfiguracion(clave, valor, metadata) {
    const id = `config-${clave.replace(/\./g, '-')}`;
    const tipo = metadata.tipo || 'STRING';
    const descripcion = metadata.descripcion || '';
    const esModificable = metadata.esModificable !== false;
    
    let html = `<div class="mb-3">
        <label for="${id}" class="form-label">
            ${formatearNombreClave(clave)}
            ${!esModificable ? '<span class="badge bg-secondary ms-2">Solo lectura</span>' : ''}
        </label>`;
    
    if (tipo === 'BOOLEAN') {
        html += `
            <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" id="${id}" name="${clave}" 
                       ${valor === true || valor === 'true' ? 'checked' : ''} 
                       ${!esModificable ? 'disabled' : ''}>
                <label class="form-check-label" for="${id}">
                    ${descripcion}
                </label>
            </div>`;
    } else if (tipo === 'INTEGER') {
        html += `
            <input type="number" class="form-control" id="${id}" name="${clave}" 
                   value="${valor || ''}" ${!esModificable ? 'readonly' : ''} 
                   min="1" step="1">`;
    } else if (clave.includes('color')) {
        html += `
            <div class="input-group">
                <input type="text" class="form-control" id="${id}" name="${clave}" 
                       value="${valor || ''}" ${!esModificable ? 'readonly' : ''} 
                       pattern="^#[0-9A-Fa-f]{6}$" placeholder="#000000">
                <div class="color-preview" style="background-color: ${valor || '#000000'}" 
                     onclick="${esModificable ? `abrirSelectorColor('${id}')` : ''}"></div>
            </div>`;
    } else if (clave.includes('email')) {
        html += `
            <input type="email" class="form-control" id="${id}" name="${clave}" 
                   value="${valor || ''}" ${!esModificable ? 'readonly' : ''}>`;
    } else if (clave.includes('telefono')) {
        html += `
            <input type="tel" class="form-control" id="${id}" name="${clave}" 
                   value="${valor || ''}" ${!esModificable ? 'readonly' : ''}>`;
    } else {
        html += `
            <input type="text" class="form-control" id="${id}" name="${clave}" 
                   value="${valor || ''}" ${!esModificable ? 'readonly' : ''}>`;
    }
    
    if (descripcion) {
        html += `<div class="form-text">${descripcion}</div>`;
    }
    
    html += '</div>';
    return html;
}

/**
 * Configura el buscador de configuraciones
 */
function configurarBuscador() {
    const buscador = document.getElementById('busquedaConfig');
    if (!buscador) return;
    
    buscador.addEventListener('input', function() {
        clearTimeout(busquedaTimeout);
        busquedaTimeout = setTimeout(() => {
            buscarConfiguraciones(this.value);
        }, 300);
    });
}

/**
 * Busca configuraciones por texto
 */
function buscarConfiguraciones(termino) {
    if (!termino.trim()) {
        limpiarResaltadoBusqueda();
        return;
    }
    
    const elementos = document.querySelectorAll('.config-item, .form-label, .config-key');
    elementos.forEach(elemento => {
        const texto = elemento.textContent.toLowerCase();
        const terminoBusqueda = termino.toLowerCase();
        
        if (texto.includes(terminoBusqueda)) {
            resaltarTexto(elemento, termino);
            elemento.closest('.config-item, .mb-3')?.scrollIntoView({ 
                behavior: 'smooth', 
                block: 'center' 
            });
        }
    });
}

/**
 * Resalta el texto encontrado en la búsqueda
 */
function resaltarTexto(elemento, termino) {
    const texto = elemento.innerHTML;
    const regex = new RegExp(`(${termino})`, 'gi');
    elemento.innerHTML = texto.replace(regex, '<span class="search-highlight">$1</span>');
}

/**
 * Limpia el resaltado de búsqueda
 */
function limpiarResaltadoBusqueda() {
    const resaltados = document.querySelectorAll('.search-highlight');
    resaltados.forEach(elemento => {
        const padre = elemento.parentNode;
        padre.replaceChild(document.createTextNode(elemento.textContent), elemento);
        padre.normalize();
    });
}

/**
 * Configura los formularios y validaciones
 */
function configurarFormularios() {
    const formularios = document.querySelectorAll('form');
    formularios.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validarFormulario(this)) {
                e.preventDefault();
                return false;
            }
        });
    });
}

/**
 * Configura los selectores de color
 */
function configurarSelectoresColor() {
    const previsualizaciones = document.querySelectorAll('.color-preview');
    previsualizaciones.forEach(preview => {
        const input = preview.previousElementSibling;
        if (input && input.type === 'text') {
            input.addEventListener('input', function() {
                if (this.value.match(/^#[0-9A-Fa-f]{6}$/)) {
                    preview.style.backgroundColor = this.value;
                }
            });
        }
    });
}

/**
 * Abre el selector de color nativo
 */
function abrirSelectorColor(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    
    const colorInput = document.createElement('input');
    colorInput.type = 'color';
    colorInput.value = input.value || '#000000';
    
    colorInput.addEventListener('change', function() {
        input.value = this.value;
        input.dispatchEvent(new Event('input'));
        actualizarPrevisualizacionColor(inputId, this.value);
    });
    
    colorInput.click();
}

/**
 * Actualiza la previsualización de color
 */
function actualizarPrevisualizacionColor(inputId, color) {
    const input = document.getElementById(inputId);
    const preview = input?.nextElementSibling?.querySelector('.color-preview') || 
                   input?.parentElement?.querySelector('.color-preview');
    
    if (preview) {
        preview.style.backgroundColor = color;
    }
}

/**
 * Configura tooltips
 */
function configurarTooltips() {
    // Inicializar tooltips de Bootstrap si están disponibles
    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
}

/**
 * Configura validaciones de formularios
 */
function configurarValidaciones() {
    const inputs = document.querySelectorAll('input[type="email"], input[type="number"], input[pattern]');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validarCampo(this);
        });
        
        input.addEventListener('input', function() {
            limpiarValidacion(this);
        });
    });
}

/**
 * Valida un campo específico
 */
function validarCampo(campo) {
    const valor = campo.value.trim();
    const tipo = campo.type;
    const patron = campo.pattern;
    let esValido = true;
    let mensaje = '';
    
    if (campo.required && !valor) {
        esValido = false;
        mensaje = 'Este campo es obligatorio';
    } else if (tipo === 'email' && valor && !validarEmail(valor)) {
        esValido = false;
        mensaje = 'Ingrese un email válido';
    } else if (tipo === 'number' && valor && isNaN(valor)) {
        esValido = false;
        mensaje = 'Ingrese un número válido';
    } else if (patron && valor && !new RegExp(patron).test(valor)) {
        esValido = false;
        mensaje = 'El formato no es válido';
    }
    
    mostrarValidacion(campo, esValido, mensaje);
    return esValido;
}

/**
 * Muestra el resultado de la validación
 */
function mostrarValidacion(campo, esValido, mensaje) {
    campo.classList.remove('is-valid', 'is-invalid');
    
    const feedbackExistente = campo.parentElement.querySelector('.valid-feedback, .invalid-feedback');
    if (feedbackExistente) {
        feedbackExistente.remove();
    }
    
    if (esValido) {
        campo.classList.add('is-valid');
    } else {
        campo.classList.add('is-invalid');
        const feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        feedback.textContent = mensaje;
        campo.parentElement.appendChild(feedback);
    }
}

/**
 * Limpia la validación de un campo
 */
function limpiarValidacion(campo) {
    campo.classList.remove('is-valid', 'is-invalid');
    const feedback = campo.parentElement.querySelector('.valid-feedback, .invalid-feedback');
    if (feedback) {
        feedback.remove();
    }
}

/**
 * Valida un formulario completo
 */
function validarFormulario(form) {
    const campos = form.querySelectorAll('input[required], input[type="email"], input[type="number"], input[pattern]');
    let formularioValido = true;
    
    campos.forEach(campo => {
        if (!validarCampo(campo)) {
            formularioValido = false;
        }
    });
    
    return formularioValido;
}

/**
 * Valida un email
 */
function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

/**
 * Guarda la configuración de una categoría específica
 */
function guardarConfiguracionCategoria(event, categoria) {
    event.preventDefault();
    
    const form = event.target;
    if (!validarFormulario(form)) {
        mostrarAlerta('Por favor, corrige los errores en el formulario', 'danger');
        return;
    }
    
    const formData = new FormData(form);
    const datos = {};
    
    for (let [clave, valor] of formData.entries()) {
        datos[clave] = valor;
    }
    
    // Procesar checkboxes que no están en FormData cuando no están marcados
    const checkboxes = form.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        if (!formData.has(checkbox.name)) {
            datos[checkbox.name] = 'false';
        }
    });
    
    mostrarLoading(form);
    
    fetch(`/admin/configuracion/categoria/${categoria}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: JSON.stringify(datos)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            mostrarAlerta('Configuraciones guardadas correctamente', 'success');
            marcarFormularioComoGuardado(form);
        } else {
            throw new Error(data.message || 'Error al guardar las configuraciones');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        mostrarAlerta('Error al guardar las configuraciones: ' + error.message, 'danger');
    })
    .finally(() => {
        ocultarLoading(form);
    });
}

/**
 * Resetea una categoría a valores por defecto
 */
function resetearCategoria(categoria) {
    if (!confirm('¿Estás seguro de que deseas resetear esta categoría a los valores por defecto? Esta acción no se puede deshacer.')) {
        return;
    }
    
    const form = document.getElementById(`form-${categoria}`);
    mostrarLoading(form);
    
    fetch(`/admin/configuracion/resetear/${categoria}`, {
        method: 'POST',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            mostrarAlerta('Categoría reseteada correctamente', 'success');
            location.reload(); // Recargar para mostrar los valores por defecto
        } else {
            throw new Error(data.message || 'Error al resetear la categoría');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        mostrarAlerta('Error al resetear la categoría: ' + error.message, 'danger');
    })
    .finally(() => {
        ocultarLoading(form);
    });
}

/**
 * Exporta las configuraciones
 */
function exportarConfiguraciones() {
    window.location.href = '/admin/configuracion/exportar';
}

/**
 * Confirma el reinicio de configuraciones
 */
function confirmarReinicio() {
    if (!confirm('¿Estás seguro de que deseas reiniciar TODAS las configuraciones a valores por defecto? Esta acción no se puede deshacer.')) {
        return;
    }
    
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/admin/configuracion/reiniciar';
    document.body.appendChild(form);
    form.submit();
}

/**
 * Carga las configuraciones iniciales
 */
function cargarConfiguracionesIniciales() {
    // Guardar los valores originales para detectar cambios
    const inputs = document.querySelectorAll('form input');
    inputs.forEach(input => {
        configuracionesOriginales[input.name] = input.value;
    });
    
    configuracionesCargadas = true;
}

/**
 * Muestra un indicador de carga
 */
function mostrarLoading(contenedor) {
    const loading = document.createElement('div');
    loading.className = 'loading-overlay';
    loading.innerHTML = '<div class="spinner-config"></div>';
    
    contenedor.style.position = 'relative';
    contenedor.appendChild(loading);
}

/**
 * Oculta el indicador de carga
 */
function ocultarLoading(contenedor) {
    const loading = contenedor.querySelector('.loading-overlay');
    if (loading) {
        loading.remove();
    }
}

/**
 * Muestra una alerta
 */
function mostrarAlerta(mensaje, tipo = 'info') {
    const alerta = document.createElement('div');
    alerta.className = `alert alert-${tipo} alert-dismissible fade show`;
    alerta.innerHTML = `
        <i class="fas fa-${tipo === 'success' ? 'check-circle' : tipo === 'danger' ? 'exclamation-circle' : 'info-circle'} me-2"></i>
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const contenedor = document.querySelector('.container-fluid');
    contenedor.insertBefore(alerta, contenedor.firstChild);
    
    // Auto-remove después de 5 segundos
    setTimeout(() => {
        if (alerta.parentElement) {
            alerta.remove();
        }
    }, 5000);
}

/**
 * Marca un formulario como guardado
 */
function marcarFormularioComoGuardado(form) {
    form.classList.add('saved');
    setTimeout(() => {
        form.classList.remove('saved');
    }, 2000);
}

/**
 * Obtiene metadatos de una configuración
 */
function obtenerMetadataConfiguracion(clave) {
    // Metadatos por defecto basados en la clave
    const metadata = {
        tipo: 'STRING',
        esModificable: true,
        descripcion: ''
    };
    
    if (clave.includes('limite') || clave.includes('max') || clave.includes('puerto')) {
        metadata.tipo = 'INTEGER';
    } else if (clave.includes('habilitado') || clave.includes('publico') || 
               clave.includes('mantenimiento') || clave.includes('log') || 
               clave.includes('ssl')) {
        metadata.tipo = 'BOOLEAN';
    }
    
    return metadata;
}

/**
 * Formatea el nombre de una clave para mostrar
 */
function formatearNombreClave(clave) {
    return clave
        .split('.')
        .map(parte => parte.replace(/_/g, ' '))
        .map(parte => parte.charAt(0).toUpperCase() + parte.slice(1))
        .join(' - ');
}

/**
 * Configura validaciones específicas de formulario
 */
function configurarValidacionesFormulario(formId) {
    const form = document.getElementById(formId);
    if (!form) return;
    
    // Validaciones específicas para campos de configuración
    const camposNumero = form.querySelectorAll('input[type="number"]');
    camposNumero.forEach(campo => {
        campo.addEventListener('input', function() {
            if (this.value < 0) {
                this.value = 0;
            }
        });
    });
    
    // Validaciones para colores
    const camposColor = form.querySelectorAll('input[pattern*="#"]');
    camposColor.forEach(campo => {
        campo.addEventListener('input', function() {
            if (this.value && !this.value.startsWith('#')) {
                this.value = '#' + this.value;
            }
        });
    });
}
