package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para el cambio de contraseña
 */
public class CambioPasswordDTO {
    
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;
    
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String passwordNueva;
    
    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmarPassword;
    
    // Constructores
    public CambioPasswordDTO() {}
    
    public CambioPasswordDTO(String passwordActual, String passwordNueva, String confirmarPassword) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
        this.confirmarPassword = confirmarPassword;
    }
    
    // Getters y Setters
    public String getPasswordActual() {
        return passwordActual;
    }
    
    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }
    
    public String getPasswordNueva() {
        return passwordNueva;
    }
    
    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }
    
    public String getConfirmarPassword() {
        return confirmarPassword;
    }
    
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
    
    /**
     * Valida que las contraseñas nuevas coincidan
     */
    public boolean passwordsCoinciden() {
        return passwordNueva != null && passwordNueva.equals(confirmarPassword);
    }
}
