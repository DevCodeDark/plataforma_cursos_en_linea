package com.devcodedark.plataforma_cursos.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcodedark.plataforma_cursos.model.Rol;
import com.devcodedark.plataforma_cursos.repository.RolRepository;
import com.devcodedark.plataforma_cursos.service.IRolService;

@Service
@Transactional
public class RolServiceJpa implements IRolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> buscarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public void guardar(Rol rol) {
        rolRepository.save(rol);
    }

    @Override
    public void modificar(Rol rol) {
        rolRepository.save(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarId(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> buscarRolesActivos() {
        return rolRepository.findAllActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> buscarPorEstado(Integer estado) {
        return rolRepository.findByEstado(estado);
    }
}