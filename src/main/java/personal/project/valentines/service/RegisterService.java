package personal.project.valentines.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import personal.project.valentines.base.Person;
import personal.project.valentines.dto.ListResponse;
import personal.project.valentines.dto.RegisterRequest;
import personal.project.valentines.dto.RemovePersonRequest;
import personal.project.valentines.dto.UpdateRequest;
import personal.project.valentines.exception.DuplicatePersonException;
import personal.project.valentines.exception.ItemIsNullException;
import personal.project.valentines.repository.UserRepository;

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

    //Checks if the request information is already in the database
    public boolean isPersonExisting(RegisterRequest request) {
        if (request == null || request.firstName() == null || request.lastName() == null) {
            return false;
        }

        List<Person> mappedPerson = userRepository.findByFirstName(request.firstName());
        if (mappedPerson.isEmpty()) {
            return false;
        }

        Person person = mappedPerson.getFirst();
        return person.getLastName() != null
                && person.getLastName().equalsIgnoreCase(request.lastName());
    }

    @Transactional
    public String registerUser(RegisterRequest request) {
        if(request == null) {
            throw new NullPointerException("Request object not found");
        }

        if(request.firstName().isBlank() || request.lastName().isBlank()) {
            throw new IllegalArgumentException("Either first name or last name is missing");
        }

        if(isPersonExisting(request)) {
            throw new DuplicatePersonException("The request information is already in the database");
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

    /*
     * Updates the row
     */
    public String updateRow(UpdateRequest request) {
        if(request.firstName() == null || request.lastName() == null) {
            throw new ItemIsNullException("Name is missing");
        }

        List<Person> mappedPerson = userRepository.findByFirstName(request.firstName());

        if(mappedPerson.isEmpty()) {
            throw new ItemIsNullException("Person does not map to anyone in the database");
        }

        Person person = mappedPerson.get(0);
        boolean isChanged = false;

        if(!person.getFirstName().equalsIgnoreCase(request.firstName())) {
            person.setFirstName(request.firstName());
            isChanged = true;
        }

        if(!person.getLastName().equalsIgnoreCase(request.lastName())) {
            person.setLastName(request.lastName());
            isChanged = true;
        }

        if(person.getAge() != null && request.age() != null) {
            if(!person.getAge().equals(request.age())) {
            person.setAge(request.age());
            isChanged = true;
            }
        }else if(person.getAge() == null && request.age() != null) {
            person.setAge(request.age());
            isChanged = true;
        }
        
        if(isChanged) {
            userRepository.save(person);

            return "Changes saved";
        }

        return "Changes not saved";
    }

    /*
    * Removes a row from the database
    */
   @Transactional
   public String removePerson(RemovePersonRequest request) {
        
        if(userRepository.existsByFirstName(request.firstName())) {
            userRepository.deleteByFirstName(request.firstName());
        
            return request.firstName() + " is successfully removed";
        }

        return "failed to remove row";
   }

}
