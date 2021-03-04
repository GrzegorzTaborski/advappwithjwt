package pl.tabo.advapp.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tabo.advapp.exception.TokenNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        Optional<ConfirmationToken> token1 = confirmationTokenRepository.findByToken(token);
        token1.ifPresentOrElse(t -> t.setConfirmedAt(LocalDateTime.now()), () -> new TokenNotFoundException("Token not found"));
    }
}
