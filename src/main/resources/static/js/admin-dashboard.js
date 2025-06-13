// JavaScript para el dashboard administrativo
document.addEventListener('DOMContentLoaded', function() {
    // Referencias a elementos del DOM
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const navLinks = document.querySelectorAll('.nav-link[data-section]');
    const contentSections = document.querySelectorAll('.content-section');
    
    /**
     * Alternar la visibilidad del sidebar
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
        }
    }
    
    /**
     * Navegar entre secciones
     */
    function navigateToSection(sectionName) {
        // Remover clase activa de todos los links
        navLinks.forEach(link => link.classList.remove('active'));
        
        // Agregar clase activa al link clickeado
        const activeLink = document.querySelector(`[data-section="${sectionName}"]`);
        if (activeLink) {
            activeLink.classList.add('active');
        }
          // Ocultar todas las secciones
        contentSections.forEach(section => {
            section.classList.add('hidden');
        });
        
        // Mostrar sección seleccionada
        const targetSection = document.getElementById(`${sectionName}-section`);
        if (targetSection) {
            targetSection.classList.remove('hidden');
            
            // Trigger custom event para cuando se cambia de sección
            window.dispatchEvent(new CustomEvent('sectionChanged', {
                detail: { section: sectionName }
            }));
        }
        
        // Guardar sección actual en localStorage
        localStorage.setItem('currentSection', sectionName);
    }
    
    /**
     * Restaurar sección activa
     */
    function restoreActiveSection() {
        const currentSection = localStorage.getItem('currentSection') || 'dashboard';
        navigateToSection(currentSection);
    }
    
    /**
     * Inicializar gráficos con Chart.js
     */
    function initCharts() {
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
                        borderColor: '#667eea',
                        backgroundColor: 'rgba(102, 126, 234, 0.1)',
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
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
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Actualizar estadísticas en tiempo real
     */
    function updateStatistics() {
        // Aquí se podría hacer una llamada AJAX para obtener estadísticas actualizadas
        // Por ahora es solo un ejemplo de cómo se haría
        
        /*
        fetch('/api/admin/statistics')
            .then(response => response.json())
            .then(data => {
                document.querySelector('[data-stat="usuarios"]').textContent = data.totalUsuarios;
                document.querySelector('[data-stat="cursos"]').textContent = data.totalCursos;
                // ... más actualizaciones
            })
            .catch(error => console.error('Error al actualizar estadísticas:', error));
        */
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
        });
    }
    
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
        mediaQuery.addListener(handleMediaChange);
    }
    
    // Event Listeners
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', toggleSidebar);
    }
    
    // Navegación
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const sectionName = this.getAttribute('data-section');
            if (sectionName) {
                navigateToSection(sectionName);
            }
        });
    });
    
    // Escuchar eventos de cambio de sección
    window.addEventListener('sectionChanged', function(e) {
        const section = e.detail.section;
        
        // Actualizar título de la página si es necesario
        if (section === 'dashboard') {
            document.title = 'Dashboard - AstroDev Academy';
        } else {
            document.title = `${section.charAt(0).toUpperCase() + section.slice(1)} - Dashboard - AstroDev Academy`;
        }
        
        // Re-inicializar componentes si es necesario
        if (section === 'dashboard') {
            setTimeout(initCharts, 100); // Delay para asegurar que el DOM esté listo
        }
    });
    
    // Inicialización
    restoreSidebarState();
    restoreActiveSection();
    handleResponsive();
    initBootstrapComponents();
    
    // Inicializar gráficos después de un pequeño delay
    setTimeout(initCharts, 250);
    
    // Actualizar estadísticas cada 5 minutos
    setInterval(updateStatistics, 5 * 60 * 1000);
    
    console.log('Dashboard administrativo inicializado correctamente');
});
