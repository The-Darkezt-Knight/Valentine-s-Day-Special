package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;

public record DedicateLetterRequest(
        @NotBlank(message = "Person to dedicate to cannot be blank")
        String firstName,

        @NotBlank(message = "Text cannot be blank")
        String text
) {
}
