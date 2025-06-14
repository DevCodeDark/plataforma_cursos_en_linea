// Cursor futurista personalizado
document.addEventListener('DOMContentLoaded', function() {
    // Crear elementos del cursor
    const cursor = document.createElement('div');
    const cursorRing = document.createElement('div');
    
    cursor.className = 'custom-cursor';
    cursorRing.className = 'custom-cursor-ring';
    
    document.body.appendChild(cursor);
    document.body.appendChild(cursorRing);
    
    let mouseX = 0;
    let mouseY = 0;
    let cursorX = 0;
    let cursorY = 0;
    let ringX = 0;
    let ringY = 0;
    
    // Seguir el movimiento del mouse
    document.addEventListener('mousemove', (e) => {
        mouseX = e.clientX;
        mouseY = e.clientY;
    });
    
    // Animar el cursor suavemente
    function animateCursor() {
        // Cursor principal (más rápido)
        cursorX += (mouseX - cursorX) * 0.2;
        cursorY += (mouseY - cursorY) * 0.2;
        cursor.style.left = cursorX + 'px';
        cursor.style.top = cursorY + 'px';
        
        // Ring del cursor (más lento para efecto de seguimiento)
        ringX += (mouseX - ringX) * 0.1;
        ringY += (mouseY - ringY) * 0.1;
        cursorRing.style.left = ringX + 'px';
        cursorRing.style.top = ringY + 'px';
        
        requestAnimationFrame(animateCursor);
    }
    
    animateCursor();
    
    // Efectos hover para elementos clickeables
    const hoverElements = document.querySelectorAll('a, button, .btn, .sidebar-nav a, .form-check-input, [role="button"], input[type="checkbox"], input[type="radio"], .clickable');
    
    hoverElements.forEach(element => {
        element.addEventListener('mouseenter', () => {
            cursor.classList.add('hover');
            cursorRing.classList.add('hover');
        });
        
        element.addEventListener('mouseleave', () => {
            cursor.classList.remove('hover');
            cursorRing.classList.remove('hover');
        });
    });
    
    // Efectos especiales para clics
    document.addEventListener('mousedown', () => {
        cursor.style.transform = 'translate(-50%, -50%) scale(0.8)';
        cursorRing.style.transform = 'translate(-50%, -50%) scale(0.8)';
    });
    
    document.addEventListener('mouseup', () => {
        cursor.style.transform = 'translate(-50%, -50%) scale(1)';
        cursorRing.style.transform = 'translate(-50%, -50%) scale(1)';
    });
    
    // Ocultar cursor cuando sale de la ventana
    document.addEventListener('mouseleave', () => {
        cursor.style.opacity = '0';
        cursorRing.style.opacity = '0';
    });
    
    document.addEventListener('mouseenter', () => {
        cursor.style.opacity = '1';
        cursorRing.style.opacity = '1';
    });
      // Efectos especiales para diferentes secciones - SIMPLIFICADO
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.addEventListener('mouseenter', () => {
            // Solo cambiar color del cursor principal, no crear elementos adicionales
            cursor.style.background = 'radial-gradient(circle, #ff6b6b, #ee5a24)';
            cursor.style.boxShadow = '0 0 10px #ff6b6b, 0 0 20px #ff6b6b, 0 0 30px #ff6b6b';
        });
    });
    
    console.log('Cursor futurista activado 🚀');
});
