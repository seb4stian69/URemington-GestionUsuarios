package org.uniremington.application.service.interfaces;

import org.uniremington.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarios {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> login(String user, String contrasena);
    List<Usuario> findAll();
    Usuario save(Usuario entity);
    void deleteById(Long id);
}
