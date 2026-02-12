package personal.project.valentines.dto;

//Changed the return value from Person to String
public record DisplayLetterResponse(
        String firstName,
        String text
) {
}
