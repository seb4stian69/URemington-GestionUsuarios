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
import java.util.Objects;
import java.util.Optional;

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
                .toList();
    }

    @Override
    @Transactional
    public Perfil save(Perfil usuario, String action) {

        PerfilEntity entity = mapper.toEntity(usuario);

        if (Objects.equals(action, "save")) {
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
        entity.setEstado(false);
        em.merge(entity);
    }

}