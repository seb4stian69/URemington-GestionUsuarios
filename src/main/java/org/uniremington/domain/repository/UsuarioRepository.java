package org.uniremington.domain.repository;

import org.uniremington.domain.model.Usuario;
import org.uniremington.shared.base.BaseRepository;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> getByUsername(Usuario user);
}
