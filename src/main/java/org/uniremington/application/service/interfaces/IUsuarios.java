package org.uniremington.application.service.interfaces;

import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.util.ApiResponse;

import java.util.List;
import java.util.Optional;

public interface IUsuarios {
    Optional<Usuario> findById(Long id);
    boolean login(Usuario user);
    List<Usuario> findAll();
    Usuario save(Usuario entity);
    void deleteById(Long id);
    ApiResponse resetPassword(String username);
}
