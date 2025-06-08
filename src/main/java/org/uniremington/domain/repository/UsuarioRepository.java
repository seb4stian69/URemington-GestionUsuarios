package org.uniremington.domain.repository;

import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.base.BaseRepository;
import org.uniremington.shared.util.ApiResponse;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> login(Usuario user);
    ApiResponse resetPassword(String username, Usuario usuario, String correo, String newPassword);
}
