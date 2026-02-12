package personal.project.valentines.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import personal.project.valentines.base.Person;
import personal.project.valentines.dto.AnswerAuthenticationRequest;
import personal.project.valentines.dto.RetrieveClickedCategory;
import personal.project.valentines.dto.ReturnMatchedAuthenticationQuestionAndAnswer;
import personal.project.valentines.repository.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
    }

    //Acquires and processes the sent category for sending the question
    public boolean retrieveClickedCategory(RetrieveClickedCategory category) {
        Objects.requireNonNull(category, "category must not be null");
        for (Person.Category c : Person.Category.values()) {
            if (c.name().equals(category.category().toString())) {
                return true;
            }
        }
        return false;
    }

    //Fetches questions and answers based on the category
    public List<ReturnMatchedAuthenticationQuestionAndAnswer> authenticateEntrance(RetrieveClickedCategory category) {
        Objects.requireNonNull(category, "category must not be null");

        if (!retrieveClickedCategory(category)) {
            return List.of();
        }

        return userRepository.findByCategory(category.category()).stream()
                .map(person -> new ReturnMatchedAuthenticationQuestionAndAnswer(
                        person.getSecretQuestion(),
                        person.getSecretAnswer()
                )).toList();
    }

    /**
     * Authenticates the user's answer against the stored secret answer.
     * Handles duplicate questions by requiring exactly one match.
     * Comparison is case-insensitive and whitespace-trimmed.
     */
    public boolean isAnswerCorrect(AnswerAuthenticationRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        if (request.question().isBlank() || request.answer().isBlank()) {
            throw new IllegalArgumentException("At least one of the required fields is missing");
        }

        List<Person> matches = userRepository.findBySecretQuestion(request.question());

        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No matching authentication question found");
        }

        if (matches.size() > 1) {
            throw new IllegalStateException(
                    "Duplicate authentication questions detected. Please use a unique identifier.");
        }

        Person person = matches.getFirst();
        String storedAnswer = person.getSecretAnswer().trim().toLowerCase();
        String userAnswer = request.answer().trim().toLowerCase();

        if (!storedAnswer.equals(userAnswer)) {
            throw new IllegalArgumentException("Perhaps, you're just not the person these letters are meant for");
        }

        return true;
    }
}
