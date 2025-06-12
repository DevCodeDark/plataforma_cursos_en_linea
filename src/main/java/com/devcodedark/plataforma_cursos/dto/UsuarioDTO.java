package com.devcodedark.plataforma_cursos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioDTO {
    
    // Campos básicos de la entidad
    private Integer id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;
    
    private String fotoPerfil;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String genero; // String en lugar del enum para API
    private String estado; // String en lugar del enum para API
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos de relación con información adicional
    private Integer rolId;
    private String rolNombre;
    private String rolDescripcion;
    
    // Campos calculados adicionales
    private String nombreCompleto;
    private Integer totalCursosCreados;
    private Integer totalInscripciones;
    private Integer totalCalificaciones;
    private Double promedioCalificaciones;
    private Integer diasSinAcceso;
    private Boolean estaActivo;
    private Boolean esDocente;
    private Boolean esEstudiante;
    private Boolean esAdministrador;
    
    // Constructores
    public UsuarioDTO() {}
    
    public UsuarioDTO(Integer id, String nombre, String apellido, String email, String usuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.usuario = usuario;
        this.nombreCompleto = nombre + " " + apellido;
    }
    
    public UsuarioDTO(Integer id, String nombre, String apellido, String email, String usuario, 
                     String estado, String rolNombre) {
        this(id, nombre, apellido, email, usuario);
        this.estado = estado;
        this.rolNombre = rolNombre;
        this.estaActivo = "activo".equalsIgnoreCase(estado);
        this.esDocente = "docente".equalsIgnoreCase(rolNombre);
        this.esEstudiante = "estudiante".equalsIgnoreCase(rolNombre);
        this.esAdministrador = "administrador".equalsIgnoreCase(rolNombre);
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
        updateNombreCompleto();
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
        updateNombreCompleto();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
        this.estaActivo = "activo".equalsIgnoreCase(estado);
    }
    
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Integer getRolId() {
        return rolId;
    }
    
    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
    
    public String getRolNombre() {
        return rolNombre;
    }
    
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
        this.esDocente = "docente".equalsIgnoreCase(rolNombre);
        this.esEstudiante = "estudiante".equalsIgnoreCase(rolNombre);
        this.esAdministrador = "administrador".equalsIgnoreCase(rolNombre);
    }
    
    public String getRolDescripcion() {
        return rolDescripcion;
    }
    
    public void setRolDescripcion(String rolDescripcion) {
        this.rolDescripcion = rolDescripcion;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public Integer getTotalCursosCreados() {
        return totalCursosCreados;
    }
    
    public void setTotalCursosCreados(Integer totalCursosCreados) {
        this.totalCursosCreados = totalCursosCreados;
    }
    
    public Integer getTotalInscripciones() {
        return totalInscripciones;
    }
    
    public void setTotalInscripciones(Integer totalInscripciones) {
        this.totalInscripciones = totalInscripciones;
    }
    
    public Integer getTotalCalificaciones() {
        return totalCalificaciones;
    }
    
    public void setTotalCalificaciones(Integer totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }
    
    public Double getPromedioCalificaciones() {
        return promedioCalificaciones;
    }
    
    public void setPromedioCalificaciones(Double promedioCalificaciones) {
        this.promedioCalificaciones = promedioCalificaciones;
    }
    
    public Integer getDiasSinAcceso() {
        return diasSinAcceso;
    }
    
    public void setDiasSinAcceso(Integer diasSinAcceso) {
        this.diasSinAcceso = diasSinAcceso;
    }
    
    public Boolean getEstaActivo() {
        return estaActivo;
    }
    
    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
    
    public Boolean getEsDocente() {
        return esDocente;
    }
    
    public void setEsDocente(Boolean esDocente) {
        this.esDocente = esDocente;
    }
    
    public Boolean getEsEstudiante() {
        return esEstudiante;
    }
    
    public void setEsEstudiante(Boolean esEstudiante) {
        this.esEstudiante = esEstudiante;
    }
    
    public Boolean getEsAdministrador() {
        return esAdministrador;
    }
    
    public void setEsAdministrador(Boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }
    
    // Métodos helper
    private void updateNombreCompleto() {
        if (nombre != null && apellido != null) {
            this.nombreCompleto = nombre + " " + apellido;
        }
    }
    
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", usuario='" + usuario + '\'' +
                ", estado='" + estado + '\'' +
                ", rolNombre='" + rolNombre + '\'' +
                '}';
    }
}
