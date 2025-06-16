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
import org.uniremington.shared.exception.NotFoundException;
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
        Long idPersona = usuario.getId();
        Long idPerfil = Long.parseLong(usuario.getIdPerfil());

        // Recuperar relaciones necesarias
        PersonaEntity persona = em.find(PersonaEntity.class, idPersona);
        if (persona == null) {
            throw new NotFoundException("Persona con ID " + idPersona + " no encontrada", JpaUsuarioRepository.class.getSimpleName());
        }

        PerfilEntity perfil = em.find(PerfilEntity.class, idPerfil);
        if (perfil == null) {
            throw new NotFoundException("Perfil con ID " + idPerfil + " no encontrado", JpaUsuarioRepository.class.getSimpleName());
        }

        UsuarioEntity entity;

        if ("save".equalsIgnoreCase(action)) {
            // CREAR nueva entidad
            entity = mapper.toEntity(usuario);
        } else {
            // ACTUALIZAR: obtener instancia gestionada y actualizar sus campos
            entity = em.find(UsuarioEntity.class, idPersona);
            if (entity == null) {
                throw new NotFoundException("Usuario con ID " + idPersona + " no encontrado", JpaUsuarioRepository.class.getSimpleName());
            }

            // Solo actualizas los campos que cambian
            entity.setNombreUsuario(usuario.getNombreUsuario());
            entity.setContrasena(usuario.getContrasena());
            entity.setSalt(usuario.getSalt());
            entity.setEstado(usuario.getEstado());
        }

        // relaciones
        entity.setPersona(persona);
        entity.setIdperfil(perfil);

        // Bidireccionalidad
        persona.setUsuario(entity);

        if ("save".equalsIgnoreCase(action)) {
            em.persist(entity);
        } // No necesitas merge si ya estás trabajando con la instancia gestionada

        // Retornar DTO
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