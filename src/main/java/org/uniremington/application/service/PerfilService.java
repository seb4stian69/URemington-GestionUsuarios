package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IPerfil;
import org.uniremington.domain.model.Perfil;
import org.uniremington.domain.repository.PerfilRepository;
import org.uniremington.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PerfilService implements IPerfil {

    PerfilRepository repository;

    @Inject
    public PerfilService(PerfilRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Perfil> findById(Long id) {

        Optional<Perfil> result = repository.findById(id);

        if(result.isPresent()){
            return result;
        }

        throw new NotFoundException("No se ha encontrado un perfil con el id:" + id, "PerfilService.class");

    }

    @Override
    public List<Perfil> findAll() {

        List<Perfil> result = repository.findAll();

        if(!result.isEmpty()){
            return result;
        }

        throw new NotFoundException("No se han encontrado perfiles almacenados", "PerfilService.class");

    }

    @Override
    public Perfil save(Perfil entity) {

        if(entity.getId() == null){
            throw new IllegalArgumentException("La cédula no puede ser nula al guardar un perfil");
        }

        Optional<Perfil> existente = repository.findById(entity.getId());

        if (existente.isPresent()) {
            return repository.save(entity, "update");
        }

        return repository.save(entity, "save");

    }

    @Override
    public void deleteById(Long id) {

        if(id == null){
            throw new IllegalArgumentException("La cédula no puede ser nula al eliminar un perfil");
        }

        Optional<Perfil> existente = repository.findById(id);

        if(existente.isPresent()){
            repository.deleteById(id);
        }else{
            throw new NotFoundException("No se ha encontrado un perfil con el id:" + id, "PerfilService.class");
        }

    }
}
