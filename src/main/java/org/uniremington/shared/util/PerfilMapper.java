package org.uniremington.shared.util;

import org.mapstruct.Mapper;
import org.uniremington.application.dto.PerfilDto;
import org.uniremington.domain.model.Perfil;
import org.uniremington.infrastructure.entity.PerfilEntity;

@Mapper(componentModel = "cdi")
public interface PerfilMapper {
    PerfilEntity toEntity(Perfil model);
    Perfil toModel(PerfilEntity entity);
    Perfil toModel(PerfilDto dto);
    PerfilEntity toEntity(PerfilDto dto);
    PerfilDto toDto(PerfilEntity entity);
    PerfilDto toDto(Perfil model);
}
