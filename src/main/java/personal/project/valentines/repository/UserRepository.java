package personal.project.valentines.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.project.valentines.base.Person;

public interface UserRepository extends JpaRepository<Person, Long> {
    List<Person> findByFirstName(String name);

    boolean existsByFirstName(String name);

    void deleteByFirstName(String name);
}
