package personal.project.valentines.service;

import org.springframework.stereotype.Service;
import personal.project.valentines.dto.AnswerAuthenticationRequest;
import personal.project.valentines.dto.RetrieveClickedCategory;
import personal.project.valentines.dto.ReturnMatchedAuthenticationQuestionAndAnswer;
import personal.project.valentines.repository.UserRepository;

import personal.project.valentines.base.Person;

import java.util.List;
import java.util.Objects;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    //Acquires and processes the sent category for sending the question
    public boolean retrieveClickedCategory(RetrieveClickedCategory category) {
        for(Person.Category c : Person.Category.values()) {
            if(c.name().equals(category.category().toString())) {
                return true;
            }
        }

        return false;
    }

    //Fetches questions and answers based on the category
    public List<ReturnMatchedAuthenticationQuestionAndAnswer> authenticateEntrance(RetrieveClickedCategory category) {

        return (retrieveClickedCategory(category)) ? userRepository.findByCategory(category.category()).stream()
                .map(person -> new ReturnMatchedAuthenticationQuestionAndAnswer(
                        person.getSecretQuestion(),
                        person.getSecretAnswer()
                )).toList() : null;
    }

    //Authenticates answers
    //Requests sent to the server must come with a question and an answer
    //Otherwise, it is automatically rejected
    public String isAnswerCorrect(AnswerAuthenticationRequest request) {

        if(request == null || request.question().isBlank() || request.answer().isBlank()) {
            throw new IllegalArgumentException("At least one of the required fields is missing");
        }

        Person question = userRepository.findBySecretQuestion(request.question()).getFirst();
        String answer = question.getSecretAnswer();

        if(!question.getSecretQuestion().equals(request.question())) {
            throw new IllegalArgumentException("Question is not the same as what was sent");
        }

        if(!answer.equals(request.answer())) {
            throw new IllegalArgumentException("Perhaps, you're just not the person these letters are meant for");
        }

        return "Authentication success! I made all of this with heart. JSYK";
    }
}
