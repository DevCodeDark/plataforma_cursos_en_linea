package com.devcodedark.plataforma_cursos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para redirecciones inteligentes basadas en roles
 */
@Controller
public class DashboardRedirectController {

    /**
     * Redirección inteligente para "Mis Cursos" basada en el rol del usuario
     */
    @GetMapping("/mis-cursos")
    public String misCursos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        // Verificar roles y redirigir al dashboard apropiado
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            
            switch (role) {
                case "ROLE_ADMINISTRADOR":
                case "ROLE_ADMIN":
                    return "redirect:/admin/dashboard";
                case "ROLE_DOCENTE":
                    return "redirect:/docente/dashboard";
                case "ROLE_ESTUDIANTE":
                    return "redirect:/estudiante/dashboard";
                default:
                    break;
            }
        }
        
        // Si no tiene un rol específico, redirigir al login
        return "redirect:/auth/login";
    }
    
    /**
     * Redirección general para dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        return misCursos(authentication);
    }
}
