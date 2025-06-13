package com.devcodedark.plataforma_cursos.controller;

import com.devcodedark.plataforma_cursos.dto.ContactoDTO;
import com.devcodedark.plataforma_cursos.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    
    private static final Logger logger = LoggerFactory.getLogger(AstroDevController.class);
    private static final String SUCCESS_KEY = "success";
    private static final String MESSAGE_KEY = "message";
    
    private final EmailService emailService;
    
    public AstroDevController(EmailService emailService) {
        this.emailService = emailService;
    }

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

    /**
     * Página "Nosotros" - Información sobre AstroDev Academy
     * 
     * @param model modelo para pasar datos a la vista
     * @return nombre de la plantilla Thymeleaf
     */
    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("titulo", "Sobre AstroDev Academy");
        model.addAttribute("mision", "Formar desarrolladores espaciales del futuro");
        return "nosotros";
    }

    /**
     * Página "Contacto" - Información de contacto y ubicación
     * 
     * @param model modelo para pasar datos a la vista
     * @return nombre de la plantilla Thymeleaf
     */
    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("titulo", "Contáctanos");
        model.addAttribute("descripcion", "Estamos aquí para ayudarte en tu viaje de aprendizaje");
        // Coordenadas reales de AstroDev Academy
        model.addAttribute("latitud", "-6.4862176060688475");
        model.addAttribute("longitud", "-76.37636938002801");
        model.addAttribute("direccion", "Jr. Circunvalación Cumbaza 298, Tarapoto, San Martín, Perú");
        return "contacto";
    }

    /**
     * Procesa el envío del formulario de contacto
     * 
     * @param contacto datos del formulario validados
     * @param bindingResult resultado de la validación
     * @return respuesta JSON con el resultado
     */
    @PostMapping("/contacto/enviar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enviarContacto(
            @Valid @RequestBody ContactoDTO contacto, 
            BindingResult bindingResult) {
        
        Map<String, Object> response = new HashMap<>();
          try {
            // Validar datos del formulario
            if (bindingResult.hasErrors()) {
                response.put(SUCCESS_KEY, false);
                response.put(MESSAGE_KEY, "Por favor, verifica los datos ingresados");
                response.put("errors", bindingResult.getAllErrors());
                return ResponseEntity.badRequest().body(response);
            }
            
            logger.info("Procesando formulario de contacto de: {} - {}", contacto.getNombre(), contacto.getEmail());
            
            // Enviar email principal al destinatario
            boolean emailEnviado = emailService.enviarEmailContacto(contacto);
            
            if (emailEnviado) {
                // Enviar email de confirmación al usuario (opcional)
                emailService.enviarEmailConfirmacion(contacto);
                  response.put(SUCCESS_KEY, true);
                String mensaje = emailService.isDemoMode() ? 
                    "¡Mensaje enviado correctamente! (Modo Demo - En producción se enviará por email real)" :
                    "¡Mensaje enviado correctamente! Te responderemos pronto.";
                response.put(MESSAGE_KEY, mensaje);
                response.put("modo", emailService.isDemoMode() ? "DEMO" : "PRODUCTION");
                logger.info("Email de contacto enviado exitosamente desde: {}", contacto.getEmail());
                
                return ResponseEntity.ok(response);
            } else {
                response.put(SUCCESS_KEY, false);
                response.put(MESSAGE_KEY, "Error al enviar el mensaje. Por favor, inténtalo nuevamente.");
                logger.error("Error al enviar email de contacto desde: {}", contacto.getEmail());
                
                return ResponseEntity.internalServerError().body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error inesperado en formulario de contacto: {}", e.getMessage(), e);
            response.put(SUCCESS_KEY, false);
            response.put(MESSAGE_KEY, "Error interno del servidor. Por favor, inténtalo más tarde.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
      /**
     * Endpoint para probar la conectividad SMTP
     * 
     * @return respuesta JSON con el resultado de la prueba
     */
    @GetMapping("/contacto/test-smtp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testSMTP() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("🧪 Iniciando prueba de conectividad SMTP...");
            
            // Verificar conectividad
            boolean conectividad = emailService.verificarConectividadSMTP();
            
            if (conectividad) {
                response.put(SUCCESS_KEY, true);
                response.put(MESSAGE_KEY, "✅ Conectividad SMTP verificada exitosamente");
                response.put("detalles", "Sistema de emails funcionando correctamente");
                response.put("modo", emailService.isDemoMode() ? "DEMO" : "PRODUCTION");
                logger.info("✅ Test SMTP exitoso");
                return ResponseEntity.ok(response);
            } else {
                response.put(SUCCESS_KEY, false);
                response.put(MESSAGE_KEY, "❌ Error de conectividad SMTP");
                response.put("detalles", "No se pudo establecer conexión con el servidor Gmail");
                logger.error("❌ Test SMTP falló");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
            
        } catch (Exception e) {
            logger.error("❌ Error durante test SMTP: {}", e.getMessage(), e);
            response.put(SUCCESS_KEY, false);
            response.put(MESSAGE_KEY, "Error interno durante la prueba SMTP");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }    }
}
