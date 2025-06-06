package org.uniremington.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.uniremington.domain.model.Perfil;
import org.uniremington.domain.repository.PerfilRepository;
import org.uniremington.infrastructure.entity.PerfilEntity;
import org.uniremington.shared.util.PerfilMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaPerfilRepository implements PerfilRepository {

    @PersistenceContext EntityManager em;

    PerfilMapper mapper;

    @Inject JpaPerfilRepository(PerfilMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public Optional<Perfil> findById(Long id) {
        PerfilEntity entity = em.find(PerfilEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public List<Perfil> findAll() {
        return em.createQuery("SELECT p FROM PerfilEntity p", PerfilEntity.class)
                .getResultList()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Perfil save(Perfil usuario) {

        PerfilEntity entity = mapper.toEntity(usuario);

        if (entity.getId() == null) {
            throw new IllegalArgumentException("La cédula no puede ser nula al guardar un perfil");
        }

        PerfilEntity existing = em.find(PerfilEntity.class, entity.getId());

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
        PerfilEntity entity = em.find(PerfilEntity.class, id);
        if (entity != null) {
            entity.setEstado(false);
            em.merge(entity);
        }
    }

}