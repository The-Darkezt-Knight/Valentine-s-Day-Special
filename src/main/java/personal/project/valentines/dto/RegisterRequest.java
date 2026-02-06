package personal.project.valentines.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RegisterRequest(
        @NotBlank(message = "First name is missing")
        String firstName,

        @NotBlank(message = "Last name is missing")
        String lastName,

        @Positive
        @Min(1)
        Integer age
) {
}
