package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerAuthenticationRequest(
        @NotBlank(message = "Question is sent blanked")
        String question,

        @NotBlank(message = "Answer is sent blanked")
        String answer
) {
}
