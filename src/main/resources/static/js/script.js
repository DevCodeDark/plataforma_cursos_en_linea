// ===== VARIABLES GLOBALES =====
let isScrolling = false;

// ===== INICIALIZACIÓN =====
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    setupNavbarEffects();
    setupSmoothScrolling();
    setupAnimationOnScroll();
    setupCounterAnimation();
    setupCourseCardEffects();
    setupTypingEffect();
}

// ===== EFECTOS DE NAVEGACIÓN =====
function setupNavbarEffects() {
    const navbar = document.querySelector('.navbar');
    
    // Efecto de transparencia en scroll
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            navbar.style.background = 'rgba(26, 26, 46, 0.98)';
            navbar.style.backdropFilter = 'blur(25px)';
        } else {
            navbar.style.background = 'rgba(26, 26, 46, 0.95)';
            navbar.style.backdropFilter = 'blur(20px)';
        }
    });

    // Cerrar menú móvil al hacer clic en un enlace
    const navLinks = document.querySelectorAll('.nav-link');
    const navbarCollapse = document.querySelector('.navbar-collapse');
    
    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            if (navbarCollapse.classList.contains('show')) {
                const bsCollapse = new bootstrap.Collapse(navbarCollapse);
                bsCollapse.hide();
            }
        });
    });
}

// ===== SCROLL SUAVE =====
function setupSmoothScrolling() {
    const links = document.querySelectorAll('a[href^="#"]');
    
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href').substring(1);
            const targetElement = document.getElementById(targetId);
            
            if (targetElement) {
                const offsetTop = targetElement.offsetTop - 80; // Ajuste por navbar fixed
                
                window.scrollTo({
                    top: offsetTop,
                    behavior: 'smooth'
                });
            }
        });
    });
}

// ===== ANIMACIONES AL HACER SCROLL =====
function setupAnimationOnScroll() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                
                // Animación especial para las tarjetas de cursos
                if (entry.target.classList.contains('course-card')) {
                    const delay = Array.from(entry.target.parentNode.children).indexOf(entry.target) * 100;
                    setTimeout(() => {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }, delay);
                }
            }
        });
    }, observerOptions);

    // Observar elementos con animación
    const animatedElements = document.querySelectorAll('.course-card, .section-title, .section-subtitle');
    animatedElements.forEach(el => {
        el.classList.add('fade-in');
        observer.observe(el);
    });
}

// ===== ANIMACIÓN DE CONTADOR =====
function setupCounterAnimation() {
    const counters = document.querySelectorAll('.stat-number');
    let hasAnimated = false;

    const animateCounters = () => {
        if (hasAnimated) return;
        
        counters.forEach(counter => {
            const target = parseInt(counter.getAttribute('data-count'));
            const duration = 2000; // 2 segundos
            const increment = target / (duration / 16); // 60 FPS
            let current = 0;

            const updateCounter = () => {
                current += increment;
                if (current < target) {
                    counter.textContent = Math.floor(current);
                    requestAnimationFrame(updateCounter);
                } else {
                    counter.textContent = target;
                }
            };

            updateCounter();
        });
        
        hasAnimated = true;
    };

    // Observar cuando las estadísticas entran en vista
    const statsSection = document.querySelector('.hero-content');
    if (statsSection) {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                animateCounters();
            }
        }, { threshold: 0.5 });

        observer.observe(statsSection);
    }
}

// ===== EFECTOS DE TARJETAS DE CURSOS =====
function setupCourseCardEffects() {
    const courseCards = document.querySelectorAll('.course-card');

    courseCards.forEach(card => {
        // Efecto parallax sutil en hover
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px) rotateX(5deg)';
            this.style.transition = 'all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) rotateX(0)';
        });

        // Efecto de brillo en hover
        card.addEventListener('mousemove', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            
            const rotateX = (y - centerY) / 10;
            const rotateY = (centerX - x) / 10;
            
            this.style.transform = `translateY(-10px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        });
    });
}

// ===== EFECTO DE ESCRITURA =====
function setupTypingEffect() {
    const heroTitle = document.querySelector('.hero-content h1');
    if (!heroTitle) return;

    const text = heroTitle.textContent;
    heroTitle.innerHTML = '';
    
    let i = 0;
    const typeWriter = () => {
        if (i < text.length) {
            heroTitle.innerHTML += text.charAt(i);
            i++;
            setTimeout(typeWriter, 50);
        } else {
            heroTitle.classList.add('text-glow');
        }
    };

    // Iniciar efecto después de un pequeño delay
    setTimeout(typeWriter, 500);
}

// ===== EFECTOS DE PARTÍCULAS =====
function createParticles() {
    const particleContainer = document.createElement('div');
    particleContainer.className = 'particle-container';
    particleContainer.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        pointer-events: none;
        z-index: 1;
    `;
    
    document.body.appendChild(particleContainer);

    // Crear partículas
    for (let i = 0; i < 50; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.cssText = `
            position: absolute;
            width: 2px;
            height: 2px;
            background: rgba(0, 212, 255, 0.6);
            border-radius: 50%;
            animation: float-particle ${Math.random() * 10 + 5}s linear infinite;
            left: ${Math.random() * 100}%;
            animation-delay: ${Math.random() * 5}s;
        `;
        
        particleContainer.appendChild(particle);
    }

    // Agregar animación CSS para las partículas
    const style = document.createElement('style');
    style.textContent = `
        @keyframes float-particle {
            0% {
                transform: translateY(100vh) rotate(0deg);
                opacity: 0;
            }
            10% {
                opacity: 1;
            }
            90% {
                opacity: 1;
            }
            100% {
                transform: translateY(-10vh) rotate(360deg);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);
}

// ===== UTILIDADES =====
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

// ===== EFECTOS ADICIONALES AL CARGAR =====
window.addEventListener('load', function() {
    // Inicializar partículas después de que la página cargue completamente
    setTimeout(createParticles, 1000);
    
    // Añadir clase para animaciones de entrada
    document.body.classList.add('loaded');
});

// ===== MANEJO DE ERRORES =====
window.addEventListener('error', function(e) {
    console.warn('Error en script.js:', e.error);
});

// ===== LAZY LOADING PARA IMÁGENES =====
function setupLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });

    images.forEach(img => imageObserver.observe(img));
}

// ===== MANEJO DE TEMAS (OPCIONAL PARA FUTURO) =====
function setupThemeToggle() {
    const themeToggle = document.querySelector('#theme-toggle');
    if (!themeToggle) return;

    themeToggle.addEventListener('click', () => {
        document.body.classList.toggle('light-theme');
        localStorage.setItem('theme', document.body.classList.contains('light-theme') ? 'light' : 'dark');
    });

    // Aplicar tema guardado
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'light') {
        document.body.classList.add('light-theme');
    }
}

// ===== FUNCIONES PARA PÁGINA DE CONTACTO =====

/**
 * Inicializa el mapa interactivo de Leaflet
 */
function initializeContactMap() {
    // Verificar si estamos en la página de contacto y si existen las variables
    if (typeof latitud === 'undefined' || typeof longitud === 'undefined' || !document.getElementById('map')) {
        return;
    }

    try {
        // Crear el mapa
        const map = L.map('map', {
            center: [parseFloat(latitud), parseFloat(longitud)],
            zoom: 16,
            zoomControl: true,
            scrollWheelZoom: true
        });

        // Agregar capa de mapa (OpenStreetMap)
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        }).addTo(map);

        // Crear icono personalizado
        const customIcon = L.divIcon({
            className: 'custom-marker',
            html: '<i class="fas fa-map-marker-alt text-primary" style="font-size: 2rem;"></i>',
            iconSize: [30, 30],
            iconAnchor: [15, 30],
            popupAnchor: [0, -30]
        });

        // Agregar marcador
        const marker = L.marker([parseFloat(latitud), parseFloat(longitud)], {
            icon: customIcon
        }).addTo(map);

        // Popup del marcador
        const popupContent = `
            <div class="map-popup">
                <h6><i class="fas fa-rocket me-2 text-primary"></i>AstroDev Academy</h6>
                <p class="mb-2"><i class="fas fa-map-marker-alt me-2"></i>${direccion || 'Lima, Perú'}</p>
                <div class="popup-buttons">
                    <a href="https://www.google.com/maps/search/?api=1&query=${latitud},${longitud}" 
                       target="_blank" class="btn btn-sm btn-primary me-2">
                        <i class="fas fa-external-link-alt me-1"></i>Ver en Google Maps
                    </a>
                    <a href="https://www.google.com/maps/dir/?api=1&destination=${latitud},${longitud}" 
                       target="_blank" class="btn btn-sm btn-outline-primary">
                        <i class="fas fa-route me-1"></i>Cómo llegar
                    </a>
                </div>
            </div>
        `;

        marker.bindPopup(popupContent, {
            maxWidth: 300,
            className: 'custom-popup'
        });

        // Abrir popup automáticamente
        marker.openPopup();

        // Animación del marcador
        setTimeout(() => {
            marker.setLatLng([parseFloat(latitud), parseFloat(longitud)]);
        }, 500);

        // Agregar controles adicionales
        const locationControl = L.control({position: 'topright'});
        locationControl.onAdd = function(map) {
            const div = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
            div.innerHTML = '<button type="button" class="location-btn" title="Centrar en AstroDev Academy"><i class="fas fa-crosshairs"></i></button>';
            div.onclick = function() {
                map.setView([parseFloat(latitud), parseFloat(longitud)], 16);
                marker.openPopup();
            };
            return div;
        };
        locationControl.addTo(map);

        console.log('Mapa inicializado correctamente');
        
    } catch (error) {
        console.error('Error al inicializar el mapa:', error);
        // Mostrar mensaje de error al usuario
        const mapContainer = document.getElementById('map');
        if (mapContainer) {
            mapContainer.innerHTML = `
                <div class="map-error">
                    <i class="fas fa-exclamation-triangle text-warning mb-3" style="font-size: 3rem;"></i>
                    <h6>Error al cargar el mapa</h6>
                    <p class="text-muted">No se pudo cargar el mapa interactivo. Por favor, inténtalo más tarde.</p>
                    <a href="https://www.google.com/maps/search/?api=1&query=${latitud},${longitud}" 
                       target="_blank" class="btn btn-primary">
                        <i class="fas fa-external-link-alt me-2"></i>Ver en Google Maps
                    </a>
                </div>
            `;
        }
    }
}

/**
 * Configura el formulario de contacto
 */
function setupContactForm() {
    const form = document.getElementById('contact-form');
    const responseDiv = document.getElementById('form-response');
    
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Obtener datos del formulario
        const formData = new FormData(form);
        const data = {
            nombre: formData.get('nombre'),
            email: formData.get('email'),
            telefono: formData.get('telefono'),
            motivo: formData.get('motivo'),
            mensaje: formData.get('mensaje')
        };

        // Validaciones adicionales del lado cliente
        if (!validateContactForm(data)) {
            return;
        }

        // Mostrar loading
        showFormLoading(true);

        // Enviar datos via AJAX
        fetch('/astrodev/contacto/enviar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            showFormLoading(false);
            
            if (result.success) {
                showFormResponse('success', result.message);
                form.reset();
                
                // Scroll suave hacia la respuesta
                responseDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            } else {
                showFormResponse('error', result.message || 'Error al enviar el mensaje');
            }
        })
        .catch(error => {
            console.error('Error al enviar formulario:', error);
            showFormLoading(false);
            showFormResponse('error', 'Error de conexión. Por favor, verifica tu conexión a internet e inténtalo nuevamente.');
        });
    });
}

/**
 * Valida el formulario de contacto
 */
function validateContactForm(data) {
    const errors = [];

    // Validar nombre
    if (!data.nombre || data.nombre.trim().length < 2) {
        errors.push('El nombre debe tener al menos 2 caracteres');
    }

    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!data.email || !emailRegex.test(data.email)) {
        errors.push('Por favor, ingresa un email válido');
    }

    // Validar teléfono
    const phoneRegex = /^[+]?[\d\s\-\(\)]{8,15}$/;
    if (data.telefono && !phoneRegex.test(data.telefono)) {
        errors.push('Por favor, ingresa un teléfono válido');
    }

    // Validar mensaje
    if (!data.mensaje || data.mensaje.trim().length < 10) {
        errors.push('El mensaje debe tener al menos 10 caracteres');
    }

    // Mostrar errores si los hay
    if (errors.length > 0) {
        showFormResponse('error', errors.join('<br>'));
        return false;
    }

    return true;
}

/**
 * Muestra el estado de loading del formulario
 */
function showFormLoading(isLoading) {
    const submitBtn = document.querySelector('#contact-form button[type="submit"]');
    const btnText = submitBtn.querySelector('.btn-text') || submitBtn;
    
    if (isLoading) {
        submitBtn.disabled = true;
        btnText.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Enviando...';
    } else {
        submitBtn.disabled = false;
        btnText.innerHTML = '<i class="fas fa-paper-plane me-2"></i>Enviar Mensaje';
    }
}

/**
 * Muestra respuesta del formulario
 */
function showFormResponse(type, message) {
    const responseDiv = document.getElementById('form-response');
    if (!responseDiv) return;

    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    const icon = type === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-triangle';

    responseDiv.innerHTML = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            <i class="${icon} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    responseDiv.style.display = 'block';
    
    // Scroll suave hacia la respuesta
    responseDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

    // Auto-ocultar después de 5 segundos si es éxito
    if (type === 'success') {
        setTimeout(() => {
            responseDiv.style.display = 'none';
        }, 5000);
    }
}

/**
 * Configura los enlaces de direcciones
 */
function setupDirectionsLink() {
    const directionsLinks = document.querySelectorAll('[data-directions]');
    
    directionsLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            if (typeof latitud !== 'undefined' && typeof longitud !== 'undefined') {
                const url = `https://www.google.com/maps/dir/?api=1&destination=${latitud},${longitud}`;
                window.open(url, '_blank');
            }
        });
    });
}

// ===== INICIALIZACIÓN FINAL =====
document.addEventListener('DOMContentLoaded', function() {
    setupLazyLoading();
    setupThemeToggle();
    
    // Optimizar scroll performance
    const scrollHandler = throttle(function() {
        // Lógica adicional de scroll si es necesaria
    }, 16);
    
    window.addEventListener('scroll', scrollHandler);
});
