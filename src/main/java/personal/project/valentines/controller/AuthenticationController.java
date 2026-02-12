package personal.project.valentines.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import personal.project.valentines.dto.AnswerAuthenticationRequest;
import personal.project.valentines.dto.RetrieveClickedCategory;
import personal.project.valentines.dto.ReturnMatchedAuthenticationQuestionAndAnswer;
import personal.project.valentines.service.AuthenticationService;

@RestController
@RequestMapping("api/admin")
public class AuthenticationController {

    private static final String SESSION_AUTH_KEY = "authenticated";

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService, "authenticationService must not be null");
    }

    @PostMapping("authenticate/send")
    public List<ReturnMatchedAuthenticationQuestionAndAnswer> authenticateEntrance(@Valid @RequestBody RetrieveClickedCategory category) {
        return authenticationService.authenticateEntrance(category);
    }

    @PostMapping("authenticate/validate")
    public String isAnswerCorrect(@Valid @RequestBody AnswerAuthenticationRequest request, HttpSession session) {
        boolean result = authenticationService.isAnswerCorrect(request);
        if (result) {
            session.setAttribute(SESSION_AUTH_KEY, true);
        }
        return "Authentication success! I made all of this with heart. JSYK";
    }
}
