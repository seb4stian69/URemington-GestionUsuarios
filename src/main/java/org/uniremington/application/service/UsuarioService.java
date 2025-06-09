package org.uniremington.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IUsuarios;
import org.uniremington.domain.model.Persona;
import org.uniremington.domain.model.Usuario;
import org.uniremington.domain.repository.PersonaRepository;
import org.uniremington.domain.repository.UsuarioRepository;
import org.uniremington.shared.exception.HashPasswordException;
import org.uniremington.shared.exception.NotFoundException;
import org.uniremington.shared.util.ApiResponse;
import org.uniremington.shared.util.PasswordGenerator;
import org.uniremington.shared.util.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService implements IUsuarios {

    UsuarioRepository mainRepository;
    PersonaRepository personaRepository;
    private static final String NAMECLASS = "UsuarioService.class";

    @Inject
    public UsuarioService(UsuarioRepository mainRepository, PersonaRepository personaRepository) {
        this.mainRepository = mainRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    public Optional<Usuario> findById(Long id) {

        Optional<Usuario> result = mainRepository.findById(id);

        if(result.isPresent()){
            return result;
        }

        throw new NotFoundException("No se ha encontrado un usuario con el id:" + id, NAMECLASS);

    }

    @Override
    public boolean login(Usuario user) {

        Usuario usuario = mainRepository.login(user).orElseThrow(
            () -> new NotFoundException("Nombre de usuario incorrecto.", NAMECLASS)
        );

        try {
            return PasswordHasher.hashPassword(user.getContrasena(), user.getSalt()).equals(usuario.getContrasena());
        } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
            throw new HashPasswordException(e.getMessage(), NAMECLASS);
        }

    }

    @Override
    public List<Usuario> findAll() {

        List<Usuario> result = mainRepository.findAll();

        if(!result.isEmpty()){
            return result;
        }

        throw new NotFoundException("No se han encontrado usuarios almacenados", NAMECLASS);

    }

    @Override
    public Usuario save(Usuario usuario) {

        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El ID de la persona (usuario) no puede ser nulo");
        }

        Optional<Usuario> existenteOpt = mainRepository.findById(usuario.getId());

        Usuario repoResult;

        if (existenteOpt.isPresent()) {

            Usuario existente = existenteOpt.get();
            existente.setNombreUsuario(usuario.getNombreUsuario());

            try {

                boolean hashEqualsPassword = PasswordHasher.hashPassword(
                        usuario.getContrasena(),
                        usuario.getSalt()
                ).equals(existente.getContrasena());

                boolean baseEqualsPassword = usuario.getContrasena().equals(existente.getContrasena());

                if (!hashEqualsPassword || baseEqualsPassword) {
                    String salt = PasswordHasher.generateSalt();
                    String hashedPassword = PasswordHasher.hashPassword(usuario.getContrasena(), salt);
                    existente.setSalt(salt);
                    existente.setContrasena(hashedPassword);
                }

            } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
                throw new HashPasswordException(e.getMessage(), NAMECLASS);
            }

            existente.setIdPerfil(usuario.getIdPerfil());
            repoResult = mainRepository.save(existente, "update");

        } else {
            try {
                String salt = PasswordHasher.generateSalt();
                String hashedPassword = PasswordHasher.hashPassword(usuario.getContrasena(), salt);
                usuario.setSalt(salt);
                usuario.setContrasena(hashedPassword);
            } catch (Exception e) {
                throw new HashPasswordException(e.getMessage(), NAMECLASS);
            }
            repoResult = mainRepository.save(usuario, "save");
        }

        return repoResult;

    }

    @Override
    public void deleteById(Long id) {

        if(id == null){
            throw new IllegalArgumentException("La cédula no puede ser nula al eliminar un perfil");
        }

        Optional<Usuario> existente = mainRepository.findById(id);

        if(existente.isPresent()){
            mainRepository.deleteById(id);
        } else{
            throw new NotFoundException("No se ha encontrado un perfil con el id:" + id, NAMECLASS);

        }
        
    }

    @Override
    public ApiResponse resetPassword(String username) {

        Usuario queryUser = new Usuario();
        queryUser.setNombreUsuario(username);

        Optional<Usuario> getUser = mainRepository.login(
            queryUser
        );

        if (getUser.isEmpty()) {
            return new ApiResponse("Usuario no encontrado.", false);
        }

        if(getUser.get().getEstado() == Boolean.FALSE){
            return new ApiResponse("Usuario inactivo.", false);
        }

        Optional<Persona> getPersona = personaRepository.findById(getUser.get().getId());

        if (getPersona.isEmpty()) {
            return new ApiResponse("El usuario no tiene una persona asociada", false);
        }

        if (getPersona.get().getCorreo().isEmpty()) {
            return new ApiResponse("La persona asociada no tiene un correo válido.", false);
        }

        return mainRepository.resetPassword(username, getUser.get(), getPersona.get().getCorreo(), PasswordGenerator.generarPassword());

    }

}
