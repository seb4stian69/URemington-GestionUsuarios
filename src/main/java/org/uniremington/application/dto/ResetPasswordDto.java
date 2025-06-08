package org.uniremington.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Data/**/@Getter
@Setter
public class ResetPasswordDto {
    @NotBlank(message = "La descripcion no puede estar vacia")
    String username;
}
