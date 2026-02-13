package personal.project.valentines.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import personal.project.valentines.base.Message;
import personal.project.valentines.dto.DisplayMessageResponse;
import personal.project.valentines.dto.SendMessageRequest;
import personal.project.valentines.exception.ItemIsNullException;
import personal.project.valentines.repository.MessageRepository;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = Objects.requireNonNull(messageRepository);
    }

    /*
    * Validates the message
    */
    public boolean isMessageStructureCorrect(SendMessageRequest request) {
        if(request == null || request.message().isBlank()) {
            return false;
        }

        return true;
    }

    /*
     * Stores message requests to the database
     * @param request
     * @return
     */
    public String composeMessage(SendMessageRequest request) {
        if(!isMessageStructureCorrect(request)) {
            throw new ItemIsNullException("Message doesn't contain a thing");
        }
        String name = "";
        
        if(request.sender() != null) {
            name = request.sender().trim();
        }

        String message = request.message().trim();

        Message newMessage = new Message();

        newMessage.setSender(name);
        newMessage.setText(message);

        messageRepository.save(newMessage);

        return "Message sent";
    }

    /*
     * Sends back message to the frontend
     */
    public List<DisplayMessageResponse> displayMessage() {

        return messageRepository.findAll().stream().map(message ->
            new DisplayMessageResponse(
                message.getText(),
                message.getSender()
            )).toList();
    }
}
