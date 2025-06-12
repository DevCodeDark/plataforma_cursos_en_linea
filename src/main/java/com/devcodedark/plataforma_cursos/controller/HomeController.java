package com.devcodedark.plataforma_cursos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para manejar las páginas principales de la aplicación
 */
@Controller
public class HomeController {

    /**
     * Mapeo para la raíz de la aplicación - redirige a AstroDev
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/astrodev/inicio";
    }
}

/**
 * Controlador específico para AstroDev Academy
 */
@Controller
@RequestMapping("/astrodev")
class AstroDevController {
    
    /**
     * Página de inicio principal
     * 
     * @param model modelo para pasar datos a la vista
     * @return nombre de la plantilla Thymeleaf
     */
    @GetMapping("/inicio")
    public String inicio(Model model) {
        // Aquí se pueden agregar datos para mostrar en la página
        model.addAttribute("mensaje", "Bienvenido a AstroDev Academy");
        return "index";
    }
    
    /**
     * Mapeo para la raíz de AstroDev
     * 
     * @param model modelo para pasar datos a la vista
     * @return nombre de la plantilla Thymeleaf
     */
    @GetMapping("")
    public String astrodevRoot(Model model) {
        return inicio(model);
    }
}
