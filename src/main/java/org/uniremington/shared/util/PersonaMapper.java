package org.uniremington.shared.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.uniremington.application.dto.PersonaDto;
import org.uniremington.domain.model.Persona;
import org.uniremington.infrastructure.entity.PersonaEntity;

@Mapper(componentModel = "cdi") // Para Quarkus
public interface PersonaMapper {

    @Mapping(target = "primerNombre", expression = "java(splitNombre(model.getNombres(), 0))")
    @Mapping(target = "segundoNombre", expression = "java(splitNombre(model.getNombres(), 1))")
    @Mapping(target = "primerApellido", expression = "java(splitNombre(model.getApellidos(), 0))")
    @Mapping(target = "segundoApellido", expression = "java(splitNombre(model.getApellidos(), 1))")
    PersonaEntity toEntity(Persona model);

    @Mapping(target = "nombres", expression = "java(entity.getPrimerNombre() + \" \" + entity.getSegundoNombre())")
    @Mapping(target = "apellidos", expression = "java(entity.getPrimerApellido() + \" \" + entity.getSegundoApellido())")
    Persona toModel(PersonaEntity entity);

    Persona toModel(PersonaDto dto);

    /* DTO */

    @Mapping(target = "primerNombre", expression = "java(splitNombre(dto.getNombres(), 0))")
    @Mapping(target = "segundoNombre", expression = "java(splitNombre(dto.getNombres(), 1))")
    @Mapping(target = "primerApellido", expression = "java(splitNombre(dto.getApellidos(), 0))")
    @Mapping(target = "segundoApellido", expression = "java(splitNombre(dto.getApellidos(), 1))")
    PersonaEntity toEntity(PersonaDto dto);

    @Mapping(target = "nombres", expression = "java(entity.getPrimerNombre() + \" \" + entity.getSegundoNombre())")
    @Mapping(target = "apellidos", expression = "java(entity.getPrimerApellido() + \" \" + entity.getSegundoApellido())")
    PersonaDto toDto(PersonaEntity entity);

    PersonaDto toDto(Persona model);

    /* Util Methods */

    default String splitNombre(String full, int index) {
        if (full == null || full.isBlank()) return null;
        String[] parts = full.trim().split(" ");
        return parts.length > index ? parts[index] : "";
    }

}