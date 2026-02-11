package personal.project.valentines.dto;

import personal.project.valentines.base.Person;

//Changed the return value from Person to String
public record DisplayLetterResponse(
        String firstName,
        String text
) {
}
