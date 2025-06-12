package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.dto.RolDTO;
import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import com.devcodedark.plataforma_cursos.repository.UsuarioRepository;
import com.devcodedark.plataforma_cursos.service.IRolService;

@Service
@Transactional
public class RolServiceJpa implements IRolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> buscarTodos() {
        return rolRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public void guardar(RolDTO rolDTO) {
        if (rolDTO == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        Rol rol = convertirAEntidad(rolDTO);
        rolRepository.save(rol);
    }

    @Override
    public void modificar(RolDTO rolDTO) {
        if (rolDTO == null || rolDTO.getId() == null) {
            throw new IllegalArgumentException("El rol y su ID no pueden ser nulos");
        }
        Rol rol = convertirAEntidad(rolDTO);
        rolRepository.save(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> buscarId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return rolRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        return rolRepository.findByNombre(nombre)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> buscarRolesActivos() {
        return rolRepository.findAllActivos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        return rolRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> buscarPorEstado(Integer estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return rolRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Métodos de conversión
    private RolDTO convertirADTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        dto.setPermisos(rol.getPermisos());
        dto.setFechaCreacion(rol.getFechaCreacion());
        dto.setFechaActualizacion(rol.getFechaActualizacion());
        dto.setEstado(rol.getEstado());

        // Enriquecer con información adicional
        Long cantidadUsuarios = usuarioRepository.countByRolId(rol.getId());
        dto.setCantidadUsuarios(cantidadUsuarios != null ? cantidadUsuarios.intValue() : 0);

        return dto;
    }

    private Rol convertirAEntidad(RolDTO dto) {
        Rol rol = new Rol();
        rol.setId(dto.getId());
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        rol.setPermisos(dto.getPermisos());
        rol.setFechaCreacion(dto.getFechaCreacion());
        rol.setFechaActualizacion(dto.getFechaActualizacion());
        rol.setEstado(dto.getEstado());

        return rol;
    }
}