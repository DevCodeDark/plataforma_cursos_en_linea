// JavaScript para la página de registro
document.addEventListener('DOMContentLoaded', function() {
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmarPassword');
    const form = document.querySelector('form');
    
    /**
     * Validar que las contraseñas coincidan
     */
    function validatePasswords() {
        if (passwordField.value !== confirmPasswordField.value) {
            confirmPasswordField.setCustomValidity('Las contraseñas no coinciden');
            confirmPasswordField.classList.add('is-invalid');
        } else {
            confirmPasswordField.setCustomValidity('');
            confirmPasswordField.classList.remove('is-invalid');
            if (confirmPasswordField.value.length > 0) {
                confirmPasswordField.classList.add('is-valid');
            }
        }
    }
    
    /**
     * Validar fortaleza de contraseña
     */
    function validatePasswordStrength() {
        const password = passwordField.value;
        const strengthIndicator = document.getElementById('password-strength');
        
        if (!strengthIndicator) return;
        
        let strength = 0;
        const checks = {
            length: password.length >= 8,
            lowercase: /[a-z]/.test(password),
            uppercase: /[A-Z]/.test(password),
            numbers: /\d/.test(password),
            special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
        };
        
        strength = Object.values(checks).filter(Boolean).length;
        
        // Actualizar indicador visual
        strengthIndicator.className = 'password-strength';
        if (strength < 3) {
            strengthIndicator.classList.add('weak');
            strengthIndicator.textContent = 'Débil';
        } else if (strength < 4) {
            strengthIndicator.classList.add('medium');
            strengthIndicator.textContent = 'Media';
        } else {
            strengthIndicator.classList.add('strong');
            strengthIndicator.textContent = 'Fuerte';
        }
    }
    
    /**
     * Validar email en tiempo real
     */
    function validateEmail() {
        const emailField = document.getElementById('email');
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (emailField.value && !emailRegex.test(emailField.value)) {
            emailField.setCustomValidity('Por favor, ingresa un email válido');
            emailField.classList.add('is-invalid');
        } else {
            emailField.setCustomValidity('');
            emailField.classList.remove('is-invalid');
            if (emailField.value.length > 0) {
                emailField.classList.add('is-valid');
            }
        }
    }
    
    /**
     * Validar nombre de usuario
     */
    function validateUsername() {
        const usernameField = document.getElementById('usuario');
        const username = usernameField.value;
        
        // Solo letras, números y guiones bajos, mínimo 3 caracteres
        const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;
        
        if (username && !usernameRegex.test(username)) {
            usernameField.setCustomValidity('El usuario debe tener al menos 3 caracteres y solo puede contener letras, números y guiones bajos');
            usernameField.classList.add('is-invalid');
        } else {
            usernameField.setCustomValidity('');
            usernameField.classList.remove('is-invalid');
            if (username.length > 0) {
                usernameField.classList.add('is-valid');
            }
        }
    }
    
    // Event listeners
    if (passwordField) {
        passwordField.addEventListener('input', function() {
            validatePasswords();
            validatePasswordStrength();
        });
    }
    
    if (confirmPasswordField) {
        confirmPasswordField.addEventListener('input', validatePasswords);
    }
    
    const emailField = document.getElementById('email');
    if (emailField) {
        emailField.addEventListener('blur', validateEmail);
        emailField.addEventListener('input', function() {
            if (this.classList.contains('is-invalid')) {
                validateEmail();
            }
        });
    }
    
    const usernameField = document.getElementById('usuario');
    if (usernameField) {
        usernameField.addEventListener('blur', validateUsername);
        usernameField.addEventListener('input', function() {
            if (this.classList.contains('is-invalid')) {
                validateUsername();
            }
        });
    }
    
    // Validación del formulario al enviar
    if (form) {
        form.addEventListener('submit', function(e) {
            validatePasswords();
            validateEmail();
            validateUsername();
            
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    }
    
    // Animación de entrada para los campos
    const formFields = document.querySelectorAll('.form-floating');
    formFields.forEach((field, index) => {
        field.style.animationDelay = `${index * 0.1}s`;
        field.classList.add('fade-in');
    });
});

// CSS para las animaciones (se puede mover a un archivo CSS)
const style = document.createElement('style');
style.textContent = `
    .fade-in {
        animation: fadeInUp 0.5s ease-out both;
    }
    
    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .password-strength {
        font-size: 0.8rem;
        margin-top: 0.25rem;
        font-weight: 500;
    }
    
    .password-strength.weak {
        color: #dc3545;
    }
    
    .password-strength.medium {
        color: #ffc107;
    }
    
    .password-strength.strong {
        color: #28a745;
    }
    
    .is-valid {
        border-color: #28a745;
    }
    
    .is-invalid {
        border-color: #dc3545;
    }
`;
document.head.appendChild(style);
