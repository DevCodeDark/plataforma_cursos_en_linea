// JavaScript para el dashboard administrativo futurista
document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard initialized'); // Debug log
    
    // Prevenir scroll automático al cargar la página
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }
      // Forzar scroll al inicio
    window.scrollTo(0, 0);
    document.documentElement.scrollTop = 0;
    document.body.scrollTop = 0;
    
    // Referencias a elementos del DOM
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const navLinks = document.querySelectorAll('.nav-link[data-section]');
    const contentSections = document.querySelectorAll('.content-section');
    
    // Verificar que los elementos existen
    console.log('Sidebar:', sidebar);
    console.log('Main content:', mainContent);
    
    // El sidebar ya está visible por defecto con el nuevo CSS
    // Solo aplicamos animaciones suaves si es necesario
    if (sidebar) {
        sidebar.style.transition = 'all 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
    }
    if (mainContent) {
        mainContent.style.transition = 'all 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
    }
    
    /**
     * Alternar la visibilidad del sidebar con animaciones
     */
    function toggleSidebar() {
        if (sidebar && mainContent) {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('full-width');
            
            // Guardar estado en localStorage
            const isCollapsed = sidebar.classList.contains('collapsed');
            localStorage.setItem('sidebarCollapsed', isCollapsed);
        }
    }
    
    /**
     * Restaurar estado del sidebar
     */
    function restoreSidebarState() {
        const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
        if (isCollapsed && sidebar && mainContent) {
            sidebar.classList.add('collapsed');
            mainContent.classList.add('full-width');
            sidebar.style.transform = 'translateX(-100%)';
        } else if (sidebar) {
            sidebar.style.transform = 'translateX(0)';
        }
    }    /**
     * Navegar entre secciones
     */
    function navigateToSection(sectionName) {
        console.log('Navigating to section:', sectionName);
        
        // Marcar que es navegación interna (no refresh)
        sessionStorage.setItem('internalNavigation', 'true');
        
        // Ejecutar todas las operaciones de manera síncrona para evitar delay visual
        // 1. Remover clase activa de todos los links y ocultar todas las secciones
        navLinks.forEach(link => link.classList.remove('active'));
        contentSections.forEach(section => {
            section.classList.add('hidden');
            section.style.display = 'none'; // Forzar ocultación inmediata
        });
        
        // 2. Activar el link correcto y mostrar la sección correspondiente
        const activeLink = document.querySelector(`[data-section="${sectionName}"]`);
        const targetSection = document.getElementById(`${sectionName}-section`);
        
        if (activeLink && targetSection) {
            // Ejecutar cambios inmediatamente
            activeLink.classList.add('active');
            targetSection.classList.remove('hidden');
            targetSection.style.display = 'block'; // Forzar visualización inmediata
            
            // Limpiar el estilo inline después para permitir CSS normal
            setTimeout(() => {
                contentSections.forEach(section => {
                    if (!section.classList.contains('hidden')) {
                        section.style.display = '';
                    }
                });
            }, 0);
            
            console.log('Section navigation completed:', sectionName);
            
            // Trigger custom event para cuando se cambia de sección
            window.dispatchEvent(new CustomEvent('sectionChanged', {
                detail: { section: sectionName }
            }));
        } else {
            console.error('Navigation failed - Link or section not found:', {
                sectionName,
                linkFound: !!activeLink,
                sectionFound: !!targetSection
            });
        }
        
        // 3. Guardar sección actual en localStorage
        localStorage.setItem('currentSection', sectionName);
    }/**
     * Obtener parámetros de la URL
     */
    function getURLParams() {
        const urlParams = new URLSearchParams(window.location.search);
        const section = urlParams.get('section');
        console.log('URL params - section:', section, 'Full URL:', window.location.href);
        return {
            section: section
        };
    }    /**
     * Detectar si es un refresh de página
     */
    function isPageRefresh() {
        // Método 1: Navigation API moderna
        if (window.performance && window.performance.getEntriesByType) {
            const perfEntries = window.performance.getEntriesByType('navigation');
            if (perfEntries.length > 0) {
                return perfEntries[0].type === 'reload';
            }
        }
        
        // Método 2: Performance.navigation (fallback para compatibilidad)
        if (window.performance && window.performance.navigation) {
            return window.performance.navigation.type === 1;
        }
        
        // Método 3: Marcar en sessionStorage cuando se navega internamente
        const wasInternalNavigation = sessionStorage.getItem('internalNavigation');
        if (wasInternalNavigation) {
            sessionStorage.removeItem('internalNavigation');
            return false; // No es refresh, fue navegación interna
        }
        
        // Por defecto, asumir que no es refresh
        return false;
    }

    /**
     * Restaurar sección activa
     */
    function restoreActiveSection() {
        const params = getURLParams();
        let currentSection = 'dashboard'; // Por defecto
        
        console.log('URL Params:', params);
        console.log('URL search params:', window.location.search);
        
        // 1. Prioridad: parámetro de URL (desde dropdown del navbar)
        if (params.section && ['dashboard', 'usuarios', 'cursos', 'roles', 'reportes', 'configuracion', 'perfil'].includes(params.section)) {
            currentSection = params.section;
            console.log('Using URL section parameter:', currentSection);
        } 
        // 2. Si no hay parámetros URL, verificar si es refresh de página
        else {
            if (isPageRefresh()) {
                // Es un refresh - usar localStorage
                const savedSection = localStorage.getItem('currentSection');
                if (savedSection && ['dashboard', 'usuarios', 'cursos', 'roles', 'reportes', 'configuracion', 'perfil'].includes(savedSection)) {
                    currentSection = savedSection;
                    console.log('Page refresh detected, using saved section from localStorage:', currentSection);
                } else {
                    currentSection = 'dashboard';
                    console.log('Page refresh but no valid localStorage, defaulting to dashboard');
                }
            } else {
                // No es refresh - viene de navegación normal (navbar, enlace directo, etc.)
                // Ir a dashboard por defecto
                currentSection = 'dashboard';
                console.log('Navigation from external source, going to dashboard');
            }
        }
        
        console.log('Final section to navigate to:', currentSection);
        navigateToSection(currentSection);
        
        // Limpiar parámetros de URL después de navegar si había parámetros
        if (params.section) {
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }    // INMEDIATAMENTE restaurar la sección activa ANTES de cualquier otra lógica
    restoreActiveSection();
    
    // Prevenir comportamiento del navegador que puede interferir
    window.addEventListener('beforeunload', function() {
        // Asegurar que se guarde la sección actual antes de salir
        const activeSection = document.querySelector('.nav-link.active');
        if (activeSection && activeSection.dataset.section) {
            localStorage.setItem('currentSection', activeSection.dataset.section);
        }
    });/**
     * Inicializar gráficos con Chart.js
     */
    function initCharts() {
        // Configuración global para Chart.js con colores blancos
        Chart.defaults.color = '#ffffff';
        Chart.defaults.borderColor = 'rgba(255, 255, 255, 0.2)';
        Chart.defaults.backgroundColor = 'rgba(255, 255, 255, 0.1)';
        
        // Gráfico de inscripciones por mes
        const inscripcionesCtx = document.getElementById('inscripcionesChart');
        if (inscripcionesCtx) {
            new Chart(inscripcionesCtx, {
                type: 'line',
                data: {
                    labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Inscripciones',
                        data: [12, 19, 3, 5, 2, 3],
                        borderColor: '#00d4ff',
                        backgroundColor: 'rgba(0, 212, 255, 0.1)',
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            labels: {
                                color: '#ffffff',
                                font: {
                                    size: 12,
                                    family: 'Exo 2'
                                }
                            }
                        }
                    },
                    scales: {
                        x: {
                            ticks: {
                                color: '#ffffff'
                            },
                            grid: {
                                color: 'rgba(255, 255, 255, 0.1)'
                            }
                        },
                        y: {
                            beginAtZero: true,
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
          // Gráfico de roles de usuarios
        const rolesCtx = document.getElementById('rolesChart');
        if (rolesCtx) {
            new Chart(rolesCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Estudiantes', 'Docentes', 'Administradores'],
                    datasets: [{
                        data: [65, 25, 10],
                        backgroundColor: [
                            '#667eea',
                            '#28a745',
                            '#ffc107'
                        ],
                        borderColor: [
                            '#5a6fd8',
                            '#20c997',
                            '#fd7e14'
                        ],
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                color: '#ffffff',
                                font: {
                                    size: 12,
                                    family: 'Exo 2'
                                },
                                padding: 20,
                                usePointStyle: true
                            }
                        }
                    }
                }
            });
        }}
    
    /**
     * Actualizar estadísticas en tiempo real
     */
    function updateStatistics() {
        // Aquí se podría hacer una llamada AJAX para obtener estadísticas actualizadas
        // Implementación futura para actualizar estadísticas dinámicamente
        console.log('Actualizando estadísticas...');
    }
    
    /**
     * Funciones para manejar acciones de usuario
     */
    window.editarUsuario = function(id) {
        alert('Editar usuario ' + id);
        // TODO: Implementar modal o página de edición
    };
    
    window.eliminarUsuario = function(id) {
        if (confirm('¿Está seguro de que desea eliminar este usuario?')) {
            alert('Usuario ' + id + ' eliminado');
            // TODO: Implementar llamada AJAX para eliminar usuario
        }
    };
    
    window.verUsuario = function(id) {
        alert('Ver detalles del usuario ' + id);
        // TODO: Implementar modal o página de detalles
    };
    
    /**
     * Configurar tooltips y popovers de Bootstrap
     */
    function initBootstrapComponents() {
        // Inicializar tooltips
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
        
        // Inicializar popovers
        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl);
        });    }
    
    /**
     * Manejar responsive behavior
     */
    function handleResponsive() {
        const mediaQuery = window.matchMedia('(max-width: 768px)');
        
        function handleMediaChange(e) {
            if (e.matches) {
                // En móvil, cerrar sidebar por defecto
                if (sidebar) {
                    sidebar.classList.add('collapsed');
                }
                if (mainContent) {
                    mainContent.classList.add('full-width');
                }
            } else {
                // En desktop, restaurar estado guardado
                restoreSidebarState();
            }
        }
          // Ejecutar al cargar
        handleMediaChange(mediaQuery);
        
        // Escuchar cambios
        mediaQuery.addEventListener('change', handleMediaChange);
    }
    
    // Event Listeners
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', toggleSidebar);
    }
    
    // Prevenir comportamiento por defecto de enlaces de navegación
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // Prevenir scroll automático
            if (window.location.hash) {
                history.replaceState(null, null, window.location.pathname + window.location.search);
            }
            
            const section = this.dataset.section;
            if (section) {
                navigateToSection(section);
            }
            return false;
        });
    });
    
    // Escuchar eventos de cambio de sección
    window.addEventListener('sectionChanged', function(e) {
        const section = e.detail.section;
        
        // Actualizar título de la página si es necesario
        if (section === 'dashboard') {
            document.title = 'Dashboard - AstroDev Academy';
        } else {
            document.title = `${section.charAt(0).toUpperCase() + section.slice(1)} - Dashboard - AstroDev Academy`;        }
        
        // Re-inicializar componentes si es necesario
        if (section === 'dashboard') {
            setTimeout(initCharts, 100); // Delay para asegurar que el DOM esté listo
        }
    });
    
    /**
     * Función para forzar el layout correcto
     */
    function enforceLayout() {
        if (sidebar && mainContent) {
            // Asegurar que el sidebar esté visible en desktop
            if (window.innerWidth > 768) {
                sidebar.style.display = 'block';
                sidebar.style.transform = 'translateX(0)';
                sidebar.classList.remove('collapsed');
                
                mainContent.style.marginLeft = '250px';
                mainContent.style.width = 'calc(100% - 250px)';
                mainContent.classList.remove('full-width');
            }
        }
    }
      // Ejecutar inmediatamente para forzar el layout
    enforceLayout();
    
    // También ejecutar en resize
    window.addEventListener('resize', enforceLayout);
    
    // Inicialización
    restoreSidebarState();
    handleResponsive();
    initBootstrapComponents();
    
    // Inicializar gráficos después de un pequeño delay
    setTimeout(initCharts, 250);
    
    // Actualizar estadísticas cada 5 minutos
    setInterval(updateStatistics, 5 * 60 * 1000);
    
    console.log('Dashboard administrativo inicializado correctamente');
});
