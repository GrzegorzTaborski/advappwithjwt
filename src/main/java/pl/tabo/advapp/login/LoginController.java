package pl.tabo.advapp.login;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.tabo.advapp.registration.MessageResponse;
import pl.tabo.advapp.user.User;
import pl.tabo.advapp.util.JwtUtil;


@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class LoginController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @ResponseStatus
    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid Credentials!!"));
        }
        User user = (User) authentication.getPrincipal();
        String roles = user.getRole().toString();
        String jwt = jwtUtil.generateToken(authRequest.getEmail());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                roles));

    }
}

