package org.uniremington.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.uniremington.domain.model.Persona;
import org.uniremington.domain.repository.PersonaRepository;
import org.uniremington.infrastructure.entity.PersonaEntity;
import org.uniremington.shared.util.PersonaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaPersonaRepository implements PersonaRepository {

    @PersistenceContext EntityManager em;

    @Inject /*->*/ PersonaMapper personaMapper;

    @Override
    public Optional<Persona> findById(Long id) {
        PersonaEntity entity = em.find(PersonaEntity.class, id);
        return Optional.ofNullable(entity).map(personaMapper::toModel);
    }

    @Override
    public List<Persona> findAll() {
        return em.createQuery("SELECT p FROM PersonaEntity p", PersonaEntity.class)
                .getResultList()
                .stream()
                .map(personaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Persona save(Persona persona) {
        PersonaEntity entity = personaMapper.toEntity(persona);
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return personaMapper.toModel(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        PersonaEntity entity = em.find(PersonaEntity.class, id);
        if (entity != null) {
            entity.setEstado("INACTIVO"); // borrado lógico
            em.merge(entity);
        }
    }

}