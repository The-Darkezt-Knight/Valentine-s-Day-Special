package personal.project.valentines.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.valentines.base.Person;

public interface UserRepository extends JpaRepository<Person, Long> {
}
