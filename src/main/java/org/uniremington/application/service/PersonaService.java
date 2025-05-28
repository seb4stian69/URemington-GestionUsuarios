package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.uniremington.domain.model.Persona;
import org.uniremington.domain.repository.PersonaRepository;
import org.uniremington.shared.base.BaseService;

@ApplicationScoped
public class PersonaService extends BaseService<Persona, Long> {
    public PersonaService(PersonaRepository repository) { super(repository); }
}
