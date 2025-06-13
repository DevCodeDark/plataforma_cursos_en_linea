package com.devcodedark.plataforma_cursos.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;
import com.devcodedark.plataforma_cursos.model.Usuario.Genero;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.ConfiguracionService;

/**
 * Inicializador de datos por defecto del sistema.
 * Se ejecuta al iniciar la aplicación y crea roles y usuario administrador
 * si no existen en la base de datos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
      // Constantes para roles
    private static final String ROL_ADMINISTRADOR = "Administrador";
    private static final String ROL_DOCENTE = "Docente";
    private static final String ROL_ESTUDIANTE = "Estudiante";
    
    // Constantes para usuario admin por defecto
    private static final String ADMIN_EMAIL = "admin@astrodev.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_DEFAULT_PASSWORD = "P@ssw0rd2025!AstroDev#"; // Contraseña temporal única
    
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfiguracionService configuracionService;

    public DataInitializer(RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          ConfiguracionService configuracionService) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;        this.configuracionService = configuracionService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Iniciando la inicialización de datos...");
            initializeRoles();
            initializeAdminUser();
            initializeConfigurations();
            logger.info("Inicialización de datos completada.");
        } catch (Exception e) {
            logger.error("Error durante la inicialización de datos: {}", e.getMessage(), e);
        }
    }/**
     * Inicializa los roles básicos del sistema si no existen
     */
    private void initializeRoles() {        logger.info("Verificando y creando roles del sistema si es necesario...");
        
        // Primero, corregir cualquier rol existente que tenga estado NULL o inactivo
        try {
            int rolesUpdated = rolRepository.updateRolesToActive();
            if (rolesUpdated > 0) {
                logger.info("✓ Se actualizaron {} roles para estado activo.", rolesUpdated);
            }
        } catch (Exception e) {
            logger.warn("No se pudieron actualizar los estados de roles existentes: {}", e.getMessage());
        }

        // Crear rol Administrador
        createRoleIfNotExists(ROL_ADMINISTRADOR, 
            "Acceso completo al sistema. Puede gestionar usuarios, cursos, roles y configuraciones.",
            "ADMIN_ALL,USER_MANAGE,COURSE_MANAGE,REPORT_VIEW,SYSTEM_CONFIG");

        // Crear rol Docente
        createRoleIfNotExists(ROL_DOCENTE,
            "Puede crear y gestionar cursos, ver reportes de sus cursos y gestionar estudiantes inscritos.",
            "COURSE_CREATE,COURSE_MANAGE,STUDENT_VIEW,REPORT_OWN,CONTENT_MANAGE");

        // Crear rol Estudiante
        createRoleIfNotExists(ROL_ESTUDIANTE,
            "Puede inscribirse en cursos, acceder al contenido y realizar evaluaciones.",
            "COURSE_ENROLL,CONTENT_VIEW,QUIZ_TAKE,PROGRESS_VIEW,CERTIFICATE_DOWNLOAD");

        logger.info("Verificación y creación de roles completada.");
    }    /**
     * Crea un rol si no existe
     */
    private void createRoleIfNotExists(String nombreRol, String descripcion, String permisos) {
        try {
            // Verificar si el rol ya existe usando consulta nativa (ignora filtros de Hibernate)
            Optional<Rol> existingRole = rolRepository.findRoleByName(nombreRol);
            
            if (existingRole.isPresent()) {
                logger.info("- Rol '{}' ya existe, omitiendo creación.", nombreRol);
                return;
            }

            // Crear el nuevo rol
            Rol newRole = new Rol();
            newRole.setNombre(nombreRol);
            newRole.setDescripcion(descripcion);
            newRole.setPermisos(permisos);
            newRole.setEstado(1);
            newRole.setFechaCreacion(LocalDateTime.now());
            newRole.setFechaActualizacion(LocalDateTime.now());

            rolRepository.save(newRole);
            logger.info("✓ Rol '{}' creado exitosamente.", nombreRol);
        } catch (Exception e) {
            // Si hay error (probablemente porque ya existe), simplemente logear
            logger.warn("- Rol '{}' no se pudo crear (probablemente ya existe): {}", nombreRol, e.getMessage());
        }
    }    /**
     * Crea el usuario administrador por defecto si no existe
     */
    private void initializeAdminUser() {
        logger.info("Verificando y creando usuario administrador si es necesario...");
        
        try {
            // Verificar si ya existe el usuario administrador por email o usuario sin filtro de estado
            if (usuarioRepository.findUserByEmail(ADMIN_EMAIL).isPresent() || 
                usuarioRepository.findUserByUsuario(ADMIN_USERNAME).isPresent()) {
                logger.info("- Usuario administrador ya existe, omitiendo creación.");
                return;
            }

            // Buscar el rol de administrador usando consulta sin filtro de estado
            // Ahora debería funcionar porque ya actualizamos los estados a 1
            Optional<Rol> adminRoleOpt = rolRepository.findByNombre(ROL_ADMINISTRADOR);
            if (!adminRoleOpt.isPresent()) {
                logger.error("Rol 'Administrador' no encontrado después de la inicialización. Verifique la base de datos.");
                return;
            }
            
            Rol adminRole = adminRoleOpt.get();

            // Crear el usuario administrador
            Usuario admin = new Usuario();
            admin.setNombre("Sistema");
            admin.setApellido("Admin");
            admin.setEmail(ADMIN_EMAIL);
            admin.setUsuario(ADMIN_USERNAME);
            admin.setPasswordHash(passwordEncoder.encode(ADMIN_DEFAULT_PASSWORD));
            admin.setRol(adminRole);
            admin.setEstado(EstadoUsuario.activo);
            admin.setGenero(Genero.Otro);
            admin.setFechaCreacion(LocalDateTime.now());
            admin.setFechaActualizacion(LocalDateTime.now());
            admin.setUltimoAcceso(LocalDateTime.now());

            usuarioRepository.save(admin);
            logger.info("✓ Usuario administrador creado exitosamente.");
            logger.info("  Email: {}", ADMIN_EMAIL);
            logger.info("  Usuario: {}", ADMIN_USERNAME);
            logger.warn("  ⚠️  IMPORTANTE: Cambie la contraseña temporal después del primer acceso.");
            
        } catch (Exception e) {
            logger.error("Error al crear el usuario administrador: {}", e.getMessage());
            // Log de depuración adicional
            logger.debug("Stack trace completo:", e);
        }
    }/**
     * Inicializa las configuraciones por defecto del sistema
     */
    private void initializeConfigurations() {
        logger.info("Verificando e inicializando configuraciones del sistema...");
        
        try {
            configuracionService.inicializarConfiguracionesPorDefecto();
            logger.info("✓ Configuraciones del sistema inicializadas correctamente.");
        } catch (Exception e) {
            logger.error("Error al inicializar configuraciones del sistema: {}", e.getMessage(), e);
        }
    }
}
