package org.uniremington.application.service.interfaces;

import org.uniremington.domain.model.Persona;

import java.util.List;
import java.util.Optional;

public interface IPersona {

    Optional<Persona> findById(Long id);
    List<Persona> findAll();
    Persona save(Persona entity);
    void deleteById(Long id);

}
