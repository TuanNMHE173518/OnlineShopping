package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.PasswordResetToken;
import org.example.itech_heaven.Entity.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user);
    boolean isValidPasswordResetToken(String token);

    void deleteToken(PasswordResetToken token);

    PasswordResetToken findByToken(String token);
}
