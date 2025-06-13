// Perfil de Usuario - JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeProfileFunctions();
});

function initializeProfileFunctions() {
    // Validación de formularios
    setupFormValidation();
    
    // Previsualización de imagen
    setupImagePreview();
    
    // Validación de contraseñas
    setupPasswordValidation();
    
    // Auto-dismiss de alertas
    setupAlertAutoDismiss();
    
    console.log('Perfil de usuario inicializado correctamente');
}

// Configurar validación de formularios
function setupFormValidation() {
    const forms = document.querySelectorAll('form');
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
function setupImagePreview() {
    const fileInput = document.getElementById('fotoPerfil');
    const profilePhoto = document.querySelector('.profile-photo');
    
    if (fileInput && profilePhoto) {
        fileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                // Validar tipo de archivo
                if (!file.type.startsWith('image/')) {
                    showAlert('Por favor, selecciona un archivo de imagen válido.', 'danger');
                    fileInput.value = '';
                    return;
                }
                
                // Validar tamaño (5MB máximo)
                if (file.size > 5 * 1024 * 1024) {
                    showAlert('El archivo es demasiado grande. Máximo 5MB permitido.', 'danger');
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
function setupPasswordValidation() {
    const passwordNueva = document.getElementById('passwordNueva');
    const confirmarPassword = document.getElementById('confirmarPassword');
    
    if (passwordNueva && confirmarPassword) {
        const validatePasswords = function() {
            const password = passwordNueva.value;
            const confirmPassword = confirmarPassword.value;
            
            // Validar fortaleza de contraseña
            const isStrongPassword = validatePasswordStrength(password);
            updatePasswordStrengthIndicator(password, isStrongPassword);
            
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
function validatePasswordStrength(password) {
    if (password.length < 6) return false;
    
    // Al menos una letra minúscula, una mayúscula, un número o carácter especial
    const hasLower = /[a-z]/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    
    return hasLower && hasUpper && (hasNumber || hasSpecial);
}

// Actualizar indicador de fortaleza de contraseña
function updatePasswordStrengthIndicator(password, isStrong) {
    const passwordInput = document.getElementById('passwordNueva');
    
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
function setupAlertAutoDismiss() {
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.classList.contains('show')) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000); // Auto-dismiss después de 5 segundos
    });
}

// Mostrar alerta dinámica
function showAlert(message, type = 'info') {
    const alertContainer = document.querySelector('.container .mt-4') || document.querySelector('.container');
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        <i class="fas fa-${getIconForAlertType(type)} me-2"></i>
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
function getIconForAlertType(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-circle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// Validación adicional para el formulario de perfil
function validateProfileForm() {
    const email = document.getElementById('email');
    const nombre = document.getElementById('nombre');
    const apellido = document.getElementById('apellido');
    
    if (!email || !nombre || !apellido) return true;
    
    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value)) {
        showAlert('Por favor, ingresa un email válido.', 'danger');
        return false;
    }
    
    // Validar nombres
    if (nombre.value.trim().length < 2) {
        showAlert('El nombre debe tener al menos 2 caracteres.', 'danger');
        return false;
    }
    
    if (apellido.value.trim().length < 2) {
        showAlert('El apellido debe tener al menos 2 caracteres.', 'danger');
        return false;
    }
    
    return true;
}

// Event listeners adicionales
document.addEventListener('DOMContentLoaded', function() {
    // Agregar validación personalizada al formulario de perfil
    const perfilForm = document.querySelector('form[action*="/perfil/actualizar"]');
    if (perfilForm) {
        perfilForm.addEventListener('submit', function(event) {
            if (!validateProfileForm()) {
                event.preventDefault();
                event.stopPropagation();
            }
        });
    }
    
    // Mejorar UX con tooltips si Bootstrap está disponible
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
    
    // Animaciones de carga
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
});