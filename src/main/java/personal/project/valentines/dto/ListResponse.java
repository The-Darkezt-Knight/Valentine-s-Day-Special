package personal.project.valentines.dto;

import personal.project.valentines.base.Person;

public record ListResponse(
        String firstName,
        String lastName,
        Integer age,
        Person.Category category
) {
}
