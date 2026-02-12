package personal.project.valentines.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.valentines.dto.AnswerAuthenticationRequest;
import personal.project.valentines.dto.RetrieveClickedCategory;
import personal.project.valentines.dto.ReturnMatchedAuthenticationQuestionAndAnswer;
import personal.project.valentines.service.AuthenticationService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/admin")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    @PostMapping("authenticate/send")
    public List<ReturnMatchedAuthenticationQuestionAndAnswer> authenticateEntrance(@Valid @RequestBody RetrieveClickedCategory category) {
        return authenticationService.authenticateEntrance(category);
    }

    @PostMapping("authenticate/validate")
    public String isAnswerCorrect(@Valid @RequestBody AnswerAuthenticationRequest request) {
        return authenticationService.isAnswerCorrect(request);
    }
}
