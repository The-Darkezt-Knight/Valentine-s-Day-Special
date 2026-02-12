package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import personal.project.valentines.base.Person;

public record RetrieveClickedCategory(
        @NotNull(message = "Category must not be blank")
        Person.Category category
) {
}
