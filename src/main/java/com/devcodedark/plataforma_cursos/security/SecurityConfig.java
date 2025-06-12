package com.devcodedark.plataforma_cursos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad para la aplicación de plataforma de cursos.
 * Define la configuración de autenticación, autorización y CORS.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean de PasswordEncoder para el cifrado de contraseñas.
     * Utiliza BCrypt como algoritmo de cifrado recomendado.
     * 
     * @return PasswordEncoder instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de la cadena de filtros de seguridad.
     * Por ahora permite todas las peticiones para facilitar el desarrollo,
     * pero se puede personalizar según las necesidades de seguridad.
     * 
     * @param http configuración HTTP de seguridad
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si hay problemas en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar autorización - por ahora permite todo
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            
            // Deshabilitar autenticación HTTP básica por defecto
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // Deshabilitar formulario de login por defecto
            .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde diferentes orígenes.
     * Configurado para desarrollo, en producción se debe restringir los orígenes permitidos.
     * 
     * @return CorsConfigurationSource configuración de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir todos los orígenes (solo para desarrollo)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales
        configuration.setAllowCredentials(true);
        
        // Configurar para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
