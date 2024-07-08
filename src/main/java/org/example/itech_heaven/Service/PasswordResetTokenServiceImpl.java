package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.PasswordResetToken;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Repository.PasswordResetTokenRepository;
import org.example.itech_heaven.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    @Override
    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUserId(user.getId());
        if (existingToken != null) {
            deleteToken(existingToken);
        }

        UUID uuid = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiryTime = currentTime.plusMinutes(5);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDate(expiryTime);
        return passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public boolean isValidPasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        return resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public void deleteToken(PasswordResetToken token) {

        passwordResetTokenRepository.delete(token);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }


}
