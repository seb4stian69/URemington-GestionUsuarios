package org.uniremington.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.uniremington.domain.model.Usuario;
import org.uniremington.domain.repository.UsuarioRepository;
import org.uniremington.infrastructure.entity.UsuarioEntity;
import org.uniremington.shared.exception.HashPasswordException;
import org.uniremington.shared.util.PasswordHasher;
import org.uniremington.shared.util.UsuarioMapper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaUsuarioRepository implements UsuarioRepository {

    @PersistenceContext EntityManager em;
    UsuarioMapper mapper;

    @Inject
    public JpaUsuarioRepository (UsuarioMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        UsuarioEntity entity = em.find(UsuarioEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public Optional<Usuario> login(String user, String contrasena) {

        try {

            UsuarioEntity usuario = em.createQuery(
                "SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario = :nombreUsuario AND u.contrasena = :contrasena", UsuarioEntity.class)
                .setParameter("nombreUsuario", user)
                .setParameter("contrasena", contrasena)
                .getSingleResult();

            return Optional.of(mapper.toModel(usuario));

        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Usuario> findAll() {
        return em.createQuery("SELECT p FROM UsuarioEntity p", UsuarioEntity.class)
                .getResultList()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);

        if (entity.getId() == null) {
            throw new IllegalArgumentException("La cédula no puede ser nula al guardar un usuario");
        }

        UsuarioEntity existing = em.find(UsuarioEntity.class, entity.getId());

        try {
            entity.setContrasena(
                    PasswordHasher.hashPassword(
                            entity.getContrasena(),
                            PasswordHasher.generateSalt()
                    )
            );
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new HashPasswordException(e.getMessage());
        }

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
        UsuarioEntity entity = em.find(UsuarioEntity.class, id);
        if (entity != null) {
            entity.setEstado(false);
            em.merge(entity);
        }
    }

}