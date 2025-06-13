package com.devcodedark.plataforma_cursos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Configuración para el manejo de archivos estáticos y uploads
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${upload.path:uploads/profiles/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar el manejo de archivos subidos
        String uploadsPath = new File(uploadPath).getAbsolutePath() + File.separator;
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadsPath);
        
        // Mantener la configuración por defecto para archivos estáticos
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
