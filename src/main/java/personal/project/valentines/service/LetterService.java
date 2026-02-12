package personal.project.valentines.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import personal.project.valentines.base.Letter;
import personal.project.valentines.base.Person;
import personal.project.valentines.dto.DisplayLetterRequest;
import personal.project.valentines.dto.DisplayLetterResponse;
import personal.project.valentines.exception.ItemIsNullException;
import personal.project.valentines.repository.LetterRepository;
import personal.project.valentines.repository.UserRepository;

@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    public LetterService(LetterRepository letterRepository, UserRepository userRepository) {
            this.letterRepository = Objects.requireNonNull(letterRepository);
            this.userRepository = Objects.requireNonNull(userRepository);
    }

    /*
     *Adds a new letter
     * @Param String text
     * @Param Person recipient
     */
    @Transactional
    public String dedicateLetter(String firstName, String text) {
        // 1. Fail-fast validation for input
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Letter content cannot be empty");
        }

        // 2. Fetch and Validate Uniqueness (Defensive Java Standards)
        List<Person> results = userRepository.findByFirstName(firstName);

        if (results.isEmpty()) {
            throw new ItemIsNullException("No person found with the name: " + firstName);
        }

        if (results.size() > 1) {
            throw new IllegalStateException("Multiple people found with the name '" + firstName +
                    "'. Please use a unique identifier (ID) to ensure the letter reaches the right person.");
        }

        Person recipient = results.getFirst();

        // 3. Composition: Create and save the new Letter
        Letter newLetter = new Letter();
        newLetter.setRecipient(recipient);
        newLetter.setText(text.trim());

        // CRITICAL: Persist to database
        letterRepository.save(newLetter);

        return "Successfully created a letter for " + recipient.getFirstName() + " " + recipient.getLastName();
    }

    /*
     *
     * @param category
     * @return
     *
     * Changed the return value from List<Letter> to List<DisplayLetterResponse>
     */
    public List<DisplayLetterResponse> displayLettersByCategory(DisplayLetterRequest request) {
        if(request == null) {
            throw  new IllegalArgumentException("Category must not be null");
        }

        List<Letter> letters = letterRepository.findByRecipientCategory(request.category());

        return  letters.stream()
                .map(letter -> new DisplayLetterResponse(
                        letter.getRecipient().getFirstName(),
                        letter.getText()
                )).toList();
    }
}
