package personal.project.valentines.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(
    @NotBlank(message = "Message is blank")
    String message,

    String sender
) {
    
}
