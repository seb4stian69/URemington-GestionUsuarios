package org.uniremington.application.service.interfaces;

import org.uniremington.domain.model.Perfil;

import java.util.List;
import java.util.Optional;

public interface IPerfil {
    Optional<Perfil> findById(Long id);
    List<Perfil> findAll();
    Perfil save(Perfil entity);
    void deleteById(Long id);
}
