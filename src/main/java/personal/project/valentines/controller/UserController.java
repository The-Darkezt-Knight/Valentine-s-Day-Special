package personal.project.valentines.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import personal.project.valentines.dto.ListResponse;
import personal.project.valentines.dto.RegisterRequest;
import personal.project.valentines.service.RegisterService;

import java.util.List;
import java.util.Objects;

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
}
