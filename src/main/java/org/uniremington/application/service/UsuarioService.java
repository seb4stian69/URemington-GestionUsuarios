package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IUsuarios;
import org.uniremington.domain.model.Usuario;
import org.uniremington.domain.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService implements IUsuarios {

    UsuarioRepository repository;

    @Inject
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> login(String user, String contrasena) { return repository.login(user, contrasena); }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario save(Usuario entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
