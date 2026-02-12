package personal.project.valentines.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import personal.project.valentines.dto.DedicateLetterRequest;
import personal.project.valentines.dto.DisplayLetterRequest;
import personal.project.valentines.dto.DisplayLetterResponse;
import personal.project.valentines.service.LetterService;

@RestController
@RequestMapping("api/admin")
public class LetterController {

    private final LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = Objects.requireNonNull(letterService);
    }

    @PostMapping("letter/dedicate")
    public String dedicateLetterTo(@Valid @RequestBody DedicateLetterRequest request) {
            return letterService.dedicateLetter(request.firstName(), request.text());
    }

    @GetMapping("letter/display")
    public List<DisplayLetterResponse> displayLetters(@Valid @RequestBody DisplayLetterRequest request) {
        return letterService.displayLettersByCategory(request);
    }
}
