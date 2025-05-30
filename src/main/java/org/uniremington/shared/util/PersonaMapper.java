package org.uniremington.shared.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.uniremington.application.dto.PersonaDto;
import org.uniremington.domain.model.Persona;
import org.uniremington.infrastructure.entity.PersonaEntity;

@Mapper(componentModel = "cdi")
public interface PersonaMapper {

    @Mapping(target = "primerNombre", source = "nombres", qualifiedByName = "splitNombre0")
    @Mapping(target = "segundoNombre", source = "nombres", qualifiedByName = "splitNombre1")
    @Mapping(target = "primerApellido", source = "apellidos", qualifiedByName = "splitApellido0")
    @Mapping(target = "segundoApellido", source = "apellidos", qualifiedByName = "splitApellido1")
    PersonaEntity toEntity(Persona model);

    @Mapping(target = "nombres", expression = "java(entity.getPrimerNombre() + \" \" + entity.getSegundoNombre())")
    @Mapping(target = "apellidos", expression = "java(entity.getPrimerApellido() + \" \" + entity.getSegundoApellido())")
    Persona toModel(PersonaEntity entity);

    Persona toModel(PersonaDto dto);

    @Mapping(target = "primerNombre", source = "nombres", qualifiedByName = "splitNombre0")
    @Mapping(target = "segundoNombre", source = "nombres", qualifiedByName = "splitNombre1")
    @Mapping(target = "primerApellido", source = "apellidos", qualifiedByName = "splitApellido0")
    @Mapping(target = "segundoApellido", source = "apellidos", qualifiedByName = "splitApellido1")
    PersonaEntity toEntity(PersonaDto dto);

    @Mapping(target = "nombres", expression = "java(entity.getPrimerNombre() + \" \" + entity.getSegundoNombre())")
    @Mapping(target = "apellidos", expression = "java(entity.getPrimerApellido() + \" \" + entity.getSegundoApellido())")
    PersonaDto toDto(PersonaEntity entity);

    PersonaDto toDto(Persona model);

    /* ------------------ Métodos auxiliares anotados para que MapStruct los reconozca por nombre ------------------ */

    @Named("splitNombre0")
    default String splitNombre0(String full) { return split(full, 0); }

    @Named("splitNombre1")
    default String splitNombre1(String full) { return split(full, 1); }

    @Named("splitApellido0")
    default String splitApellido0(String full) { return split(full, 0); }

    @Named("splitApellido1")
    default String splitApellido1(String full) { return split(full, 1); }

    default String split(String full, int index) {
        if (full == null || full.isBlank()) return null;
        String[] parts = full.trim().split(" ");
        return parts.length > index ? parts[index] : "";
    }

}
