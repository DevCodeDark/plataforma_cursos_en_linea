package com.devcodedark.plataforma_cursos.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.model.Usuario;
import com.devcodedark.plataforma_cursos.model.Usuario.EstadoUsuario;
import com.devcodedark.plataforma_cursos.model.Usuario.Genero;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;

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
    private static final String ADMIN_DEFAULT_PASSWORD = "Admin123!"; // Contraseña segura por defecto

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }    /**
     * Inicializa los roles básicos del sistema si no existen
     */
    private void initializeRoles() {
        // Crear rol Administrador
        if (!rolRepository.existsByNombre(ROL_ADMINISTRADOR)) {
            Rol adminRole = new Rol();
            adminRole.setNombre(ROL_ADMINISTRADOR);
            adminRole.setDescripcion("Acceso completo al sistema. Puede gestionar usuarios, cursos, roles y configuraciones.");
            adminRole.setPermisos("ADMIN_ALL,USER_MANAGE,COURSE_MANAGE,REPORT_VIEW,SYSTEM_CONFIG");
            adminRole.setEstado(1);
            adminRole.setFechaCreacion(LocalDateTime.now());
            adminRole.setFechaActualizacion(LocalDateTime.now());
            rolRepository.save(adminRole);
            logger.info("✓ Rol '{}' creado exitosamente", ROL_ADMINISTRADOR);
        }

        // Crear rol Docente
        if (!rolRepository.existsByNombre(ROL_DOCENTE)) {
            Rol docenteRole = new Rol();
            docenteRole.setNombre(ROL_DOCENTE);
            docenteRole.setDescripcion("Puede crear y gestionar cursos, ver reportes de sus cursos y gestionar estudiantes inscritos.");
            docenteRole.setPermisos("COURSE_CREATE,COURSE_MANAGE,STUDENT_VIEW,REPORT_OWN,CONTENT_MANAGE");
            docenteRole.setEstado(1);
            docenteRole.setFechaCreacion(LocalDateTime.now());
            docenteRole.setFechaActualizacion(LocalDateTime.now());
            rolRepository.save(docenteRole);
            logger.info("✓ Rol '{}' creado exitosamente", ROL_DOCENTE);
        }

        // Crear rol Estudiante
        if (!rolRepository.existsByNombre(ROL_ESTUDIANTE)) {
            Rol estudianteRole = new Rol();
            estudianteRole.setNombre(ROL_ESTUDIANTE);
            estudianteRole.setDescripcion("Puede inscribirse en cursos, acceder al contenido y realizar evaluaciones.");
            estudianteRole.setPermisos("COURSE_ENROLL,CONTENT_VIEW,QUIZ_TAKE,PROGRESS_VIEW,CERTIFICATE_DOWNLOAD");
            estudianteRole.setEstado(1);
            estudianteRole.setFechaCreacion(LocalDateTime.now());
            estudianteRole.setFechaActualizacion(LocalDateTime.now());
            rolRepository.save(estudianteRole);
            logger.info("✓ Rol '{}' creado exitosamente", ROL_ESTUDIANTE);
        }
    }    /**
     * Crea el usuario administrador por defecto si no existe
     */
    private void initializeAdminUser() {
        if (!usuarioRepository.existsByEmail(ADMIN_EMAIL)) {
            // Buscar el rol de administrador
            Rol adminRole = rolRepository.findByNombre(ROL_ADMINISTRADOR)
                    .orElseThrow(() -> new RuntimeException("Rol Administrador no encontrado"));

            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
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
            logger.info("✓ Usuario administrador creado exitosamente");
            logger.info("  Email: {}", ADMIN_EMAIL);
            logger.info("  Contraseña: {}", ADMIN_DEFAULT_PASSWORD);
            logger.warn("  ⚠️  IMPORTANTE: Cambie la contraseña por defecto después del primer acceso");
        }
    }
}
