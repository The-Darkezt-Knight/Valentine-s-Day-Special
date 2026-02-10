package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;

public record RemovePersonRequest(
    @NotBlank(message = "First name is empty")
    String firstName
) {
    
}
