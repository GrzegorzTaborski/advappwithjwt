package pl.tabo.advapp.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tabo.advapp.exception.EmailAlreadyConfirmedException;
import pl.tabo.advapp.exception.TokenNotFoundException;



@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @ResponseStatus
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws Exception {
        return registrationService.register(request);

    }

    @ResponseStatus
    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) throws Exception {
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

