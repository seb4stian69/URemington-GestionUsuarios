package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IPersona;
import org.uniremington.domain.model.Persona;
import org.uniremington.domain.repository.PersonaRepository;
import org.uniremington.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonaService implements IPersona {

    PersonaRepository repository;

    @Inject
    public PersonaService(PersonaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Persona> findById(Long id) {

        Optional<Persona> result = repository.findById(id);

        if(result.isPresent()){
            return result;
        }

        throw new NotFoundException("No se ha encontrado una persona con el id:" + id, "PersonaService.class");

    }

    @Override
    public List<Persona> findAll() {

        List<Persona> result = repository.findAll();

        if(!result.isEmpty()){
            return result;
        }

        throw new NotFoundException("No se han encontrado personas almacenadas", "PersonaService.class");

    }

    @Override
    public Persona save(Persona entity) {

        if(entity.getId() == null){
            throw new IllegalArgumentException("La cédula no puede ser nula al guardar una persona");
        }

        Optional<Persona> existente = repository.findById(entity.getId());

        if (existente.isPresent()) {
            return repository.save(entity, "update");
        }

        return repository.save(entity, "save");

    }

    @Override
    public void deleteById(Long id) {

        if(id == null){
            throw new IllegalArgumentException("La cédula no puede ser nula al eliminar una persona");
        }

        Optional<Persona> existente = repository.findById(id);

        if(existente.isPresent()){
            repository.deleteById(id);
        }

        throw new NotFoundException("No se ha encontrado una persona con el id:" + id, "PersonaService.class");

    }

}
