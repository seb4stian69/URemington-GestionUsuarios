package org.uniremington.application.service.interfaces;

import org.uniremington.domain.model.Usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUsuarios {
    Optional<Usuario> findById(Long id);
    boolean login(Usuario user);
    List<Usuario> findAll();
    Usuario save(Usuario entity);
    Usuario getByUsername(String username);
    void deleteById(Long id);
    Map<String, Object> resetPassword(String username);
}
