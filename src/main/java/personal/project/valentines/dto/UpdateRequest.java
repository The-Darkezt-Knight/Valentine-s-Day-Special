package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequest(
    @NotBlank(message = "Canot process request. First name is missing")
    String firstName,

    @NotBlank(message = "Canot process request. First name is missing")
    String lastName,
    
    Integer age
) {
    
}
