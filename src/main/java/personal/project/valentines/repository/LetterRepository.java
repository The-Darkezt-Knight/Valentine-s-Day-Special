package personal.project.valentines.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.valentines.base.Letter;
import personal.project.valentines.base.Person;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    List<Person> findByRecipientId(Person id);

    List<Letter> findByRecipientCategory(Person.Category category);
}
