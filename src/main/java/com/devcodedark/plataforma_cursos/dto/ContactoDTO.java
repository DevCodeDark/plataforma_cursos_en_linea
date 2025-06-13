package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para el formulario de contacto
 */
public class ContactoDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;
    
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
    
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 10, max = 1000, message = "El mensaje debe tener entre 10 y 1000 caracteres")
    private String mensaje;
    
    // Constructores
    public ContactoDTO() {}
    
    public ContactoDTO(String nombre, String email, String telefono, String motivo, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.motivo = motivo;
        this.mensaje = mensaje;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    // Método toString para debugging
    @Override
    public String toString() {
        return "ContactoDTO{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", motivo='" + motivo + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
