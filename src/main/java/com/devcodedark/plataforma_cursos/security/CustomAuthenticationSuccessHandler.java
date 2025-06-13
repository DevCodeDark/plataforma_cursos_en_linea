package com.devcodedark.plataforma_cursos.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * Manejador personalizado para redirección después de autenticación exitosa.
 * Redirige a diferentes dashboards según el rol del usuario.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        String redirectUrl = determineTargetUrl(authorities);
        
        response.sendRedirect(redirectUrl);
    }    /**
     * Determina la URL de redirección basada en los roles del usuario
     */
    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        // Verificar roles en orden de prioridad
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            
            switch (role) {
                case "ROLE_ADMINISTRADOR":
                    return "/astrodev/inicio";
                case "ROLE_DOCENTE":
                    return "/astrodev/inicio";
                case "ROLE_ESTUDIANTE":
                    return "/astrodev/inicio";
                default:
                    // Continuar con el siguiente rol
                    break;
            }
        }
        
        // Redirección por defecto si no se encuentra ningún rol específico
        return "/astrodev/inicio";
    }
}
