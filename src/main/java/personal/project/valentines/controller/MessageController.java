package personal.project.valentines.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import personal.project.valentines.dto.DisplayMessageResponse;
import personal.project.valentines.dto.SendMessageRequest;
import personal.project.valentines.service.MessageService;


@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
                this.messageService = Objects.requireNonNull(messageService);
    }

    @MessageMapping("/message/send")
    @SendTo("/topic/messages")
    public DisplayMessageResponse composeMessage(SendMessageRequest request) {
                messageService.composeMessage(request);
                String sender = request.sender() != null ? request.sender().trim() : "";
                return new DisplayMessageResponse(request.message().trim(), sender);
    }

    @GetMapping("/api/public/message/display")
    @ResponseBody
    public List<DisplayMessageResponse> displayMessage() {
                return messageService.displayMessage();
    }
    
    
}
