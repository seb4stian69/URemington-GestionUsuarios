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

    @Inject /*->*/ PersonaMapper mapper;

    @Override
    public Optional<Persona> findById(Long id) {
        PersonaEntity entity = em.find(PersonaEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public List<Persona> findAll() {
        return em.createQuery("SELECT p FROM PersonaEntity p", PersonaEntity.class)
                .getResultList()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Persona save(Persona usuario, String action) {

        PersonaEntity entity = mapper.toEntity(usuario);

        if (entity.getId() == null) {
            throw new IllegalArgumentException("La cédula no puede ser nula al guardar una persona");
        }

        PersonaEntity existing = em.find(PersonaEntity.class, entity.getId());

        if (existing == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }

        return mapper.toModel(entity);

    }



    @Override
    @Transactional
    public void deleteById(Long id) {
        PersonaEntity entity = em.find(PersonaEntity.class, id);
        if (entity != null) {
            entity.setEstado(false);
            em.merge(entity);
        }
    }

}