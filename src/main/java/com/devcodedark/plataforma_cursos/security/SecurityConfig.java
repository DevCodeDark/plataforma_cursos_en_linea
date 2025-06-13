package com.devcodedark.plataforma_cursos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService, 
                         CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }    /**
     * Bean de PasswordEncoder para el cifrado de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para desarrollo (en producción evaluar si habilitarlo)
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar autorización por URLs
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso público a páginas principales
                .requestMatchers("/", "/astrodev/**", "/css/**", "/js/**", "/img/**", "/api/cursos/**").permitAll()
                
                // Páginas de autenticación
                .requestMatchers("/auth/login", "/auth/registro", "/auth/recuperar-password").permitAll()
                
                // Dashboard administrativo - solo administradores
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                
                // Dashboard docente - docentes y administradores
                .requestMatchers("/docente/**").hasAnyRole("DOCENTE", "ADMINISTRADOR")
                
                // Dashboard estudiante - estudiantes, docentes y administradores
                .requestMatchers("/estudiante/**").hasAnyRole("ESTUDIANTE", "DOCENTE", "ADMINISTRADOR")
                
                // API endpoints con autenticación
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/docente/**").hasAnyRole("DOCENTE", "ADMINISTRADOR")
                .requestMatchers("/api/estudiante/**").hasAnyRole("ESTUDIANTE", "DOCENTE", "ADMINISTRADOR")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
              // Configurar formulario de login
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            
            // Configurar logout
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/astrodev/inicio?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            
            // Configurar UserDetailsService
            .userDetailsService(userDetailsService);

        return http.build();
    }

    /**
     * Configuración de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
