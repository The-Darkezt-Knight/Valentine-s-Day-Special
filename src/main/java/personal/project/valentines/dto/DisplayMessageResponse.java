package personal.project.valentines.dto;

import jakarta.annotation.Nullable;

public record DisplayMessageResponse(
    String message,

    @Nullable
    String sender
) {
    
}
