package pl.tabo.advapp.registration;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tabo.advapp.exception.EmailAlreadyConfirmedException;
import pl.tabo.advapp.exception.TokenNotFoundException;



@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @ResponseStatus
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws Exception {
        return registrationService.register(request);

    }

    @ResponseStatus
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody String token) throws Exception {
        try {
            return registrationService.confirmToken(token);
        } catch (TokenNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Token not found!!"));
        } catch (EmailAlreadyConfirmedException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already confirmed!"));
        }
    }

}

