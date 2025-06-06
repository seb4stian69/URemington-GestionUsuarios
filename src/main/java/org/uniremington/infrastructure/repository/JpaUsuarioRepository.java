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
import org.uniremington.shared.exception.HashPasswordException;
import org.uniremington.shared.util.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUsuarioRepository implements UsuarioRepository {

    @PersistenceContext EntityManager em;
    UsuarioMapper mapper;
    CorreoService mailer;

    @Inject JpaUsuarioRepository (UsuarioMapper mapper, CorreoService mailer){
        this.mapper = mapper;
        this.mailer = mailer;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        UsuarioEntity entity = em.find(UsuarioEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public Optional<Usuario> login(String user) {

        try {

            UsuarioEntity usuario = em.createQuery(

                    "SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario = :nombreUsuario", UsuarioEntity.class

            ).setParameter("nombreUsuario", user).getSingleResult();

            Usuario usRes = mapper.toModel(usuario);
            usRes.setIdPerfil(usuario.getIdperfil().getId().toString());

            return Optional.of(usRes);

        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public ApiResponse resetPassword(String username) {
        try {
            UsuarioEntity usuario = em.createQuery(
                            "SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario = :nombreUsuario", UsuarioEntity.class)
                    .setParameter("nombreUsuario", username)
                    .getSingleResult();

            if (usuario == null || usuario.getEstado() == Boolean.FALSE) {
                return new ApiResponse("Usuario no encontrado o inactivo.", false);
            }

            PersonaEntity persona = em.createQuery(
                            "SELECT p FROM PersonaEntity p WHERE p.id = :idUsuario", PersonaEntity.class)
                    .setParameter("idUsuario", usuario.getId())
                    .getSingleResult();

            if (persona == null || persona.getCorreo() == null || persona.getCorreo().isEmpty()) {
                return new ApiResponse("La persona asociada no tiene un correo válido.", false);
            }

            String newPassword = PasswordGenerator.generarPassword();
            Usuario updateUser = this.mapper.toModel(usuario);
            updateUser.setContrasena(newPassword);
            updateUser.setIdPerfil(usuario.getIdperfil().getId().toString());
            this.save(updateUser, "update");

            this.mailer.enviarCorreo(username, newPassword, persona.getCorreo());

            return new ApiResponse("Contraseña cambiada de manera exitosa", true);

        } catch (NoResultException e) {
            return new ApiResponse("Usuario no encontrado.", false);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Ocurrió un error al restablecer la contraseña.", false);
        }
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

        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El ID de la persona (usuario) no puede ser nulo");
        }

        // Obtener la persona asociada (se asume que el ID del usuario es el mismo de la persona)
        PersonaEntity persona = em.find(PersonaEntity.class, usuario.getId());
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró la persona con ID " + usuario.getId());
        }

        // Buscar si el usuario ya existe en base de datos
        UsuarioEntity existing = em.find(UsuarioEntity.class, usuario.getId());

        UsuarioEntity entity;

        if (existing != null) {
            // Actualizar campos en la entidad existente
            entity = existing;
            entity.setNombreUsuario(usuario.getNombreUsuario());

            if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
                try {
                    String salt = PasswordHasher.generateSalt();
                    entity.setSalt(salt);
                    entity.setContrasena(PasswordHasher.hashPassword(usuario.getContrasena(), salt));
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    throw new HashPasswordException(e.getMessage());
                }
            }

        } else {
            // Crear nueva entidad desde el DTO
            entity = mapper.toEntity(usuario);

            // Hashear la contraseña
            try {
                String salt = PasswordHasher.generateSalt();
                entity.setSalt(salt);
                entity.setContrasena(PasswordHasher.hashPassword(entity.getContrasena(), salt));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new HashPasswordException(e.getMessage());
            }

            // Establecer relación bidireccional
            persona.setUsuario(entity);
            entity.setPersona(persona);
        }

        // Establecer el perfil
        PerfilEntity perfil = em.find(PerfilEntity.class, usuario.getIdPerfil());
        if (perfil == null) {
            throw new IllegalArgumentException("No se encontró el perfil con ID " + usuario.getIdPerfil());
        }
        entity.setIdperfil(perfil);

        // Persistir o actualizar según corresponda
        if (existing == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }

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

}