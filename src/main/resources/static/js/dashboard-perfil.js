/**
 * Dashboard Perfil - JavaScript
 * Funcionalidades específicas para la sección de perfil dentro del dashboard administrativo
 */

document.addEventListener('DOMContentLoaded', function() {
    // Solo inicializar si estamos en la sección de perfil
    const perfilSection = document.getElementById('perfil-section');
    if (perfilSection) {
        initializePerfilDashboard();
    }
});

function initializePerfilDashboard() {
    console.log('Inicializando funcionalidades de perfil en dashboard...');
    
    // Validación de formularios
    setupPerfilFormValidation();
    
    // Previsualización de imagen
    setupPerfilImagePreview();
    
    // Validación de contraseñas
    setupPerfilPasswordValidation();
    
    // Auto-dismiss de alertas
    setupPerfilAlertAutoDismiss();
    
    // Función para exportar perfil
    setupPerfilExport();
    
    console.log('Perfil dashboard inicializado correctamente');
}

// Configurar validación de formularios
function setupPerfilFormValidation() {
    const forms = document.querySelectorAll('#perfil-section form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}

// Configurar previsualización de imagen
function setupPerfilImagePreview() {
    const fileInput = document.querySelector('#perfil-section #fotoPerfil');
    const profilePhoto = document.querySelector('#perfil-section .profile-photo');
    
    if (fileInput && profilePhoto) {
        fileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                // Validar tipo de archivo
                if (!file.type.startsWith('image/')) {
                    showPerfilAlert('Por favor, selecciona un archivo de imagen válido.', 'danger');
                    fileInput.value = '';
                    return;
                }
                
                // Validar tamaño (5MB máximo)
                if (file.size > 5 * 1024 * 1024) {
                    showPerfilAlert('El archivo es demasiado grande. Máximo 5MB permitido.', 'danger');
                    fileInput.value = '';
                    return;
                }
                
                // Mostrar previsualización
                const reader = new FileReader();
                reader.onload = function(e) {
                    profilePhoto.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }
}

// Configurar validación de contraseñas
function setupPerfilPasswordValidation() {
    const passwordNueva = document.querySelector('#perfil-section #passwordNueva');
    const confirmarPassword = document.querySelector('#perfil-section #confirmarPassword');
    
    if (passwordNueva && confirmarPassword) {
        const validatePasswords = function() {
            const password = passwordNueva.value;
            const confirmPassword = confirmarPassword.value;
            
            // Validar fortaleza de contraseña
            const isStrongPassword = validatePerfilPasswordStrength(password);
            updatePerfilPasswordStrengthIndicator(password, isStrongPassword);
            
            // Validar coincidencia
            if (confirmPassword && password !== confirmPassword) {
                confirmarPassword.setCustomValidity('Las contraseñas no coinciden');
                confirmarPassword.classList.add('is-invalid');
                confirmarPassword.classList.remove('is-valid');
            } else if (confirmPassword && password === confirmPassword && isStrongPassword) {
                confirmarPassword.setCustomValidity('');
                confirmarPassword.classList.add('is-valid');
                confirmarPassword.classList.remove('is-invalid');
            } else {
                confirmarPassword.setCustomValidity('');
                confirmarPassword.classList.remove('is-invalid', 'is-valid');
            }
        };
        
        passwordNueva.addEventListener('input', validatePasswords);
        confirmarPassword.addEventListener('input', validatePasswords);
    }
}

// Validar fortaleza de contraseña
function validatePerfilPasswordStrength(password) {
    if (password.length < 6) return false;
    
    // Al menos una letra minúscula, una mayúscula, un número o carácter especial
    const hasLower = /[a-z]/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    
    return hasLower && hasUpper && (hasNumber || hasSpecial);
}

// Actualizar indicador de fortaleza de contraseña
function updatePerfilPasswordStrengthIndicator(password, isStrong) {
    const passwordInput = document.querySelector('#perfil-section #passwordNueva');
    
    if (password.length === 0) {
        passwordInput.classList.remove('is-valid', 'is-invalid');
        return;
    }
    
    if (isStrong) {
        passwordInput.classList.add('is-valid');
        passwordInput.classList.remove('is-invalid');
    } else {
        passwordInput.classList.add('is-invalid');
        passwordInput.classList.remove('is-valid');
    }
}

// Configurar auto-dismiss de alertas
function setupPerfilAlertAutoDismiss() {
    const alerts = document.querySelectorAll('#perfil-section .alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.classList.contains('show')) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000); // Auto-dismiss después de 5 segundos
    });
}

// Mostrar alerta dinámica en la sección de perfil
function showPerfilAlert(message, type = 'info') {
    const perfilSection = document.getElementById('perfil-section');
    if (!perfilSection) return;
    
    const alertContainer = perfilSection.querySelector('.row') || perfilSection;
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        <i class="fas fa-${getPerfilIconForAlertType(type)} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.insertBefore(alertDiv, alertContainer.firstChild);
    
    // Auto-dismiss
    setTimeout(() => {
        if (alertDiv.classList.contains('show')) {
            const bsAlert = new bootstrap.Alert(alertDiv);
            bsAlert.close();
        }
    }, 5000);
}

// Obtener icono para tipo de alerta
function getPerfilIconForAlertType(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-circle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// Validación adicional para el formulario de perfil
function validatePerfilForm() {
    const email = document.querySelector('#perfil-section #email');
    const nombre = document.querySelector('#perfil-section #nombre');
    const apellido = document.querySelector('#perfil-section #apellido');
    
    if (!email || !nombre || !apellido) return true;
    
    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value)) {
        showPerfilAlert('Por favor, ingresa un email válido.', 'danger');
        return false;
    }
    
    // Validar nombres
    if (nombre.value.trim().length < 2) {
        showPerfilAlert('El nombre debe tener al menos 2 caracteres.', 'danger');
        return false;
    }
    
    if (apellido.value.trim().length < 2) {
        showPerfilAlert('El apellido debe tener al menos 2 caracteres.', 'danger');
        return false;
    }
    
    return true;
}

// Configurar funcionalidad de exportar perfil
function setupPerfilExport() {
    // Esta función se llamará desde el botón exportar en el template
    window.exportarPerfil = function() {
        try {
            const perfilData = {
                nombre: document.querySelector('#perfil-section #nombre')?.value || '',
                apellido: document.querySelector('#perfil-section #apellido')?.value || '',
                email: document.querySelector('#perfil-section #email')?.value || '',
                usuario: document.querySelector('#perfil-section #usuario')?.value || '',
                telefono: document.querySelector('#perfil-section #telefono')?.value || '',
                fechaNacimiento: document.querySelector('#perfil-section #fechaNacimiento')?.value || '',
                genero: document.querySelector('#perfil-section #genero')?.value || '',
                fechaExportacion: new Date().toISOString()
            };
            
            const dataStr = JSON.stringify(perfilData, null, 2);
            const dataBlob = new Blob([dataStr], {type: 'application/json'});
            
            const url = URL.createObjectURL(dataBlob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `perfil_${perfilData.usuario || 'usuario'}_${new Date().toISOString().split('T')[0]}.json`;
            
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            
            URL.revokeObjectURL(url);
            
            showPerfilAlert('Datos del perfil exportados exitosamente.', 'success');
        } catch (error) {
            console.error('Error al exportar perfil:', error);
            showPerfilAlert('Error al exportar los datos del perfil.', 'danger');
        }
    };
}

// Event listeners adicionales
document.addEventListener('DOMContentLoaded', function() {
    // Agregar validación personalizada al formulario de perfil del dashboard
    const perfilForm = document.querySelector('#perfil-section form[action*="/perfil/actualizar"]');
    if (perfilForm) {
        perfilForm.addEventListener('submit', function(event) {
            if (!validatePerfilForm()) {
                event.preventDefault();
                event.stopPropagation();
            }
        });
    }
    
    // Mejorar UX con tooltips si Bootstrap está disponible
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('#perfil-section [data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
    
    // Animaciones de carga específicas para la sección de perfil
    const perfilCards = document.querySelectorAll('#perfil-section .dashboard-card, #perfil-section .card');
    perfilCards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
});
