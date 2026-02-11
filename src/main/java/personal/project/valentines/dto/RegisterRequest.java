package personal.project.valentines.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import personal.project.valentines.base.Person;

public record RegisterRequest(
        @NotBlank(message = "First name is missing")
        String firstName,

        @NotBlank(message = "Last name is missing")
        String lastName,

        @Positive
        @Min(1)
        Integer age,

        @NotNull(message = "Category is required")
        Person.Category category,

        String secretQuestion,

        String secretAnswer
) {
}
