package org.uniremington.domain.repository;

import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.base.BaseRepository;
import org.uniremington.shared.util.ApiResponse;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> getByUser(String user);
    ApiResponse resetPassword(String username);
}
