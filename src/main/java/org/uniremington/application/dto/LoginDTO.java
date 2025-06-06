package org.uniremington.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RegisterForReflection
@Data/**/@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "El username no puede estar vacío")
    String username;
    @NotBlank(message = "El password no puede estar vacío")
    String password;
}
