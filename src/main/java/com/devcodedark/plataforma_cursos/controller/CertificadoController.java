package com.devcodedark.plataforma_cursos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devcodedark.plataforma_cursos.model.Certificado;
import com.devcodedark.plataforma_cursos.service.ICertificadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "*")
public class CertificadoController {

    @Autowired
    private ICertificadoService certificadoService;

    // Listar todos los certificados
    @GetMapping
    public ResponseEntity<List<Certificado>> listarTodos() {
        try {
            List<Certificado> certificados = certificadoService.buscarTodos();
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar certificado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Certificado> buscarPorId(@PathVariable Integer id) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarId(id);
            if (certificado.isPresent()) {
                return ResponseEntity.ok(certificado.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear nuevo certificado
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody Certificado certificado) {
        try {
            certificadoService.guardar(certificado);
            return ResponseEntity.status(HttpStatus.CREATED).body("Certificado creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el certificado: " + e.getMessage());
        }
    }

    // Generar certificado para inscripción
    @PostMapping("/generar/{inscripcionId}")
    public ResponseEntity<String> generarCertificado(@PathVariable Integer inscripcionId) {
        try {
            certificadoService.generarCertificado(inscripcionId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Certificado generado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al generar el certificado: " + e.getMessage());
        }
    }

    // Actualizar certificado
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @Valid @RequestBody Certificado certificado) {
        try {
            Optional<Certificado> certificadoExistente = certificadoService.buscarId(id);
            if (!certificadoExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            certificado.setId(id);
            certificadoService.modificar(certificado);
            return ResponseEntity.ok("Certificado actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el certificado: " + e.getMessage());
        }
    }

    // Eliminar certificado
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarId(id);
            if (!certificado.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            certificadoService.eliminar(id);
            return ResponseEntity.ok("Certificado eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar el certificado: " + e.getMessage());
        }
    }

    // Buscar certificado por código de verificación
    @GetMapping("/codigo/{codigoVerificacion}")
    public ResponseEntity<Certificado> buscarPorCodigo(@PathVariable String codigoVerificacion) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarPorCodigoVerificacion(codigoVerificacion);
            if (certificado.isPresent()) {
                return ResponseEntity.ok(certificado.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar certificados por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Certificado>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<Certificado> certificados = certificadoService.buscarPorEstudiante(estudianteId);
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar certificados por curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Certificado>> buscarPorCurso(@PathVariable Integer cursoId) {
        try {
            List<Certificado> certificados = certificadoService.buscarPorCurso(cursoId);
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar certificados válidos
    @GetMapping("/validos")
    public ResponseEntity<List<Certificado>> listarValidos() {
        try {
            List<Certificado> certificados = certificadoService.buscarCertificadosValidos();
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Verificar certificado
    @GetMapping("/verificar/{codigoVerificacion}")
    public ResponseEntity<Boolean> verificarCertificado(@PathVariable String codigoVerificacion) {
        try {
            boolean esValido = certificadoService.verificarCertificado(codigoVerificacion);
            return ResponseEntity.ok(esValido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Invalidar certificado
    @PutMapping("/{id}/invalidar")
    public ResponseEntity<String> invalidarCertificado(@PathVariable Integer id) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarId(id);
            if (!certificado.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            certificadoService.invalidarCertificado(id);
            return ResponseEntity.ok("Certificado invalidado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al invalidar el certificado: " + e.getMessage());
        }
    }

    // Contar certificados por curso
    @GetMapping("/curso/{cursoId}/count")
    public ResponseEntity<Long> contarPorCurso(@PathVariable Integer cursoId) {
        try {
            Long count = certificadoService.contarCertificadosPorCurso(cursoId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Generar PDF del certificado
    @GetMapping("/{id}/pdf")
    public ResponseEntity<String> generarPdf(@PathVariable Integer id) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarId(id);
            if (!certificado.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            String rutaPdf = certificadoService.generarPdfCertificado(id);
            return ResponseEntity.ok(rutaPdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al generar el PDF: " + e.getMessage());
        }
    }

    // Buscar certificado por inscripción
    @GetMapping("/inscripcion/{inscripcionId}")
    public ResponseEntity<Certificado> buscarPorInscripcion(@PathVariable Integer inscripcionId) {
        try {
            Optional<Certificado> certificado = certificadoService.buscarPorInscripcion(inscripcionId);
            if (certificado.isPresent()) {
                return ResponseEntity.ok(certificado.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}