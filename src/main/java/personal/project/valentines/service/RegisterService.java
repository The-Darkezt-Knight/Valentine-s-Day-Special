package personal.project.valentines.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import personal.project.valentines.base.Person;
import personal.project.valentines.dto.ListResponse;
import personal.project.valentines.dto.RegisterRequest;
import personal.project.valentines.repository.UserRepository;

import java.util.List;
import java.util.Objects;

/*
 * Adding user is meant to be the job of the admin as he's the only one
 * knowledgeable of whom the people these letters are meant for
 */
@Service
public class RegisterService {

    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    /*
     * Adds a new user to the database
     * @param request
     * @return
     */
    @Transactional
    public String registerUser(RegisterRequest request) {
        if(request == null) {
            throw new NullPointerException("Request object not found");
        }

        if(request.firstName().isBlank() || request.lastName().isBlank()) {
            throw new IllegalArgumentException("Either first name or last name is missing");
        }

        Person newPerson = new Person();
        newPerson.setFirstName(request.firstName().trim());
        newPerson.setLastName(request.lastName().trim());

        if(request.age() != null) {
            newPerson.setAge(request.age());
        }

        userRepository.save(newPerson);
        return "Successfully added " + request.firstName().concat(" " + request.lastName()) + " to the user list";
    }

    /*
     * Displays in the table a list of all users
     * @return
     */
    public List<ListResponse> displayList() {
        return userRepository.findAll().stream()
                .map(user -> new ListResponse(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAge()
                )).toList();
    }

}
