package pl.tabo.advapp.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tabo.advapp.email.EmailSender;
import pl.tabo.advapp.exception.EmailAlreadyTakenException;
import pl.tabo.advapp.registration.MessageResponse;
import pl.tabo.advapp.registration.token.ConfirmationToken;
import pl.tabo.advapp.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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


    public ResponseEntity<?> getUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            UserDto userdto = new UserDto();
            userdto.setEmail(user.get().getEmail());
            userdto.setFirstName(user.get().getFirstName());
            userdto.setLastName(user.get().getLastName());
            userdto.setId(user.get().getId());
            return ResponseEntity.ok(userdto);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Bad Request!"));
        }

    }

    public ResponseEntity<?> changeDetails(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            user.get().setEmail(userDto.getEmail());
            user.get().setFirstName(userDto.getFirstName());
            user.get().setLastName(userDto.getLastName());
            userRepository.save(user.get());
            return ResponseEntity.ok(new MessageResponse("Account updated"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found!!"));
        }
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            if (changePasswordRequest.getCurrentPassword() != null && changePasswordRequest.getChangedPassword() != null) {
                if (bCryptPasswordEncoder.matches(changePasswordRequest.getCurrentPassword(),user.get().getPassword())) {
                    String newPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getChangedPassword());
                    user.get().setPassword(newPassword);
                    userRepository.save(user.get());
                    return ResponseEntity.ok(new MessageResponse("Password updated"));
                } else {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Invalid Password"));
                }
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: password can't be empty"));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found!!"));

        }

    }
}
