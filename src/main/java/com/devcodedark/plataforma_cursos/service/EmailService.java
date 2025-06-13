package com.devcodedark.plataforma_cursos.service;

import com.devcodedark.plataforma_cursos.dto.ContactoDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para el envío de emails
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${email.destinatario}")
    private String emailDestinatario;
    
    @Value("${email.remitente}")
    private String emailRemitente;
    
    @Value("${email.demo.mode:false}")
    private boolean demoMode;
    
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
      /**
     * Envía un email de contacto desde el formulario web
     * @param contacto Datos del formulario de contacto
     * @return true si se envió correctamente, false en caso contrario
     */
    public boolean enviarEmailContacto(ContactoDTO contacto) {
        // Si está en modo demo, simular el envío
        if (demoMode) {
            return enviarEmailDemo(contacto);
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Configurar destinatario y remitente
            helper.setTo(emailDestinatario);
            helper.setFrom(emailRemitente);
            helper.setReplyTo(contacto.getEmail());
            
            // Asunto del email
            String asunto = String.format("[AstroDev Academy] Nuevo mensaje de contacto: %s", contacto.getMotivo());
            helper.setSubject(asunto);
            
            // Generar contenido HTML del email
            String contenidoHtml = generarContenidoHtml(contacto);
            helper.setText(contenidoHtml, true);
            
            // Enviar email
            mailSender.send(message);
            
            logger.info("✅ Email de contacto enviado exitosamente a {} desde {}", emailDestinatario, contacto.getEmail());
            return true;
            
        } catch (MessagingException e) {
            logger.error("Error al enviar email de contacto: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email: {}", e.getMessage(), e);
            return false;
        }
    }
      /**
     * Genera el contenido HTML del email de contacto
     * @param contacto Datos del formulario
     * @return Contenido HTML del email
     */
    private String generarContenidoHtml(ContactoDTO contacto) {
        Context context = new Context();
        context.setVariable("nombre", contacto.getNombre());
        context.setVariable("email", contacto.getEmail());
        context.setVariable("telefono", contacto.getTelefono());
        context.setVariable("motivo", contacto.getMotivo());
        context.setVariable("mensaje", contacto.getMensaje());
        context.setVariable("fechaHora", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        
        // Intentar usar la plantilla de email, si falla usar HTML simple
        try {
            String html = templateEngine.process("email-contacto", context);
            logger.debug("Template email-contacto procesado correctamente");
            return html;
        } catch (Exception e) {
            logger.warn("No se pudo procesar template email-contacto, usando HTML simple: {}", e.getMessage());
            return generarHtmlSimple(contacto);
        }
    }
    
    /**
     * Genera HTML simple para el email cuando no hay plantilla disponible
     */
    private String generarHtmlSimple(ContactoDTO contacto) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f8f9fa;'>");
        
        // Encabezado
        html.append("<div style='background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f0f1e 100%); color: white; padding: 20px; border-radius: 10px; text-align: center; margin-bottom: 20px;'>");
        html.append("<h1 style='margin: 0; color: #00d4ff;'>🚀 AstroDev Academy</h1>");
        html.append("<h2 style='margin: 10px 0 0 0; font-size: 18px;'>Nuevo mensaje de contacto</h2>");
        html.append("</div>");
        
        // Contenido
        html.append("<div style='background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>");
        html.append("<h3 style='color: #333; border-bottom: 2px solid #00d4ff; padding-bottom: 10px;'>Información del contacto</h3>");
        
        html.append("<table style='width: 100%; border-collapse: collapse;'>");
        html.append("<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Nombre:</td><td style='padding: 8px 0;'>").append(contacto.getNombre()).append("</td></tr>");
        html.append("<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Email:</td><td style='padding: 8px 0;'>").append(contacto.getEmail()).append("</td></tr>");
        
        if (contacto.getTelefono() != null && !contacto.getTelefono().trim().isEmpty()) {
            html.append("<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Teléfono:</td><td style='padding: 8px 0;'>").append(contacto.getTelefono()).append("</td></tr>");
        }
        
        html.append("<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Motivo:</td><td style='padding: 8px 0;'>").append(obtenerMotivoLegible(contacto.getMotivo())).append("</td></tr>");
        html.append("<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Fecha:</td><td style='padding: 8px 0;'>").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("</td></tr>");
        html.append("</table>");
        
        html.append("<h3 style='color: #333; border-bottom: 2px solid #00d4ff; padding-bottom: 10px; margin-top: 20px;'>Mensaje</h3>");
        html.append("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; border-left: 4px solid #00d4ff;'>");
        html.append("<p style='margin: 0; line-height: 1.6; color: #333;'>").append(contacto.getMensaje().replace("\n", "<br>")).append("</p>");
        html.append("</div>");
        
        html.append("</div>");
        
        // Pie
        html.append("<div style='text-align: center; margin-top: 20px; color: #666; font-size: 12px;'>");
        html.append("<p>Este mensaje fue enviado desde el formulario de contacto de AstroDev Academy</p>");
        html.append("<p>Para responder, utiliza directamente el email: ").append(contacto.getEmail()).append("</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    /**
     * Convierte el código del motivo en texto legible
     */
    private String obtenerMotivoLegible(String motivo) {
        switch (motivo) {
            case "informacion-cursos": return "Información sobre cursos";
            case "soporte-tecnico": return "Soporte técnico";
            case "alianzas": return "Alianzas y partnerships";
            case "trabajo": return "Oportunidades de trabajo";
            case "otro": return "Otro";
            default: return motivo;
        }
    }
    
    /**
     * Envía un email de confirmación al usuario que envió el formulario
     */
    public boolean enviarEmailConfirmacion(ContactoDTO contacto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(contacto.getEmail());
            helper.setFrom(emailRemitente);
            helper.setSubject("✅ Confirmación - Tu mensaje ha sido recibido - AstroDev Academy");
            
            String contenidoConfirmacion = generarHtmlConfirmacion(contacto);
            helper.setText(contenidoConfirmacion, true);
            
            mailSender.send(message);
            
            logger.info("Email de confirmación enviado a {}", contacto.getEmail());
            return true;
            
        } catch (Exception e) {
            logger.error("Error al enviar email de confirmación: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Genera HTML para el email de confirmación al usuario
     */
    private String generarHtmlConfirmacion(ContactoDTO contacto) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f8f9fa;'>");
        
        // Encabezado
        html.append("<div style='background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f0f1e 100%); color: white; padding: 20px; border-radius: 10px; text-align: center; margin-bottom: 20px;'>");
        html.append("<h1 style='margin: 0; color: #00d4ff;'>🚀 AstroDev Academy</h1>");
        html.append("<h2 style='margin: 10px 0 0 0; font-size: 18px;'>¡Gracias por contactarnos!</h2>");
        html.append("</div>");
        
        // Contenido
        html.append("<div style='background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>");
        html.append("<p style='font-size: 16px; color: #333;'>Hola <strong>").append(contacto.getNombre()).append("</strong>,</p>");
        html.append("<p style='color: #333; line-height: 1.6;'>Hemos recibido tu mensaje y te responderemos pronto. Nuestro equipo revisará tu consulta sobre <strong>").append(obtenerMotivoLegible(contacto.getMotivo())).append("</strong> y se pondrá en contacto contigo a la brevedad.</p>");
        
        html.append("<div style='background: #f0f8ff; padding: 15px; border-radius: 5px; border-left: 4px solid #00d4ff; margin: 20px 0;'>");
        html.append("<h4 style='margin: 0 0 10px 0; color: #333;'>Resumen de tu mensaje:</h4>");
        html.append("<p style='margin: 0; color: #666;'><strong>Motivo:</strong> ").append(obtenerMotivoLegible(contacto.getMotivo())).append("</p>");
        html.append("<p style='margin: 5px 0 0 0; color: #666;'><strong>Mensaje:</strong> ").append(contacto.getMensaje().length() > 100 ? contacto.getMensaje().substring(0, 100) + "..." : contacto.getMensaje()).append("</p>");
        html.append("</div>");
        
        html.append("<p style='color: #333; line-height: 1.6;'>Mientras tanto, te invitamos a explorar nuestros cursos y recursos disponibles en nuestra plataforma.</p>");
        html.append("<p style='color: #333;'>¡Gracias por confiar en AstroDev Academy!</p>");
        html.append("</div>");
        
        // Pie
        html.append("<div style='text-align: center; margin-top: 20px; color: #666; font-size: 12px;'>");
        html.append("<p>AstroDev Academy - Jr. Circunvalación Cumbaza 298, Tarapoto, San Martín, Perú</p>");
        html.append("<p>📞 +51 925 859 554 | 📧 guiro240303@gmail.com</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
      /**
     * Verifica la conectividad SMTP con Gmail
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public boolean verificarConectividadSMTP() {
        // Si está en modo demo, simular conectividad exitosa
        if (demoMode) {
            logger.info("🎭 MODO DEMO - Simulando verificación de conectividad SMTP...");
            logger.info("✅ Conectividad SMTP simulada exitosamente (MODO DEMO)");
            return true;
        }
        
        try {
            logger.info("Verificando conectividad SMTP con Gmail...");
            
            // Crear una conexión de prueba
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(emailDestinatario);
            helper.setFrom(emailRemitente);
            helper.setSubject("[AstroDev Academy] Test de conectividad SMTP");
            helper.setText("Este es un email de prueba para verificar la conectividad SMTP.", false);
            
            // Intentar enviar
            mailSender.send(message);
            
            logger.info("✅ Conectividad SMTP verificada exitosamente");
            return true;
            
        } catch (Exception e) {
            logger.error("❌ Error de conectividad SMTP: {}", e.getMessage(), e);
            return false;
        }    }
    
    /**
     * Simula el envío de un email en modo demo
     * @param contacto Datos del formulario de contacto
     * @return true (siempre exitoso en modo demo)
     */
    private boolean enviarEmailDemo(ContactoDTO contacto) {
        logger.info("🎭 MODO DEMO - Simulando envío de email de contacto:");
        logger.info("📧 DE: {} <{}>", contacto.getNombre(), contacto.getEmail());
        logger.info("📨 PARA: {} <{}>", "AstroDev Academy", emailDestinatario);
        logger.info("📋 MOTIVO: {}", contacto.getMotivo());
        logger.info("💬 MENSAJE: {}", contacto.getMensaje().length() > 100 ? 
                   contacto.getMensaje().substring(0, 100) + "..." : contacto.getMensaje());
        logger.info("📅 FECHA: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        logger.info("✅ Email simulado enviado exitosamente (MODO DEMO)");
        
        // Simular un pequeño delay como si fuera un envío real
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return true;
    }
    
    /**
     * Verifica si el servicio está en modo demo
     * @return true si está en modo demo
     */
    public boolean isDemoMode() {
        return demoMode;
    }
}
