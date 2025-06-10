package org.uniremington.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.uniremington.domain.model.Usuario;
import org.uniremington.domain.repository.UsuarioRepository;
import org.uniremington.infrastructure.entity.PerfilEntity;
import org.uniremington.infrastructure.entity.PersonaEntity;
import org.uniremington.infrastructure.entity.UsuarioEntity;
import org.uniremington.shared.util.UsuarioMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUsuarioRepository implements UsuarioRepository {

    @PersistenceContext EntityManager em;
    UsuarioMapper mapper;

    @Inject JpaUsuarioRepository (UsuarioMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        UsuarioEntity entity = em.find(UsuarioEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public List<Usuario> findAll() {

        List<UsuarioEntity> list = em.createQuery("SELECT p FROM UsuarioEntity p", UsuarioEntity.class)
                .getResultList();

        List<Usuario> response = new ArrayList<>();

        list.forEach(user-> response.add(
            new Usuario(
                user.getId(),
                user.getNombreUsuario(),
                user.getContrasena(),
                user.getIdperfil().getId().toString(),
                user.getSalt(),
                user.getEstado()
            )
        ));

        return response;

    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario, String action) {

        UsuarioEntity entity;

        if ("save".equals(action)) {
            entity = mapper.toEntity(usuario);
            em.persist(entity);
        } else {
            entity = mapper.toEntity(usuario);
            entity = em.merge(entity); // evita persistir entidad duplicada
        }

        PersonaEntity persona = em.find(PersonaEntity.class, usuario.getId());
        persona.setUsuario(entity);
        entity.setPersona(persona);

        PerfilEntity perfil = em.find(PerfilEntity.class, Long.parseLong(usuario.getIdPerfil()));
        entity.setIdperfil(perfil);

        return new Usuario(
                entity.getId(),
                entity.getNombreUsuario(),
                entity.getContrasena(),
                String.valueOf(entity.getIdperfil().getId()),
                entity.getSalt(),
                entity.getEstado()
        );
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        UsuarioEntity entity = em.find(UsuarioEntity.class, id);
        if (entity != null) {
            entity.setEstado(false);
            em.merge(entity);
        }
    }

    @Override
    public Optional<Usuario> getByUsername(Usuario user) {

        try {

            UsuarioEntity usuario = em.createQuery(

                    "SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario = :nombreUsuario", UsuarioEntity.class

            ).setParameter("nombreUsuario", user.getNombreUsuario()).getSingleResult();

            Usuario usRes = mapper.toModel(usuario);
            usRes.setIdPerfil(usuario.getIdperfil().getId().toString());

            return Optional.of(usRes);

        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

}