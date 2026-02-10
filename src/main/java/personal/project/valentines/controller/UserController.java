package personal.project.valentines.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import personal.project.valentines.dto.ListResponse;
import personal.project.valentines.dto.RegisterRequest;
import personal.project.valentines.dto.RemovePersonRequest;
import personal.project.valentines.dto.UpdateRequest;
import personal.project.valentines.service.RegisterService;


@RestController
@RequestMapping("api/admin")
public class UserController {

    private final RegisterService registerService;

    public UserController(RegisterService registerService) {
        this.registerService = Objects.requireNonNull(registerService);
    }

    @PostMapping("list/register")
    public String registerUser(@Valid @RequestBody RegisterRequest request) {
        return registerService.registerUser(request);
    }

    @GetMapping("list/display")
    public List<ListResponse> displayUserList() {
        return registerService.displayList();
    }

    @PostMapping("list/update")
    public String updatePerson(@Valid @RequestBody UpdateRequest request) {
        return registerService.updateRow(request);
    }

    @PostMapping("list/remove")
    public String removePerson(@Valid @RequestBody RemovePersonRequest request) {
        return registerService.removePerson(request);
    }

    
    
    
}
