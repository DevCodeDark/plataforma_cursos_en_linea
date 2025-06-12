package com.devcodedark.plataforma_cursos.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario específico
 */
public class UsuarioNoEncontradoException extends RuntimeException {
    
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public UsuarioNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public UsuarioNoEncontradoException(Integer usuarioId) {
        super("Usuario con ID " + usuarioId + " no encontrado");
    }
}
