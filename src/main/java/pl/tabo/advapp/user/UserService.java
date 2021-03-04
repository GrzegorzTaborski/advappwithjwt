package pl.tabo.advapp.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tabo.advapp.email.EmailSender;
import pl.tabo.advapp.exception.EmailAlreadyTakenException;
import pl.tabo.advapp.registration.token.ConfirmationToken;
import pl.tabo.advapp.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final EmailSender emailSender;

    private final static String LINK = "http://localhost:8070/register/confirm?token=";

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (userExists) {
            throw new EmailAlreadyTakenException("Error: Email already taken!");

        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10512000),
                user
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);


        return token;
    }

    public void enableUser(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresentOrElse(u -> u.setEnabled(true), () -> new UsernameNotFoundException("Doesn't Exist"));
        userRepository.save(user.get());
    }


}
