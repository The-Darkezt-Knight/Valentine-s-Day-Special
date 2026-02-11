package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;
import personal.project.valentines.base.Person;

public record DisplayLetterRequest(
        Person.Category category
) {
}
