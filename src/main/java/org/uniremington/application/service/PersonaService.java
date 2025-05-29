package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.domain.model.Persona;
import org.uniremington.domain.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonaService implements IPersona {

    @Inject
    PersonaRepository repository;

    @Override
    public Optional<Persona> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Persona> findAll() {
        return repository.findAll();
    }

    @Override
    public Persona save(Persona entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
