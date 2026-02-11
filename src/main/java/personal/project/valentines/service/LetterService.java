package personal.project.valentines.service;

import org.springframework.stereotype.Service;
import personal.project.valentines.base.Person;
import personal.project.valentines.repository.LetterRepository;

import java.util.Objects;

@Service
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterService(LetterRepository letterRepository) {
            this.letterRepository = Objects.requireNonNull(letterRepository);
    }

    /*
     *Adds a new letter
     * @Param String text
     * @Param Person recipient
     */
    public String dedicateLetter(String text, Person recipient) {


            return "Failed to create a letter";
    }
}
