package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IPerfil;
import org.uniremington.domain.model.Perfil;
import org.uniremington.domain.repository.PerfilRepository;

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
        return repository.findById(id);
    }

    @Override
    public List<Perfil> findAll() {
        return repository.findAll();
    }

    @Override
    public Perfil save(Perfil entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
