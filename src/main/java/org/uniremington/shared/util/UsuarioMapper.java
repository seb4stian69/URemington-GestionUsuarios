package org.uniremington.shared.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.uniremington.application.dto.UsuarioDto;
import org.uniremington.domain.model.Usuario;
import org.uniremington.infrastructure.entity.UsuarioEntity;

@Mapper(componentModel = "cdi")
public interface UsuarioMapper {
    UsuarioEntity toEntity(Usuario model);
    Usuario toModel(UsuarioEntity entity);
    Usuario toModel(UsuarioDto dto);
    UsuarioEntity toEntity(UsuarioDto dto);
    UsuarioDto toDto(UsuarioEntity entity);
    UsuarioDto toDto(Usuario model);
}
