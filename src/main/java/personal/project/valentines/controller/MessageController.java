package personal.project.valentines.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import personal.project.valentines.dto.DisplayMessageResponse;
import personal.project.valentines.dto.SendMessageRequest;
import personal.project.valentines.service.MessageService;


@RestController
@RequestMapping("api/public")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
                this.messageService = Objects.requireNonNull(messageService);
    }

    
    @PostMapping("message/send")
    public String composeMessage( @Valid @RequestBody SendMessageRequest request) {
                return messageService.composeMessage(request);
    }

    @GetMapping("/message/display")
    public List<DisplayMessageResponse> displayMessage() {
                return messageService.displayMessage();
    }
    
    
}
